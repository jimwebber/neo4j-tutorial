package org.neo4j.tutorial;

import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.kernel.impl.util.StringLogger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * In this Koan we learn how to create, update, and delete nodes and relationships in the
 * database using the Cypher language.
 */
public class Koan2
{
    @Test
    public void shouldCreateASingleNode()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE n";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "START n=node(*) return n" );

        assertEquals( 1, executionResult.size() );
    }

    @Test
    public void shouldCreateASingleNodeWithSomeProperties()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE (n { firstname : 'Tom', lastname : 'Baker' })";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute(
                "MATCH (n {firstname: 'Tom', lastname: 'Baker'}) RETURN n" );

        /* Geek question: if you've read ahead, what's the Big O cost of this match? */

        assertEquals( 1, executionResult.size() );
    }

    @Test
    public void shouldCreateASimpleConnectedGraph()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE (doctor { character : 'Doctor' }), (master { character : 'Master' }), " +
                "(doctor)<-[r:ENEMY_OF]-(master) ";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute(
                "MATCH (a {character: 'Doctor'})<-[:ENEMY_OF]-(b {character: 'Master'}) \n" +
                        "RETURN a, b \n" );

        /* Same geek question: if you've read ahead, what's the Big O cost of this match? */

        assertFalse( executionResult.isEmpty() );
        assertEquals( 2, executionResult.columns().toList().size() );
        assertEquals( 1, executionResult.size() );
    }
}
