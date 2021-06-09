package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;
import static uk.org.webcompere.modelassert.json.PathWildCard.ANY;
import static uk.org.webcompere.modelassert.json.PathWildCard.ANY_SUBTREE;

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

    @Test
    void keysMustBeInCorrectOrderDownTheTree() {
        assertAllWays("{\"a\":1, \"b\":2, \"c\":{\"d\":3, \"e\":4}}",
            "{\"b\":2, \"a\":1, \"c\":{\"d\":3, \"e\":4}}",
            assertion -> assertion.isEqualTo("{\"a\":1, \"b\":2, \"c\":{\"d\":3, \"e\":4}}"));
    }

    @Test
    void keysCanBeInLooseOrderDownTheTree() {
        assertAllWays("{\"b\":2, \"a\":1, \"c\":{\"e\":4, \"d\":3}}",
            "{\"b\":1, \"a\":1, \"c\":{\"d\":3, \"e\":4}}",
            assertion -> assertion.where().keysInAnyOrder()
                .isEqualTo("{\"a\":1, \"b\":2, \"c\":{\"d\":3, \"e\":4}}"));
    }

    @Test
    void keysCanBeInLooseOrderInSubTree() {
        assertAllWays("{\"a\":1, \"b\":2, \"c\":{\"d\":3, \"e\":4}}",
            "{\"b\":2, \"a\":1, \"c\":{\"d\":3, \"e\":4}}",
            assertion -> assertion.where()
                .path("c").keysInAnyOrder()
                .isEqualTo("{\"a\":1, \"b\":2, \"c\":{\"e\":4, \"d\":3}}"));
    }

    @Test
    void keysCanBeInLooseOrderInSubTreeByAt() {
        assertAllWays("{\"a\":1, \"b\":2, \"c\":{\"d\":3, \"e\":4}}",
            "{\"b\":2, \"a\":1, \"c\":{\"d\":3, \"e\":4}}",
            assertion -> assertion.where()
                .at("/c").keysInAnyOrder()
                .isEqualTo("{\"a\":1, \"b\":2, \"c\":{\"e\":4, \"d\":3}}"));
    }

    @Test
    void keysCanBeInLooseOrderInSubTreeByRegex() {
        assertAllWays("{\"a\":1, \"b\":2, \"c\":{\"d\":3, \"e\":4}}",
            "{\"b\":2, \"a\":1, \"c\":{\"d\":3, \"e\":4}}",
            assertion -> assertion.where()
                .path(Pattern.compile("c")).keysInAnyOrder()
                .isEqualTo("{\"a\":1, \"b\":2, \"c\":{\"e\":4, \"d\":3}}"));
    }

    @Test
    void keysCanBeInLooseOrderInSubTreeByWildCard() {
        assertAllWays("{\"a\":1, \"b\":2, \"c\":{\"d\":3, \"e\":4}}",
            "{\"b\":2, \"a\":1, \"c\":{\"d\":3, \"e\":4}}",
            assertion -> assertion.where()
                .path(ANY).keysInAnyOrder()
                .isEqualTo("{\"a\":1, \"b\":2, \"c\":{\"e\":4, \"d\":3}}"));
    }

    @Test
    void keysCanBeInLooseOrderInSubTreeBySubtreeWildCardAndField() {
        assertAllWays("{\"a\":1, \"b\":2, \"c\":{\"d\":3, \"e\":4}}",
            "{\"b\":2, \"a\":1, \"c\":{\"d\":3, \"e\":4}}",
            assertion -> assertion.where()
                .path(ANY_SUBTREE, "c").keysInAnyOrder()
                .isEqualTo("{\"a\":1, \"b\":2, \"c\":{\"e\":4, \"d\":3}}"));
    }
}
