package io.arieta.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonHandler<T> {
    private final Class<T> type;

    public JsonHandler(Class<T> type) {
        this.type = type;
    }

    public void writeJson(String filePath, T object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Exclui campos nulos
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), object);
    }

    public T readJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), type);
    }
}
