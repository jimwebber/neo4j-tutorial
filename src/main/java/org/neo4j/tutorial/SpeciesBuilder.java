package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class SpeciesBuilder {
    
    private final String speciesName;
    private String planet;
    private String[] enemies;
    private String[] enemySpecies;

    public static SpeciesBuilder species(String speciesName) {
        return new SpeciesBuilder(speciesName);
    }
    
    private SpeciesBuilder(String speciesName) {
        this.speciesName = speciesName;

    }
    
    public void fact(DoctorWhoUniverse universe) {
        Node speciesNode = ensureSpeciesInDb(speciesName, universe);
        
        if(planet != null) {
            Node planetNode = PlanetBuilder.ensurePlanetInDb(planet, universe);
            speciesNode.createRelationshipTo(planetNode, DoctorWhoUniverse.COMES_FROM);
        }
        
        if(enemies != null) {
            for(String enemy : enemies) {
                Node enemyNode = CharacterBuilder.ensureCharacterIsInDb(enemy, universe);
                enemyNode.createRelationshipTo(speciesNode, DoctorWhoUniverse.ENEMY_OF);
                speciesNode.createRelationshipTo(enemyNode, DoctorWhoUniverse.ENEMY_OF);
            }
        }
        
        if(enemySpecies != null) {
            for(String eSpecies : enemySpecies) {
               Node enemySpeciesNode = ensureSpeciesInDb(eSpecies, universe);
               enemySpeciesNode.createRelationshipTo(speciesNode, DoctorWhoUniverse.ENEMY_OF);
               speciesNode.createRelationshipTo(enemySpeciesNode, DoctorWhoUniverse.ENEMY_OF);
            }
        }
    }
    
    public static Node ensureSpeciesInDb(String theSpecies, DoctorWhoUniverse universe) {
        ensureArgumentsAreSane(theSpecies, universe);
        
        GraphDatabaseService db = universe.getDatabase();
        
        Node speciesNode = universe.speciesIndex.get("species", theSpecies).getSingle();

        if (speciesNode == null) {
            speciesNode = db.createNode();
            speciesNode.setProperty("species", theSpecies);
            universe.speciesIndex.add(speciesNode, "species", theSpecies);
        }

        return speciesNode;
    }

    private static void ensureArgumentsAreSane(String theSpecies, DoctorWhoUniverse universe) {
        if(theSpecies == null) {
            throw new RuntimeException("Must provide a value for the species to the species builder");
        }
        
        if(universe == null) {
            throw new RuntimeException("Must provide a value for the universe to the species builder");
        }
    }

    public SpeciesBuilder isFrom(String planet) {
        this.planet = planet;
        return this;
    }

    public SpeciesBuilder isEnemyOf(String... enemies) {
        this.enemies = enemies;
        return this;
    }

    public SpeciesBuilder isEnemyOfSpecies(String... enemySpecies) {
        this.enemySpecies = enemySpecies;
        return this;
    }
}
