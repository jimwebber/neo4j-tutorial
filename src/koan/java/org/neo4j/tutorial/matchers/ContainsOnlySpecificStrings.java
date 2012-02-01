package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.*;

public class ContainsOnlySpecificStrings extends TypeSafeMatcher<Iterable<String>>
{

    private final Set<String> specificStrings;
    private Iterable<String> candidateStrings;

    public ContainsOnlySpecificStrings(String... strings)
    {
        this.specificStrings = new HashSet<String>();
        Collections.addAll(this.specificStrings, strings);
    }

    public void describeTo(Description description)
    {
        description.appendText("Failed to exactly match all strings");
        description.appendText(System.getProperty("line.separator"));
        description.appendValueList("Expected: ", ",", "", specificStrings);
        description.appendText(System.getProperty("line.separator"));
        description.appendValueList("Received: ", ",", "", candidateStrings);
    }

    @Override
    public boolean matchesSafely(Iterable<String> candidateStrings)
    {
        this.candidateStrings = candidateStrings;
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
