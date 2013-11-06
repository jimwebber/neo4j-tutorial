package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static junit.framework.Assert.assertEquals;

public class AwesomenessRatingEngineTest
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
    public void shouldRateTheDoctorAs100PercentAwesome()
    {
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            AwesomenessRatingEngine engine = new AwesomenessRatingEngine();
            assertEquals( 100.0, engine.rateAwesomeness( universe.getDatabase(), universe.theDoctor().getId() ) );
            tx.failure();
        }

    }

    @Test
    public void shouldRateCompanionsAs50PercentAwesome()
    {
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            Node rose = universe.getDatabase().index().forNodes( "characters" ).get( "character",
                    "Rose Tyler" ).getSingle();

            AwesomenessRatingEngine engine = new AwesomenessRatingEngine();
            assertEquals( 50.0, engine.rateAwesomeness( universe.getDatabase(), rose.getId() ) );
            tx.failure();
        }
    }


    @Test
    public void shouldRateEarthAs33PercentAwesome()
    {
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            Node earth = universe.getDatabase().index().forNodes( "planets" ).get( "planet", "Earth" ).getSingle();

            AwesomenessRatingEngine engine = new AwesomenessRatingEngine();
            assertEquals( 33.3, engine.rateAwesomeness( universe.getDatabase(), earth.getId() ), 0.3 );
            tx.failure();
        }
    }
}
