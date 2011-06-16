package org.neo4j.tutorial;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;

public abstract class DoctorWhoUniverse<T> {

    public static final RelationshipType REGENERATED_TO = DynamicRelationshipType.withName("REGENERATED_TO");
    public static final RelationshipType PLAYED = DynamicRelationshipType.withName("PLAYED");
    public static final RelationshipType ENEMY_OF = DynamicRelationshipType.withName("ENEMY_OF");
    public static final RelationshipType COMES_FROM = DynamicRelationshipType.withName("COMES_FROM");
    public static final RelationshipType IS_A = DynamicRelationshipType.withName("IS_A");
    public static final RelationshipType COMPANION_OF = DynamicRelationshipType.withName("COMPANION_OF");
    public static final RelationshipType APPEARED_IN = DynamicRelationshipType.withName("APPEARED_IN");
    public static final RelationshipType LOVES = DynamicRelationshipType.withName("LOVES");
    public static final RelationshipType OWNS = DynamicRelationshipType.withName("OWNS");
    public static final RelationshipType ALLY_OF = DynamicRelationshipType.withName("ALLY_OF");
    
    private final String dbDir = DatabaseHelper.createTempDatabaseDir().getAbsolutePath();
    
    public DoctorWhoUniverse() {
    	GraphDatabaseService db = DatabaseHelper.createDatabase(dbDir);
        addCharacters(db);
        addSpecies(db);
        addPlanets(db);
        addEpisodes(db);
        db.shutdown();
    }

    private void addEpisodes(GraphDatabaseService db) {
        Episodes episodes = new Episodes(db);
        episodes.insert();
    }

    private void addCharacters(GraphDatabaseService db) {
        Characters characters = new Characters(db);
        characters.insert();
    }

    private void addSpecies(GraphDatabaseService db) {
        Species species = new Species(db);
        species.insert();
    }

    private void addPlanets(GraphDatabaseService db) {
        Planets planets = new Planets(db);
        planets.insert();
    }
    
    protected final String getDatabaseDirectory() {
    	return dbDir;
    }

    abstract T theDoctor();

    abstract void stop();
}
