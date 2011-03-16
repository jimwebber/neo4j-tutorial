package org.neo4j.tutorial;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;


public class DoctorWhoUniverseTest {
    
    private GraphDatabaseService doctorWhoDatabase;
    
    @Before
    public void startDatabase() {
        doctorWhoDatabase = new DoctorWhoUniverse().getDoctorWhoDatabase();
    }
    
    @After
    public void stopDatabase() {
        doctorWhoDatabase.shutdown();
    }
    
    @Test
    public void shouldHave11Doctors() {
        int numberOfDoctors = 11;
        int referenceNodeCount = 1;
        assertEquals(numberOfDoctors + referenceNodeCount, DatabaseHelper.countNodes(doctorWhoDatabase.getAllNodes()));
        
    }
    
    @Test
    public void shouldBeTenRegenerationRelationshipsBetweenTheElevenDoctors() {
        
    }
}
