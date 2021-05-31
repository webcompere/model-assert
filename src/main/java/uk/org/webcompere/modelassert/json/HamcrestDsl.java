package uk.org.webcompere.modelassert.json;

import static uk.org.webcompere.modelassert.json.JsonAssertDsl.OBJECT_MAPPER;

public class HamcrestDsl<T> {
    private JsonProvider<T> provider;

    public HamcrestDsl(JsonProvider<T> provider) {
        this.provider = provider;
    }

    public static HamcrestDsl<String> json() {
        return new HamcrestDsl<>(OBJECT_MAPPER::readTree);
    }

    public JsonAssertDsl.At<T, HamcrestJsonAssertion<T>> at(String path) {
        return new JsonAssertDsl.At<>(new HamcrestJsonAssertion<>(provider), path);
    }
}
