package uk.org.webcompere.modelassert.json.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.ArrayNodeDsl;
import uk.org.webcompere.modelassert.json.dsl.nodespecific.ObjectNodeDsl;

import static uk.org.webcompere.modelassert.json.JsonAssertions.jsonNode;
import static uk.org.webcompere.modelassert.json.Patterns.GUID_PATTERN;
import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

@DisplayName("Common assertions for the built in behaviour of all variants of the CoreJson assertion")
class CoreJsonAssertionTest {

    @Test
    void jsonAt() {
        assertAllWays("{\"name\":\"Jason\"}", "{\"name\":\"Damian\"}",
                assertion -> assertion.at("/name").hasValue("Jason"));
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
    void jsonIsNull() {
        assertAllWays("null", "{\"name\":\"Damian\"}",
                JsonNodeAssertDsl::isNull);
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

    @Test
    void numericIsGreaterThan() {
        assertAllWays("{\"count\":10}", "{\"count\":9}",
                assertion -> assertion.at("/count").isGreaterThan(9));
    }

    @Test
    void intIsGreaterThan() {
        assertAllWays("{\"count\":10}", "{\"count\":9}",
                assertion -> assertion.at("/count").isGreaterThanInt(9));
    }

    @Test
    void longIsGreaterThan() {
        assertAllWays("{\"count\":10}", "{\"count\":9}",
                assertion -> assertion.at("/count").isGreaterThanLong(9));
    }

    @Test
    void longIsGreaterThanOrEqualTo() {
        assertAllWays("{\"count\":9}", "{\"count\":8}",
                assertion -> assertion.at("/count").isGreaterThanOrEqualToLong(9));
    }

    @Test
    void numericIsGreaterThanViaNumbersDsl() {
        assertAllWays("{\"count\":10}", "{\"count\":9}",
                assertion -> assertion.at("/count").number().isGreaterThan(9));
    }

    @Test
    void jsonAtStringInStringContext() {
        assertAllWays("{\"name\":\"Jason\"}", "{\"name\":\"Damian\"}",
                assertion -> assertion.at("/name")
                        .text().isText("Jason"));
    }

    @Test
    void jsonAtStringInStringContextDetectString() {
        assertAllWays("{\"name\":\"Jason\"}", "{\"name\":null}",
                assertion -> assertion.at("/name")
                        .text().isText());
    }

    @Test
    void recursiveAtIsPossibleEvenThoughPointless() {
        assertAllWays("{\"root\":{\"name\":\"Jason\"}}", "{\"root\":{\"name\":\"Damo\"}}",
                assertion -> assertion.at("/root").at("/name")
                        .text().isText("Jason"));
    }

    @Test
    void isObjectOnRoot() {
        assertAllWays("{}", "null",
                ObjectNodeDsl::isObject);
    }

    @Test
    void isNotObjectOnRoot() {
        assertAllWays("123", "{}",
                ObjectNodeDsl::isNotObject);
    }

    @Test
    void isObjectOnRootViaObjectContext() {
        assertAllWays("{}", "null",
                assertion -> assertion.object().isObject());
    }

    @Test
    void isArrayOnRoot() {
        assertAllWays("[]", "{}", ArrayNodeDsl::isArray);
    }

    @Test
    void isNotArrayOnRoot() {
        assertAllWays("{}", "[]", ArrayNodeDsl::isNotArray);
    }

    @Test
    void isNotArrayContextOnRoot() {
        assertAllWays("{}", "[]",
                assertion -> assertion.isNotArray());
    }
}
