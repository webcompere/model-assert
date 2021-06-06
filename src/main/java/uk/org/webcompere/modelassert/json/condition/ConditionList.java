package uk.org.webcompere.modelassert.json.condition;

import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.dsl.JsonNodeAssertDsl;

import java.util.LinkedList;
import java.util.List;

/**
 * A set of conditions for matching
 */
public class ConditionList implements JsonNodeAssertDsl<ConditionList> {
    private List<Condition> conditionList = new LinkedList<>();

    /**
     * Create a list of conditions using the DSL
     * @return the conditions
     */
    public static ConditionList conditions() {
        return new ConditionList();
    }

    /**
     * Get the conditions
     * @return
     */
    public List<Condition> getConditionList() {
        return conditionList;
    }

    @Override
    public ConditionList satisfies(Condition condition) {
        conditionList.add(condition);
        return this;
    }

    /**
     * Convert the list into a composite AND condition
     * @return {@link Condition}
     */
    public Condition toCondition() {
        if (conditionList.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an empty condition");
        }

        if (conditionList.size() == 1) {
            return conditionList.get(0);
        }

        Condition lastCondition = conditionList.get(0);
        for (int i = 1; i < conditionList.size(); i++) {
            lastCondition = lastCondition.and(conditionList.get(i));
        }
        return lastCondition;
    }
}
