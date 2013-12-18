package org.neo4j.tutorial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.neo4j.cypher.ExecutionEngine;

import static java.lang.String.format;
import static java.lang.String.valueOf;

import static org.neo4j.tutorial.ShortIdGenerator.shortId;

public class CharacterBuilder
{
    private final String characterName;
    private HashSet<String> species = new HashSet<>();
    private boolean companion = false;
    private String[] loverNames;
    private String planet;
    private String[] things;
    private boolean enemy;
    private boolean ally;
    private ArrayList<String> regenerations = new ArrayList<>();
    private HashMap<String, Integer> startDates = new HashMap<>();
    private String wikipediaUri;
    private String[] children;
    private Episode firstAppearance;
    private Episode diedIn;
    private String singleActorName;

    public static CharacterBuilder character( String characterName )
    {
        return new CharacterBuilder( characterName );
    }

    public CharacterBuilder( String characterName )
    {
        this.characterName = characterName;
    }

    public CharacterBuilder isA( String speciesString )
    {
        species.add( speciesString );
        return this;
    }

    public CharacterBuilder isCompanion()
    {
        companion = true;
        return this;
    }

    public void fact( ExecutionEngine engine )
    {
        if ( regenerations.size() > 0 && singleActorName != null )
        {
            throw new IllegalStateException(
                    "Cannot have both regenerations and a single played by actor specified for the same character." );
        }

        StringBuilder sb = new StringBuilder();

        if ( wikipediaUri != null )
        {
            sb.append( format( "MERGE (c:Character {character: \"%s\"}) ON MATCH SET c.wikipedia = \"%s\" ON CREATE " +
                    "SET c" +
                    ".wikipedia = \"%s\"",
                    characterName, wikipediaUri, wikipediaUri ) );
        }
        else
        {
            sb.append( format( "MERGE (c:Character {character: \"%s\"}) ", characterName ) );
        }

        // A list of actors playing a role (e.g. for timelords) XOR a single actor
        if ( regenerations.size() > 0 )
        {
            String previousActorId = null;
            for ( String actor : regenerations )
            {
                String actorId = shortId( "actor" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Actor {actor: \"%s\"})", actorId, actor ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (c)<-[:PLAYED]-(%s)", actorId ) );

                if ( previousActorId != null )
                {
                    sb.append( System.lineSeparator() );
                    if ( startDates.get( actor ) == null )
                    {
                        sb.append( format( "MERGE (%s)-[:REGENERATED_TO]->(%s)", previousActorId, actorId ) );
                    }
                    else
                    {
                        sb.append( format( "MERGE (%s)-[:REGENERATED_TO {year: %d}]->(%s)", previousActorId,
                                startDates.get( actor ), actorId ) );
                    }
                }

                previousActorId = actorId;
            }
        }
        else if ( singleActorName != null )
        {
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (actor:Actor {actor: \"%s\"})", singleActorName ) );
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (c)<-[:PLAYED]-(actor)" );
        }


        if ( species.size() > 0 )
        {
            for ( String speciesString : species )
            {
                String id = shortId( "species" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Species {species: \"%s\"})", id, speciesString ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (c)-[:IS_A]->(%s)", id ) );
            }
        }

        if ( enemy )
        {
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (doctor:Character {character: 'Doctor'})" );
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (c)-[:ENEMY_OF]->(doctor)" );
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (c)<-[:ENEMY_OF]-(doctor)" );
        }

        if ( ally )
        {
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (doctor:Character {character: 'Doctor'})" );
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (c)-[:ALLY_OF]->(doctor)" );
        }

        if ( loverNames != null )
        {
            for ( String loverName : loverNames )
            {
                String id = shortId( "lover" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Character {character: \"%s\"})", id, loverName ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (c)-[:LOVES]->(%s)", id ) );
            }
        }

        if ( planet != null )
        {
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (planet:Planet {planet: \"%s\"})", planet ) );
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (c)-[:COMES_FROM]->(planet)" );
        }

        if ( things != null )
        {
            for ( String thing : things )
            {
                String id = shortId( "thing" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Thing {thing: \"%s\"})", id, thing ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (c)-[:OWNS]->(%s)", id ) );
            }
        }

        if ( children != null )
        {
            for ( String child : children )
            {
                String id = shortId( "child" );

                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (%s:Character {character: \"%s\"})", id, child ) );
                sb.append( System.lineSeparator() );
                sb.append( format( "MERGE (c)<-[:CHILD_OF]-(%s)", id ) );
            }
        }

        if ( firstAppearance != null )
        {
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (fa:Episode {title: \"%s\", episode: \"%s\"})", firstAppearance.episodeTitle,
                    firstAppearance.episodeNumber ) );
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (fa)<-[:FIRST_APPEARED]-(c)" );
        }

        if ( diedIn != null )
        {
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (di:Episode {title: \"%s\", episode: \"%s\"})", diedIn.episodeTitle,
                    diedIn.episodeNumber ) );
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (di)<-[:DIED_IN]-(c)" );
        }

        if ( companion )
        {
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (doctor:Character {character: 'Doctor'})" );
            sb.append( System.lineSeparator() );
            sb.append( "MERGE (c)-[:COMPANION_OF]->(doctor)" );
        }

        sb.append( System.lineSeparator() );
        sb.append( System.lineSeparator() );
        sb.append( System.lineSeparator() );

        engine.execute( sb.toString() );
    }

    public CharacterBuilder loves( String... loverNames )
    {
        this.loverNames = loverNames;
        return this;
    }

    public CharacterBuilder isFrom( String planet )
    {
        this.planet = planet;
        return this;
    }

    public CharacterBuilder owns( String... things )
    {
        this.things = things;
        return this;
    }

    public CharacterBuilder isEnemy()
    {
        this.enemy = true;
        return this;
    }

    public CharacterBuilder isAlly()
    {
        ally = true;
        return this;
    }

    public CharacterBuilder regeneration( String... actors )
    {
        Collections.addAll( regenerations, actors );
        return this;
    }

    public CharacterBuilder regeneration( String actor, int year )
    {
        regeneration( actor );
        this.startDates.put( actor, year );
        return this;
    }

    public CharacterBuilder wikipedia( String wikipediaEntry )
    {
        this.wikipediaUri = wikipediaEntry;
        return this;
    }

    public CharacterBuilder fatherOf( String... children )
    {
        this.children = children;
        return this;
    }

    public CharacterBuilder firstAppearedIn( int episodeNumber, String episodeTitle )
    {
        return firstAppearedIn( valueOf( episodeNumber ), episodeTitle );
    }

    public CharacterBuilder firstAppearedIn( String episodeNumber, String episodeTitle )
    {
        this.firstAppearance = new Episode( episodeNumber, episodeTitle );
        return this;
    }

    public CharacterBuilder diedIn( int episodeNumber, String episodeTitle )
    {
        return diedIn( String.valueOf( episodeNumber ), episodeTitle );
    }

    public CharacterBuilder diedIn( String episodeNumber, String episodeTitle )
    {
        this.diedIn = new Episode( episodeNumber, episodeTitle );
        return this;
    }

    public CharacterBuilder playedBy( String actorName )
    {
        this.singleActorName = actorName;
        return this;
    }

    private static class Episode
    {
        public final String episodeNumber;
        public final String episodeTitle;

        public Episode( String episodeNumber, String episodeTitle )
        {
            this.episodeNumber = episodeNumber;
            this.episodeTitle = episodeTitle;
        }
    }
}