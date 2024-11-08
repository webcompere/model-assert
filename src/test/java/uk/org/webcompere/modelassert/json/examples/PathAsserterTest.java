package uk.org.webcompere.modelassert.json.examples;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

class PathAsserterTest {
    private static final String EXAMPLE_JSON =
        "{" +
            "\"make\":\"VW\"," +
            "\"model\":\"Golf Plus\"," +
            "\"owner\":\"\"," +
            "\"color\":null" +
            "}";

    @Test
    void fluentAssertionExample() {
        assertJson(EXAMPLE_JSON)
            .at("/make").isText("VW")
            .at("/model").isText("Golf Plus")
            .at("/owner").isEmpty()
            .at("/color").isNull()
            .at("/make").isNotNull()
            .at("/owner").isNotNull();
    }

    @Test
    void isEmptyWithTextSpecific() {
        assertJson(EXAMPLE_JSON)
            .at("/owner").text().isEmpty();
    }

    @Test
    void isEmptyAsTextOnNullFails() {
        assertThatThrownBy(() -> assertJson(EXAMPLE_JSON)
            .at("/color").text().isEmpty())
            .isInstanceOf(AssertionFailedError.class);
    }

    @Test
    void isNotEmptyWithTextSpecific() {
        assertJson(EXAMPLE_JSON)
            .at("/model").text().isNotEmpty();
    }

    @Test
    void foundPropertyCanBeAsserted() {
        assertJson(EXAMPLE_JSON)
            .at("/make").isText("VW");
    }

    @Test
    void foundPropertyWithIncorrectAssertionIsError() {
        assertJson(EXAMPLE_JSON)
            .at("/make").isNotText("Ford");
    }

    @Test
    void isEmptyOnEmptyPasses() {
        assertJson(EXAMPLE_JSON)
            .at("/owner").isEmpty();
    }

    @Test
    void isEmptyOnNonEmptyFails() {
        assertJson(EXAMPLE_JSON)
            .at("/make").isNotEmpty();
    }

    @Test
    void startsWithWhenCorrectPasses() {
        assertJson(EXAMPLE_JSON)
            .at("/model").textStartsWith("Golf");
    }

    @Test
    void doesNotStartWith() {
        assertJson(EXAMPLE_JSON)
            .at("/model").textDoesNotStartWith("Beetle");
    }

    @Test
    void containsWhenCorrectPasses() {
        assertJson(EXAMPLE_JSON)
            .at("/model").textContains("Plus");
    }

    @Test
    void containsWhenIncorrectFails() {
        assertJson(EXAMPLE_JSON)
            .at("/model").textDoesNotContain("Guice");
    }

    @Test
    void isNotNullWhenCorrectPasses() {
        assertJson(EXAMPLE_JSON)
            .at("/model").isNotNull();
    }

    @Test
    void isNullWhenCorrectPasses() {
        assertJson(EXAMPLE_JSON)
            .at("/color").isNull();
    }

    @Test
    void failedAssertionContainsHelpfulMessageWithAllInputsIn() {
        assertThatThrownBy(() -> assertJson(EXAMPLE_JSON)
            .at("/model").textContains("Guice"))
            .hasMessageContaining("model")
            .hasMessageContaining("Guice");
    }
}
