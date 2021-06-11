package uk.org.webcompere.modelassert.json.condition.array;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import uk.org.webcompere.modelassert.json.Condition;
import uk.org.webcompere.modelassert.json.Result;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Loose array comparison
 */
public class LooseComparison {
    private static class Match {
        private int index;
        private Set<Integer> counterPartIndices = new HashSet<>();

        public Match(int index) {
            this.index = index;
        }

        public int size() {
            return counterPartIndices.size();
        }

        public int getIndex() {
            return index;
        }

        public void add(int counterPart) {
            counterPartIndices.add(counterPart);
        }

        public void remove(int counterPart) {
            counterPartIndices.remove(counterPart);
        }

        public Stream<Integer> streamCounterparts() {
            return counterPartIndices.stream();
        }

        public boolean contains(int counterpart) {
            return counterPartIndices.contains(counterpart);
        }
    }

    private List<ArrayElementCondition> arrayElementConditions;
    private Supplier<String> description;


    public LooseComparison(List<ArrayElementCondition> arrayElementConditions, Supplier<String> description) {
        this.arrayElementConditions = arrayElementConditions;
        this.description = description;
    }

    /**
     * Construct the comparison from the provided regular conditions
     * @param arrayElementConditions the conditions
     * @param description the description of the assertion
     * @return a new {@link LooseComparison}
     */
    public static LooseComparison fromConditions(List<Condition> arrayElementConditions, Supplier<String> description) {
        return new LooseComparison(arrayElementConditions.stream()
            .map(ArrayElementConditionAdapter::new)
            .collect(toList()),
            description);
    }

    /**
     * Execute the loose array comparison
     * @param arrayNode the node to test
     * @return the result of comparison
     */
    public Result looseComparison(ArrayNode arrayNode) {
        // each criterion may match many other elements
        // try them all out
        List<Match> matches = IntStream.range(0, arrayElementConditions.size())
            .mapToObj(index -> calcMatches(index, arrayNode))
            .collect(toList());

        while (!matches.isEmpty()) {
            matches.sort(Comparator.comparing(Match::size));
            if (matches.get(0).size() == 0) {
                return explainMismatches(matches);
            }

            removeLeastOccurringCounterpart(matches);
        }

        return new Result(describe(), "all matched", true);
    }

    private void removeLeastOccurringCounterpart(List<Match> matches) {
        Multiset<Integer> multiset = HashMultiset.create();
        matches.stream().flatMap(Match::streamCounterparts)
            .forEach(multiset::add);

        // least frequently occurring item
        int leastFoundIndex = multiset.entrySet().stream().min(Comparator.comparing(Multiset.Entry::getCount))
            .map(Multiset.Entry::getElement)
            .orElseThrow(() -> new IllegalStateException("Cannot have no elements"));

        // now let's find the smallest match of this
        Match smallestMatch = matches.stream()
            .filter(match -> match.contains(leastFoundIndex)).min(Comparator.comparing(Match::size))
            .orElseThrow(() -> new IllegalStateException("Cannot have no elements"));

        // we can remove this one
        matches.remove(smallestMatch);

        // then take the least found index out of the remainder
        matches.forEach(match -> match.remove(leastFoundIndex));
    }

    private Result explainMismatches(List<Match> matches) {
        matches.sort(Comparator.comparing(Match::getIndex));
        return new Result(describe(), "No matches for:\n" + matches.stream()
            .filter(match -> match.size() == 0)
            .map(match -> "Index " + match.getIndex() + ": " +
                arrayElementConditions.get(match.getIndex()).describe())
            .collect(joining("\n")), false);
    }

    private Match calcMatches(int index, ArrayNode arrayNode) {
        Match match = new Match(index);
        for (int i = 0; i < arrayNode.size(); i++) {
            if (arrayElementConditions.get(index).test(arrayNode.get(i), index).isPassed()) {
                match.add(i);
            }
        }
        return match;
    }

    private Supplier<String> describe() {
        return description;
    }
}
