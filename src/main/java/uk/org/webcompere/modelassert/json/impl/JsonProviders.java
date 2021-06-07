package uk.org.webcompere.modelassert.json.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.org.webcompere.modelassert.json.JsonProvider;

import java.io.File;
import java.nio.file.Path;

public class JsonProviders {
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonProvider<String> jsonStringProvider() {
        return OBJECT_MAPPER::readTree;
    }

    public static JsonProvider<File> jsonFileProvider() {
        return OBJECT_MAPPER::readTree;
    }

    public static JsonProvider<Path> jsonPathProvider() {
        return path -> OBJECT_MAPPER.readTree(path.toFile());
    }
}
