package uk.org.webcompere.modelassert.json.dsl.nodespecific;

import org.junit.jupiter.api.Test;

import static uk.org.webcompere.modelassert.json.TestAssertions.assertAllWays;

class ObjectNodeDslTest {

    @Test
    void objectIsObject() {
        assertAllWays("{}", "null",
            ObjectNodeDsl::isObject);
    }

    @Test
    void objectIsObjectViaObjectContext() {
        assertAllWays("{}", "null",
            assertion -> assertion.object().isObject());
    }

    @Test
    void nonObjectIsNotObject() {
        assertAllWays("[]", "{}",
            ObjectNodeDsl::isNotObject);
    }

    @Test
    void objectIsEmpty() {
        assertAllWays("{}", "{\"array\":[]}",
            Sizeable::isEmpty);
    }

    @Test
    void objectContainsKey() {
        assertAllWays("{\"name\":\"Mr Name\"}", "{\"array\":[]}",
            assertion -> assertion.containsKey("name"));
    }

    @Test
    void objectDoesNotContainKey() {
        assertAllWays("{\"array\":[]}", "{\"name\":\"Mr Name\"}",
            assertion -> assertion.doesNotContainKey("name"));
    }

    @Test
    void objectDoesNotContainMultipleKeys() {
        assertAllWays("{\"array\":[]}", "{\"name\":\"Mr Name\"}",
            assertion -> assertion.doesNotContainKey("name")
                .doesNotContainKey("foo"));
    }

    @Test
    void objectContainsKeys() {
        assertAllWays("{\"name\":\"Mr Name\", \"gender\":\"male\"}", "{\"array\":[]}",
            assertion -> assertion.containsKeys("name", "gender"));
    }

    @Test
    void objectDoesNotContainKeys() {
        assertAllWays("{\"array\":[]}", "{\"name\":\"Mr Name\", \"gender\":\"male\"}",
            assertion -> assertion.doesNotContainKeys("name", "gender"));
    }

    @Test
    void objectContainsKeysExactly_whereExtraField() {
        assertAllWays("{\"name\":\"Mr Name\", \"gender\":\"male\"}",
            "{\"name\":\"Mr Name\", \"gender\":\"male\", \"field\": true}",
            assertion -> assertion.containsKeysExactly("name", "gender"));
    }

    @Test
    void objectContainsKeysExactly_whereMissingField() {
        assertAllWays("{\"name\":\"Mr Name\", \"gender\":\"male\"}",
            "{\"name\":\"Mr Name\"}",
            assertion -> assertion.containsKeysExactly("name", "gender"));
    }

    @Test
    void objectContainsKeysExactly_whereOrderIsIncorrect() {
        assertAllWays("{\"name\":\"Mr Name\", \"gender\":\"male\"}",
            "{\"gender\":\"male\", \"name\":\"Mr Name\"}",
            assertion -> assertion.containsKeysExactly("name", "gender"));
    }

    @Test
    void objectContainsKeysExactly_whereReOrderIsAllowed() {
        assertAllWays("{\"gender\":\"male\", \"name\":\"Mr Name\"}",
            "{\"gender\":\"male\", \"name\":\"Mr Name\", \"other\": true}",
            assertion -> assertion.containsKeysExactlyInAnyOrder("name", "gender"));
    }
}
