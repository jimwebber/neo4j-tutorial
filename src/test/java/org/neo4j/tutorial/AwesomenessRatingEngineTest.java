package org.neo4j.tutorial;

import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.assertEquals;

import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;
import static org.neo4j.tutorial.DoctorWhoLabels.PLANET;

public class AwesomenessRatingEngineTest
{
    @ClassRule
    public static DoctorWhoUniverseResource neo4j = new DoctorWhoUniverseResource();

    @Test
    public void shouldRateTheDoctorAs100PercentAwesome()
    {
        try ( Transaction tx = neo4j.getGraphDatabaseService().beginTx() )
        {
            AwesomenessRatingEngine engine = new AwesomenessRatingEngine( neo4j.getGraphDatabaseService() );
            assertEquals( 100.0, engine.rateAwesomeness( neo4j.theDoctor() ), 0.0 );
            tx.failure();
        }

    }

    @Test
    public void shouldRateCompanionsAs50PercentAwesome()
    {
        try ( Transaction tx = neo4j.getGraphDatabaseService().beginTx() )
        {
            Node rose = neo4j.getGraphDatabaseService().findNode( CHARACTER, "character", "Rose Tyler" );

            AwesomenessRatingEngine engine = new AwesomenessRatingEngine( neo4j.getGraphDatabaseService() );
            assertEquals( 50.0, engine.rateAwesomeness( rose ), 0.0 );
            tx.failure();
        }
    }

    @Test
    public void shouldRateEarthAs33PercentAwesome()
    {
        try ( Transaction tx = neo4j.getGraphDatabaseService().beginTx() )
        {
            Node earth = neo4j.getGraphDatabaseService().findNode( PLANET, "planet", "Earth" );

            AwesomenessRatingEngine engine = new AwesomenessRatingEngine( neo4j.getGraphDatabaseService() );
            assertEquals( 33.3, engine.rateAwesomeness( earth ), 0.3 );
            tx.failure();
        }
    }
}
