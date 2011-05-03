package org.neo4j.tutorial;

import java.io.File;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;

public class DoctorWhoUniverse {

    public static final DynamicRelationshipType REGENERATED_TO = DynamicRelationshipType.withName("REGENERATED_TO");
    public static final DynamicRelationshipType PLAYED = DynamicRelationshipType.withName("PLAYED");
    public static final DynamicRelationshipType ENEMY_OF = DynamicRelationshipType.withName("ENEMY_OF");
    public static final DynamicRelationshipType FROM = DynamicRelationshipType.withName("FROM");
    public static final DynamicRelationshipType IS_A = DynamicRelationshipType.withName("IS_A");
    public static final DynamicRelationshipType COMPANION_OF = DynamicRelationshipType.withName("COMPANION_OF");
    public static final RelationshipType APPEARED_IN = DynamicRelationshipType.withName("APPEARED_IN");

    private GraphDatabaseService db = DatabaseHelper.createDatabase();

    Index<Node> actorIndex = db.index().forNodes("actors");
    Index<Node> friendliesIndex = db.index().forNodes("friendlies");
    Index<Node> companionsIndex = db.index().forNodes("companions");
    Index<Node> enemiesIndex = db.index().forNodes("enemies");
    Index<Node> planetIndex = db.index().forNodes("planets");
    Index<Node> speciesIndex = db.index().forNodes("species");

    public DoctorWhoUniverse() {
        addPlanets();
        addSpecies();
        addFriendlies();
        addCompanions();
        addEnemySpecies();
        addEnemyIndividuals();
        addInterspeciesEnemies();
        addDoctorActors();
        addMasterActors();
        addEpisodes();
    }

    private void addEpisodes() {
        Episodes episodes = new Episodes(this);
        episodes.insert();
    }

    private void addInterspeciesEnemies() {
        InterSpeciesEnemies ise = new InterSpeciesEnemies(new File("src/main/resources/inter-species-enemies.json"));
        ise.insert(db);
    }

    private void addMasterActors() {
        Node theMaster = enemiesIndex.get("name", "Master").getSingle();
        Actors actors = new Actors(theMaster, new File("src/main/resources/master-actors.json"));
        actors.insertAndIndex(db, actorIndex, REGENERATED_TO);
    }

    private void addDoctorActors() {
        Actors actors = new Actors(theDoctor(), new File("src/main/resources/doctor-actors.json"));
        actors.insertAndIndex(db, actorIndex, REGENERATED_TO);
    }

    private void addEnemySpecies() {
        EnemySpecies enemySpecies = new EnemySpecies(theDoctor(), new File("src/main/resources/enemy-species.json"));
        enemySpecies.insertEnemySpeciesRelationships(db, speciesIndex);
    }

    private void addCompanions() {
        Companions companions = new Companions(new File("src/main/resources/companions.json"));
        companions.insertAndIndex(db, friendliesIndex, companionsIndex);
    }

    private void addEnemyIndividuals() {
        Enemies enemies = new Enemies(theDoctor(), new File("src/main/resources/enemies.json"));
        enemies.insertAndIndex(db, enemiesIndex, speciesIndex, planetIndex);
    }

    private void addFriendlies() {
        Friendlies friendlies = new Friendlies(new File("src/main/resources/friendlies.json"));
        friendlies.insertAndIndex(db, friendliesIndex, speciesIndex, planetIndex);
    }

    public Node theDoctor() {
        return friendliesIndex.get("name", "Doctor").getSingle();
    }

    private void addSpecies() {
        Species species = new Species(new File("src/main/resources/species.json"));
        species.insertAndIndex(db, speciesIndex, planetIndex);
    }

    private void addPlanets() {
        Planets planets = new Planets(new File("src/main/resources/planets.json"));
        planets.insertAndIndex(db, planetIndex);
    }

    public GraphDatabaseService getDatabase() {
        return db;
    }

    public Node theMaster() {
        return enemiesIndex.get("name", "Master").getSingle();
    }

    public void stop() {
        db.shutdown();
    }
}
