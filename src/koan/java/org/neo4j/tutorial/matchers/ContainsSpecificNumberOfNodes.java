package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Node;

public class ContainsSpecificNumberOfNodes extends TypeSafeMatcher<Iterable<Node>>
{

    private final int number;
    private int count;

    private ContainsSpecificNumberOfNodes(int number)
    {
        this.number = number;
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText(String.format("Expected [%d] nodes, found [%s]", number, count));
    }

    @Override
    public boolean matchesSafely(Iterable<Node> nodes)
    {
        count = 0;
        for (@SuppressWarnings("unused") Node n : nodes)
        {
            count++;
        }
        return count == number;
    }

    @Factory
    public static <T> Matcher<Iterable<Node>> containsNumberOfNodes(int number)
    {
        return new ContainsSpecificNumberOfNodes(number);
    }
}
