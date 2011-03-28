package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

public class DoctorWhoUniverse {

    public static final DynamicRelationshipType REGENERATED_TO = DynamicRelationshipType.withName("REGENERATED_TO");
    public static final DynamicRelationshipType PLAYED = DynamicRelationshipType.withName("PLAYED");
    public static final DynamicRelationshipType ENEMY_OF = DynamicRelationshipType.withName("ENEMY_OF");
    public static final DynamicRelationshipType FROM = DynamicRelationshipType.withName("FROM");
    public static final DynamicRelationshipType IS_A = DynamicRelationshipType.withName("IS_A");
    public static final DynamicRelationshipType COMPANION_OF = DynamicRelationshipType.withName("COMPANION_OF");

    private GraphDatabaseService db = DatabaseHelper.createDatabase();

    Index<Node> actorIndex = db.index().forNodes("actors");
    Index<Node> characterIndex = db.index().forNodes("characters");
    Index<Node> planetIndex = db.index().forNodes("planets");
    Index<Node> speciesIndex = db.index().forNodes("species");

    public DoctorWhoUniverse() throws RuntimeException, JsonParseException, JsonMappingException, IOException {
        Transaction tx = db.beginTx();
        try {
            Node timelord = createSpecies("Timelord");
            Node gallifrey = createPlanet("Gallifrey");
            speciesComesFromPlanet(timelord, gallifrey);

            Node theDoctor = loadTimelordActors("Doctor", timelord, REGENERATED_TO, new File("src/main/resources/doctors.json"));
            Node theMaster = loadTimelordActors("Master", timelord, REGENERATED_TO, new File("src/main/resources/masters.json"));
            makeEnemies(theMaster, theDoctor);

            loadCompanions(theDoctor, new File("src/main/resources/companions.json"));

            loadEnemyIndividuals(theDoctor, new File("src/main/resources/enemy-individuals.json"));
            loadEnemySpecies(theDoctor, new File("src/main/resources/enemy-species.json"));

            tx.success();
        } finally {
            tx.finish();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void loadEnemySpecies(Node enemyOf, File enemiesData) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper m = new ObjectMapper();
        Map enemies = m.readValue(enemiesData, Map.class);

        for (Object speciesName : enemies.keySet()) {
            addEnemySpecies(enemyOf, (String) speciesName, (Map<String, String>) enemies.get(speciesName));
        }
    }

    private void addEnemySpecies(Node enemyOf, String enemySpeciesName, Map<String, String> enemyInfo) {
        Node enemySpecies = db.createNode();
        enemySpecies.setProperty("species", enemySpeciesName);
        speciesIndex.add(enemySpecies, "species", enemySpeciesName);
        enemySpecies.createRelationshipTo(enemyOf, ENEMY_OF);
        enemyOf.createRelationshipTo(enemySpecies, ENEMY_OF);

        for (String key : enemyInfo.keySet()) {
            createPlanet(enemyInfo.get(key));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void loadEnemyIndividuals(Node theDoctor, File enemiesData) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper m = new ObjectMapper();
        Map enemies = m.readValue(enemiesData, Map.class);

        for (Object enemyName : enemies.keySet()) {
            addEnemyIndividual(theDoctor, (String) enemyName, (Map<String, String>) enemies.get(enemyName));
        }
    }

    private void addEnemyIndividual(Node enemyOf, String enemyName, Map<String, String> enemyInfo) {
        Node enemy = db.createNode();
        enemy.setProperty("name", enemyName);
        characterIndex.add(enemy, "name", enemyName);
        enemy.createRelationshipTo(enemyOf, ENEMY_OF);
        enemyOf.createRelationshipTo(enemy, ENEMY_OF);

        for (String key : enemyInfo.keySet()) {
            String value = enemyInfo.get(key);
            enemy.setProperty(key, value);
            if (key.equals("species")) {
                createSpecies(value);
            }
            
            if(key.equals("planet")) {
                createPlanet(value);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void loadCompanions(Node theDoctor, File companionsData) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper m = new ObjectMapper();
        List<ArrayList> companionsList = m.readValue(companionsData, List.class);

        for (ArrayList companionData : companionsList) {
            addCompanion(theDoctor, companionData);
        }
    }

    private void addCompanion(Node theDoctor, ArrayList companionData) {
        if (companionData.size() < 1 || companionData.size() > 2) {
            return;
        }

        Node companion = db.createNode();
        if (companionData.size() == 1) {
            Object characterName = companionData.get(0);
            companion.setProperty("name", characterName);
            characterIndex.add(companion, "name", characterName);
        } else if (companionData.size() == 2) {
            Object firstName = companionData.get(0);
            companion.setProperty("firstname", firstName);
            characterIndex.add(companion, "firstname", firstName);

            Object lastName = companionData.get(1);
            companion.setProperty("lastname", lastName);
            characterIndex.add(companion, "lastname", lastName);
        }
        companion.createRelationshipTo(theDoctor, COMPANION_OF);
    }

    private Node createSpecies(String species) {
        Node speciesNode = speciesIndex.get("species", species).getSingle();
        if (speciesNode == null && species != null) {
            speciesNode = db.createNode();
            speciesNode.setProperty("species", species);
            speciesIndex.add(speciesNode, "species", species);
        }
        return speciesNode;
    }

    private Node createPlanet(String homePlanetName) {
        Node homePlanetNode = planetIndex.get("planet", homePlanetName).getSingle();
        if (homePlanetNode == null && homePlanetName != null) {
            homePlanetNode = db.createNode();
            homePlanetNode.setProperty("planet", homePlanetName);
            planetIndex.add(homePlanetNode, "planet", homePlanetName);
        }

        return homePlanetNode;
    }

    private void speciesComesFromPlanet(Node species, Node planet) {
        species.createRelationshipTo(planet, FROM);
    }

    private void makeEnemies(Node a, Node b) {
        a.createRelationshipTo(b, ENEMY_OF);
        b.createRelationshipTo(a, ENEMY_OF);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Node loadTimelordActors(String characterName, Node species, RelationshipType relationshipType, File actorData) throws JsonParseException,
            JsonMappingException, IOException {

        ObjectMapper m = new ObjectMapper();
        List<ArrayList> actorList = m.readValue(actorData, List.class);

        Node character = db.createNode();
        character.setProperty("name", characterName);
        characterIndex.add(character, "name", characterName);

        character.createRelationshipTo(species, IS_A);

        if (actorList.size() > 1) {
            insertCharacterActorsInChronologicalOrder(character, relationshipType, actorList);
        } else {

        }

        return character;
    }

    private void insertCharacterActorsInChronologicalOrder(Node character, RelationshipType relationshipType,
            @SuppressWarnings("rawtypes") List<ArrayList> actorList) {

        Node previousActor = null;
        for (int i = 0; i < actorList.size(); i++) {
            @SuppressWarnings("unchecked")
            Node currentActor = createActor(((ArrayList<String>) actorList.get(i)).get(0), ((ArrayList<String>) actorList.get(i)).get(1));

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

        actorIndex.add(actor, "lastname", lastname);

        return actor;
    }

    public GraphDatabaseService getDatabase() {
        return db;
    }

    public Index<Node> getActorIndex() {
        return actorIndex;
    }

    public Index<Node> getCharacterIndex() {
        return characterIndex;
    }

    public Index<Node> getPlanetIndex() {
        return planetIndex;
    }

    public Index<Node> getSpeciesIndex() {
        return speciesIndex;
    }
}
