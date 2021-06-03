package uk.org.webcompere.modelassert.json;

/**
 *
 */
public class Result {
    private String condition;
    private String was;
    private boolean isPassed;

    public Result(String condition, String was, boolean isPassed) {
        this.condition = condition;
        this.was = was;
        this.isPassed = isPassed;
    }

    public String getCondition() {
        return condition;
    }

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
}
