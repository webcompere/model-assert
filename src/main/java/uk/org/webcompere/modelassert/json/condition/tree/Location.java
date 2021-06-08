package uk.org.webcompere.modelassert.json.condition.tree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Describes the JSON pointer up to a position in the tree
 */
public class Location {
    private List<String> path;

    /**
     * An empty location
     */
    public Location() {
        this.path = new LinkedList<>();
    }

    private Location(List<String> pathSoFar, String child) {
        this.path = Stream.concat(pathSoFar.stream(), Stream.of(child))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    private Location(List<String> path) {
        this.path = Collections.unmodifiableList(path);
    }

    /**
     * The location with a child
     * @param child the child to add
     * @return a new {@link Location} with the child attached
     */
    public Location child(String child) {
        return new Location(path, child);
    }

    /**
     * Slice the front off the location, and produce the rest of the path
     * @return the remainder of the location
     */
    public Location peelOffFirst() {
        return new Location(path.subList(1, path.size()));
    }


    @Override
    public String toString() {
        return path.stream().collect(joining("/", "/", ""));
    }

    /**
     * Is there nothing left of the path - are we at the root? or have we peeled off all the first
     * @return true if there's no path in here
     */
    public boolean isEmpty() {
        return path.isEmpty();
    }

    /**
     * Get the first part of the path
     * @return the first part of the path or <code>null</code> if there isn't any
     */
    public String first() {
        return !path.isEmpty() ? path.get(0) : null;
    }
}
