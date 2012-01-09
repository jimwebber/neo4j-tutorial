package org.neo4j.tutorial;

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

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

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
                .usingDatabaseDir(doctorWhoUniverseGenerator.getDatabaseDirectory())
                .build();

        universe = new ServerDoctorWhoUniverse(server, doctorWhoUniverseGenerator);
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }


    @Test
    public void shouldFindAwesomenessRatingsForTheDoctorRoseGallifreyAndEarth() throws Exception
    {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource resource = client.resource("http://localhost:7474/db/data/index/node/characters/character/Doctor");
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        List<Map<String, Object>> json = JsonHelper.jsonToList(response.getEntity(String.class));
        URI doctorsAwesomeness = extractAwesomenessUri(json);



        assertEquals(200, response.getStatus());
    }

    private URI extractAwesomenessUri(List<Map<String, Object>> json)
    {
        return null;
    }
}
