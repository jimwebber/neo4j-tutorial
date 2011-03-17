package org.neo4j.tutorial;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.index.IndexService;

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
        int referenceNodeCount = 1;
        assertEquals(numberOfDoctors + referenceNodeCount, DatabaseHelper.countNodes(doctorWhoDatabase.getAllNodes()));

    }

    @Test
    public void shouldBeTenRegenerationRelationshipsBetweenTheElevenDoctors() {
        IndexService index = doctorWhoUniverse.getIndex();
        Node currentDoctor = index.getSingleNode("lastname", "Hartnell");
        int regenerationCount = 0;
        while (true) {
            List<Relationship> relationships = DatabaseHelper.toList(currentDoctor.getRelationships(DynamicRelationshipType.withName("REGENERATED_TO"),
                    Direction.OUTGOING));

            if (relationships.size() == 1) {
                Relationship regeneratedTo = relationships.get(0);
                currentDoctor = regeneratedTo.getEndNode();
                regenerationCount++;
            } else {
                break;
            }
        }
        assertEquals(10, regenerationCount);
    }
}
