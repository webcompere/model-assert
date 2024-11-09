package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;
import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

class BooleanNodeDslTest {

    @Test
    void booleanIsTrue() {
        assertAllWays("{\"good\":true}", "{\"good\":false}",
                assertion -> assertion.at("/good").isTrue());
    }

    @Test
    void booleanIsFalse() {
        assertAllWays("{\"good\":false}", "{\"good\":true}",
                assertion -> assertion.at("/good").isFalse());
    }

    @Test
    void booleanIsBoolean() {
        assertAllWays("{\"good\":false}", "{\"good\":0}",
                assertion -> assertion.at("/good").isBoolean());
    }

    @Test
    void booleanIsNotBoolean() {
        assertAllWays("{\"good\":1}", "{\"good\":true}",
                assertion -> assertion.at("/good").isNotBoolean());
    }

    @Test
    void booleanIsTrueInBooleanContext() {
        assertAllWays("{\"good\":true}", "{\"good\":false}",
                assertion -> assertion.at("/good").booleanNode().isTrue());
    }

    @Test
    void whenUsingBooleanNodeThenNodeMustBeBoolean() {
        assertThatThrownBy(() -> assertJson("{foo:{}}")
            .at("/foo").booleanNode())
            .isInstanceOf(AssertionFailedError.class);
    }

    @Test
    void whenUsingBooleanNodeThenIsTrueWorks() {
        assertJson("{foo:true}")
            .at("/foo").booleanNode().isTrue();
    }

    @Test
    void whenUsingBooleanNodeThenIsFalseWorks() {
        assertJson("{foo:false}")
            .at("/foo").booleanNode().isFalse();
    }
}
