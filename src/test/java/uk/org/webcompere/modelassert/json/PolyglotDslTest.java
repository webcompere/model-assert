package uk.org.webcompere.modelassert.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.org.webcompere.modelassert.json.AssertJsonDsl.assertJson;
import static uk.org.webcompere.modelassert.json.HamcrestDsl.json;

@DisplayName("The JSON assertions can be expressed either as fluent assertions or hamcrest matchers")
class PolyglotDslTest {

    @Test
    void jsonAtInHamcrest() {
        assertThat("{\"name\":\"Bill\"}", json().at("/name").isEqualTo("Bill"));
    }

    @Test
    void multiJsonAtInHamcrest() {
        assertThat("{\"name\":\"Bill\", \"age\":42}",
                json().at("/name").isEqualTo("Bill")
                        .at("/age").isEqualTo(42));
    }

    @Test
    void jsonAtInHamcrestNegative() {
        assertThatThrownBy(() -> assertThat("{\"name\":\"Bill\"}", json().at("/name").isEqualTo("Not Bill")))
                .isInstanceOf(Error.class)
                .hasMessage("\nExpected: Path at /name is equal to Not Bill\n     but: /name was \"Bill\"");
    }

    @Test
    void jsonAtMultiInHamcrestNegative() {
        assertThatThrownBy(() -> assertThat("{\"name\":\"Bill\", \"age\":42}",
                json().at("/name").isEqualTo("Bill")
                        .at("/age").isEqualTo(12)))
                .isInstanceOf(Error.class)
                .hasMessage("\nExpected: Path at /name is equal to Bill\nPath at /age is equal to 12\n     but: /age was 42");
    }

    @Test
    void jsonAtInHamcrestBadJson() {
        assertThatThrownBy(() -> assertThat("{\"name\"", json().at("/name").isEqualTo("Not Bill")))
                .isInstanceOf(Error.class);
    }

    @Test
    void jsonAtWithAssertJson() {
        assertJson("{\"name\":\"Bill\"}")
                .at("/name")
                .isEqualTo("Bill");
    }

    @Test
    void jsonAtWithAssertJsonNegative() {
        assertThatThrownBy(() -> assertJson("{\"name\":\"Bill\"}").at("/name").isEqualTo("Not Bill"))
                .isInstanceOf(Error.class)
                .hasMessage("Expected: Path at /name is equal to Not Bill\n     but: /name was \"Bill\"");
    }

    @Test
    void multiJsonWithAssertJson() {
        assertJson("{\"name\":\"Bill\", \"age\":42}")
                .at("/name").isEqualTo("Bill")
                .at("/age").isEqualTo(42);
    }

    @Test
    void jsonAtMultiWithAssertJsonNegative() {
        assertThatThrownBy(() -> assertJson("{\"name\":\"Bill\", \"age\":42}")
                .at("/name").isEqualTo("Bill")
                        .at("/age").isEqualTo(12))
                .isInstanceOf(Error.class)
                .hasMessage("Expected: Path at /age is equal to 12\n     but: /age was 42");
    }
}
