package org.neo4j.tutorial;

import static org.neo4j.tutorial.DatabaseHelper.ensureRelationshipInDb;

import java.util.HashSet;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class CharacterBuilder {
    private final String characterName;
    private HashSet<String> species;
    private boolean companion = false;
    private String[] loverNames;
    private String planet;
    private String[] things;
    private boolean enemy;
    private boolean ally;
    private String[] actors;

    public static CharacterBuilder character(String characterName) {
        return new CharacterBuilder(characterName);
    }

    public CharacterBuilder(String characterName) {
        this.characterName = characterName;
    }

    public CharacterBuilder isA(String speciesString) {
        if (species == null) {
            species = new HashSet<String>();
        }
        species.add(speciesString);
        return this;
    }

    public CharacterBuilder isCompanion() {
        companion = true;
        return this;
    }

    public void fact(GraphDatabaseService db) {
        Node characterNode = ensureCharacterIsInDb(characterName, db);
        Node theDoctor = db.index().forNodes("characters").get("name", "Doctor").getSingle();

        if (species != null) {
            for (String speciesString : species) {
                ensureRelationshipInDb(characterNode, DoctorWhoUniverseGenerator.IS_A, SpeciesBuilder.ensureSpeciesInDb(speciesString, db));
            }
        }

        if (companion) {
            ensureCompanionRelationshipInDb(characterNode, db);
        }

        if (enemy) {
            ensureEnemyOfRelationshipInDb(characterNode, db);
        }

        if (ally) {
            ensureRelationshipInDb(characterNode, DoctorWhoUniverseGenerator.ALLY_OF, theDoctor);
        }

        if (loverNames != null) {
            ensureLoversInDb(characterNode, loverNames, db);
        }

        if (planet != null) {
            ensurePlanetInDb(characterNode, planet, db);
        }

        if (things != null) {
            ensureThingsInDb(characterNode, things, db);
        }

        if (actors != null) {
            ensureActorsInDb(characterNode, actors, db);
        }
    }

    public static void ensureAllyOfRelationshipInDb(Node allyNode, GraphDatabaseService db) {
        Node theDoctor = db.index().forNodes("characters").get("name", "Doctor").getSingle();
    	ensureRelationshipInDb(allyNode, DoctorWhoUniverseGenerator.ALLY_OF, theDoctor);
        ensureRelationshipInDb(theDoctor, DoctorWhoUniverseGenerator.ALLY_OF, allyNode);
    }
    
    public static void ensureEnemyOfRelationshipInDb(Node enemyNode, GraphDatabaseService db) {
    	Node theDoctor = db.index().forNodes("characters").get("name", "Doctor").getSingle();
    	ensureRelationshipInDb(enemyNode, DoctorWhoUniverseGenerator.ENEMY_OF, theDoctor);
        ensureRelationshipInDb(theDoctor, DoctorWhoUniverseGenerator.ENEMY_OF, enemyNode);
    }

    public static void ensureCompanionRelationshipInDb(Node companionNode, GraphDatabaseService db) {
    	Node theDoctor = db.index().forNodes("characters").get("name", "Doctor").getSingle();
        ensureRelationshipInDb(companionNode, DoctorWhoUniverseGenerator.COMPANION_OF, theDoctor);
    }

    public static void ensureActorsInDb(Node characterNode, String[] actors, GraphDatabaseService db) {
        Node previousActorNode = null;
        for (String actor : actors) {
            Node theActorNode = db.index().forNodes("actors").get("actor", actor).getSingle();
            if (theActorNode == null) {
                theActorNode = db.createNode();
                theActorNode.setProperty("actor", actor);
                db.index().forNodes("actors").add(theActorNode, "actor", actor);
            }
            
            ensureRelationshipInDb(theActorNode, DoctorWhoUniverseGenerator.PLAYED, characterNode);
            db.index().forNodes("actors").add(theActorNode, "actor", actor);

            if (previousActorNode != null) {
                ensureRelationshipInDb(previousActorNode, DoctorWhoUniverseGenerator.REGENERATED_TO, theActorNode);
            }

            previousActorNode = theActorNode;
        }
    }

    private static void ensureThingsInDb(Node characterNode, String[] things, GraphDatabaseService db) {
        for (String thing : things) {
            ensureRelationshipInDb(characterNode, DoctorWhoUniverseGenerator.OWNS, ensureThingInDb(thing, db));
        }
    }

    private static Node ensureThingInDb(String thing, GraphDatabaseService database) {
        Node theThingNode = database.index().forNodes("things").get("thing", thing).getSingle();
        if (theThingNode == null) {
            theThingNode = database.createNode();
            theThingNode.setProperty("thing", thing);
            ensureThingIsIndexed(theThingNode, database);
        }

        return theThingNode;
    }

    private static void ensureThingIsIndexed(Node thingNode, GraphDatabaseService database) {
        database.index().forNodes("things").add(thingNode, "thing", thingNode.getProperty("thing"));
    }

    private static Node ensurePlanetInDb(Node characterNode, String planet, GraphDatabaseService database) {
        Node thePlanetNode = database.index().forNodes("planets").get("planet", planet).getSingle();
        if (thePlanetNode == null) {
            thePlanetNode = database.createNode();
            thePlanetNode.setProperty("planet", planet);
            ensurePlanetIsIndexed(thePlanetNode, database);
        }

        ensureRelationshipInDb(characterNode, DoctorWhoUniverseGenerator.COMES_FROM, thePlanetNode);

        return thePlanetNode;
    }

    private static void ensurePlanetIsIndexed(Node thePlanetNode, GraphDatabaseService database) {
        database.index().forNodes("planets").add(thePlanetNode, "planet", thePlanetNode.getProperty("planet"));
    }

    public static Node ensureCharacterIsInDb(String name, GraphDatabaseService db) {
        Node theCharacterNode = db.index().forNodes("characters").get("name", name).getSingle();
        if (theCharacterNode == null) {
            theCharacterNode = db.createNode();
            theCharacterNode.setProperty("name", name);
            ensureCharacterIsIndexed(theCharacterNode, db);
        }
        return theCharacterNode;
    }

    private static void ensureCharacterIsIndexed(Node characterNode, GraphDatabaseService database) {
        if (database.index().forNodes("characters").get("name", characterNode.getProperty("name")).getSingle() == null) {
            database.index().forNodes("characters").add(characterNode, "name", characterNode.getProperty("name"));
        }
    }

    private static void ensureLoversInDb(Node characterNode, String[] loverNames, GraphDatabaseService db) {
        for (String lover : loverNames) {
            ensureRelationshipInDb(characterNode, DoctorWhoUniverseGenerator.LOVES, ensureCharacterIsInDb(lover, db));
        }
    }

    public CharacterBuilder loves(String... loverNames) {
        this.loverNames = loverNames;
        return this;
    }

    public CharacterBuilder isFrom(String planet) {
        this.planet = planet;
        return this;
    }

    public CharacterBuilder owns(String... things) {
        this.things = things;
        return this;
    }

    public CharacterBuilder isEnemy() {
        this.enemy = true;
        return this;
    }

    public CharacterBuilder isAlly() {
        ally = true;
        return this;
    }

    public CharacterBuilder regenerationSequence(String... actors) {
        this.actors = actors;
        return this;
    }
}
