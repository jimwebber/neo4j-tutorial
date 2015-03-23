package org.neo4j.tutorial;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.cypher.CypherExecutionException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.fail;

import static org.neo4j.graphdb.DynamicLabel.label;

/**
 * This koan introduces Neo4j 2.x constraints, which are a bit like schema insofar as they allow you to control
 * some aspects of the graph at design/runtime.
 */
public class Koan13
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @BeforeClass
    public static void dropConstraint() throws Exception
    {
        try ( Transaction tx = neo4jResource.getGraphDatabaseService().beginTx() )
        {
            // Unique actors constraint is created by the universe, we'll drop it here...
            neo4jResource.getGraphDatabaseService().schema().getConstraints( label( "Actor" ) ).iterator().next().drop();
            tx.success();
        }
    }

    @Test(expected = QueryExecutionException.class)
    public void shouldRejectDuplicateActor()
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE CONSTRAINT ON (a:Actor) ASSERT a.actor IS UNIQUE";

        // SNIPPET_END

        db.execute( cql );

        db.execute( "CREATE (:Actor {actor: 'David Tennant'})" );

        /* Acting trivia: American and British actors' unions demand that no two actors have the same name! */
        fail( "Should not be able to create another actor called David Tennant" );
    }
}
