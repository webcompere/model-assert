package uk.org.webcompere.modelassert.json;

import uk.org.webcompere.modelassert.json.impl.MemoizedSupplier;

import java.util.function.Supplier;

/**
 * The result of comparison. A mutable object passed through conditions to reach a pass/fail
 * with some explanation of why
 */
public class Result {
    private MemoizedSupplier<String> conditionSupplier;
    private String was;
    private boolean isPassed;

    /**
     * Construct a result
     * @param condition what was the condition being evaluated
     * @param was what the answer actually was
     * @param isPassed is this a success?
     */
    public Result(String condition, String was, boolean isPassed) {
        this.conditionSupplier = MemoizedSupplier.of(() -> condition);
        this.was = was;
        this.isPassed = isPassed;
    }

    /**
     * Construct a result
     * @param conditionSupplier supplies the condition being evaluated
     * @param was what the answer actually was
     * @param isPassed is this a success?
     */
    public Result(Supplier<String> conditionSupplier, String was, boolean isPassed) {
        this.conditionSupplier = MemoizedSupplier.of(conditionSupplier);
        this.was = was;
        this.isPassed = isPassed;
    }

    public String getCondition() {
        return conditionSupplier.get();
    }

    /**
     * Add a prefix to the condition description
     * @param condition the prefix to add
     * @return this for fluent calls
     */
    public Result withPreCondition(String condition) {
        Supplier<String> previousSupplier = conditionSupplier;
        this.conditionSupplier = MemoizedSupplier.of(() -> condition + " " + previousSupplier.get());
        return this;
    }

    /**
     * Change the description of the condition
     * @param condition new description
     * @return this for fluent calls
     */
    public Result withNewDescription(String condition) {
        this.conditionSupplier = MemoizedSupplier.of(() -> condition);
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
