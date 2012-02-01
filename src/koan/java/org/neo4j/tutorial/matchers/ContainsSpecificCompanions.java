package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

public class ContainsSpecificCompanions extends TypeSafeMatcher<Index<Node>>
{

    private final String[] companionNames;

    private ContainsSpecificCompanions(String[] companionNames)
    {
        this.companionNames = companionNames;
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText(
                "Checks whether each index in the presented arguments contains the supplied companion names.");
    }

    @Override
    public boolean matchesSafely(Index<Node> companions)
    {
        for (String name : companionNames)
        {
            if (companions.get("character", name)
                          .getSingle() == null)
            {
                return false;
            }
        }

        return true;
    }

    @Factory
    public static <T> Matcher<Index<Node>> contains(String... companionNames)
    {
        return new ContainsSpecificCompanions(companionNames);
    }
}
