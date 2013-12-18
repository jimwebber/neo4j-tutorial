package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static junit.framework.Assert.assertEquals;

import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;
import static org.neo4j.tutorial.DoctorWhoLabels.PLANET;

public class AwesomenessRatingEngineTest
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
    public void shouldRateTheDoctorAs100PercentAwesome()
    {
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            AwesomenessRatingEngine engine = new AwesomenessRatingEngine( universe.getDatabase() );
            assertEquals( 100.0, engine.rateAwesomeness( universe.theDoctor() ) );
            tx.failure();
        }

    }

    @Test
    public void shouldRateCompanionsAs50PercentAwesome()
    {
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            Node rose = universe.getDatabase().findNodesByLabelAndProperty( CHARACTER, "character",
                    "Rose Tyler" ).iterator().next();

            AwesomenessRatingEngine engine = new AwesomenessRatingEngine( universe.getDatabase() );
            assertEquals( 50.0, engine.rateAwesomeness( rose ) );
            tx.failure();
        }
    }


    @Test
    public void shouldRateEarthAs33PercentAwesome()
    {
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            Node earth = universe.getDatabase().findNodesByLabelAndProperty( PLANET, "planet",
                    "Earth" ).iterator().next();

            AwesomenessRatingEngine engine = new AwesomenessRatingEngine( universe.getDatabase() );
            assertEquals( 33.3, engine.rateAwesomeness( earth ), 0.3 );
            tx.failure();
        }
    }
}
