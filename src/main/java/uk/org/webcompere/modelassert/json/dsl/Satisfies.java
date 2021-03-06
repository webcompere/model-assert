package uk.org.webcompere.modelassert.json.dsl;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.condition.ConditionList;

@FunctionalInterface
public interface Satisfies<A> {
    /**
     * Add a condition that the input JSON must satisfy
     * @param condition the condition
     * @return <code>this</code> for fluent calling
     */
    A satisfies(Condition condition);

    /**
     * Add multiple conditions in a list
     * @param conditionList the list of conditions
     * @return <code>this</code> for fluent calling
     */
    default A satisfies(ConditionList conditionList) {
        return satisfies(conditionList.toCondition());
    }
}
