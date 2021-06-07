package uk.org.webcompere.modelassert.json.condition.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Location {
    private List<String> path;

    public Location() {
        this.path = new LinkedList<>();
    }

    private Location(List<String> pathSoFar, String child) {
        this.path = Stream.concat(pathSoFar.stream(), Stream.of(child))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Location child(String child) {
        return new Location(path, child);
    }

    @Override
    public String toString() {
        return path.stream().collect(joining("/", "/", ""));
    }
}
