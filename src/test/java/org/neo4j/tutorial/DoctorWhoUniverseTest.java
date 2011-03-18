package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.neo4j.tutorial.DoctorWhoUniverse.PLAYED;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class DoctorWhoUniverseTest {
    
    private DoctorWhoUniverse doctorWhoUniverse = new DoctorWhoUniverse();
    private GraphDatabaseService doctorWhoDatabase;

    @Before
    public void startDatabase() {
        doctorWhoDatabase = doctorWhoUniverse.getDatabase();
    }

    @After
    public void stopDatabase() {
        doctorWhoDatabase.shutdown();
    }

    @Test
    public void shouldHave11Doctors() {
        int numberOfDoctors = 11;
        
        Node theDoctor = doctorWhoUniverse.getIndex().getSingleNode("timelord-name", "Doctor");
        assertNotNull(theDoctor);
        assertEquals(numberOfDoctors, DatabaseHelper.countRelationships(theDoctor.getRelationships(PLAYED, Direction.INCOMING)));
    }

    @Test
    public void shouldBeTenRegenerationRelationshipsBetweenTheElevenDoctors() {
        Node currentDoctor = doctorWhoUniverse.getIndex().getSingleNode("lastname", "Hartnell");
        int numberOfDoctorsRegenerations = 10;
        assertEquals(numberOfDoctorsRegenerations, countRelationships(currentDoctor));
    }
    
    @Test
    public void shouldBeSevenRegenerationRelationshipsBetweenTheEightMasters() {
        Node currentDoctor = doctorWhoUniverse.getIndex().getSingleNode("lastname", "Delgado");
        int numberOfMastersRegenerations = 7;
        assertEquals(numberOfMastersRegenerations, countRelationships(currentDoctor));
    }

    private int countRelationships(Node timelord) {
        int regenerationCount = 0;
        while (true) {
            List<Relationship> relationships = DatabaseHelper.toList(timelord.getRelationships(DoctorWhoUniverse.REGENERATED_TO,
                    Direction.OUTGOING));

            if (relationships.size() == 1) {
                Relationship regeneratedTo = relationships.get(0);
                timelord = regeneratedTo.getEndNode();
                regenerationCount++;
            } else {
                break;
            }
        }
        return regenerationCount;
    }

    @Test
    public void shouldHave8Masters() {
        int numberOfMasters = 8;
        
        Node theMaster = doctorWhoUniverse.getIndex().getSingleNode("timelord-name", "Master");
        assertNotNull(theMaster);
        assertEquals(numberOfMasters, DatabaseHelper.countRelationships(theMaster.getRelationships(PLAYED, Direction.INCOMING)));
    }
    
    @Test
    public void shouldContainMasterAndDoctorInTheIndex() {
        assertNotNull(doctorWhoUniverse.getIndex().getSingleNode("timelord-name", "Master"));
        assertNotNull(doctorWhoUniverse.getIndex().getSingleNode("timelord-name", "Doctor"));
    }
    
    @Test
    public void doctorAndMasterAreTimelords() {
        Node timelord = doctorWhoUniverse.getIndex().getSingleNode("species", "Timelord");
        
        Iterable<Relationship> relationships = timelord.getRelationships(DoctorWhoUniverse.IS_A, Direction.INCOMING);
        
        int numberOfTimelords = 2;
        assertEquals(numberOfTimelords, DatabaseHelper.countRelationships(relationships));
    }
    
    @Test
    public void timelordsAreFromGalifrey() {
        Node planet = doctorWhoUniverse.getIndex().getSingleNode("planet", "Galifrey");
       
        Iterable<Relationship> relationships = planet.getRelationships(DoctorWhoUniverse.FROM, Direction.INCOMING);
        
        int numberOfSpeciesFromGalifrey = 1;
        assertEquals(numberOfSpeciesFromGalifrey, DatabaseHelper.countRelationships(relationships));
    }
    
}
