package org.neo4j.tutorial.matchers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class ContainsOnlySpecificTitles extends TypeSafeMatcher<Iterable<Node>>
{

    private final Set<String> titles;
    private Node failedNode;
    private final GraphDatabaseService database;

    public ContainsOnlySpecificTitles( GraphDatabaseService database, String... specificTitles )
    {
        this.database = database;
        this.titles = new HashSet<>();
        Collections.addAll( titles, specificTitles );
    }

    public void describeTo( Description description )
    {
        if ( failedNode != null )
        {
            description.appendText( String.format( "Node [%d] does not contain all of the specified titles",
                    failedNode.getId() ) );
        }
        else
        {
            description.appendText( "Unable to match all of the specified titles" );
        }
    }

    @Override
    public boolean matchesSafely( Iterable<Node> candidateNodes )
    {
        for ( Node n : candidateNodes )
        {
            try ( Transaction tx = database.beginTx() )
            {
                String property = String.valueOf( n.getProperty( "title" ) );

                if ( !titles.contains( property ) )
                {
                    failedNode = n;
                    return false;
                }
                titles.remove( property );
                tx.success();
            }
        }

        return titles.size() == 0;

    }

    @Factory
    public static Matcher<Iterable<Node>> containsOnlyTitles( GraphDatabaseService database, String... titles )
    {
        return new ContainsOnlySpecificTitles( database, titles );
    }
}
