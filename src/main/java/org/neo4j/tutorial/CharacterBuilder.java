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

    public void fact(DoctorWhoUniverse universe) {
        GraphDatabaseService database = universe.getDatabase();
        Node characterNode = ensureCharacterIsInDb(characterName, universe);

        if (species != null) {
            for (String speciesString : species) {
                ensureRelationshipInDb(characterNode, DoctorWhoUniverse.IS_A, SpeciesBuilder.ensureSpeciesInDb(speciesString, universe));
            }
        }

        if (companion) {
            ensureCompanionRelationshipInDb(characterNode, universe);
        }

        if (enemy) {
            ensureEnemyOfRelationshipInDb(characterNode, universe);
        }

        if (ally) {
            ensureRelationshipInDb(characterNode, DoctorWhoUniverse.ALLY_OF, universe.theDoctor());
        }

        if (loverNames != null) {
            ensureLoversInDb(characterNode, loverNames, universe);
        }

        if (planet != null) {
            ensurePlanetInDb(characterNode, planet, database);
        }

        if (things != null) {
            ensureThingsInDb(characterNode, things, universe);
        }

        if (actors != null) {
            ensureActorsInDb(characterNode, actors, universe);
        }
    }

    public static void ensureEnemyOfRelationshipInDb(Node enemyNode, DoctorWhoUniverse universe) {
        ensureRelationshipInDb(enemyNode, DoctorWhoUniverse.ENEMY_OF, universe.theDoctor());
        ensureRelationshipInDb(universe.theDoctor(), DoctorWhoUniverse.ENEMY_OF, enemyNode);
    }

    public static void ensureCompanionRelationshipInDb(Node companionNode, DoctorWhoUniverse universe) {
        ensureRelationshipInDb(companionNode, DoctorWhoUniverse.COMPANION_OF, universe.theDoctor());
    }

    public static void ensureActorsInDb(Node characterNode, String[] actors, DoctorWhoUniverse universe) {
        Node previousActorNode = null;
        for (String actor : actors) {
            Node theActorNode = universe.getDatabase().index().forNodes("actors").get("actor", actor).getSingle();
            if (theActorNode == null) {
                theActorNode = universe.getDatabase().createNode();
                theActorNode.setProperty("actor", actor);
                universe.getDatabase().index().forNodes("actors").add(theActorNode, "actor", actor);
            }
            
            ensureRelationshipInDb(theActorNode, DoctorWhoUniverse.PLAYED, characterNode);
            universe.actorIndex.add(theActorNode, "actor", actor);

            if (previousActorNode != null) {
                ensureRelationshipInDb(previousActorNode, DoctorWhoUniverse.REGENERATED_TO, theActorNode);
            }

            previousActorNode = theActorNode;
        }
    }

    private static void ensureThingsInDb(Node characterNode, String[] things, DoctorWhoUniverse universe) {
        for (String thing : things) {
            ensureRelationshipInDb(characterNode, DoctorWhoUniverse.OWNS, ensureThingInDb(thing, universe.getDatabase()));
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

        ensureRelationshipInDb(characterNode, DoctorWhoUniverse.COMES_FROM, thePlanetNode);

        return thePlanetNode;
    }

    private static void ensurePlanetIsIndexed(Node thePlanetNode, GraphDatabaseService database) {
        database.index().forNodes("planets").add(thePlanetNode, "planet", thePlanetNode.getProperty("planet"));
    }

    public static Node ensureCharacterIsInDb(String name, DoctorWhoUniverse universe) {
        Node theCharacterNode = universe.getDatabase().index().forNodes("characters").get("name", name).getSingle();
        if (theCharacterNode == null) {
            theCharacterNode = universe.getDatabase().createNode();
            theCharacterNode.setProperty("name", name);
            ensureCharacterIsIndexed(theCharacterNode, universe.getDatabase());
        }
        return theCharacterNode;
    }

    private static void ensureCharacterIsIndexed(Node characterNode, GraphDatabaseService database) {
        if (database.index().forNodes("characters").get("name", characterNode.getProperty("name")).getSingle() == null) {
            database.index().forNodes("characters").add(characterNode, "name", characterNode.getProperty("name"));
        }
    }

    private static void ensureLoversInDb(Node characterNode, String[] loverNames, DoctorWhoUniverse universe) {
        for (String lover : loverNames) {
            ensureRelationshipInDb(characterNode, DoctorWhoUniverse.LOVES, ensureCharacterIsInDb(lover, universe));
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
