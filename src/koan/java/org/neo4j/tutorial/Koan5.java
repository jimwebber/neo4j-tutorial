package org.neo4j.tutorial;

import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * In this Koan we learn how to merge new nodes and relationships into an existing graph using
 * using the Cypher language.
 */
public class Koan5
{
    // TODO: write an exercise
    @Test
    public void shouldMERGE_ALL_THE_THINGS()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START



        // SNIPPET_END

        engine.execute( cql );

    }
}
