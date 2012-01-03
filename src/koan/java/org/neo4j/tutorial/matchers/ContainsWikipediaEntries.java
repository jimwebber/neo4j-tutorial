package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ContainsWikipediaEntries extends TypeSafeMatcher<Iterable<String>>
{

    private final Set<String> entries = new HashSet<String>();
    public ContainsWikipediaEntries(String... wikipediaEntries)
    {
        Collections.addAll(this.entries, wikipediaEntries);
    }

    public void describeTo(Description description)
    {
        description.appendText("Failed to match wikipedia entries to given nodes.");
    }

    @Override
    public boolean matchesSafely(Iterable<String> candidateStrings)
    {
        for (String s : candidateStrings)
        {
            if (s != null)
            {
                entries.remove(s);
            }
        }

        return entries.isEmpty();
    }

    @Factory
    public static Matcher<Iterable<String>> containsWikipediaEntries(String... wikepediaEntries)
    {
        return new ContainsWikipediaEntries(wikepediaEntries);
    }
}
