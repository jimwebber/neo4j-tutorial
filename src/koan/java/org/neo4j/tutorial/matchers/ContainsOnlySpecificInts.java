package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.HashSet;
import java.util.Set;

public class ContainsOnlySpecificInts extends TypeSafeMatcher<Iterable<Object>>
{

    private final Set<Object> specificInts;

    public ContainsOnlySpecificInts(int... ints)
    {
        this.specificInts = new HashSet<Object>();
        for(int i : ints) {
            specificInts.add(new Integer(i));
        }
    }

    public void describeTo(Description description)
    {
//        description.appendText(String.format("Node [%d] does not contain all of the specified titles",
//                                             failedNode.getId()));
    }

    @Override
    public boolean matchesSafely(Iterable<Object> candidateInts)
    {
        for (Object o : candidateInts)
        {
            if (o != null)
            {
                specificInts.remove(o);
            }
        }

        return specificInts.isEmpty();
    }

    @Factory
    public static Matcher<Iterable<Object>> containsOnlySpecificInts(int... ints)
    {
        return new ContainsOnlySpecificInts(ints);
    }
}
