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
    public void shouldCreateASingleProperty()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );

        engine.execute( "CREATE ({actor: 'David Tennant'})" );

        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE n";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "START n=node(*) return n" );

        assertEquals( 1, executionResult.size() );
    }
}
