package uk.org.webcompere.modelassert.json;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.org.webcompere.modelassert.json.condition.HasSize;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.JsonAssertions.*;
import static uk.org.webcompere.modelassert.json.Patterns.GUID_PATTERN;
import static uk.org.webcompere.modelassert.json.condition.ConditionList.conditions;
import static uk.org.webcompere.modelassert.json.condition.MatchesTextCondition.textMatches;
import static uk.org.webcompere.modelassert.json.PathWildCard.ANY_SUBTREE;

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
    void assertTheLengthOfString() {
        assertJson("\"some string\"")
            .hasSize(11);
    }

    @Test
    void lengthOfAnArray() {
        assertJson("[1, 2, 3]")
            .hasSize(3);
    }

    @Test
    void lengthOfAnArrayBetweenValues() {
        assertJson("[1, 2, 3]")
            .size().isBetween(3, 9);
    }

    @Test
    void canApplyStandardSetOfAssertions() {
        assertJson("{\"root\":{\"name\":\"Mr Name\"}}")
                .is(ExamplesTest::theUsual)
                .isNotEmpty(); // additional clause
    }

    @Test
    void canUseGreaterThanWithInteger() {
        assertJson("{number:12}")
            .at("/number").isGreaterThan(10);
    }

    @Test
    void canUseBetweenWithInteger() {
        assertJson("{number:12}")
            .at("/number").isBetween(2, 29);
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

    @Test
    void compareWholeJsonTree() {
        assertJson(Paths.get("src", "test", "resources", "simple.json"))
            .isEqualTo(Paths.get("src", "test", "resources", "simple-copy.json"));
    }

    @Test
    void compareWholeJsonTreeByString() {
        assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
            .isEqualTo("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}");
    }

    @Test
    void compareWholeJsonTreeByStringHamcrest() {
        MatcherAssert.assertThat("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}",
            json().isEqualTo("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}"));
    }

    @Test
    void compareWholeJsonTreeByStringAndNotEqual() {
        assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
            .isNotEqualTo("{\"versions\":[1.00, 1.01, 1.02], \"name\":\"ModelAssert\"}");
    }

    @Test
    void compareWholeJsonTreeByStringWithKeyOrderingRelaxed() {
        assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
            .where()
                .keysInAnyOrder()
            .isEqualTo("{\"versions\":[1.00, 1.01, 1.02], \"name\":\"ModelAssert\"}");
    }

    @Test
    void compareJsonSubTree() {
        assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
            .at("/versions")
            .isEqualTo("[1.00, 1.01, 1.02]");
    }

    @Test
    void replacePathWithCondition() {
        assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
            .where().path("versions").isArrayContaining(1.01d)
            .isEqualTo("{\"name\":\"ModelAssert\"}");
    }

    @Test
    void replacePathWithIgnore() {
        assertJson("{\"name\":\"ModelAssert\",\"versions\":[1.00, 1.01, 1.02]}")
            .where().path("versions").isIgnored()
            .isEqualTo("{\"name\":\"ModelAssert\"}");
    }

    @Test
    void matchesAnyGuid() {
        assertJson("{\"a\":{\"guid\":\"fa82142d-13d2-49c4-9878-619c90a9f986\"}," +
            "\"b\":{\"guid\":\"96734f31-33c3-4e50-a72b-49bf2d990e33\"}," +
            "\"c\":{\"guid\":\"064c8c5a-c9c1-4ea0-bf36-1994104aa870\"}}")
            .where().path(ANY_SUBTREE, "guid").matches(GUID_PATTERN)
            .isEqualTo("{\"a\":{\"guid\":\"?\"}," +
                "\"b\":{\"guid\":\"?\"}," +
                "\"c\":{\"guid\":\"?\"}}");
    }

    @Test
    void matchesGuidsUsingAt() {
        assertJson("{\"a\":{\"guid\":\"fa82142d-13d2-49c4-9878-619c90a9f986\"}," +
            "\"b\":{\"guid\":\"96734f31-33c3-4e50-a72b-49bf2d990e33\"}," +
            "\"c\":{\"guid\":\"064c8c5a-c9c1-4ea0-bf36-1994104aa870\"}}")
            .where()
                .at("/a/guid").matches(GUID_PATTERN)
                .at("/b/guid").matches(GUID_PATTERN)
                .at("/c/guid").matches(GUID_PATTERN)
            .isEqualTo("{\"a\":{\"guid\":\"?\"}," +
                "\"b\":{\"guid\":\"?\"}," +
                "\"c\":{\"guid\":\"?\"}}");
    }

    @Test
    void isNotMatchesForNonGuid() {
        assertJson("{\"a\":{\"guid\":\"fa82142d-13d2\"}," +
            "\"b\":{\"guid\":\"96734f31-33c3-4e50\"}," +
            "\"c\":{\"guid\":\"064c8c5a-c9c1-4ea0\"}}")
            .where().path(ANY_SUBTREE, "guid").matches(GUID_PATTERN)
            .isNotEqualTo("{\"a\":{\"guid\":\"?\"}," +
                "\"b\":{\"guid\":\"?\"}," +
                "\"c\":{\"guid\":\"?\"}}");
    }

    @Test
    void usingAndLogic() {
        assertJson("\"some string\"").satisfies(
            textMatches(Pattern.compile("[a-z ]+"))
                .and(new HasSize(11)));
    }

    @Test
    void usingAndLogicWithNot() {
        assertJson("\"some string\"").satisfies(
            textMatches(Pattern.compile("[a-z ]+"))
                .and(new HasSize(12))
                .inverted());
    }

    @Test
    void usingOrLogic() {
        assertJson("\"some string\"").satisfies(
            textMatches(Pattern.compile("[0-9]+"))
                .or(new HasSize(11)));
    }

    @Test
    void jacksonWithNonQuotedFieldNames() {
        assertJson("{field:\"some string\"}")
            .at("/field").isText("some string");
    }

    @Test
    void canCompareObjects() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("a", UUID.randomUUID().toString());
        objectMap.put("b", UUID.randomUUID().toString());
        assertJson(objectMap)
            .where()
               .path(Pattern.compile("[ab]")).matches(GUID_PATTERN)
            .isEqualTo("{\"a\":\"\",\"b\":\"\"}");
    }

    @Test
    void canCompareObjectsWithHamcrest() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("a", UUID.randomUUID().toString());
        objectMap.put("b", UUID.randomUUID().toString());
        MatcherAssert.assertThat(objectMap, jsonObject()
            .where()
            .path(Pattern.compile("[ab]")).matches(GUID_PATTERN)
            .isEqualTo("{\"a\":\"\",\"b\":\"\"}"));
    }

    @Test
    void canCompareObjectToObject() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("a", UUID.randomUUID().toString());
        objectMap.put("b", UUID.randomUUID().toString());

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("a", "");
        expectedMap.put("b", "");

        assertJson(objectMap)
            .where()
            .path(Pattern.compile("[ab]")).matches(GUID_PATTERN)
            .isEqualTo(expectedMap);
    }

    @Test
    void canCompareTwoYamls() {
        String yaml1 =
                "name: Mr Yaml\n" +
                "age: 42\n" +
                "items:\n" +
                "  - a\n" +
                "  - b\n";

        String yaml2 =
            "name: Mr Yaml\n" +
                "age: 42\n" +
                "items:\n" +
                "  - a\n" +
                "  - b\n";

        assertYaml(yaml1)
            .isEqualToYaml(yaml2);
    }

    @Test
    void canCompareTwoYamlWhenNotEqual() {
        String yaml1 =
            "name: Mr Yaml\n" +
                "age: 42\n" +
                "items:\n" +
                "  - a\n" +
                "  - b\n";

        String yaml2 =
            "name: Mrs Yaml\n" +
                "age: 43\n" +
                "items:\n" +
                "  - c\n" +
                "  - d\n";

        assertYaml(yaml1)
            .isNotEqualToYaml(yaml2);
    }

    @Test
    void canCompareTwoYamlsHamcrestStyle() {
        String yaml1 =
            "name: Mr Yaml\n" +
                "age: 42\n" +
                "items:\n" +
                "  - a\n" +
                "  - b\n";

        String yaml2 =
            "name: Mr Yaml\n" +
                "age: 42\n" +
                "items:\n" +
                "  - a\n" +
                "  - b\n";

        MatcherAssert.assertThat(yaml1, yaml().isEqualToYaml(yaml2));
    }

    @Test
    void canCompareJsonWithYaml() {
        assertJson("[1, 2, 3, 4]")
            .isEqualToYaml("- 1\n- 2\n- 3\n- 4");
    }

    private static <A> A theUsual(JsonNodeAssertDsl<A> assertion) {
        return assertion.at("/root/name").isText("Mr Name");
    }
}
