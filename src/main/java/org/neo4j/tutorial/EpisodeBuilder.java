package org.neo4j.tutorial;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.cypher.ExecutionEngine;

import static java.lang.String.format;
import static java.util.Collections.addAll;

import static org.neo4j.tutorial.ShortIdGenerator.shortId;

public class EpisodeBuilder
{
    private String title;
    private List<String> companionNames = new ArrayList<>();
    private String episodeNumber = null;
    private List<String> doctorActors = new ArrayList<>();
    private List<String> enemySpecies = new ArrayList<>();
    private List<String> enemies = new ArrayList<>();
    private String[] allies;
    private List<String> alliedSpecies = new ArrayList<>();

    private static Episode previousEpisode = null;
    private List<String> others = new ArrayList<>();

    public EpisodeBuilder( String episodeNumber )
    {
        this.episodeNumber = episodeNumber;
    }

    public static void reset()
    {
        previousEpisode = null;
    }

    public static EpisodeBuilder episode( int episodeNumber )
    {
        return EpisodeBuilder.episode( String.valueOf( episodeNumber ) );
    }

    public static EpisodeBuilder episode( String episodeNumber )
    {
        return new EpisodeBuilder( episodeNumber );
    }

    public EpisodeBuilder title( String title )
    {
        this.title = title;
        return this;
    }

    public EpisodeBuilder doctor( String... actorNames )
    {
        for ( String actor : actorNames )
        {
            if ( actor.contains( "," ) )
            {
                throw new RuntimeException( "Doctor actor appears to have inappropriate punctuation in name. Perhaps " +
                        "you've written the builder call incorrectly." );
            }
        }
        addAll( doctorActors, actorNames );
        return this;
    }

    public EpisodeBuilder companion( String... namesOfCompanions )
    {
        addAll( companionNames, namesOfCompanions );
        return this;
    }

    public EpisodeBuilder enemySpecies( String... enemySpecies )
    {
        addAll( this.enemySpecies, enemySpecies );
        return this;
    }

    public EpisodeBuilder enemy( String... enemies )
    {
        addAll( this.enemies, enemies );
        return this;
    }

    public void fact( ExecutionEngine engine )
    {
        checkEpisodeNumberAndTitle();

        StringBuilder sb = new StringBuilder();

        String episodeId = shortId( "ep" );
        final String doctorId = shortId( "doctor" );

        sb.append( format( "MERGE (%s:Character {character: 'Doctor'})", doctorId ) );
        sb.append( System.lineSeparator() );

        sb.append( format( "MERGE (%s:Episode {episode: \"%s\", title:\"%s\"}) ", episodeId, episodeNumber, title ) );

        for ( String actor : doctorActors )
        {
            String actorId = shortId( "actor" );

            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (%s:Actor {actor: \"%s\"})", actorId, actor ) );
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (%s)-[:APPEARED_IN]->(%s)", actorId, episodeId ) );
        }

        if ( this.companionNames != null )
        {
            for ( String companionName : companionNames )
            {
                String companionId = shortId( "companion" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Character {character: \"%s\"})", companionId, companionName ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:APPEARED_IN]->(%s)", companionId, episodeId ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)<-[:COMPANION_OF]-(%s)", doctorId, companionId ) );
            }
        }

        if ( this.enemies != null )
        {
            for ( String enemy : enemies )
            {
                String enemyId = shortId( "enemy" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Character {character: \"%s\"})", enemyId, enemy ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:APPEARED_IN]->(%s)", enemyId, episodeId ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:ENEMY_OF]->(%s)", enemyId, doctorId ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)<-[:ENEMY_OF]-(%s)", enemyId, doctorId ) );
            }
        }

        if ( this.enemySpecies != null )
        {
            for ( String eSpecies : enemySpecies )
            {
                String enemySpeciesId = shortId( "enemySpecies" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Species {species: \"%s\"})", enemySpeciesId, eSpecies ) );
                sb.append( System.lineSeparator() );
                final String format = format( "MERGE (%s)-[:APPEARED_IN]->(%s)", enemySpeciesId, episodeId );
                sb.append( format );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:ENEMY_OF]->(%s)", enemySpeciesId, doctorId ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)<-[:ENEMY_OF]-(%s)", enemySpeciesId, doctorId ) );
            }
        }

        if ( this.allies != null )
        {
            for ( String ally : allies )
            {
                String allyId = shortId( "ally" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Character {character: \"%s\"})", allyId, ally ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:APPEARED_IN]->(%s)", allyId, episodeId ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:ALLY_OF]->(%s)", allyId, doctorId ) );
            }
        }

        if ( this.alliedSpecies != null )
        {
            for ( String aSpecies : alliedSpecies )
            {
                String alliedSpeciesId = shortId( "alliedSpecies" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Species {species: \"%s\"})", alliedSpeciesId, aSpecies ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:APPEARED_IN]->(%s)", alliedSpeciesId, episodeId ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:ALLY_OF]->(%s)", alliedSpeciesId, doctorId ) );
            }
        }

        if ( this.others != null )
        {
            for ( String other : others )
            {
                String otherId = shortId( "other" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Character {character: \"%s\"})", otherId, other ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s)-[:APPEARED_IN]->(%s)", otherId, episodeId ) );
            }
        }

        if ( previousEpisode != null )
        {
            sb.append( System.lineSeparator() );
            sb.append( format( "WITH %s", episodeId ) );
            sb.append( System.lineSeparator() );
            sb.append( format( "MATCH (prevEp:Episode {title: \"%s\", episode: \"%s\"})",
                    previousEpisode.title, previousEpisode.episodeNumber ) );
            sb.append( System.lineSeparator() );
            sb.append( String.format( "MERGE (%s)-[:PREVIOUS]->(prevEp)-[:NEXT]->(%s)", episodeId, episodeId ) );
        }

        previousEpisode = new Episode( episodeNumber, title );

        engine.execute( sb.toString() );
    }

    private void checkEpisodeNumberAndTitle()
    {
        if ( title == null )
        {
            throw new RuntimeException( "Episodes must have a title" );
        }

        if ( episodeNumber == null )
        {
            throw new RuntimeException( "Episodes must have a number" );
        }
    }

    public EpisodeBuilder allies( String... allies )
    {
        this.allies = allies;
        return this;
    }

    public EpisodeBuilder alliedSpecies( String... alliedSpecies )
    {
        addAll( this.alliedSpecies, alliedSpecies );
        return this;
    }

    public EpisodeBuilder others( String... others )
    {
        addAll( this.others, others );
        return this;
    }

    private static class Episode
    {
        public String episodeNumber;
        public String title;

        public Episode( String episodeNumber, String title )
        {
            this.episodeNumber = episodeNumber;
            this.title = title;
        }
    }
}
