package uk.org.webcompere.modelassert.json.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.org.webcompere.modelassert.json.dsl.JsonAssertDsl;

import static uk.org.webcompere.modelassert.json.JsonAssertions.jsonNode;
import static uk.org.webcompere.modelassert.json.Patterns.GUID_PATTERN;
import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

@DisplayName("Common assertions for the built in behaviour of all variants of the CoreJson assertion")
class CoreJsonAssertionTest {

    @Test
    void jsonAt() {
        assertAllWays("{\"name\":\"Jason\"}", "{\"name\":\"Damian\"}",
                assertion -> assertion.at("/name").isEqualTo("Jason"));
    }

    @Test
    void jsonAtIsNull() {
        assertAllWays("{\"name\":null}", "{\"name\":\"Damian\"}",
                assertion -> assertion.at("/name").isNull());
    }

    @Test
    void jsonAtIsMissing() {
        assertAllWays("{}", "{\"name\":\"Damian\"}",
                assertion -> assertion.at("/name").isMissing());
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

    @Test
    void jsonIsNull() {
        assertAllWays("null", "{\"name\":\"Damian\"}",
                JsonAssertDsl::isNull);
    }

    @Test
    void recursiveJsonMatching() {
        assertAllWays("{\"guid\":\"625110f5-502f-4748-8f1d-bad2237aa0aa\"}",
                "{\"guid\":\"not-a-guid\"}",
                assertion -> assertion.at("")
                        .matches(jsonNode()
                                .at("/guid")
                                .matches(GUID_PATTERN.pattern())));
    }
}