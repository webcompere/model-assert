package uk.org.webcompere.modelassert.json.impl;

import java.util.function.Supplier;

/**
 * A supplier which returns the same answer after the first time
 * @param <T> the answer
 */
public class MemoizedSupplier<T> implements Supplier<T> {
    private boolean hasEvaluated;
    private T answer;
    private Supplier<T> actualSupplier;

    public MemoizedSupplier(Supplier<T> actualSupplier) {
        this.actualSupplier = actualSupplier;
    }

    public static <T> MemoizedSupplier<T> of(Supplier<T> supplier) {
        return new MemoizedSupplier<>(supplier);
    }

    @Override
    public T get() {
        if (!hasEvaluated) {
            hasEvaluated = true;
            answer = actualSupplier.get();
        }
        return answer;
    }
}
