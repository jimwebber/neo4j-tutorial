package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;

import static java.lang.String.format;

import static org.neo4j.tutorial.ShortIdGenerator.shortId;

public class SpeciesBuilder
{

    private final String speciesName;
    private String planet;
    private String[] enemies;
    private String[] enemySpecies;

    public static SpeciesBuilder species( String speciesName )
    {
        return new SpeciesBuilder( speciesName );
    }

    private SpeciesBuilder( String speciesName )
    {
        this.speciesName = speciesName;
    }

    public void fact( GraphDatabaseService db)
    {
        StringBuilder sb = new StringBuilder();

        sb.append( System.lineSeparator() );
        sb.append( format( "MERGE (species:Species {species: \"%s\"})", speciesName ) );

        if ( planet != null )
        {
            String planetId = shortId( "planet" );

            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (%s:Planet {planet: \"%s\"})", planetId, planet ) );
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (species)-[:COMES_FROM]->(%s)", planetId ) );
        }

        if ( enemies != null )
        {
            for ( String enemy : enemies )
            {
                String enemyId = shortId( "enemy" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Character {character: \"%s\"})", enemyId, enemy ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (species)-[:ENEMY_OF]->(%s)", enemyId ) );
            }
        }

        if ( enemySpecies != null )
        {
            for ( String eSpecies : enemySpecies )
            {
                String enemySpeciesId = shortId( "enemySpecies" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Species {species: \"%s\"})", enemySpeciesId, eSpecies ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (species)-[:ENEMY_OF]->(%s)", enemySpeciesId ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (species)<-[:ENEMY_OF]-(%s)", enemySpeciesId ) );
            }
        }

        db.execute( sb.toString() );
    }

    public SpeciesBuilder isFrom( String planet )
    {
        this.planet = planet;
        return this;
    }

    public SpeciesBuilder isEnemyOf( String... enemies )
    {
        this.enemies = enemies;
        return this;
    }

    public SpeciesBuilder isEnemyOfSpecies( String... enemySpecies )
    {
        this.enemySpecies = enemySpecies;
        return this;
    }
}
