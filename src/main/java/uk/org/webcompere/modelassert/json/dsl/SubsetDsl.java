package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.Condition;

/**
 * Reduce the DSL down to a subset
 * @param <A> the type of assertion
 */
public class SubsetDsl<A> implements Satisfies<A> {
    private Satisfies<A> satisfies;

    public SubsetDsl(Satisfies<A> satisfies) {
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
