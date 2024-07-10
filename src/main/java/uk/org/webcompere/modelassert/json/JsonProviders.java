package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Collection of built in convertions to {@link JsonNode}
 */
public class JsonProviders {
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = defaultObjectMapper();

    private static final ObjectMapper DEFAULT_YAML_MAPPER = defaultYamlMapper();

    public static ObjectMapper defaultObjectMapper() {
        return new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public static ObjectMapper defaultYamlMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    private static ThreadLocal<ObjectMapper> overrideObjectMapper = new ThreadLocal<>();
    private static ThreadLocal<ObjectMapper> overrideYamlObjectMapper = new ThreadLocal<>();

    private static ObjectMapper getObjectMapper() {
        return Optional.ofNullable(overrideObjectMapper.get()).orElse(DEFAULT_OBJECT_MAPPER);
    }

    private static ObjectMapper getYamlObjectMapper() {
        return Optional.ofNullable(overrideYamlObjectMapper.get()).orElse(DEFAULT_YAML_MAPPER);
    }

    public static void overrideObjectMapper(ObjectMapper mapper) {
        overrideObjectMapper.set(mapper);
    }

    public static void clearObjectMapperOverride() {
        overrideObjectMapper.remove();
    }

    public static void overrideYamlObjectMapper(ObjectMapper mapper) {
        overrideYamlObjectMapper.set(mapper);
    }

    public static void clearYamlObjectMapperOverride() {
        overrideYamlObjectMapper.remove();
    }

    /**
     * A provider which parses String to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<String> jsonStringProvider() {
        return getObjectMapper()::readTree;
    }

    /**
     * A provider which parses Object to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<Object> jsonObjectProvider() {
        return object -> getObjectMapper().convertValue(object, JsonNode.class);
    }

    /**
     * A provider which parses the contents of a file to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<File> jsonFileProvider() {
        return getObjectMapper()::readTree;
    }

    /**
     * A provider which parses the contents of a file to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<Path> jsonPathProvider() {
        return path -> getObjectMapper().readTree(path.toFile());
    }

    /**
     * A provider which parses a yaml string to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<String> yamlStringProvider() {
        return getYamlObjectMapper()::readTree;
    }

    /**
     * A provider which parses a yaml file to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<File> yamlFileProvider() {
        return getYamlObjectMapper()::readTree;
    }

    /**
     * A provider which parses a yaml file to {@link JsonNode}
     * @return the provider
     */
    public static JsonProvider<Path> yamlPathProvider() {
        return path -> getYamlObjectMapper().readTree(path.toFile());
    }
}
