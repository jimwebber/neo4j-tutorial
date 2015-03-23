package org.neo4j.tutorial;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import org.neo4j.graphdb.*;
import org.neo4j.tooling.GlobalGraphOperations;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;

import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;

/**
 * In this Koan we learn how to erase structural elements of the graph, starting with Star Trek...
 */
public class Koan8
{
    @Rule
    public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldRemoveCaptainKirkFromTheDatabase()
    {
        polluteUniverseWithStarTrekData();
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService(); 
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (:Character {character: 'Doctor'})<-[r:COMPANION_OF]-" +
                "(c:Character {firstname: 'James', initial: 'T', lastname: 'Kirk'}) " +
                "DELETE r, c";


        // SNIPPET_END

        db.execute( cql );

        final Result result = db.execute( deletedKirkQuery );

        assertFalse(result.hasNext());
    }

    @Test
    public void shouldRemoveSalaryDataFromDoctorActors()
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService(); 
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (doctor:Character {character: 'Doctor'})" +
                "<-[:PLAYED]-(actor:Actor) " +
                "WHERE HAS (actor.salary) " +
                "REMOVE actor.salary";

        // SNIPPET_END

        // Just for now, while we're converting the builder code to Cypher
        db.execute( cql );

        final Result result = db.execute(
                "MATCH (doctor:Character {character: 'Doctor'})<-[:PLAYED]-(actor:Actor) " +
                        "WHERE HAS (actor.salary) " +
                        "RETURN actor"
        );

        assertFalse(result.hasNext());
    }

    @Test
    public void shouldDeleteAllTheThings() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService(); 
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (n)\n" +
                "OPTIONAL MATCH (n)-[r]-()\n" +
                "DELETE n,r";

        // SNIPPET_END

        db.execute( cql );

        assertThat( db, allNodesAndRelationshipsErased() );

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
                    boolean result = (new DatabaseHelper( graphDatabaseService ).destructivelyCount(
                            GlobalGraphOperations.at( graphDatabaseService ).getAllNodes() ) == 0) &&
                            (new DatabaseHelper( graphDatabaseService ).destructivelyCount(
                                    GlobalGraphOperations.at( graphDatabaseService ).getAllRelationships() ) == 0);
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
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        Node captainKirk = null;
        try ( Transaction tx = db.beginTx() )
        {
            Node theDoctor = neo4jResource.theDoctor();

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
