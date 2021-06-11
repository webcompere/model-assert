package uk.org.webcompere.modelassert.json.examples;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertYaml;
import static uk.org.webcompere.modelassert.json.PathWildCard.ANY;
import static uk.org.webcompere.modelassert.json.Patterns.GUID_PATTERN;

class WholeTreeUseCaseTest {

    @Test
    void identicalJsonsAreEqual() {
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(json).isEqualTo(json);
    }

    @Test
    void nonIdenticalJsonsAreNotEqual() {
        String wrongJson = "{\"id\":2, \"name\":\"Bob\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(wrongJson).isNotEqualTo(json);
    }

    @Test
    void acceptDifferencesInJson() {
        String idDifferentJson = "{\"id\":2, \"name\":\"Chris\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where()
                .path("id").isAnyNode()
            .isEqualTo(json);
    }

    @Test
    void whenDifferencesAreBeyondAcceptableThereIsError() {
        String idDifferentJson = "{\"id\":2, \"name\":\"Billy\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where()
                .path("id").isAnyNode()
            .isNotEqualTo(json);
    }

    @Test
    void useConfiguredByInsteadOfInline() {
        String idDifferentJson = "{\"id\":2, \"name\":\"Chris\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where()
            .configuredBy(where -> where.path("id").isAnyNode())
            .isEqualTo(json);
    }

    @Test
    void acceptAnyNumeric() {
        String idDifferentJson = "{\"id\":2, \"name\":\"Chris\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where().path("id").isNumber()
            .isEqualTo(json);
    }

    @Test
    void acceptAnyNumericWithBigNumbers() {
        String idDifferentJson = "{\"time\":1548693546000, \"name\":\"Chris\"}";
        String json = "{\"time\":1548693546001, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where().path("time").isNumber()
            .isEqualTo(json);
    }

    @Test
    void acceptAnyNumericAsLongWithBigNumbers() {
        String idDifferentJson = "{\"time\":1548693546000, \"name\":\"Chris\"}";
        String json = "{\"time\":1548693546001, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where().path("time").isLong()
            .isEqualTo(json);
    }

    @Test
    void acceptAnyInteger() {
        String idDifferentJson = "{\"id\":2, \"name\":\"Chris\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where().path("id").isInteger()
            .isEqualTo(json);
    }

    @Test
    void acceptAnyDouble() {
        String idDifferentJson = "{\"id\":2.1, \"name\":\"Chris\"}";
        String json = "{\"id\":1.1, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where().path("id").isDouble()
            .isEqualTo(json);
    }

    @Test
    void rejectDoubleWhenInteger() {
        String idDifferentJson = "{\"id\":2.1, \"name\":\"Chris\"}";

        assertThatThrownBy(() -> assertJson(idDifferentJson)
            .at("/id").isInteger())
            .isInstanceOf(AssertionFailedError.class);
    }

    @Test
    void acceptIfIntegerInRange() {
        String idDifferentJson = "{\"id\":2, \"name\":\"Chris\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where().path("id").isBetween(0, 100)
            .isEqualTo(json);
    }

    @Test
    void rejectIfOutOfIntegerRange() {
        String idDifferentJson = "{\"id\":200, \"name\":\"Chris\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(idDifferentJson)
            .where().path("id").isBetween(0, 100)
            .isNotEqualTo(json);
    }

    @Test
    void acceptIfIgnoringID() {
        String idDifferentJson = "{\"id\":1, \"name\":\"Bob\", \"phone\":123456, \"country\":\"Wales\"}";
        String json = "{\"id\":2, \"name\":\"Bob\", \"phone\":123456, \"country\":\"Wales\"}";

        assertJson(idDifferentJson)
            .where().path("id").isAnyNode()
            .isEqualTo(json);
    }

    @Test
    void acceptIfIgnoringString() {
        String countryDifferentJson = "{\"id\":1, \"name\":\"Bob\", \"phone\":123456, \"country\":\"Wales\"}";
        String json = "{\"id\":1, \"name\":\"Bob\", \"phone\":123456, \"country\":\"England\"}";

        assertJson(countryDifferentJson)
            .where().path("country").isAnyNode()
            .isEqualTo(json);
    }

    @Test
    void acceptIfIgnoringInt() {
        String phoneDifferentJson = "{\"id\":1, \"name\":\"Bob\", \"phone\":123456, \"country\":\"England\"}";
        String json = "{\"id\":1, \"name\":\"Bob\", \"phone\":987654, \"country\":\"England\"}";

        assertJson(phoneDifferentJson)
            .where().path("phone").isAnyNode()
            .isEqualTo(json);
    }

    @Test
    void rejectIfIgnoredFieldNotPresent() {
        String countryMissingJson = "{\"id\":1, \"name\":\"Bob\", \"phone\":123456}";
        String json = "{\"id\":1, \"name\":\"Bob\", \"phone\":123456, \"country\":\"Wales\"}";

        assertJson(countryMissingJson)
            .where().path("country").isAnyNode()
            .isNotEqualTo(json);
    }

    @Test
    void rejectIfIgnoredIdentifierNotPresent() {
        String idMissingJson = "{\"record\":1, \"name\":\"Bob\", \"phone\":123456, \"country\":\"Wales\"}";
        String json = "{\"id\":1, \"name\":\"Bob\", \"phone\":123456, \"country\":\"Wales\"}";

        assertJson(idMissingJson)
            .where().path("id").isAnyNode()
            .isNotEqualTo(json);
    }


    @Test
    void acceptIfString() {
        String wrongJson = "{\"id\":2, \"name\":\"Bob\"}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(wrongJson)
            .where().path("id").isAnyNode()
                .path("name").isText()
            .isEqualTo(json);
    }

