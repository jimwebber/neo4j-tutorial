package org.neo4j.tutorial;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.tutorial.server.ServerBuilder;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import static org.junit.Assert.assertEquals;

/**
 * In this Koan we enhance the default REST API with managed extensions
 * to augment each node in the graph with new capabilities advertised
 * through hypermedia links.
 */

// TODO: consider deleting this because managed extensions are awful.

public class ManagedExtensionsFormerlyKoan12
{

    private static ServerDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        DoctorWhoUniverseGenerator doctorWhoUniverseGenerator = new DoctorWhoUniverseGenerator();

        CommunityNeoServer server = ServerBuilder
                .server()
                .usingDatabaseDir( doctorWhoUniverseGenerator.getCleanlyShutdownDatabaseDirectory() )
                .build();

        universe = new ServerDoctorWhoUniverse( server );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }


    @Test
    public void shouldFindAwesomenessRatingsRoseTyler() throws Exception
    {
        // This unit test provides the client side HTTP actions and assertions.
        // Your work happens in
        // org.neo4j.tutorial.managed_extension.AwesomenessServerPlugin
        // where you have to build the server plugin implementation to make this Koan pass.

        // Remember to configure src/koan/resources as test source in your IDE or the org.neo4j.server.plugins
        // .ServerPlugin
        // file will not be found and this unit test will fail (the Ant build will still be ok)
        // See: http://www.markhneedham.com/blog/2011/06/09/intellij-adding-resources-with-unusual-extensions-onto
        // -the-classpath/
        // You'll need to add *ServerPlugin* onto your classpath accordingly.

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create( config );

        WebResource resource = client.resource(
                "http://localhost:7474/db/data/label/Character/nodes?character=%22Rose+Tyler%22" );
        ClientResponse response = resource.accept( APPLICATION_JSON ).get( ClientResponse.class );

        List<Map<String, Object>> json = JsonHelper.jsonToList( response.getEntity( String.class ) );
        URI roseAwesomeness = extractAwesomenessUri( json );

        response = client.resource( roseAwesomeness ).accept( APPLICATION_JSON ).post( ClientResponse.class );

        assertEquals( 200, response.getStatus() );
        assertEquals( 50.0, Double.valueOf( response.getEntity( String.class ) ), 0.0 );
    }

    @SuppressWarnings("unchecked")
    private URI extractAwesomenessUri( List<Map<String, Object>> json ) throws Exception
    {
        Map<String, Map<String, String>> extensions = (Map<String, Map<String, String>>) json.get( 0 )
                .get( "extensions" );
        String s = extensions.get( "AwesomenessServerPlugin" ).get( "awesomeness" );
        return new URI( s );
    }
}
