package org.neo4j.tutorial;

import org.junit.Before;

/**
 * In this Koan we start using the simple traversal framework to find interesting information
 * from the graph.
 */
public class Koan05 {

    private DoctorWhoUniverse universe;

    @Before
    public void createADatabase() {

        universe = new DoctorWhoUniverse();
    }
    
    @Test
    public void shouldFindAllCompanions() {
        
    }
}
