
package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.config.Setting;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilder;
import org.neo4j.harness.internal.*;
import org.neo4j.kernel.GraphDatabaseDependencies;
import org.neo4j.kernel.InternalAbstractGraphDatabase.Dependencies;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.logging.ClassicLoggingService;
import org.neo4j.kernel.logging.Logging;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.configuration.ServerSettings;

import static org.neo4j.graphdb.factory.GraphDatabaseSettings.store_dir;
import static org.neo4j.helpers.collection.MapUtil.stringMap;
import static org.neo4j.server.configuration.Configurator.DATABASE_LOCATION_PROPERTY_KEY;
import static org.neo4j.server.configuration.Configurator.WEBSERVER_PORT_PROPERTY_KEY;

/**
 * a clone of {@link InProcessServerBuilder}
 * due to too restrict private declarations. This class does *not* force a new store directory, instead exisiting stores
 * can be reused
 */
public class InProcessServerBuilderPatched implements TestServerBuilder
{
    private File serverFolder;
    private Logging logging;
    private final Extensions extensions = new Extensions();
    private final Fixtures fixtures = new Fixtures();

    /**
     * Config options for both database and server.
     */
    private final Map<String, String> config = new HashMap<>();

    public InProcessServerBuilderPatched( File workingDir )
    {
        setDirectory( workingDir );
        withConfig( ServerSettings.auth_enabled, "false" );
        withConfig( WEBSERVER_PORT_PROPERTY_KEY, Integer.toString( freePort() ) );
    }

    @Override
    public ServerControls newServer()
    {
        Dependencies dependencies = GraphDatabaseDependencies.newDependencies().logging( logging );
        InProcessServerControls controls = new InProcessServerControls( serverFolder,
                new CommunityNeoServer( new MapConfigurator( config, extensions.toList() ), dependencies ), logging );
        controls.start();
        try
        {
            fixtures.applyTo( controls.httpURI() );
        }
        catch(RuntimeException e)
        {
            controls.close();
            throw e;
        }
        return controls;
    }

    @Override
    public TestServerBuilder withConfig( Setting<?> key, String value )
    {
        return withConfig( key.name(), value );
    }

    @Override
    public TestServerBuilder withConfig( String key, String value )
    {
        config.put( key, value );
        return this;
    }

    @Override
    public TestServerBuilder withExtension( String mountPath, Class<?> extension )
    {
        return withExtension( mountPath, extension.getPackage().getName() );
    }

    @Override
    public TestServerBuilder withExtension( String mountPath, String packageName )
    {
        extensions.add(mountPath, packageName);
        return this;
    }

    @Override
    public TestServerBuilder withFixture( File cypherFileOrDirectory )
    {
        fixtures.add( cypherFileOrDirectory );
        return this;
    }

    @Override
    public TestServerBuilder withFixture( String fixtureStatement )
    {
        fixtures.add( fixtureStatement );
        return this;
    }

    private TestServerBuilder setDirectory( File dir )
    {
        this.serverFolder = dir;
        config.put( DATABASE_LOCATION_PROPERTY_KEY, serverFolder.getAbsolutePath() );
        logging = new ClassicLoggingService( new Config( stringMap( store_dir.name(), serverFolder.getAbsolutePath() ) ) );
        return this;
    }

    private int freePort()
    {
        try
        {
            return Ports.findFreePort( Ports.INADDR_LOCALHOST, new int[]{7474, 10000} ).getPort();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Unable to find an available port: " + e.getMessage(), e );
        }
    }
}
