package dev.logicojp.micronaut.codelessapp;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;

@Controller("/greet")
public class GreetController {

    private final GreetingProvider greetingProvider;

    public GreetController(GreetingProvider greetingProvider) {
        this.greetingProvider = greetingProvider;
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public GreetingMessage getDefaultMessage() {
        return createResponse("World");
    }

    @Get("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public GreetingMessage getMessage(String name) {
        return createResponse(name);
    }

    @Put("/greeting")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> updateGreeting(@Body GreetingUpdate update) {
        if (update == null || update.greeting() == null) {
            return HttpResponse.badRequest(new ErrorMessage("No greeting provided"));
        }

        greetingProvider.setMessage(update.greeting());
        return HttpResponse.noContent();
    }

    private GreetingMessage createResponse(String who) {
        return new GreetingMessage("%s %s!".formatted(greetingProvider.getMessage(), who));
    }
}
