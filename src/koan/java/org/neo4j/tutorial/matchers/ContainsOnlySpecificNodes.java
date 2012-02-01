package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

import java.util.HashSet;

public class ContainsOnlySpecificNodes extends TypeSafeMatcher<Path>
{

    private final HashSet<Node> nodes = new HashSet<Node>();

    public ContainsOnlySpecificNodes(Node... nodes)
    {
        for (Node n : nodes)
        {
            this.nodes.add(n);
        }
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText(String.format("Path does not contain only the specified nodes."));
    }

    @Override
    public boolean matchesSafely(Path path)
    {
        for (Node n : path.nodes())
        {
            if (nodes.contains(n))
            {
                nodes.remove(n);
            }
            else
            {
                return false;
            }
        }

        return nodes.size() == 0;
    }

    @Factory
    public static <T> Matcher<Path> containsOnlySpecificNodes(Node... nodes)
    {
        return new ContainsOnlySpecificNodes(nodes);
    }
}
