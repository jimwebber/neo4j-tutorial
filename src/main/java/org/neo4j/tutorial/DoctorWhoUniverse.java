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
import org.neo4j.graphdb.RelationshipType;
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

    public DoctorWhoUniverse() throws RuntimeException, JsonParseException, JsonMappingException, IOException {
        Transaction tx = db.beginTx();
        try {
            Node timelord = createSpecies("Timelord", "Gallifrey");
            
            Node theDoctor = loadActors("Doctor", timelord, REGENERATED_TO, new File("src/main/resources/doctors.json"));
            Node theMaster = loadActors("Master", timelord, REGENERATED_TO, new File("src/main/resources/masters.json"));
            
            Node cyberman = createSpecies("Cyberman", "Mondas"); // Not Telos, that was just occupied
            Node dalek = createSpecies("Dalek", "Skaro");
            
            makeEnemies(theDoctor, theMaster);
            makeEnemies(dalek, cyberman);
            makeEnemies(theDoctor, dalek);
            makeEnemies(theDoctor, cyberman);
            
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private Node createSpecies(String species, String homePlanetName) {
        Node speciesNode = db.createNode();
        speciesNode.setProperty("species", species);
        index.index(speciesNode, "species", species);
        
        Node homePlanetNode = db.createNode();
        homePlanetNode.setProperty("planet", homePlanetName);
        index.index(homePlanetNode, "planet", homePlanetName);
        
        
        speciesNode.createRelationshipTo(homePlanetNode, FROM);
        
        
        return speciesNode;
    }

    private void makeEnemies(Node a, Node b) {
        a.createRelationshipTo(b, ENEMY_OF);
        b.createRelationshipTo(a, ENEMY_OF);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Node loadActors(String characterName, Node species, RelationshipType relationshipType, File actorData) throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper m = new ObjectMapper();
        List<ArrayList> actorList = m.readValue(actorData, List.class);

        Node character = db.createNode();
        character.setProperty("name", characterName);
        index.index(character, "name", characterName);

        character.createRelationshipTo(species, IS_A);
        
        if(actorList.size() > 1) {
            insertCharacterActorsInChronologicalOrder(character, relationshipType, actorList);
        } else {
            
        }

        return character;
    }

    private void insertCharacterActorsInChronologicalOrder(Node character, RelationshipType relationshipType, @SuppressWarnings("rawtypes") List<ArrayList> actorList) {
       
        Node previousActor = null;
        for (int i = 0; i < actorList.size(); i++) {
            @SuppressWarnings("unchecked")
            Node currentActor = createActor(((ArrayList<String>) actorList.get(i)).get(0),
                    ((ArrayList<String>) actorList.get(i)).get(1));
            
            currentActor.createRelationshipTo(character, PLAYED);
            
            if (previousActor != null) {
                previousActor.createRelationshipTo(currentActor, relationshipType);
            }

            previousActor = currentActor;
        }
    }

   
    private Node createActor(String firstname, String lastname) {
        Node actor = db.createNode();
        actor.setProperty("firstname", firstname);
        actor.setProperty("lastname", lastname);
        
        index.index(actor, "lastname", lastname);
        
        return actor;
    }

    public GraphDatabaseService getDatabase() {
        return db;
    }

    public IndexService getIndex() {
        return index;
    }
}
