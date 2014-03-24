package org.neo4j.tutorial;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;

public class Koan8
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator().getDatabase() );
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

        cql = "MATCH (doctor:Character {character: 'Doctor'})<-[r:COMPANION_OF]-(companion:Character) " +
                "WHERE has(companion.firstname) AND companion.firstname='James' " +
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

        cql = "MATCH (doctor:Character {character: 'Doctor'})" +
                "<-[:PLAYED]-(actor:Actor) " +
                "WHERE HAS (actor.salary) " +
                "REMOVE actor.salary";

        // SNIPPET_END

        // Just for now, while we're converting the builder code to Cypher
        engine.execute( cql );

        final ExecutionResult executionResult = engine.execute(
                "MATCH (doctor:Character {character: 'Doctor'})<-[:PLAYED]-(actor:Character) " +
                        "WHERE HAS (actor.salary) " +
                        "RETURN actor" );

        assertEquals( 0, executionResult.size() );
    }

    @Test
    public void shouldDeleteAllTheThings() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (n)\n" +
                "OPTIONAL MATCH (n)-[r]-()\n" +
                "DELETE n,r";

        // SNIPPET_END

        engine.execute( cql );

        assertThat( universe.getDatabase(), allNodesAndRelationshipsErased() );

    }

    private Matcher<GraphDatabaseService> allNodesAndRelationshipsErased()
    {
        return new TypeSafeMatcher<GraphDatabaseService>()
        {
            @Override
            protected boolean matchesSafely( GraphDatabaseService graphDatabaseService )
            {
                try ( Transaction tx = graphDatabaseService.beginTx() )
                {
                    boolean result = new DatabaseHelper( graphDatabaseService ).destructivelyCount(
                            GlobalGraphOperations.at( graphDatabaseService ).getAllNodes() ) == 0 &&
                            new DatabaseHelper( graphDatabaseService ).destructivelyCount(
                                    GlobalGraphOperations.at( graphDatabaseService ).getAllRelationships() ) == 0;
                    tx.success();

                    return result;
                }
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "" );
            }
        };
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
