package dev.logicojp.micronaut.codelessapp;

import java.util.concurrent.atomic.AtomicReference;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class GreetingProvider {
    private final AtomicReference<String> message = new AtomicReference<>();

    public GreetingProvider(@Value("${app.greeting}") String message) {
        this.message.set(message);
    }

    String getMessage() {
        return message.get();
    }

    void setMessage(String message) {
        this.message.set(message);
    }
}
