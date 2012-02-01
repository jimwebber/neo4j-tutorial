package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashSet;
import java.util.Set;

public class ContainsOnlySpecificInts extends TypeSafeMatcher<Iterable<Integer>>
{

    private final Set<Integer> specificInts;
    private Iterable<Integer> candidateInts;

    public ContainsOnlySpecificInts(int... ints)
    {
        this.specificInts = new HashSet<Integer>();
        for(int i : ints) {
            specificInts.add(new Integer(i));
        }
    }

    public void describeTo(Description description)
    {
        description.appendText("Failed to match all integers.");
        description.appendValueList("Expected: ", ",", "", specificInts);
        description.appendValueList("Received: ", ",", "", candidateInts);
    }

    @Override
    public boolean matchesSafely(Iterable<Integer> candidateInts)
    {
        this.candidateInts = candidateInts;
        for (Integer i : candidateInts)
        {
            if (i != null)
            {
                specificInts.remove(i);
            }
        }

        return specificInts.isEmpty();
    }

    @Factory
    public static Matcher<Iterable<Integer>> containsOnlySpecificInts(int... ints)
    {
        return new ContainsOnlySpecificInts(ints);
    }
}
