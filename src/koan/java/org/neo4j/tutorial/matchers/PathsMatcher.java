package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

import java.util.Set;

public class PathsMatcher extends TypeSafeMatcher<Iterable<Path>>
{
    private final Set<Node> nodes;
    private final Node start;
    private final Node end;

    private PathsMatcher(Node start, Set<Node> nodes, Node end)
    {
        this.start = start;
        this.nodes = nodes;
        this.end = end;

    }

    public void describeTo(Description description)
    {
        description.appendText(String.format("Expected nodes: "));
        for (Node n : nodes)
        {
            description.appendText("[");
            description.appendText(String.format(String.valueOf(n.getId())));
            description.appendText("]");
        }
    }

    @Override
    public boolean matchesSafely(Iterable<Path> paths)
    {
        int numberOfPaths = 0;
        for (Path p : paths)
        {
            numberOfPaths++;
            boolean middleNodeFound = false;
            for (Node middle : nodes)
            {
                if (nodesAreInPath(p, start, middle, end))
                {
                    middleNodeFound = true;
                    break;
                }
            }

            if (!middleNodeFound)
            {
                return false;
            }
        }

        return numberOfPaths == nodes.size();
    }

    private boolean nodesAreInPath(Path p, Node start, Node middle, Node end)
    {
        for (Node n : p.nodes())
        {
            if (!(n.equals(start) || n.equals(end) || n.equals(middle)))
            {
                return false;
            }
        }
        return true;
    }

    @Factory
    public static <T> Matcher<Iterable<Path>> consistPreciselyOf(Node start, Set<Node> nodes, Node end)
    {
        return new PathsMatcher(start, nodes, end);
    }
}
