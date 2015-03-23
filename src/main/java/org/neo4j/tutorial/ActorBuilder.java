package org.neo4j.tutorial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;

import static java.lang.String.format;

import static org.neo4j.tutorial.ShortIdGenerator.shortId;

public class ActorBuilder
{
    private String actorName;
    private List<String> characterNames = new ArrayList<>();
    private int cash = -1;
    private String wikipediaUri;

    public static ActorBuilder actor( String actorName )
    {
        return new ActorBuilder( actorName );
    }

    public ActorBuilder( String actorName )
    {
        this.actorName = actorName;
    }

    public ActorBuilder played( String... characterNames )
    {
        Collections.addAll( this.characterNames, characterNames );
        return this;
    }

    public ActorBuilder wikipedia( String wikipediaUri )
    {
        this.wikipediaUri = wikipediaUri;
        return this;
    }

    public ActorBuilder salary( int cash )
    {
        this.cash = cash;
        return this;
    }

    public void fact( GraphDatabaseService graphDatabaseService)
    {
        StringBuilder sb = new StringBuilder();
        for ( String characterName : characterNames )
        {
            String characterId = shortId( "character" );
            String actorId = shortId( "actor" );

            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (%s:Character {character: \"%s\"})", characterId, characterName ) );
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (%s:Actor {actor: \"%s\"})", actorId, actorName) );
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (%s)-[:PLAYED]->(%s)", actorId, characterId) );
        }

        if ( wikipediaUri != null )
        {
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (a1:Actor {actor: \"%s\"})", actorName ) );
            sb.append( System.lineSeparator() );
            sb.append( format( "SET a1.wikipedia = \"%s\"", wikipediaUri ) );
        }

        if ( cash > -1 )
        {
            sb.append( System.lineSeparator() );
            sb.append( format( "MERGE (a2:Actor {actor: \"%s\"})", actorName ) );
            sb.append( System.lineSeparator() );
            sb.append( format( "SET a2.salary= %d", cash ) );
        }

        graphDatabaseService.execute( sb.toString() );
    }
}
