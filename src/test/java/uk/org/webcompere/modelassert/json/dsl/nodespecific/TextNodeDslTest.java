package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

import static uk.org.webcompere.modelassert.json.Patterns.GUID_PATTERN;
import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

class TextNodeDslTest {

    @Test
    void isText() {
        assertAllWays("\"Damian\"", "null",
                TextNodeDsl::isText);
    }

    @Test
    void isNotText() {
        assertAllWays("null", "\"Damian\"",
                TextNodeDsl::isNotText);
    }

    @Test
    void isTextWithValue() {
        assertAllWays("\"Damian\"", "\"Jason\"",
                assertion -> assertion.isText("Damian"));
    }

    @Test
    void isEmptyText() {
        assertAllWays("\"\"", "\"Jason\"",
                JsonNodeAssertDsl::isEmpty);
    }

    @Test
    void isNotEmptyText() {
        assertAllWays("\"Jason\"", "\"\"",
                JsonNodeAssertDsl::isNotEmptyText);
    }

    @Test
    void isNotEmptyTextDoesNotApplyToNonText() {
        assertAllWays("\"Jason\"", "false",
                JsonNodeAssertDsl::isNotEmptyText);
        assertAllWays("\"Jason\"", "null",
                JsonNodeAssertDsl::isNotEmptyText);
        assertAllWays("\"Jason\"", "[]",
                JsonNodeAssertDsl::isNotEmptyText);
        assertAllWays("\"Jason\"", "{}",
                JsonNodeAssertDsl::isNotEmptyText);
    }

    @Test
    void jsonAtMatches() {
        assertAllWays("{\"guid\":\"625110f5-502f-4748-8f1d-bad2237aa0aa\"}",
                "{\"guid\":\"not-a-guid\"}",
                assertion -> assertion.at("/guid").matches(GUID_PATTERN));
    }

    @Test
    void jsonAtMatchesByString() {
        assertAllWays("{\"guid\":\"625110f5-502f-4748-8f1d-bad2237aa0aa\"}",
                "{\"guid\":\"not-a-guid\"}",
                assertion -> assertion.at("/guid").matches(GUID_PATTERN.pattern()));
    }
}