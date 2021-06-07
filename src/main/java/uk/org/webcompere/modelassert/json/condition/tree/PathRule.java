package uk.org.webcompere.modelassert.json.condition.tree;

import uk.org.webcompere.modelassert.json.Condition;

/**
 * Dictates a {@link TreeRule} that applies at a given path match and any operand for that rule
 */
public class PathRule {
    private PathMatch pathMatch;
    private TreeRule rule;
    private Condition ruleCondition;

    /**
     * Constructed with a stateless tree rule for everywhere
     * @param rule the rule
     */
    public PathRule(TreeRule rule) {
        this(PathMatch.all(), rule);
    }

    /**
     * Construct to apply a rule to a path
     * @param pathMatch the path the rule applies to
     * @param rule the rule to apply - no operand
     */
    public PathRule(PathMatch pathMatch, TreeRule rule) {
        this.pathMatch = pathMatch;
        this.rule = rule;
        this.ruleCondition = null;
    }

    /**
     * Construct to apply a condition rule to a path
     * @param pathMatch the path the rule applies to
     * @param ruleCondition the condition to apply
     */
    public PathRule(PathMatch pathMatch, Condition ruleCondition) {
        this.pathMatch = pathMatch;
        this.rule = TreeRule.CONDITION;
        this.ruleCondition = ruleCondition;
    }

    /**
     * Does this rule apply to this location
     * @param location the location to test
     * @return <code>true</code> when the rule applies
     */
    public boolean matches(Location location) {
        return pathMatch.matches(location);
    }

    /**
     * Get the rule type
     * @return the type of rule
     */
    public TreeRule getRule() {
        return rule;
    }

    /**
     * Get any condition associated with the rule
     * @return condition
     */
    public Condition getRuleCondition() {
        return ruleCondition;
    }

    @Override
    public String toString() {
        return "at " + pathMatch + " " + rule + " " + (ruleCondition != null ? ruleCondition.describe() : "");
    }
}
