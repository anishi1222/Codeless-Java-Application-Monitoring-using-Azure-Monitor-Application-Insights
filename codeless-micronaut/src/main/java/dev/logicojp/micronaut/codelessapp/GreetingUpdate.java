package dev.logicojp.micronaut.codelessapp;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GreetingUpdate(String greeting) {
}
