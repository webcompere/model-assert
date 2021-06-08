package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;
import uk.org.webcompere.modelassert.json.assertjson.AssertJson;
import uk.org.webcompere.modelassert.json.dsl.HamcrestJsonAssertionBuilder;

import java.io.File;
import java.nio.file.Path;

import static uk.org.webcompere.modelassert.json.impl.JsonProviders.*;

/**
 * Jumping on point - create either an assertJson or a hamcrest. Facade/factory methods.
 */
public class JsonAssertions {

    /**
     * Begin an <code>assertJson</code> style assertion
     * @param json the String json to assert
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<String> assertJson(String json) {
        return new AssertJson<>(jsonStringProvider(), json);
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
        return new AssertJson<>(jsonFileProvider(), file);
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
     * Begin an <code>assertJson</code> style assertion
     * @param object an object to convert to JSON to start with
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<Object> assertJson(Object object) {
        return new AssertJson<>(jsonObjectProvider(), object);
    }

    /**
     * Begin a hamcrest matcher based on a json String
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<String> json() {
        return new HamcrestJsonAssertionBuilder<>(jsonStringProvider());
    }

    /**
     * Begin a hamcrest matcher based on an object
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<Object> jsonObject() {
        return new HamcrestJsonAssertionBuilder<>(jsonObjectProvider());
    }

    /**
     * Begin a hamcrest matcher based on a json node
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<JsonNode> jsonNode() {
        return new HamcrestJsonAssertionBuilder<>(node -> node);
    }

    /**
     * Begin a hamcrest matcher based on a json file
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<File> jsonFile() {
        return new HamcrestJsonAssertionBuilder<>(jsonFileProvider());
    }

    /**
     * Begin a hamcrest matcher based on a json file via {@link Path}
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<Path> jsonFilePath() {
        return new HamcrestJsonAssertionBuilder<>(jsonPathProvider());
    }

    /**
     * Begin an <code>assertYaml</code> style assertion
     * @param yaml the String yaml to assert
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<String> assertYaml(String yaml) {
        return new AssertJson<>(yamlStringProvider(), yaml);
    }

    /**
     * Begin an <code>assertYaml</code> style assertion
     * @param file the file containing the yaml to assert
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<File> assertYaml(File file) {
        return new AssertJson<>(yamlFileProvider(), file);
    }

    /**
     * Begin an <code>assertYaml</code> style assertion
     * @param path the path to the file containing the yaml to assert
     * @return an {@link AssertJson} object for adding assertions to
     */
    public static AssertJson<Path> assertYaml(Path path) {
        return new AssertJson<>(yamlPathProvider(), path);
    }

    /**
     * Begin a hamcrest matcher based on a yaml String
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<String> yaml() {
        return new HamcrestJsonAssertionBuilder<>(yamlStringProvider());
    }

    /**
     * Begin a hamcrest matcher based on a yaml file
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<File> yamlFile() {
        return new HamcrestJsonAssertionBuilder<>(yamlFileProvider());
    }

    /**
     * Begin a hamcrest matcher based on a yaml file via {@link Path}
     * @return the matcher
     */
    public static HamcrestJsonAssertionBuilder<Path> yamlFilePath() {
        return new HamcrestJsonAssertionBuilder<>(yamlPathProvider());
    }
}
