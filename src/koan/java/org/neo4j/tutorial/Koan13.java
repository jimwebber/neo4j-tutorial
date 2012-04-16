package org.neo4j.tutorial;

import static junit.framework.Assert.assertEquals;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.server.NeoServerWithEmbeddedWebServer;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.tutorial.server.ServerBuilder;

/**
 * In this Koan we enhance the default REST API with managed extensions
 * to augment each node in the graph with new capabilities advertised
 * through hypermedia links.
 */
public class Koan13
{

    private static ServerDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        DoctorWhoUniverseGenerator doctorWhoUniverseGenerator = new DoctorWhoUniverseGenerator();

        NeoServerWithEmbeddedWebServer server = ServerBuilder
            .server()
            .usingDatabaseDir( doctorWhoUniverseGenerator.getDatabaseDirectory() )
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
        // org.neo4j.tutorial.koan13.AwesomenessServerPlugin
        // where you have to build the server plugin implementation to make this Koan pass.

        // Remember to configure src/koan/resources as test source in your IDE or the org.neo4j.server.plugins.ServerPlugin
        // file will not be found and this unit test will fail (the Ant build will still be ok)
        // See: http://www.markhneedham.com/blog/2011/06/09/intellij-adding-resources-with-unusual-extensions-onto-the-classpath/

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create( config );

        WebResource resource = client.resource(
            "http://localhost:7474/db/data/index/node/characters/character/Rose%20Tyler" );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ).get( ClientResponse.class );

        List<Map<String, Object>> json = JsonHelper.jsonToList( response.getEntity( String.class ) );
        URI roseAwesomeness = extractAwesomenessUri( json );

        response = client.resource( roseAwesomeness ).accept( MediaType.APPLICATION_JSON ).post( ClientResponse.class );

        assertEquals( 200, response.getStatus() );
        assertEquals( 50.0, Double.valueOf( response.getEntity( String.class ) ) );
    }

    private URI extractAwesomenessUri( List<Map<String, Object>> json ) throws Exception
    {
        Map<String, Map<String, String>> extensions = (Map<String, Map<String, String>>) json.get( 0 ).get(
            "extensions" );
        String s = extensions.get( "AwesomenessServerPlugin" ).get( "awesomeness" );
        return new URI( s );
    }
}
