package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;

import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

class TreeComparisonDslTest {

    @Test
    void canCompareStringNodes() {
        assertAllWays("\"string\"", "\"not the string\"",
            assertion -> assertion.isEqualTo("\"string\""));
    }

    @Test
    void stringNodeWontMatchIfWrongTye() {
        assertAllWays("\"true\"", "true",
            assertion -> assertion.isEqualTo("\"true\""));

        assertAllWays("\"true\"", "{}",
            assertion -> assertion.isEqualTo("\"true\""));
    }

    @Test
    void twoEmptyObjectsAreTheSame() {
        assertAllWays("{}", "{\"name\":null}",
            assertion -> assertion.isEqualTo("{}"));
    }

    @Test
    void emptyObjectDoesNotMatchWithNull() {
        assertAllWays("{}", "null",
            assertion -> assertion.isEqualTo("{}"));
    }

    @Test
    void keysMustBeInCorrectOrder() {
        assertAllWays("{\"name\":null, \"age\":42, \"isOk\":true}",
            "{\"name\":null, \"isOk\":true, \"age\":42}",
            assertion -> assertion.isEqualTo("{\"name\":null, \"age\":42, \"isOk\":true}"));
    }

    @Test
    void keysMustAllBePresent() {
        assertAllWays("{\"name\":null, \"age\":42, \"isOk\":true}",
            "{\"age\":42, \"isOk\":true}",
            assertion -> assertion.isEqualTo("{\"name\":null, \"age\":42, \"isOk\":true}"));
    }

    @Test
    void arrayOfZeroSizeMatches() {
        assertAllWays("[]", "[1]",
            assertion -> assertion.isEqualTo("[]"));
    }

    @Test
    void arrayOfZeroSizeWontMatchIncorrectType() {
        assertAllWays("[]", "null",
            assertion -> assertion.isEqualTo("[]"));
    }

    @Test
    void arraysOfOneElementMatch() {
        assertAllWays("[1]", "[]",
            assertion -> assertion.isEqualTo("[1]"));
    }

    @Test
    void arraysOfTwoElementsMatchInOrder() {
        assertAllWays("[1, 2]", "[2, 1]",
            assertion -> assertion.isEqualTo("[1, 2]"));
    }
}
