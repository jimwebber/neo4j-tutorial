package org.neo4j.tutorial;

import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.server.rest.domain.JsonParseException;
import org.neo4j.tutorial.server.rest.FunctionalTestHelper;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerDoctorWhoUniverse
{
    private final CommunityNeoServer server;

    public ServerDoctorWhoUniverse( CommunityNeoServer server ) throws Exception
    {
        this.server = server;
        server.start();
    }

    public Map<String, Object> theDoctor()
    {
        return getJsonFor( createUriForNode( "Character", "character", "Doctor" ) );
    }

    public Map<String, Object> getJsonFor( String uri )
    {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create( config );
        WebResource resource = client.resource( uri );
        String response = resource.accept( APPLICATION_JSON ).get( String.class );
        try
        {
            return JsonHelper.jsonToMap( response );
        }
        catch ( JsonParseException e )
        {
            throw new RuntimeException( "Invalid response when looking up node", e );
        }
    }

    public String createUriForNode( String label, String key, String value )
    {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create( config );
        WebResource resource = client.resource( new FunctionalTestHelper( server )
                .nodeUri( label, key, value ) );
        String response = resource.accept( APPLICATION_JSON ).get( String.class );
        try
        {
            return JsonHelper.jsonToList( response ).get( 0 ).get( "self" ).toString();
        }
        catch ( JsonParseException e )
        {
            throw new RuntimeException( "Invalid response when looking up node" );
        }
    }

    public void stop()
    {
        server.stop();
    }

    public CommunityNeoServer getServer()
    {
        return server;
    }

}
