package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;

import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

class NumericNodeDslTest {

    @Test
    void integerEqual() {
        assertAllWays("42", "0",
                assertion -> assertion.isNumberEqualTo(42));
    }

    @Test
    void integerNotEqual() {
        assertAllWays("42", "43",
                assertion -> assertion.isNumberNotEqualTo(43));
    }

    @Test
    void longEqual() {
        assertAllWays("21474836470", "0",
                assertion -> assertion.isNumberEqualTo(21474836470L));
    }

    @Test
    void integerGreaterThan() {
        assertAllWays("42", "41",
                assertion -> assertion.isGreaterThanInt(41));
    }

    @Test
    void integerGreaterThanOrEqualTo() {
        assertAllWays("42", "41",
                assertion -> assertion.isGreaterThanOrEqualToInt(42));
    }

    @Test
    void integerLessThanOrEqualTo() {
        assertAllWays("42", "43",
                assertion -> assertion.isLessThanOrEqualToInt(42));
    }

    @Test
    void longLessThanOrEqualTo() {
        assertAllWays("42", "43",
                assertion -> assertion.isLessThanOrEqualToLong(42L));
    }

    @Test
    void longGreaterThan() {
        assertAllWays("42", "41",
                assertion -> assertion.isGreaterThanLong(41L));
    }

    @Test
    void doubleEqualTo() {
        assertAllWays("1.23", "3.21",
                assertion -> assertion.isNumberEqualTo(1.23d));
    }

    @Test
    void doubleEqualToNoD() {
        assertAllWays("1.23", "1.23001",
                assertion -> assertion.isNumberEqualTo(1.23));
    }

    @Test
    void doubleGreaterThan() {
        assertAllWays("1.23001", "1.23",
                assertion -> assertion.isGreaterThanDouble(1.23));
    }

    @Test
    void doubleGreaterThanOrEqualTo() {
        assertAllWays("1.23", "1.229999",
                assertion -> assertion.isGreaterThanOrEqualToDouble(1.23));
    }

    @Test
    void doubleLessThanOrEqualTo() {
        assertAllWays("1.23", "1.2300001",
                assertion -> assertion.isLessThanOrEqualToDouble(1.23));
    }

    @Test
    void numberLessThan() {
        assertAllWays("41", "42",
                assertion -> assertion.isLessThan(42));
    }

    @Test
    void intLessThan() {
        assertAllWays("41", "42",
                assertion -> assertion.isLessThanInt(42));
    }

    @Test
    void longLessThan() {
        assertAllWays("41", "42",
                assertion -> assertion.isLessThanLong(42));
    }

    @Test
    void doubleLessThan() {
        assertAllWays("41.99999", "42",
                assertion -> assertion.isLessThanDouble(42));
    }

    @Test
    void doubleIsZero() {
        assertAllWays("0.0", "0.1",
            NumberComparisonDsl::isZero);
    }

    @Test
    void zeroIsZero() {
        assertAllWays("0", "123",
            NumberComparisonDsl::isZero);
    }

    @Test
    void isGreaterThanOrEqualToWhenEqual() {
        assertAllWays("12", "10",
            assertion -> assertion.isGreaterThanOrEqualTo(11));
    }

    @Test
    void isGreaterThanOrEqualToWhenGreater() {
        assertAllWays("13", "10",
            assertion -> assertion.isGreaterThanOrEqualTo(11));
    }

    @Test
    void isLessThanOrEqualToWhenEqual() {
        assertAllWays("12", "13",
            assertion -> assertion.isLessThanOrEqualTo(12));
    }

    @Test
    void isLessThanOrEqualToWhenLess() {
        assertAllWays("10", "12",
            assertion -> assertion.isLessThanOrEqualTo(11));
    }

    @Test
    void isBetweenWhenBetween() {
        assertAllWays("5", "0",
            assertion -> assertion.isBetween(2, 7));
    }

    @Test
    void isBetweenWhenLowerLimit() {
        assertAllWays("2", "0",
            assertion -> assertion.isBetween(2, 7));
    }

    @Test
    void isBetweenWhenUpperLimit() {
        assertAllWays("7", "0",
            assertion -> assertion.isBetween(2, 7));
    }

    @Test
    void isBetweenWhenUpperLimitAndIncorrectIsAfterUpper() {
        assertAllWays("7", "8",
            assertion -> assertion.isBetween(2, 7));
    }
}
