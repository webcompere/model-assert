package uk.org.webcompere.modelassert.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class JsonAssertion<T, SELF extends JsonAssertion<T, SELF>> extends BaseMatcher<T> {
    private JsonProvider<T> jsonProvider;
    private List<Comparison> comparisons = new LinkedList<>();

    public JsonAssertion(JsonProvider<T> jsonProvider) {
        this.jsonProvider = jsonProvider;
    }

    @SuppressWarnings("unchecked")
    public SELF plus(Comparison comparison) {
        comparisons.add(comparison);
        return (SELF)this;
    }

    public JsonAssertDsl.At<T, SELF> at(String path) {
        return new JsonAssertDsl.At<>(this, path);
    }

    @Override
    public boolean matches(Object item) {
        JsonNode jsonNode = jsonProvider.jsonFrom(item);
        return comparisons.stream()
                .map(comparison -> comparison.test(jsonNode))
                .allMatch(Result::isPassed);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(comparisons.stream()
                .map(Comparison::describe)
                .collect(joining("\n")));
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        JsonNode jsonNode = jsonProvider.jsonFrom(item);
        description.appendText(comparisons.stream()
                .map(comparison -> comparison.test(jsonNode))
                .filter(res -> !res.isPassed())
                .map(res -> res.getExpected() + " was " + res.getWas())
                .collect(joining("\n")));
    }
}
