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
import org.neo4j.tutorial.server.ServerBuilder;

import javax.ws.rs.core.MediaType;

import static junit.framework.Assert.assertEquals;

/**
 * In this Koan we enhance the default REST API with unmanaged (JAX-RS) extensions
 * to provide a domain-specific set of Doctor Who resources inside the Neo4j server.
 */
public class Koan12
{

    private static ServerDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        DoctorWhoUniverseGenerator doctorWhoUniverseGenerator = new DoctorWhoUniverseGenerator();

        NeoServerWithEmbeddedWebServer server = ServerBuilder
                .server()
                .usingDatabaseDir(doctorWhoUniverseGenerator.getDatabaseDirectory())
                .withThirdPartyJaxRsPackage("org.neo4j.tutorial.koan12", "/koan12")
                .build();

        universe = new ServerDoctorWhoUniverse(server );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldReturnTheDoctorsHomePlanetName() throws Exception
    {
        // This unit test provides the client side HTTP actions and assertions.
        // Your work happens in org.neo4j.tutorial.koan12.HomePlanetUnmanagedExtension
        // where you have to build the server-side infrastructure to make this
        // Koan pass.

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource resource = client.resource("http://localhost:7474/koan12/Doctor/homeplanet");
        ClientResponse response = resource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);

        assertEquals(200, response.getStatus());
        assertEquals("text/plain", response.getHeaders().get("Content-Type").get(0));
        assertEquals("Gallifrey", response.getEntity(String.class));
    }
}
