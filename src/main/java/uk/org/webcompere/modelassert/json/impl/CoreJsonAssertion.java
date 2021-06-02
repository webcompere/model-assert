package uk.org.webcompere.modelassert.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.JsonProvider;
import uk.org.webcompere.modelassert.json.Result;
import uk.org.webcompere.modelassert.json.dsl.JsonAssertDsl;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Common implementation of both variants of the json assertion. Is itself a {@link BaseMatcher} as
 * the implementation has to work hardest to be compatible with hamcrest
 * @param <T> the type of JSON source - e.g. String or File
 * @param <SELF> the type of the subclass, into which the fluent methods cast <code>this</code>
 */
public abstract class CoreJsonAssertion<T, SELF extends CoreJsonAssertion<T, SELF>> extends BaseMatcher<T>
        implements JsonAssertDsl<T, SELF> {

    private JsonProvider<T> jsonProvider;
    private List<Condition> conditions = new LinkedList<>();

    protected CoreJsonAssertion(JsonProvider<T> jsonProvider) {
        this.jsonProvider = jsonProvider;
    }

    @Override
    public SELF satisfies(Condition condition) {
        conditions.add(condition);
        return assertion();
    }

    @SuppressWarnings("unchecked")
    @Override
    public SELF assertion() {
        return (SELF)this;
    }

    @Override
    public boolean matches(Object item) {
        JsonNode jsonNode = jsonProvider.jsonFrom(item);
        return conditions.stream()
                .map(condition -> condition.test(jsonNode))
                .allMatch(Result::isPassed);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(conditions.stream()
                .map(Condition::describe)
                .collect(joining("\n")));
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        JsonNode jsonNode = jsonProvider.jsonFrom(item);
        description.appendText(conditions.stream()
                .map(condition -> condition.test(jsonNode))
                .filter(res -> !res.isPassed())
                .map(res -> res.getExpected() + " was " + res.getWas())
                .collect(joining("\n")));
    }
}
