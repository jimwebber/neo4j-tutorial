package org.neo4j.tutorial.server.rest;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.CommunityNeoServer;

public final class FunctionalTestHelper
{
    private final CommunityNeoServer server;

    public FunctionalTestHelper( CommunityNeoServer server )
    {
        if ( server.getDatabase() == null )
        {
            throw new RuntimeException( "Server must be started before using " + getClass().getName() );
        }
        this.server = server;
    }

    public String dataUri()
    {
        return server.baseUri()
                .toString() + "db/data/";
    }

    public String nodeUri()
    {
        return dataUri() + "node";
    }

    public String nodeUri( long id )
    {
        return nodeUri() + "/" + id;
    }

    public String relationshipUri()
    {
        return dataUri() + "relationship";
    }

    public String relationshipUri( long id )
    {
        return relationshipUri() + "/" + id;
    }

    public GraphDatabaseService getDatabase()
    {
        return server.getDatabase().getGraph();
    }

    public String nodeUri( String label, String key, String value )
    {
        return dataUri() + "label/" + label + "/nodes?" + key + "=%22" + convertAnySpaces( value ) + "%22";
    }

    private String convertAnySpaces( String value )
    {
        return value.replace( ' ', '+' );
    }
}
