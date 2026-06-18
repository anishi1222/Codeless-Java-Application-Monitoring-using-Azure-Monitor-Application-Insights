package dev.logicojp.spring.codelessapp;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

@Service
public class GreetingProvider {

    private final AtomicReference<String> message = new AtomicReference<>();

    public GreetingProvider(GreetingProperties properties) {
        this.message.set(properties.getGreeting());
    }

    String getMessage() {
        return message.get();
    }

    void setMessage(String message) {
        this.message.set(message);
    }
}
