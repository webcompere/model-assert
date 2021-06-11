package uk.org.webcompere.modelassert.json.condition.array;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Cheap version of multiset to avoid importing all of guava!
 * @param <T> the type of value in the set
 */
public class Multiset<T> {
    private Map<T, Integer> items = new HashMap<>();

    public void add(T item) {
        items.merge(item, 1, Integer::sum);
    }

    public Stream<Map.Entry<T, Integer>> entries() {
        return items.entrySet().stream();
    }
}
