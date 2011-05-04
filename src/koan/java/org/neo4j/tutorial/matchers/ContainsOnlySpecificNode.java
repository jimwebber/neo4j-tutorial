package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

public class ContainsOnlySpecificNode extends TypeSafeMatcher<Path> {

    private final Node node;

    public ContainsOnlySpecificNode(Node node) {
        this.node = node;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("Path does not contain the specified node [%d]", node.getId()));
    }

    @Override
    public boolean matchesSafely(Path path) {
        for(Node n : path.nodes()) {
            if(n.equals(node)) {
                return true;
            }
        }
        return false;
    }

    @Factory
    public static <T> Matcher<Path> contains(Node node) {
        return new ContainsOnlySpecificNode(node);
    }
}
