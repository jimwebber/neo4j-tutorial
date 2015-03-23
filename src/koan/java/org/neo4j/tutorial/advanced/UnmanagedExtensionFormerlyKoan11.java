package org.neo4j.tutorial.advanced;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.tutorial.DoctorWhoUniverseServerResource;

import static junit.framework.Assert.assertEquals;

/**
 * In this Koan we enhance the default REST API with unmanaged (JAX-RS) extensions
 * to provide a domain-specific set of Doctor Who resources inside the Neo4j server.
 */
public class UnmanagedExtensionFormerlyKoan11
{

    @ClassRule
    public static DoctorWhoUniverseServerResource neo4j = new DoctorWhoUniverseServerResource()
        .withThirdPartyJaxRsPackage( "org.neo4j.tutorial.unmanaged_extension", "/tutorial" );

    @Test
    public void shouldReturnTheDoctorsHomePlanetName() throws Exception
    {
        // This unit test provides the client side HTTP actions and assertions.
        // Your work happens in org.neo4j.tutorial.unmanaged_extension.HomePlanetUnmanagedExtension
        // where you have to build the server-side infrastructure to make this
        // Koan pass.

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create( config );

        WebResource resource = client.resource( neo4j.getBaseUrl() +  "tutorial/Doctor/homeplanet" );
        ClientResponse response = resource.accept( MediaType.TEXT_PLAIN ).get( ClientResponse.class );

        assertEquals( 200, response.getStatus() );
        assertEquals( "text/plain", response.getHeaders().get( "Content-Type" ).get( 0 ) );
        assertEquals( "Gallifrey", response.getEntity( String.class ) );
    }
}
