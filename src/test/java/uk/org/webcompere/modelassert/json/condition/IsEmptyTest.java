package uk.org.webcompere.modelassert.json.condition;

import org.junit.jupiter.api.Test;

import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

class IsEmptyTest {

    @Test
    void isNotEmptyOnNumberIsTrue() {
        assertAllWays("{\"count\":0}", "{\"count\":[]}",
                assertion -> assertion.at("/count").isNotEmpty());
    }

    @Test
    void isNotEmptyOnMissingIsFalse() {
        assertAllWays("{\"count\":0}", "{}",
            assertion -> assertion.at("/count").isNotEmpty());
    }


    @Test
    void isEmptyOnArrayIsTrue() {
        assertAllWays("{\"count\":[]}", "{\"count\":[\"value\"]}",
                assertion -> assertion.at("/count").isEmpty());
    }

    @Test
    void isEmptyOnObjectIsTrue() {
        assertAllWays("{\"thing\":{}}", "{\"thing\":{\"foo\":true}}",
                assertion -> assertion.at("/thing").isEmpty());
    }

    @Test
    void isEmptyOnStringIsTrue() {
        assertAllWays("{\"text\":\"\"}", "{\"text\":\"value\"}",
                assertion -> assertion.at("/text").isEmpty());
    }
}
