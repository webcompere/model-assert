package uk.org.webcompere.modelassert.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

@DisplayName("Some usage examples")
class ExamplesTest {

    @Test
    void assertAnElementIsPresent() {
        assertJson("{\"name\":\"Model\"}")
                .at("/name").isNotMissing();
    }

    @Test
    void assertAnElementHasAValue() {
        assertJson("{\"name\":\"Model\"}")
                .at("/name").hasValue("Model");
    }

    @Test
    void assertAnElementInAnArrayHasAValue() {
        assertJson("{\"names\":[\"Model\",\"Assert\"]}")
                .at("/names/1").hasValue("Assert");
    }

    @Test
    void assertRootNodeWhenPrimitive() {
        assertJson("\"ModelAssert\"")
                .hasValue("ModelAssert");
    }

    @Test
    void assertBooleanNodeIsTrue() {
        assertJson("true")
                .isTrue();
    }

    @Test
    void assertBooleanFieldIsTrue() {
        assertJson("{\"isOld\":true}")
                .at("/isOld")
                .isTrue();
    }

    @Test
    void assertBooleanFieldIsFalse() {
        assertJson("{\"isOld\":false}")
                .at("/isOld")
                .isFalse();
    }
}
