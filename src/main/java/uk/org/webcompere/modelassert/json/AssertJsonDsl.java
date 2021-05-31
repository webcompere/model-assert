package uk.org.webcompere.modelassert.json;

import static uk.org.webcompere.modelassert.json.JsonAssertDsl.OBJECT_MAPPER;

public class AssertJsonDsl {
    public static AssertJson<String> assertJson(String json) {
        return new AssertJson<>(OBJECT_MAPPER::readTree, json);
    }
}
