package uk.org.webcompere.modelassert.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.org.webcompere.modelassert.json.condition.ConditionList;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;
import static uk.org.webcompere.modelassert.json.condition.ConditionList.conditions;

@DisplayName("Some usage examples")
class ExamplesTest {

    @Test
    void isText() {
        assertJson("\"theText\"")
            .isText();

        assertJson("\"theText\"")
            .isText("theText");

        assertJson("{\"child\":{\"age\":123}}")
            .at("/child/age").isNotText();
    }

    @Test
    void emptyText() {
        assertJson("\"\"")
            .isEmptyText();

        // wrong type - so this would be an assertion failure
        assertThatThrownBy(() -> assertJson("0")
            .isNotEmptyText())
            .isInstanceOf(AssertionFailedError.class);

        // non empty string passes assertion
        assertJson("\"0\"")
            .isNotEmptyText();
    }

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
    void assertNullWithIsNull() {
        assertJson("null")
            .isNull();
    }

    @Test
    void assertNullWithHasValue() {
        assertJson("null")
            .hasValue(null);
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

    @Test
    void customCondition_isNotEvenNumber() {
        assertJson("43")
            .isNot("Even number", jsonNode -> jsonNode.isNumber() && jsonNode.asInt() % 2 == 0);
    }

    @Test
    void complexAssertionsOnArrayWithConditionList() {
        assertJson("[" +
            "{\"name\":\"Model\",\"ok\":true}," +
            "{\"name\":\"Model\",\"ok\":false}," +
            "{\"name\":\"Assert\"}," +
            "{\"age\":1234}" +
            "]")
            .isArrayContainingExactlyInAnyOrder(conditions()
                .at("/name").isText("Assert")
                .at("/name").hasValue("Model")
                .at("/ok").isFalse()
                .at("/age").isNumberEqualTo(1234));
    }

    @Test
    void deeperComplexityOnArrayWithConditionList() {
        assertJson("[" +
            "{\"name\":\"Model\",\"ok\":true}," +
            "{\"name\":\"Model\",\"ok\":false}," +
            "{\"name\":\"Model\"}," +
            "{\"age\":1234}" +
            "]")
            .isArrayContainingExactlyInAnyOrder(conditions()
                .at("/name").isText("Model")
                .satisfies(conditions()
                    .at("/name").hasValue("Model")
                    .at("/ok").isTrue())
                .satisfies(conditions()
                    .at("/ok").isFalse()
                    .at("/name").isText("Model"))
                .at("/age").isNumberEqualTo(1234));
    }

    @Test
    void subtreeAssertionsWithConditionList() {
        assertJson("[" +
            "{\"name\":\"Model\",\"ok\":true}," +
            "{\"name\":\"Model\",\"ok\":false}," +
            "{\"name\":\"Model\"}," +
            "{\"age\":1234}" +
            "]")
            .at("/1").satisfies(conditions()
                    .at("/name").hasValue("Model")
                    .at("/ok").isFalse());
    }

    private static <A> A theUsual(JsonNodeAssertDsl<A> assertion) {
        return assertion.at("/root/name").isText("Mr Name");
    }
}
