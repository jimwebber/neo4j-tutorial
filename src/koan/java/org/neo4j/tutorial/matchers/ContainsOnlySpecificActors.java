package org.neo4j.tutorial.matchers;

import java.util.Collections;
import java.util.HashSet;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Node;

public class ContainsOnlySpecificActors extends TypeSafeMatcher<Iterable<Node>>
{

    private final HashSet<String> actorNames = new HashSet<String>();
    private boolean matchedParameterLengths;
    private boolean nodeIsNotAnActor;

    private ContainsOnlySpecificActors( String... actors )
    {
        Collections.addAll( actorNames, actors );
    }

    public void describeTo( Description description )
    {
        if ( nodeIsNotAnActor )
        {
            description.appendText( "A supplied node does not have the property 'actor'" );
        }

        if ( !matchedParameterLengths )
        {
            description.appendText( "Number of actor names in traverser result does not match number of actors required by matcher" );
        }
    }

    @Override
    public boolean matchesSafely( Iterable<Node> actors )
    {

        for ( Node actor : actors )
        {
            if ( !actor.hasProperty( "actor" ) )
            {
                nodeIsNotAnActor = true;
                return false;
            }

            Object actorProperty = actor.getProperty( "actor" );
            if ( !actorNames.contains( actorProperty ) )
            {
                nodeIsNotAnActor = true;
                return false;
            }
            else
            {
                actorNames.remove( actorProperty );
            }
        }
        return matchedParameterLengths = actorNames.size() == 0;
    }

    @Factory
    public static Matcher<Iterable<Node>> containsOnlyActors( String... actorNames )
    {
        return new ContainsOnlySpecificActors( actorNames );
    }
}