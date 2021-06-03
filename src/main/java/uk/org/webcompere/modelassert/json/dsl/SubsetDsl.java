package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

/**
 * Reduce the DSL down to a subset
 * @param <T> the type of source JSON
 * @param <A> the type of assertion
 */
public class SubsetDsl<T, A extends CoreJsonAssertion<T, A>> implements Satisfies<T, A> {
    private Satisfies<T, A> satisfies;

    public SubsetDsl(Satisfies<T, A> satisfies) {
        this.satisfies = satisfies;
    }

    /**
     * Add a condition that the input JSON must satisfy
     *
     * @param condition the condition
     * @return <code>this</code> for fluent calling
     */
    @Override
    public A satisfies(Condition condition) {
        return satisfies.satisfies(condition);
    }
}