package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

/**
 * In this Koan we learn how to create, update, and delete nodes and relationships in the
 * database using the Cypher language.
 */
public class Koan08a
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldCreateASingleNode()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase() );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE n";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( "START n=node(*) return n" );

        final int oneNewNodePlusTheReferenceNodeExpected = 2;
        assertEquals( oneNewNodePlusTheReferenceNodeExpected, executionResult.size() );
    }

    @Test
    public void shouldCreateASingleNodeWithSomeProperties()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase() );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE n = { firstname : 'Tom', lastname : 'Baker' }";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        final ExecutionResult executionResult = engine.execute(
                "START n=node(*) WHERE has(n.firstname) AND n.firstname = 'Tom' AND  has(n.lastname) AND n.lastname = 'Baker' return n" );

        assertEquals( 1, executionResult.size() );
    }

    @Test
    public void shouldCreateASimpleConnectedGraph()
    {
        ExecutionEngine engine = new ExecutionEngine( DatabaseHelper.createDatabase() );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE doctor = { character : 'Doctor' }, master = { character : 'Master' }, (doctor)<-[r:ENEMY_OF]-(master) ";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        final ExecutionResult executionResult = engine.execute(
                "START a=node(*) \n" +
                        "MATCH a<-[:ENEMY_OF]-b \n" +
                        "WHERE has(a.character) AND a.character='Doctor' AND has(b.character) AND b.character = 'Master' \n" +
                        "RETURN a, b \n" );

        assertFalse( executionResult.isEmpty() );
    }
}
