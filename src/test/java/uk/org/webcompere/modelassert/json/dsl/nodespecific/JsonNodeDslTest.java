package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

class JsonNodeDslTest {

    @Test
    void isText() {
        assertAllWays("\"Damian\"", "null",
            JsonNodeAssertDsl::isText);
    }

    @Test
    void isNotText() {
        assertAllWays("null", "\"Damian\"",
            JsonNodeAssertDsl::isNotText);
    }

    @Test
    void canDetectArray() {
        assertAllWays("[]", "{}",
            JsonNodeAssertDsl::isArray);
    }

    @Test
    void canDetectNonArray() {
        assertAllWays("{}", "[]",
            JsonNodeAssertDsl::isNotArray);
    }

    @Test
    void objectIsObject() {
        assertAllWays("{}", "null",
            JsonNodeAssertDsl::isObject);
    }

    @Test
    void nonObjectIsNotObject() {
        assertAllWays("[]", "{}",
            JsonNodeAssertDsl::isNotObject);
    }
}
