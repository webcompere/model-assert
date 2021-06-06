package uk.org.webcompere.modelassert.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

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

    @Test
    void assertBooleanFieldIsBoolean() {
        assertJson("{\"isOld\":false}")
                .at("/isOld")
                .isBoolean();
    }

    @Test
    void assertBooleanFieldIsNotText() {
        assertJson("{\"isOld\":false}")
                .at("/isOld")
                .isNotText();
    }

    @Test
    void canApplyStandardSetOfAssertions() {
        assertJson("{\"root\":{\"name\":\"Mr Name\"}}")
                .is(ExamplesTest::theUsual)
                .isNotEmpty(); // additional clause
    }

    @Test
    void customCondition_isEvenNumber() {
        assertJson("42")
                .is("Even number", jsonNode -> jsonNode.isNumber() && jsonNode.asInt() % 2 == 0);
    }

    private static <T, A extends CoreJsonAssertion<T, A>> A theUsual(JsonNodeAssertDsl<T, A> assertion) {
        return assertion.at("/root/name").isText("Mr Name");
    }
}