    @Test
    void rejectIfNotString() {
        String wrongJson = "{\"id\":2, \"name\":99}";
        String json = "{\"id\":1, \"name\":\"Chris\"}";

        assertJson(wrongJson)
            .where().path("id").isAnyNode()
            .path("name").isText()
            .isNotEqualTo(json);
    }

    @Test
    void acceptIfStringMatchingRegex() {
        String wrongJson = "{\"id\":2, \"guid\":\"24f48b78-42e9-48af-a879-c383a6fbd0ab\"}";
        String json = "{\"id\":1, \"guid\":\"7c75a188-850b-43c5-8aca-ab000798b2ee\"}";

        assertJson(wrongJson)
            .where()
                .path("id").isAnyNode()
                .path("guid").matches(GUID_PATTERN)
            .isEqualTo(json);
    }

    @Test
    void doNotAcceptIfDoesNotMatchRegex() {
        String wrongJson = "{\"id\":2, \"guid\":\"I am not a GUID\"}";
        String json = "{\"id\":1, \"guid\":\"7c75a188-850b-43c5-8aca-ab000798b2ee\"}";

        assertJson(wrongJson)
            .where()
                .path("id").isAnyNode()
                .path("guid").matches(GUID_PATTERN)
            .isNotEqualTo(json);
    }

    @Test
    void requireExactMatchOfStringSucceedsOnFull() {
        String actual = "{\"id\":1, \"guid\":\"12345-abcde-12345\"}";
        String template = "{\"id\":2, \"guid\":\"9999\"}";

        assertJson(actual)
            .where()
                .path("id").isAnyNode()
                .path("guid").textStartsWith("12345-")
            .isEqualTo(template);
    }

    @Test
    void canAssertExactPath() {
        String actual = "{\"id\":1, \"value\":\"12345\"}";

        assertJson(actual)
            .at("/value").isText("12345");
    }

    @Test
    void canAssertFailedExactPath() {
        String actual = "{\"id\":1, \"value\":\"12345\"}";

        assertThatThrownBy(() -> assertJson(actual)
            .at("/value").isText("999"))
            .isInstanceOf(AssertionFailedError.class);
    }

    @Test
    void aDocumentEqualsItself() {
        assertJson(resourcePath("json-alphabetic.json"))
            .isEqualTo(resourcePath("json-alphabetic.json"));
    }

    @Test
    void twoDocumentsWithFieldsInDifferentOrderAreSame() {
        assertJson(resourcePath("json-alphabetic.json"))
            .where().keysInAnyOrder()
            .isEqualTo(resourcePath("json-scrambled.json"));
    }

    @Test
    void tolerateArrayOutOfOrder() throws Exception {
        assertJson(resourcePath("json-string-array-alphabetic.json"))
            .where().path("data").arrayInAnyOrder()
            .isEqualTo(resourcePath("json-string-array-scrambled.json"));
    }

    @Test
    void willNotTolerateArrayMissingAValue() {
        assertJson(resourcePath("json-string-array-alphabetic.json"))
            .where().path("data").arrayInAnyOrder()
            .isNotEqualTo(resourcePath("json-string-array-scrambled-and-missing.json"));
    }

    @Test
    void willNotTolerateArrayWithExtraValue() {
        assertJson(resourcePath("json-string-array-alphabetic.json"))
            .where().path("data").arrayInAnyOrder()
            .isNotEqualTo(resourcePath("json-string-array-scrambled-and-additional.json"));
    }

    @Test
    void willAllowSubObjectsInSameOrder() throws Exception {
        // without the any order
        assertJson(resourcePath("json-array-of-objects-in-order.json"))
            .isEqualTo(resourcePath("json-array-of-objects-in-order.json"));

        // with the any order
        assertJson(resourcePath("json-array-of-objects-in-order.json"))
            .where().path("data").arrayInAnyOrder()
            .isEqualTo(resourcePath("json-array-of-objects-in-order.json"));
    }

    @Test
    void willAllowSubObjectsInDifferentOrder() throws Exception {
        assertJson(resourcePath("json-array-of-objects-in-order.json"))
            .where().path("data").arrayInAnyOrder()
            .isEqualTo(resourcePath("json-array-of-objects-out-of-order.json"));
    }

    @Test
    void willNotAllowSubObjectsWithAdditional() {
        assertJson(resourcePath("json-array-of-objects-in-order.json"))
            .where().path("data").arrayInAnyOrder()
            .isNotEqualTo(resourcePath("json-array-of-objects-with-additional.json"));
    }

    @Test
    void yamlTreeEqualToItself() {
        assertYaml(resourcePath("yaml-tree.yml"))
            .isEqualToYaml(resourcePath("yaml-tree.yml"));
    }

    @Test
    void yamlTreeNotEqualToTwistedVersionOfItself() {
        assertYaml(resourcePath("yaml-tree.yml"))
            .isNotEqualToYaml(resourcePath("yaml-tree-2.yml"));
    }

    @Test
    void yamlTreeEqualToTwistedVersionOfItselfWithAnyArrayOrder() {
        assertYaml(resourcePath("yaml-tree.yml"))
            .where().arrayInAnyOrder()
            .isEqualToYaml(resourcePath("yaml-tree-2.yml"));
    }

    @Test
    void yamlTreeNotEqualToTwistedVersionOfItselfWithArrayInAnyOrderOnlyAtRoot() {
        assertYaml(resourcePath("yaml-tree.yml"))
            .where().path(ANY).arrayInAnyOrder()
            .isNotEqualToYaml(resourcePath("yaml-tree-2.yml"));
    }

    private Path resourcePath(String file) {
        return Paths.get("src", "test", "resources", "examples", file);
    }
}
