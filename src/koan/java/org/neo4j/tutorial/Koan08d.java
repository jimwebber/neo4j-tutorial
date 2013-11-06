package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static junit.framework.Assert.assertEquals;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;

public class Koan08d
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
    public void shouldRemoveCaptainKirkFromTheDatabase()
    {
        polluteUniverseWithStarTrekData();
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (doctor:Character)<-[r:COMPANION_OF]-(companion:Character) " +
                "WHERE doctor.character='Doctor' " +
                "AND has(companion.firstname) AND companion.firstname='James' " +
                "AND has(companion.initial) AND companion.initial='T' " +
                "AND has(companion.lastname) AND companion.lastname='Kirk' " +
                "DELETE r, companion";


        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute( deletedKirkQuery );

        assertEquals( 0, executionResult.size() );
    }

    @Test
    public void shouldRemoveSalaryDataFromDoctorActors()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (doctor:Character)<-[:PLAYED]-(actor:Character) " +
                "WHERE doctor.character='Doctor' " +
                "DELETE actor.salary";

        // SNIPPET_END

        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute(
                "MATCH (doctor:Character)<-[:PLAYED]-(actor:Character) " +
                        "WHERE doctor.character='Doctor' " +
                        "RETURN actor" );

        assertEquals( 0, executionResult.size() );
    }

    private void polluteUniverseWithStarTrekData()
    {
        GraphDatabaseService db = universe.getDatabase();
        Node captainKirk = null;
        try ( Transaction tx = db.beginTx() )
        {
            Node theDoctor = universe.theDoctor();

            captainKirk = db.createNode();
            captainKirk.setProperty( "firstname", "James" );
            captainKirk.setProperty( "initial", "T" );
            captainKirk.setProperty( "lastname", "Kirk" );
            captainKirk.addLabel( CHARACTER );

            captainKirk.createRelationshipTo( theDoctor, DynamicRelationshipType.withName( "COMPANION_OF" ) );

            tx.success();
        }
    }

    private static final String deletedKirkQuery =
            "MATCH (doctor:Character)<-[:COMPANION_OF]-(companion:Character) " +
                    "WHERE doctor.character='Doctor'" +
                    "AND has(companion.firstname) AND companion.firstname='James' " +
                    "AND has(companion.initial) AND companion.initial='T' " +
                    "AND has(companion.lastname) AND companion.lastname='Kirk' " +
                    "RETURN companion";
}
