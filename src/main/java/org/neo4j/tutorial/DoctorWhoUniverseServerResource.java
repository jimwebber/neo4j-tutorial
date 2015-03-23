package org.neo4j.tutorial;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.rules.ExternalResource;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilder;
import org.neo4j.harness.internal.InProcessServerControls;
import org.neo4j.server.AbstractNeoServer;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.server.rest.domain.JsonParseException;
import org.neo4j.tutorial.server.rest.FunctionalTestHelper;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class DoctorWhoUniverseServerResource extends ExternalResource
{

    private GraphDatabaseService graphDatabaseService;
    private String baseUrl;
    private URI baseUri;
    private ServerControls controls;
    private Map<String, String> thirdPartyJaxRsExtensions = new HashMap<>();
    private Map<String, String> config = new HashMap<>();

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public URI getBaseUri()
    {
        return baseUri;
    }

    public ServerControls getControls()
    {
        return controls;
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
        WebResource resource = client.resource( new FunctionalTestHelper( baseUri )
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

    public GraphDatabaseService getGraphDatabaseService()
    {
        return graphDatabaseService;
    }

    @Override
    protected void before() throws Throwable
    {

        String doctorWhoUniverseDir = new DoctorWhoUniverseGenerator().generate();
        TestServerBuilder builder = new InProcessServerBuilderPatched( new File( doctorWhoUniverseDir ) );

        for ( Map.Entry<String, String> entry : thirdPartyJaxRsExtensions.entrySet() )
        {
            builder.withExtension( entry.getValue(), entry.getKey() );
        }

        for ( Map.Entry<String, String> entry : config.entrySet() )
        {
            builder.withConfig( entry.getKey(), entry.getValue() );
        }

        controls = builder.newServer();
        baseUri = controls.httpURI();
        baseUrl = baseUri.toASCIIString();

        // rant: the server property is private and does not have an getter, so we use reflection to get it
        Field serverField = InProcessServerControls.class.getDeclaredField( "server" );
        serverField.setAccessible( true );
        AbstractNeoServer server = (AbstractNeoServer) serverField.get( controls );
        graphDatabaseService = server.getDatabase().getGraph();
    }

    @Override
    protected void after()
    {
        controls.close();
    }

    public DoctorWhoUniverseServerResource withThirdPartyJaxRsPackage( String packageName, String mountpoint )
    {
        thirdPartyJaxRsExtensions.put( packageName, mountpoint );
        return this;
    }

    public DoctorWhoUniverseServerResource withConfig( String key, String value )
    {
        config.put( key, value );
        return this;
    }
}
