package uk.org.webcompere.modelassert.json;

/**
 * The result of comparison
 */
public class Result {
    private String condition;
    private String was;
    private boolean isPassed;

    /**
     * Construct a result
     * @param condition what was the condition being evaluated
     * @param was what the answer actually was
     * @param isPassed is this a success?
     */
    public Result(String condition, String was, boolean isPassed) {
        this.condition = condition;
        this.was = was;
        this.isPassed = isPassed;
    }

    public String getCondition() {
        return condition;
    }

    /**
     * Add a prefix to the condition description
     * @param condition the prefix to add
     * @return this for fluent calls
     */
    public Result withPreCondition(String condition) {
        this.condition = condition + " " + this.condition;
        return this;
    }

    public String getWas() {
        return was;
    }

    public boolean isPassed() {
        return isPassed;
    }

    /**
     * Flip the result
     * @return this for fluent calls
     */
    public Result invert() {
        isPassed = !isPassed;
        return this;
    }
}
