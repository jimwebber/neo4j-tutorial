package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ContainsOnlySpecificTitles extends TypeSafeMatcher<Iterable<Node>>
{

    private final Set<String> titles;
    private Node failedNode;

    public ContainsOnlySpecificTitles(String... specificTitles)
    {
        this.titles = new HashSet<String>();
        Collections.addAll(titles, specificTitles);
    }

    public void describeTo(Description description)
    {
        description.appendText(String.format("Node [%d] does not contain all of the specified titles",
                                             failedNode.getId()));
    }

    @Override
    public boolean matchesSafely(Iterable<Node> candidateNodes)
    {

        for (Node n : candidateNodes)
        {
            String property = String.valueOf(n.getProperty("title"));

            if (!titles.contains(property))
            {
                failedNode = n;
                return false;
            }
            titles.remove(property);
        }

        return titles.size() == 0;
    }

    @Factory
    public static Matcher<Iterable<Node>> containsOnlyTitles(String... titles)
    {
        return new ContainsOnlySpecificTitles(titles);
    }
}
