package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.nio.file.Path;

/**
 * Collection of built in convertions to {@link JsonNode}
 */
public class JsonProviders {
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

    private static ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    /**
     * A provider which parses String to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<String> jsonStringProvider() {
        return OBJECT_MAPPER::readTree;
    }

    /**
     * A provider which parses Object to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<Object> jsonObjectProvider() {
        return object -> OBJECT_MAPPER.convertValue(object, JsonNode.class);
    }

    /**
     * A provider which parses the contents of a file to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<File> jsonFileProvider() {
        return OBJECT_MAPPER::readTree;
    }

    /**
     * A provider which parses the contents of a file to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<Path> jsonPathProvider() {
        return path -> OBJECT_MAPPER.readTree(path.toFile());
    }

    /**
     * A provider which parses a yaml string to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<String> yamlStringProvider() {
        return YAML_MAPPER::readTree;
    }

    /**
     * A provider which parses a yaml file to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<File> yamlFileProvider() {
        return YAML_MAPPER::readTree;
    }

    /**
     * A provider which parses a yaml file to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<Path> yamlPathProvider() {
        return path -> YAML_MAPPER.readTree(path.toFile());
    }
}
