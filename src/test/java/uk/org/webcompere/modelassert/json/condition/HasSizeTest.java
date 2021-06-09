package uk.org.webcompere.modelassert.json.condition;

import org.junit.jupiter.api.Test;

import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

class HasSizeTest {

    @Test
    void arrayHasSizeOne() {
        assertAllWays("[{}]", "[]",
                assertion -> assertion.hasSize(1));
    }

    @Test
    void stringHasSizeOne() {
        assertAllWays("\"A\"", "\"AAAAA\"",
                assertion -> assertion.hasSize(1));
    }

    @Test
    void objectHasSizeOne() {
        assertAllWays("{\"name\":\"A\"}", "{}",
                assertion -> assertion.hasSize(1));
    }

    @Test
    void sizeFailsWhenNotValidType() {
        assertAllWays("{}", "false",
                assertion -> assertion.hasSize(0));
    }

    @Test
    void arraySizeInBoundary() {
        assertAllWays("[1, 2, 3, 4]", "[]",
            assertion -> assertion.size().isBetween(2, 7));
    }
}
