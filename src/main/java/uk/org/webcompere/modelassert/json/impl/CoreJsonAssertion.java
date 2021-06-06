package uk.org.webcompere.modelassert.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.JsonProvider;
import uk.org.webcompere.modelassert.json.Result;
import uk.org.webcompere.modelassert.json.condition.JsonIsNotNull;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Common implementation of both variants of the json assertion. Is itself a {@link BaseMatcher} as
 * the implementation has to work hardest to be compatible with hamcrest
 * @param <T> the type of JSON source - e.g. String or File
 * @param <A> the type of the assertion subclass, into which the fluent methods cast <code>this</code>
 */
public abstract class CoreJsonAssertion<T, A extends CoreJsonAssertion<T, A>> extends BaseMatcher<T>
        implements JsonNodeAssertDsl<T, A>, uk.org.webcompere.modelassert.json.dsl.Satisfies<T, A> {

    private JsonProvider<T> jsonProvider;
    private List<Condition> conditions = new LinkedList<>();

    protected CoreJsonAssertion(JsonProvider<T> jsonProvider) {
        this.jsonProvider = jsonProvider;
        conditions.add(new JsonIsNotNull());
    }

    @Override
    public A satisfies(Condition condition) {
        conditions.add(condition);
        return assertion();
    }

    @SuppressWarnings("unchecked")
    A assertion() {
        return (A)this;
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
                .map(res -> res.getCondition() + " was " + res.getWas())
                .collect(joining("\n")));
    }

    /**
     * Convert this to a mockito argument matcher in order to test JSON sent through to a method
     * or provide responses based on JSON
     * @return this as an argument matcher
     */
    public ArgumentMatcher<T> toArgumentMatcher() {
        return this::matches;
    }
}
