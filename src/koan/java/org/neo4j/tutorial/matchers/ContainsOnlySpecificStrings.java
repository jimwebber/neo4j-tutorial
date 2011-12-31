package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.*;

public class ContainsOnlySpecificStrings extends TypeSafeMatcher<Iterable<String>>
{

    private final Set<String> specificStrings;

    public ContainsOnlySpecificStrings(String... strings)
    {
        this.specificStrings = new HashSet<String>();
        Collections.addAll(this.specificStrings, strings);
    }

    public void describeTo(Description description)
    {
//        description.appendText(String.format("Node [%d] does not contain all of the specified titles",
//                                             failedNode.getId()));
    }

    @Override
    public boolean matchesSafely(Iterable<String> candidateStrings)
    {
        for (String s : candidateStrings)
        {
            if (s != null)
            {
                specificStrings.remove(s);
            }
        }

        return specificStrings.isEmpty();
    }

    @Factory
    public static Matcher<Iterable<String>> containsOnlySpecificStrings(String... strings)
    {
        return new ContainsOnlySpecificStrings(strings);
    }
}
