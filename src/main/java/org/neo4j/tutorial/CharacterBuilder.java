package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class CharacterBuilder {
    private final String characterName;
    private String species;
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

    public CharacterBuilder isA(String species) {
        this.species = species;
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
            characterNode.createRelationshipTo(SpeciesBuilder.ensureSpeciesInDb(species, universe), DoctorWhoUniverse.IS_A);
        }

        if (companion) {
            characterNode.createRelationshipTo(universe.theDoctor(), DoctorWhoUniverse.COMPANION_OF);
        }

        if(enemy) {
            characterNode.createRelationshipTo(universe.theDoctor(), DoctorWhoUniverse.ENEMY_OF);
            universe.theDoctor().createRelationshipTo(characterNode, DoctorWhoUniverse.ENEMY_OF);
        }
        
        if(ally) {
            characterNode.createRelationshipTo(universe.theDoctor(), DoctorWhoUniverse.ALLY_OF);
        }
        
        if (loverNames != null) {
            ensureLoversInDb(characterNode, loverNames, universe);
        }
        
        if(planet != null) {
            ensurePlanetInDb(characterNode, planet, database);
        }
        
        if(things != null) {
            ensureThingsInDb(characterNode, things, universe);
        }
        
        if(actors != null) {
            ensureActorsInDb(characterNode, actors, universe);
        }
    }

    public static void ensureActorsInDb(Node characterNode, String[] actors, DoctorWhoUniverse universe) {
        Node previousActorNode = null;
        for(String actor : actors) {
            Node theActorNode = universe.getDatabase().index().forNodes("actors").get("actor", actor).getSingle();
            if(theActorNode == null) {
                theActorNode = universe.getDatabase().createNode();
                theActorNode.setProperty("actor", actor);
                universe.getDatabase().index().forNodes("actors").add(theActorNode, "actor", actor);
            }
            theActorNode.createRelationshipTo(characterNode, DoctorWhoUniverse.PLAYED);
            
            if(previousActorNode != null) {
                previousActorNode.createRelationshipTo(theActorNode, DoctorWhoUniverse.REGENERATED_TO);
            }
            
            previousActorNode = theActorNode;
        }
    }

    private static void ensureThingsInDb(Node characterNode, String[] things, DoctorWhoUniverse universe) {
        for(String thing : things) {
            characterNode.createRelationshipTo(ensureThingInDb(thing, universe.getDatabase()), DoctorWhoUniverse.OWNS);
        }
    }

    private static Node ensureThingInDb(String thing, GraphDatabaseService database) {
        Node theThingNode = database.index().forNodes("things").get("thing", thing).getSingle();
        if(theThingNode == null) {
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
        
        characterNode.createRelationshipTo(thePlanetNode, DoctorWhoUniverse.COMES_FROM);
        
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
        if(database.index().forNodes("characters").get("name", characterNode.getProperty("name")).getSingle() == null) {
            database.index().forNodes("characters").add(characterNode, "name", characterNode.getProperty("name"));
        }
    }

    private static void ensureLoversInDb(Node characterNode, String[] loverNames, DoctorWhoUniverse universe) {
        for(String lover : loverNames) {
            characterNode.createRelationshipTo(ensureCharacterIsInDb(lover, universe), DoctorWhoUniverse.LOVES);
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

    public CharacterBuilder regenerationSequence(String...actors) {
        this.actors = actors;
        return this;
    }
}
