package dev.logicojp.micronaut.codelessapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MicronautTest
class GreetControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    GreetingProvider greetingProvider;

    @BeforeEach
    void resetGreeting() {
        greetingProvider.setMessage("Hello");
    }

    @Test
    void returnsDefaultGreeting() {
        GreetingMessage greeting = client.toBlocking().retrieve("/greet", GreetingMessage.class);

        assertEquals("Hello World!", greeting.message());
    }

    @Test
    void returnsNamedGreeting() {
        GreetingMessage greeting = client.toBlocking().retrieve("/greet/Joe", GreetingMessage.class);

        assertEquals("Hello Joe!", greeting.message());
    }

    @Test
    void updateGreetingChangesFutureResponses() {
        HttpRequest<String> request = HttpRequest.PUT("/greet/greeting", "{\"greeting\":\"Hola\"}")
                .contentType(MediaType.APPLICATION_JSON);
        HttpResponse<?> response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.NO_CONTENT, response.status());
        GreetingMessage greeting = client.toBlocking().retrieve("/greet/Jose", GreetingMessage.class);
        assertEquals("Hola Jose!", greeting.message());
    }

    @Test
    void missingGreetingIsRejected() {
        HttpRequest<String> request = HttpRequest.PUT("/greet/greeting", "{}")
                .contentType(MediaType.APPLICATION_JSON);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request, ErrorMessage.class));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        ErrorMessage error = exception.getResponse().getBody(ErrorMessage.class).orElseThrow();
        assertEquals("No greeting provided", error.error());
    }
}
