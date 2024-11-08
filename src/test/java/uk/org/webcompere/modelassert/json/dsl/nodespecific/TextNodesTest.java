package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

class TextNodesTest {

    @Test
    void whenUsingTextNodeNodeMustBeText() {
        assertThatThrownBy(() -> assertJson("{foo:[]}")
            .at("/foo").text().isEmpty())
            .isInstanceOf(AssertionFailedError.class);
    }

    @Test
    void whenUsingTextNodeWithTextIsEmptyWorks() {
        assertJson("{foo:\"\"}")
            .at("/foo").text().isEmpty();
    }

    @Test
    void whenUsingTextNodeWithTextIsNotEmptyWorks() {
        assertJson("{foo:\"bar\"}")
            .at("/foo").text().isNotEmpty();
    }
}
