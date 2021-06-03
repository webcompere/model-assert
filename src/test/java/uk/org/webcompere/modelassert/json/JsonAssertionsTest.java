package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.org.webcompere.modelassert.json.JsonAssertions.*;

@DisplayName("The JSON assertions can be expressed either as fluent assertions or hamcrest matchers")
class JsonAssertionsTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Path SIMPLE_JSON = Paths.get("src", "test", "resources", "simple.json");

    @Test
    void jsonAtInHamcrest() {
        assertThat("{\"name\":\"Bill\"}", json().at("/name").hasValue("Bill"));
    }

    @Test
    void multiJsonAtInHamcrest() {
        assertThat("{\"name\":\"Bill\", \"age\":42}",
                json().at("/name").hasValue("Bill")
                        .at("/age").hasValue(42));
    }

    @Test
    void jsonAtInHamcrestNegative() {
        assertThatThrownBy(() -> assertThat("{\"name\":\"Bill\"}", json().at("/name").hasValue("Not Bill")))
                .isInstanceOf(Error.class)
                .hasMessage("\nExpected: Json is not null\nPath at /name is equal to Not Bill\n" +
                        "     but: /name is equal to Not Bill was \"Bill\"");
    }

    @Test
    void jsonAtMultiInHamcrestNegative() {
        assertThatThrownBy(() -> assertThat("{\"name\":\"Bill\", \"age\":42}",
                json().at("/name").hasValue("Bill")
                        .at("/age").hasValue(12)))
                .isInstanceOf(Error.class)
                .hasMessage("\nExpected: Json is not null\nPath at /name is equal to Bill\nPath at /age is equal to 12\n" +
                        "     but: /age is equal to 12 was 42");
    }

    @Test
    void jsonAtInHamcrestBadJson() {
        assertThatThrownBy(() -> assertThat("{\"name\"", json().at("/name").hasValue("Not Bill")))
                .isInstanceOf(Error.class);
    }

    @Test
    void jsonAtWithAssertJson() {
        assertJson("{\"name\":\"Bill\"}")
                .at("/name")
                .hasValue("Bill");
    }

    @Test
    void jsonAtWithAssertJsonNegative() {
        assertThatThrownBy(() -> assertJson("{\"name\":\"Bill\"}").at("/name").hasValue("Not Bill"))
                .isInstanceOf(Error.class)
                .hasMessage("Expected: Path at /name is equal to Not Bill\n     but: /name is equal to Not Bill was \"Bill\"");
    }

    @Test
    void multiJsonWithAssertJson() {
        assertJson("{\"name\":\"Bill\", \"age\":42}")
                .at("/name").hasValue("Bill")
                .at("/age").hasValue(42);
    }

    @Test
    void jsonAtMultiWithAssertJsonNegative() {
        assertThatThrownBy(() -> assertJson("{\"name\":\"Bill\", \"age\":42}")
                .at("/name").hasValue("Bill")
                        .at("/age").hasValue(12))
                .isInstanceOf(Error.class)
                .hasMessage("Expected: Path at /age is equal to 12\n     but: /age is equal to 12 was 42");
    }

    @Test
    void canAssertWithJsonNode() throws Exception {
        JsonNode node = OBJECT_MAPPER.readTree("{\"name\":\"John\"}");
        assertJson(node)
                .at("/name")
                .hasValue("John");
    }

    @Test
    void canUseHamcrestAssertWithJsonNode() throws Exception {
        JsonNode node = OBJECT_MAPPER.readTree("{\"name\":\"John\"}");
        assertThat(node, jsonNode()
                .at("/name")
                .hasValue("John"));
    }

    @Test
    void canAssertWithPath() {
        assertJson(SIMPLE_JSON)
                .at("/child/name")
                .hasValue("Ms Child");
    }

    @Test
    void canUseHamcrestAssertWithPath() {
        assertThat(SIMPLE_JSON, jsonFilePath()
            .at("/child/name")
            .hasValue("Ms Child"));
    }

    @Test
    void canUseHamcrestAssertWithFile() {
        assertThat(SIMPLE_JSON.toFile(), jsonFile()
                .at("/child/name")
                .hasValue("Ms Child"));
    }
}
