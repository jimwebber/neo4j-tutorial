package org.neo4j.tutorial;

import org.junit.Test;
import scala.collection.convert.Wrappers;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.kernel.impl.util.StringLogger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * In this Koan we learn how to create, update, and erase properties and labels in Cypher.
 */
public class Koan4
{
    @Test
    public void shouldCreateAnUnlabelledNodeWithActorPropertyToRepresentDavidTennant()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );

        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE ({actor: 'David Tennant'})";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "MATCH (a {actor: 'David Tennant'}) RETURN a.actor" );

        assertEquals( "David Tennant", executionResult.javaColumnAs( "a.actor" ).next() );
    }

    @Test
    public void shouldAddOriginalNamePropertyForDavidTennantNode()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );

        engine.execute( "CREATE ({actor: 'David Tennant'}) " );

        String cql = "MATCH (a {actor: 'David Tennant'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "SET a.original_name = 'David McDonald'";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "MATCH (a {actor: 'David Tennant'}) RETURN a" +
                ".original_name" );

        assertEquals( "David McDonald", executionResult.javaColumnAs( "a.original_name" ).next() );
    }

    @Test
    public void shouldChangeOriginalNamePropertyForDavidTennantNodeToSomethingComical()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );

        engine.execute( "CREATE ({actor: 'David Tennant', original_name: 'David McDonald'}) " );

        String cql = "MATCH (a {actor: 'David Tennant'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "SET a.original_name = 'Ronald McDonald'";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "MATCH (a {actor: 'David Tennant'}) RETURN a" +
                ".original_name" );

        assertEquals( "Ronald McDonald", executionResult.javaColumnAs( "a.original_name" ).next() );
    }


    @Test
    public void shouldCreateAnActorLabelledNodeRepresentingDavidTennant()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );


        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE (a:Actor {actor: 'David Tennant'})";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "MATCH (a:Actor {actor: 'David Tennant'}) RETURN a.actor" );

        assertEquals( "David Tennant", executionResult.javaColumnAs( "a.actor" ).next() );
    }

    @Test
    public void shouldAddAnActorLabelToAnExistingDavidTennantNode()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );

        engine.execute( "CREATE ({actor: 'David Tennant'})" );

        String cql = "MATCH (a {actor: 'David Tennant'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "SET a:Actor";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "MATCH (a {actor: 'David Tennant'}) RETURN a.actor" );

        assertEquals( "David Tennant", executionResult.javaColumnAs( "a.actor" ).next() );
    }

    @Test
    public void shouldAddActorMaleAndScottishLabelsToAnExistingDavidTennantNode()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );

        engine.execute( "CREATE ({actor: 'David Tennant'})" );

        String cql = "MATCH (a {actor: 'David Tennant'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "SET a:Actor:Male:Scottish";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "MATCH (a {actor: 'David Tennant'}) RETURN labels(a)" );

        Wrappers.SeqWrapper wrapper = (Wrappers.SeqWrapper) executionResult.javaColumnAs( "labels(a)" ).next();
        assertTrue( wrapper.contains( "Male" ) );
        assertTrue( wrapper.contains( "Actor" ) );
        assertTrue( wrapper.contains( "Scottish" ) );
    }
}
