package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;
import uk.org.webcompere.modelassert.json.condition.NullCondition;

/**
 * The DSL as applies to the JsonAssertion itself, regardless of whether we've built one, or are
 * adding to one
 * @param <T> the type of source json the assertion processes, String, File etc
 * @param <A> the final type of the assertion
 */
public interface JsonAssertDsl<T, A extends CoreJsonAssertion<T, A>> {

    /**
     * Reflexive get of the assertion itself
     * @return the equivalent of <code>this</code>
     */
    A assertion();

    /**
     * Add a condition that the input JSON must satisfy
     * @param condition the condition
     * @return <code>this</code> for fluent calling
     */
    A satisfies(Condition condition);

    /**
     * Start adding an {@link JsonAssertDslBuilders.At} expression to the assertion
     * @param path the json path to use
     * @return the {@link JsonAssertDslBuilders.At} to construct the at expression
     */
    default JsonAssertDslBuilders.At<T, A> at(String path) {
        return new JsonAssertDslBuilders.At<>(assertion(), path);
    }

    /**
     * Add a condition that requires the JSON to be null
     * @return the assertion for fluent use
     */
    default A isNull() {
        return satisfies(NullCondition.getInstance());
    }
}
