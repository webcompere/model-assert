package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;

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

}