package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;

import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;
import static uk.org.webcompere.modelassert.json.condition.ConditionList.conditions;

class ArrayNodeDslTest {

    @Test
    void canDetectArray() {
        assertAllWays("[]", "{}",
            ArrayNodeDsl::isArray);
    }

    @Test
    void canDetectNonArray() {
        assertAllWays("{}", "[]",
            ArrayNodeDsl::isNotArray);
    }

    @Test
    void isArrayContainingStrings() {
        assertAllWays("[\"a\",\"b\",\"c\"]", "[]",
            assertion -> assertion.isArrayContaining("b", "c"));
    }

    @Test
    void isArrayContainingNumbers() {
        assertAllWays("[1, 2, 3, 4]", "[]",
            assertion -> assertion.isArrayContaining(2, 4));
    }

    @Test
    void isArrayContainingNull() {
        assertAllWays("[null]", "[]",
            assertion -> assertion.isArrayContaining(null));
    }

    @Test
    void factorInDuplicatesWhereSecondParameterIsNull() {
        assertAllWays("[null, null]", "[null]",
            assertion -> assertion.isArrayContaining(null, null));
    }

    @Test
    void factorInDuplicates() {
        assertAllWays("[null, null, null]", "[null]",
            assertion -> assertion.isArrayContaining(null, null, null));
    }

    @Test
    void isArrayContainingNumbersExactly() {
        assertAllWays("[1, 2, 3, 4]", "[1, 2, 3, 4, 5]",
            assertion -> assertion.isArrayContainingExactly(1, 2, 3, 4));
    }

    @Test
    void isArrayContainingNumbersExactly_whereFailsBecauseOfOrder() {
        assertAllWays("[1, 2, 3, 4]", "[4, 3, 2, 1]",
            assertion -> assertion.isArrayContainingExactly(1, 2, 3, 4));
    }

    @Test
    void isArrayContainingNumbersExactly_inAnyOrder() {
        assertAllWays("[1, 2, 3, 4]", "[4, 3, 2, 1, 0]",
            assertion -> assertion.isArrayContainingExactlyInAnyOrder(4, 3, 2, 1));
    }

    @Test
    void isArrayContainingMixOfValuesExactly_inAnyOrder() {
        assertAllWays("[1, null, \"three\", true]", "[4, 3, 2, 1, 0]",
            assertion -> assertion.isArrayContainingExactlyInAnyOrder(1, null, "three", true));
        assertAllWays("[1, null, \"three\", true]", "[4, 3, 2, 1, 0]",
            assertion -> assertion.isArrayContainingExactlyInAnyOrder(null,1,  true, "three"));
    }

    @Test
    void isArrayContainingNumbersExactlyAsConditions() {
        assertAllWays("[1, 2, 3, 4]", "[1, 2, 3, 4, 5]",
            assertion -> assertion.isArrayContainingExactly(conditions()
                .hasValue(1)
                .hasValue(2)
                .hasValue(3)
                .hasValue(4)));
    }

    @Test
    void isArrayContainingNumbersExactlyAsConditions_whereFailsBecauseOfOrder() {
        assertAllWays("[1, 2, 3, 4]", "[4, 3, 2, 1]",
            assertion -> assertion.isArrayContainingExactly(conditions()
                .hasValue(1)
                .hasValue(2)
                .hasValue(3)
                .hasValue(4)));
    }

    @Test
    void isArrayContainingNumbersExactlyAsConditions_inAnyOrder() {
        assertAllWays("[1, 2, 3, 4]", "[4, 3, 2, 1, 0]",
            assertion -> assertion.isArrayContainingExactlyInAnyOrder(conditions()
                .hasValue(4)
                .hasValue(3)
                .hasValue(2)
                .hasValue(1)));
    }
}
