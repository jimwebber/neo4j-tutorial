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

    public static final DynamicRelationshipType REGENERATED_TO = DynamicRelationshipType.withName("REGENERATED_TO");
    public static final DynamicRelationshipType PLAYED = DynamicRelationshipType.withName("PLAYED");
    public static final DynamicRelationshipType ENEMY_OF = DynamicRelationshipType.withName("ENEMY_OF");
    public static final DynamicRelationshipType FROM = DynamicRelationshipType.withName("FROM");
    public static final DynamicRelationshipType IS_A = DynamicRelationshipType.withName("IS_A");

    private GraphDatabaseService db = DatabaseHelper.createDatabase();
    private IndexService index = new LuceneIndexService(db);

    public DoctorWhoUniverse() throws RuntimeException {
        try {
            loadAndIndexTimelordActors("Doctor", new File("src/main/resources/doctors.json"));
            loadAndIndexTimelordActors("Master", new File("src/main/resources/masters.json"));

            makeDoctorAndMasterEnemies();

            makeDoctorAndMasterTimelords();

            makeTimelordsComeFromGalifrey();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void makeTimelordsComeFromGalifrey() {
        Transaction tx = db.beginTx();
        try {

            Node galifrey = createPlanet("Galifrey");

            Node timelord = index.getSingleNode("species", "Timelord");

            timelord.createRelationshipTo(galifrey, FROM);

            tx.success();
        } finally {
            tx.finish();
        }
    }

    private Node createPlanet(String name) {
        Node planet = db.createNode();
        planet.setProperty("planet", name);

        index.index(planet, "planet", planet.getProperty("planet"));
        
        return planet;
    }

    private void makeDoctorAndMasterTimelords() {
        Transaction tx = db.beginTx();
        try {

            Node theDoctor = index.getSingleNode("timelord-name", "Doctor");
            Node theMaster = index.getSingleNode("timelord-name", "Master");

            Node timelord = db.createNode();
            timelord.setProperty("species", "Timelord");

            theDoctor.createRelationshipTo(timelord, IS_A);
            theMaster.createRelationshipTo(timelord, IS_A);

            index.index(timelord, "species", timelord.getProperty("species"));

            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void makeDoctorAndMasterEnemies() {
        Transaction tx = db.beginTx();
        try {

            Node theDoctor = index.getSingleNode("timelord-name", "Doctor");
            Node theMaster = index.getSingleNode("timelord-name", "Master");
            theDoctor.createRelationshipTo(theMaster, ENEMY_OF);
            theMaster.createRelationshipTo(theDoctor, ENEMY_OF);

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
                    previousActor.createRelationshipTo(currentActor, REGENERATED_TO);
                }

                previousActor = currentActor;
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void linkToTimelord(Node currentActor, Node timelord) {
        currentActor.createRelationshipTo(timelord, PLAYED);
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
