package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class DoctorWhoUniverse {
    private GraphDatabaseService db = DatabaseHelper.createDatabase();

    public DoctorWhoUniverse() throws RuntimeException {
        try {
            loadDoctors(new File("src/main/resources/doctors.json"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void loadDoctors(File doctorsData) throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper m = new ObjectMapper();
        List<ArrayList> doctorsList = m.readValue(doctorsData, List.class);
        
        Transaction tx = db.beginTx();
        try {
            for(int i = 0; i < doctorsList.size(); i++) {
                createADoctor(((ArrayList<String>)doctorsList.get(i)).get(0), ((ArrayList<String>)doctorsList.get(i)).get(1), i);
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void createADoctor(String firstname, String lastname, int incarnation) {
        Node doctor = db.createNode();
        doctor.setProperty("firstname", firstname);
        doctor.setProperty("lastname", lastname);
        doctor.setProperty("incarnation", incarnation);
    }

}
