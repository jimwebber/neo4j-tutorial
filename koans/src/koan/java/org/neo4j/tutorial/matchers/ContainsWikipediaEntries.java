package org.neo4j.tutorial.matchers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ContainsWikipediaEntries extends TypeSafeMatcher<Iterator<String>>
{

    private final Set<String> entries = new HashSet<String>();
    private boolean nullsInResults;

    public ContainsWikipediaEntries( String... wikipediaEntries )
    {
        Collections.addAll( this.entries, wikipediaEntries );
    }

    public void describeTo( Description description )
    {
        if ( !nullsInResults )
        {
            description.appendText( "Failed to match wikipedia entries to given nodes." );
        }
        else
        {
            description.appendText( "Failed to match wikipedia entries to given nodes. Some of the result rows contained null." );
        }
    }

    @Override
    public boolean matchesSafely( Iterator<String> candidateStrings )
    {
        while ( candidateStrings.hasNext() )
        {
            String s = candidateStrings.next();
            if ( s != null )
            {
                entries.remove( s );
            }
            else
            {
                nullsInResults = true;
                return false;
            }
        }

        return entries.isEmpty();
    }

    @Factory
    public static Matcher<Iterator<String>> containsOnlyWikipediaEntries( String... wikepediaEntries )
    {
        return new ContainsWikipediaEntries( wikepediaEntries );
    }
}
