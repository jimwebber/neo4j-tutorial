package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * In this Koan we start using the new traversal framework to find interesting
 * information from the graph about the Doctor's love life.
 */
public class Koan06 {

    private static DoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception {
        universe = new DoctorWhoUniverse();
    }

    @AfterClass
    public static void closeTheDatabase() {
        universe.stop();
    }

    @Test
    public void shouldFindTheLoveRivalOfRiver() throws Exception {
    }
}
