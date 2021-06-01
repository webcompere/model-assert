package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.org.webcompere.modelassert.json.assertjson.AssertJson;
import uk.org.webcompere.modelassert.json.dsl.HamcrestJsonAssertionBuilder;

import java.io.File;
import java.nio.file.Path;

/**
 * Jumping on point - create either an assertJson or a hamcrest
 */
public class JsonAssertions {
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Begin an <code>assertJson</code> style assertion
     * @param json the String json to assert
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<String> assertJson(String json) {
        return new AssertJson<>(OBJECT_MAPPER::readTree, json);
    }

    /**
     * Begin an <code>assertJson</code> style assertion
     * @param jsonNode the already loaded {@link JsonNode} with the JSON to assert
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<JsonNode> assertJson(JsonNode jsonNode) {
        return new AssertJson<>(node -> node, jsonNode);
    }

    /**
     * Begin an <code>assertJson</code> style assertion
     * @param file the file containing the json to assert
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<File> assertJson(File file) {
        return new AssertJson<>(OBJECT_MAPPER::readTree, file);
    }

    /**
     * Begin an <code>assertJson</code> style assertion
     * @param path the path to the file containing the json to assert
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<File> assertJson(Path path) {
        return assertJson(path.toFile());
    }

    /**
     * Begin a hamcrest matcher based on a json String
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<String> json() {
        return new HamcrestJsonAssertionBuilder<>(OBJECT_MAPPER::readTree);
    }

    /**
     * Begin a hamcrest matcher based on a json node
     */
    public static HamcrestJsonAssertionBuilder<JsonNode> jsonNode() {
        return new HamcrestJsonAssertionBuilder<>(node -> node);
    }

    /**
     * Begin a hamcrest matcher based on a json file
     */
    public static HamcrestJsonAssertionBuilder<File> jsonFile() {
        return new HamcrestJsonAssertionBuilder<>(OBJECT_MAPPER::readTree);
    }

    /**
     * Begin a hamcrest matcher based on a json file via {@link Path}
     */
    public static HamcrestJsonAssertionBuilder<Path> jsonFilePath() {
        return new HamcrestJsonAssertionBuilder<>(path -> OBJECT_MAPPER.readTree(path.toFile()));
    }
}
