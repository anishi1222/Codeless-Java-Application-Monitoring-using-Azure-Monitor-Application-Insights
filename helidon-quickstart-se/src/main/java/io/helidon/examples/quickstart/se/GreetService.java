
package io.helidon.examples.quickstart.se;

import java.io.StringReader;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.helidon.config.Config;
import io.helidon.http.Status;
import io.helidon.service.registry.Services;
import io.helidon.webclient.api.WebClient;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

/**
 * A simple service to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe:
 * curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting
 * curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object
 */

public class GreetService implements HttpService {

    /**
     * The config value for the key {@code greeting}.
     */
    private final AtomicReference<String> greeting = new AtomicReference<>();

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    private static final Logger LOGGER = Logger.getLogger(GreetService.class.getName());

    GreetService() {
        this(Services.get(Config.class).get("app"));
    }

    GreetService(Config appConfig) {
        greeting.set(appConfig.get("greeting").asString().orElse("Ciao"));
    }

    /**
     * A service registers itself by updating the routing rules.
     * @param rules the routing rules.
     */
    @Override
    public void routing(HttpRules rules) {
        rules
            .get("/", this::getDefaultMessageHandler)
            .get("/greet2mp/{name}", this::get2MPMessageHandler)
            .get("/{name}", this::getMessageHandler)
            .put("/greeting", this::updateGreetingHandler);
    }

    private void get2MPMessageHandler(ServerRequest serverRequest, ServerResponse serverResponse) {

        // The following statement might throw exception
        // when the environment variable "MP_URL" is not configured.
        String mpUrl = System.getenv("MP_URL");

        WebClient client = WebClient.builder()
                .baseUri(mpUrl)
                .build();
        String name = serverRequest.path().pathParameters().get("name");
        String response = client.get("/greet/" + name).requestEntity(String.class);
        try (JsonReader reader = Json.createReader(new StringReader(response))) {
            JsonObject jsonObj = reader.readObject();
            sendResponse(serverResponse, jsonObj.getString("message"));
        } catch (JsonException e) {
            LOGGER.log(Level.WARNING, "Invalid JSON response from MP service", e);
            JsonObject jsonErrorObject = JSON.createObjectBuilder()
                    .add("error", "Invalid JSON")
                    .build();
            serverResponse.status(Status.INTERNAL_SERVER_ERROR_500).send(jsonErrorObject);
        }
    }

    /**
     * Return a worldly greeting message.
     * @param request the server request
     * @param response the server response
     */
    private void getDefaultMessageHandler(ServerRequest request, ServerResponse response) {
        sendResponse(response, "World");
    }

    /**
     * Return a greeting message using the name that was provided.
     * @param request the server request
     * @param response the server response
     */
    private void getMessageHandler(ServerRequest request, ServerResponse response) {
        String name = request.path().pathParameters().get("name");
        sendResponse(response, name);
    }

    private void sendResponse(ServerResponse response, String name) {
        String msg = String.format("%s %s!", greeting.get(), name);

        JsonObject returnObject = JSON.createObjectBuilder()
                .add("message", msg)
                .build();
        response.send(returnObject);
    }

    private static <T> T processErrors(Throwable ex, ServerRequest request, ServerResponse response) {

         if (ex instanceof JsonException || ex.getCause() instanceof JsonException){

            LOGGER.log(Level.FINE, "Invalid JSON", ex);
            JsonObject jsonErrorObject = JSON.createObjectBuilder()
                .add("error", "Invalid JSON")
                .build();
            response.status(Status.BAD_REQUEST_400).send(jsonErrorObject);
        }  else {

            LOGGER.log(Level.FINE, "Internal error", ex);
            JsonObject jsonErrorObject = JSON.createObjectBuilder()
                .add("error", "Internal error")
                .build();
            response.status(Status.INTERNAL_SERVER_ERROR_500).send(jsonErrorObject);
        }

        return null;
    }

    private void updateGreetingFromJson(JsonObject jo, ServerResponse response) {
        if (!jo.containsKey("greeting")) {
            JsonObject jsonErrorObject = JSON.createObjectBuilder()
                    .add("error", "No greeting provided")
                    .build();
            response.status(Status.BAD_REQUEST_400)
                    .send(jsonErrorObject);
            return;
        }

        greeting.set(jo.getString("greeting"));
        response.status(Status.NO_CONTENT_204).send();
    }

    /**
     * Set the greeting to use in future messages.
     * @param request the server request
     * @param response the server response
     */
    private void updateGreetingHandler(ServerRequest request,
                                       ServerResponse response) {
        try {
            updateGreetingFromJson(request.content().as(JsonObject.class), response);
        } catch (RuntimeException ex) {
            processErrors(ex, request, response);
        }
    }
}