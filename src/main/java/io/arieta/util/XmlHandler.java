package io.arieta.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class XmlHandler<T> {
    private final Class<T> type;

    public XmlHandler(Class<T> type) {
        this.type = type;
    }

    public T readXml(String filePath) throws IOException {
        XmlMapper mapper = new XmlMapper();
        return mapper.readValue(new File(filePath), type);
    }

    public void writeXml(String filePath, T object) throws IOException {
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Exclui campos nulos
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), object);
    }
}
