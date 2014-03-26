package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.CypherExecutionException;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.fail;

import static org.neo4j.graphdb.DynamicLabel.label;
import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

/**
 * This koan introduces Neo4j 2.x constraints, which are a bit like schema insofar as they allow you to control
 * some aspects of the graph at design/runtime.
 */
public class Koan13
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator().getDatabase() );
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            // Unique actors constraint is created by the universe, we'll drop it here...
            universe.getDatabase().schema().getConstraints( label( "Actor" ) ).iterator().next().drop();
            tx.success();
        }

    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test(expected = CypherExecutionException.class)
    public void shouldRejectDuplicateActor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE CONSTRAINT ON (a:Actor) ASSERT a.actor IS UNIQUE";

        // SNIPPET_END

        engine.execute( cql );

        engine.execute( "CREATE (:Actor {actor: 'David Tennant'})" );

        /* Acting trivia: American and British actors' unions demand that no two actors have the same name! */
        fail( "Should not be able to create another actor called David Tennant" );
    }
}
