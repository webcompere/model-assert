package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.impl.CoreJsonAssertion;

public interface Satisfies<T, A extends CoreJsonAssertion<T, A>> {
    /**
     * Add a condition that the input JSON must satisfy
     * @param condition the condition
     * @return <code>this</code> for fluent calling
     */
    A satisfies(Condition condition);
}
