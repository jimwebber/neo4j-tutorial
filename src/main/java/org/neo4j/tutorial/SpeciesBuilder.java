package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import static org.neo4j.tutorial.DatabaseHelper.ensureRelationshipInDb;

public class SpeciesBuilder
{

    private final String speciesName;
    private String planet;
    private String[] enemies;
    private String[] enemySpecies;

    public static SpeciesBuilder species(String speciesName)
    {
        return new SpeciesBuilder(speciesName);
    }

    private SpeciesBuilder(String speciesName)
    {
        this.speciesName = speciesName;
    }

    public void fact(GraphDatabaseService db)
    {
        Node speciesNode = ensureSpeciesInDb(speciesName, db);

        if (planet != null)
        {
            Node planetNode = PlanetBuilder.ensurePlanetInDb(planet, db);
            ensureRelationshipInDb(speciesNode, DoctorWhoRelationships.COMES_FROM, planetNode);
        }

        if (enemies != null)
        {
            for (String enemy : enemies)
            {
                Node enemyNode = CharacterBuilder.ensureCharacterIsInDb(enemy, db);
                ensureRelationshipInDb(enemyNode, DoctorWhoRelationships.ENEMY_OF, speciesNode);
                ensureRelationshipInDb(speciesNode, DoctorWhoRelationships.ENEMY_OF, enemyNode);
            }
        }

        if (enemySpecies != null)
        {
            for (String eSpecies : enemySpecies)
            {
                Node enemySpeciesNode = ensureSpeciesInDb(eSpecies, db);
                ensureRelationshipInDb(enemySpeciesNode, DoctorWhoRelationships.ENEMY_OF, speciesNode);
                ensureRelationshipInDb(speciesNode, DoctorWhoRelationships.ENEMY_OF, enemySpeciesNode);
            }
        }
    }

    public static Node ensureSpeciesInDb(String theSpecies, GraphDatabaseService db)
    {
        ensureArgumentsAreSane(theSpecies, db);

        Node speciesNode = db.index()
                             .forNodes("species")
                             .get("species", theSpecies)
                             .getSingle();

        if (speciesNode == null)
        {
            speciesNode = db.createNode();
            speciesNode.setProperty("species", theSpecies);
            db.index()
              .forNodes("species")
              .add(speciesNode, "species", theSpecies);
        }

        return speciesNode;
    }

    private static void ensureArgumentsAreSane(String theSpecies, GraphDatabaseService db)
    {
        if (theSpecies == null)
        {
            throw new RuntimeException("Must provide a value for the species to the species builder");
        }

        if (db == null)
        {
            throw new RuntimeException("Must provide a value for the universe to the species builder");
        }
    }

    public SpeciesBuilder isFrom(String planet)
    {
        this.planet = planet;
        return this;
    }

    public SpeciesBuilder isEnemyOf(String... enemies)
    {
        this.enemies = enemies;
        return this;
    }

    public SpeciesBuilder isEnemyOfSpecies(String... enemySpecies)
    {
        this.enemySpecies = enemySpecies;
        return this;
    }
}
