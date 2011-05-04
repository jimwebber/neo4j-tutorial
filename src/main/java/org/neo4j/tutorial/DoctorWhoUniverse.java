package org.neo4j.tutorial;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;

public class DoctorWhoUniverse {

    public static final DynamicRelationshipType REGENERATED_TO = DynamicRelationshipType.withName("REGENERATED_TO");
    public static final DynamicRelationshipType PLAYED = DynamicRelationshipType.withName("PLAYED");
    public static final DynamicRelationshipType ENEMY_OF = DynamicRelationshipType.withName("ENEMY_OF");
    public static final DynamicRelationshipType COMES_FROM = DynamicRelationshipType.withName("COMES_FROM");
    public static final DynamicRelationshipType IS_A = DynamicRelationshipType.withName("IS_A");
    public static final DynamicRelationshipType COMPANION_OF = DynamicRelationshipType.withName("COMPANION_OF");
    public static final RelationshipType APPEARED_IN = DynamicRelationshipType.withName("APPEARED_IN");
    public static final RelationshipType LOVES = DynamicRelationshipType.withName("LOVES");
    public static final RelationshipType OWNS = DynamicRelationshipType.withName("OWNS");
    public static final RelationshipType ALLY_OF = DynamicRelationshipType.withName("ALLY_OF");

    private GraphDatabaseService db = DatabaseHelper.createDatabase();

    Index<Node> characterIndex = db.index().forNodes("characters");
    Index<Node> planetIndex = db.index().forNodes("planets");
    Index<Node> speciesIndex = db.index().forNodes("species");
    Index<Node> thingsIndex = db.index().forNodes("things");

    public DoctorWhoUniverse() {
        addCharacters();
        addSpecies();
        addPlanets();
        addEpisodes();
    }

    private void addEpisodes() {
        Episodes episodes = new Episodes(this);
        episodes.insert();
    }

    private void addCharacters() {
        Characters characters = new Characters(this);
        characters.insert();
    }

    private void addSpecies() {
        Species species = new Species(this);
        species.insert();
    }

    private void addPlanets() {
        Planets planets = new Planets(this);
        planets.insert();
    }

    public Node theDoctor() {
        return characterIndex.get("name", "Doctor").getSingle();
    }

    public GraphDatabaseService getDatabase() {
        return db;
    }

    public void stop() {
        db.shutdown();
    }
}
