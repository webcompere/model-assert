package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

class ObjectNodesTest {

    @Test
    void whenUsingObjectNodeThenNodeMustBeObject() {
        assertThatThrownBy(() -> assertJson("{foo:[]}")
            .at("/foo").object().isEmpty())
            .isInstanceOf(AssertionFailedError.class);
    }

    @Test
    void whenUsingObjectNodeWithObjectThenIsEmptyWorks() {
        assertJson("{foo:{}}")
            .at("/foo").object().isEmpty();
    }

    @Test
    void whenUsingObjectNodeWithObjectThenIsNotEmptyWorks() {
        assertJson("{foo:{bar:\"bar\"}}")
            .at("/foo").object().isNotEmpty();
    }
}
