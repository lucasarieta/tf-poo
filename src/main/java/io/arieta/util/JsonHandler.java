package io.arieta.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.io.File;
import java.io.IOException;

public class JsonHandler<T> {
    private final Class<T> type;

    public JsonHandler(Class<T> type) {
        this.type = type;
    }

    public void writeJson(String filePath, T object) throws IOException {
        ObjectMapper mapper = getMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), object);
    }

    public T readJson(String filePath) throws IOException {
        ObjectMapper mapper = getMapper();
        return mapper.readValue(new File(filePath), type);
    }

    private ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Formata o JSON


        return mapper;
    }
}
