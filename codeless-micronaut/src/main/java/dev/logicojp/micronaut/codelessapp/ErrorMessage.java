package dev.logicojp.micronaut.codelessapp;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ErrorMessage(String error) {
}
