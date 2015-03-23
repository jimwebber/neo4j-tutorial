package org.neo4j.tutorial.advanced;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.tutorial.DoctorWhoUniverseServerResource;
import org.neo4j.tutorial.security_rule.UserNameAndPasswordForSalariesSecurityRule;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import static junit.framework.Assert.assertEquals;

/**
 * In this Koan we mix an unmanaged (JAX-RS) extension with security rules to provide
 * to provide authenticated domain-specific set of Doctor Who resources inside
 * the Neo4j server.
 */
public class SecureTheServerFormerlyKoan13
{

    @ClassRule
    public static DoctorWhoUniverseServerResource neo4j = new DoctorWhoUniverseServerResource()
            .withThirdPartyJaxRsPackage("org.neo4j.tutorial.security_rule", "/security_rule")
            .withConfig("org.neo4j.server.rest.security_rules", UserNameAndPasswordForSalariesSecurityRule.class.getName());

    @Test
    public void regularRESTAPIShouldNotBeSecured()
    {
        // This unit test provides the client side HTTP actions and assertions.
        // Your work happens in
        // org.neo4j.tutorial.security_rule.UserNameAndPasswordForSalariesSecurityRule
        // where you have to build the server-side infrastructure to make this Koan pass.

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create( config );

        ClientResponse response = client.resource( "http://localhost:7474/db/data" ).get( ClientResponse.class );

        assertEquals( 200, response.getStatus() );

    }

    @Test
    public void shouldNotAccessDavidTennantSalaryWithoutProperCredentials()
    {

        // This unit test provides the client side HTTP actions and assertions.
        // Your work happens in
        // org.neo4j.tutorial.security_rule.UserNameAndPasswordForSalariesSecurityRule
        // where you have to build the server-side infrastructure to make this Koan pass.

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create( config );

        ClientResponse response = client.resource( "http://localhost:7474/security_rule/David%20Tennant/salary" )
                .accept(
                        TEXT_PLAIN ).get( ClientResponse.class );

        assertEquals( 401, response.getStatus() );

    }

    @Test
    public void shouldAccessDavidTennantSalaryWithProperCredentials()
    {

        // This unit test provides the client side HTTP actions and assertions.
        // Your work happens in
        // org.neo4j.tutorial.security_rule.UserNameAndPasswordForSalariesSecurityRule
        // where you have to build the server-side infrastructure to make this Koan pass.

        // Note: this isn't a secure password scheme - this Koan is only to aid
        // understanding of how to register security rules with the server.

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create( config );

        ClientResponse response = client.resource( "http://localhost:7474/security_rule/David%20Tennant/salary" )
                .header(
                        "X-Username", "Alice" ).header( "X-Password", "1337" ).accept( TEXT_PLAIN )
                .get( ClientResponse.class );

        assertEquals( 200, response.getStatus() );
        assertEquals( "text/plain", response.getHeaders().get( "Content-Type" ).get( 0 ) );
        assertEquals( "1000000", response.getEntity( String.class ) );
    }
}
