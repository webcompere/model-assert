package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

class ArrayNodesTest {

    @Test
    void whenUsingArrayNodeThenNodeMustBeArray() {
        assertThatThrownBy(() -> assertJson("{foo:{}}")
            .at("/foo").array().isEmpty())
            .isInstanceOf(AssertionFailedError.class);
    }

    @Test
    void whenUsingArrayNodeWithArrayThenIsEmptyWorks() {
        assertJson("{foo:[]}")
            .at("/foo").array().isEmpty();
    }

    @Test
    void whenUsingArrayNodeWithArrayThenIsNotEmptyWorks() {
        assertJson("{foo:[\"bar\"]}")
            .at("/foo").array().isNotEmpty();
    }
}
