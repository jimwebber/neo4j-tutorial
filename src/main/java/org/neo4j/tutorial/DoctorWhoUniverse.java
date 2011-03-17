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
            loadAndIndexAllDoctors(new File("src/main/resources/doctors.json"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void loadAndIndexAllDoctors(File doctorsData) throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper m = new ObjectMapper();
        List<ArrayList> doctorsList = m.readValue(doctorsData, List.class);

        Transaction tx = db.beginTx();
        try {
            Node previousDoctor = null;
            for (int i = 0; i < doctorsList.size(); i++) {
                int incarnation = i+1;
                Node currentDoctor = createADoctor(((ArrayList<String>) doctorsList.get(i)).get(0), ((ArrayList<String>) doctorsList.get(i)).get(1), incarnation);

                addDoctorToIndex(currentDoctor);

                if (previousDoctor != null) {
                    previousDoctor.createRelationshipTo(currentDoctor, DynamicRelationshipType.withName("REGENERATED_TO"));
                }

                previousDoctor = currentDoctor;
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void addDoctorToIndex(Node currentDoctor) {
        index.index(currentDoctor, "firstname", currentDoctor.getProperty("firstname"));
        index.index(currentDoctor, "lastname", currentDoctor.getProperty("lastname"));
        index.index(currentDoctor, "incarnation", currentDoctor.getProperty("incarnation"));
    }

    private Node createADoctor(String firstname, String lastname, int incarnation) {
        Node doctor = db.createNode();
        doctor.setProperty("firstname", firstname);
        doctor.setProperty("lastname", lastname);
        doctor.setProperty("incarnation", incarnation);

        return doctor;
    }

    public GraphDatabaseService getDatabase() {
        return db;
    }
    
    public IndexService getIndex() {
        return index;
    }
}
