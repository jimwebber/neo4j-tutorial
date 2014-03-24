package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * In this koan, we create indexes to boost query performance.
 */
public class Koan12
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

    // TODO: write an exercise
}