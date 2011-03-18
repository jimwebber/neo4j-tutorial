package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;

public class DoctorWhoUniverse {
    private GraphDatabaseService db = DatabaseHelper.createDatabase();
    private IndexService index = new LuceneIndexService(db);

    public DoctorWhoUniverse() throws RuntimeException {
        try {
            loadAndIndexTimelordActors("Doctor", new File("src/main/resources/doctors.json"));
            loadAndIndexTimelordActors("Master", new File("src/main/resources/masters.json"));

            makeDoctorAndMasterEnemies();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void makeDoctorAndMasterEnemies() {
        Transaction tx = db.beginTx();
        try {
            
            Node theDoctor = index.getSingleNode("timelord-name", "Doctor");
            Node theMaster = index.getSingleNode("timelord-name", "Master");
            theDoctor.createRelationshipTo(theMaster, DynamicRelationshipType.withName("ENEMY_OF"));
            theMaster.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("ENEMY_OF"));

            tx.success();
        } finally {
            tx.finish();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void loadAndIndexTimelordActors(String timelordName, File timelordActorData) throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper m = new ObjectMapper();
        List<ArrayList> timelordActorList = m.readValue(timelordActorData, List.class);

        Transaction tx = db.beginTx();
        try {
            Node timelord = db.createNode();
            timelord.setProperty("timelord-name", timelordName);
            addTimelordToIndex(timelord);

            Node previousActor = null;
            for (int i = 0; i < timelordActorList.size(); i++) {
                int incarnation = i + 1;
                Node currentActor = createTimelordActor(((ArrayList<String>) timelordActorList.get(i)).get(0),
                        ((ArrayList<String>) timelordActorList.get(i)).get(1), incarnation);
                linkToTimelord(currentActor, timelord);
                addTimelordActorToIndex(currentActor);

                if (previousActor != null) {
                    previousActor.createRelationshipTo(currentActor, DynamicRelationshipType.withName("REGENERATED_TO"));
                }

                previousActor = currentActor;
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void linkToTimelord(Node currentActor, Node timelord) {
        currentActor.createRelationshipTo(timelord, DynamicRelationshipType.withName("PLAYED"));
    }

    private void addTimelordToIndex(Node timelord) {
        index.index(timelord, "timelord-name", timelord.getProperty("timelord-name"));
    }

    private void addTimelordActorToIndex(Node actor) {
        index.index(actor, "firstname", actor.getProperty("firstname"));
        index.index(actor, "lastname", actor.getProperty("lastname"));
    }

    private Node createTimelordActor(String firstname, String lastname, int incarnation) {
        Node timelord = db.createNode();
        timelord.setProperty("firstname", firstname);
        timelord.setProperty("lastname", lastname);
        timelord.setProperty("incarnation", incarnation);

        return timelord;
    }

    public GraphDatabaseService getDatabase() {
        return db;
    }

    public IndexService getIndex() {
        return index;
    }
}
