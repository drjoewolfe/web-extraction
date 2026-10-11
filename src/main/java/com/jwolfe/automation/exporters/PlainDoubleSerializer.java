package com.jwolfe.automation.exporters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class PlainDoubleSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(
            Double value,
            JsonGenerator gen,
            SerializerProvider serializers) throws IOException {

        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeNumber(BigDecimal.valueOf(value));
        }
    }
}