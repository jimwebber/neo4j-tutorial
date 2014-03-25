package org.neo4j.tutorial;

import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.kernel.impl.util.StringLogger;

import static org.junit.Assert.assertEquals;

/**
 * In this Koan we learn how to create, update, and erase properties and labels in Cypher.
 */
public class Koan4
{

    @Test
    public void shouldCreateALabelledNode()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );

        engine.execute( "CREATE (n {actor: 'David Tennant'}) RETURN n" );

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
    public void shouldAddALabelToAnExistingNode()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );

        engine.execute( "CREATE (n {actor: 'David Tennant'}) RETURN n" );

        String cql = "MATCH (a {actor: 'David Tennant'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "SET a:Actor";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "MATCH (a {actor: 'David Tennant'}) RETURN a.actor" );

        assertEquals( "David Tennant", executionResult.javaColumnAs( "a.actor" ).next() );
    }
}
