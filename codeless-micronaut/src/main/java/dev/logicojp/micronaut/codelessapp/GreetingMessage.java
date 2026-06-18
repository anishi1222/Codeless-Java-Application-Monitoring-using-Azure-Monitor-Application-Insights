package dev.logicojp.micronaut.codelessapp;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GreetingMessage(String message) {
}
