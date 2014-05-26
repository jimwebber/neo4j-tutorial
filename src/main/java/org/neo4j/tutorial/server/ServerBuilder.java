package org.neo4j.tutorial.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.logging.ConsoleLogger;
import org.neo4j.kernel.logging.DevNullLoggingService;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.configuration.Configurator;
import org.neo4j.server.configuration.PropertyFileConfigurator;
import org.neo4j.server.configuration.validation.DatabaseLocationMustBeSpecifiedRule;
import org.neo4j.server.configuration.validation.Validator;

public class ServerBuilder
{
    private String portNo = "7474";
    private String maxThreads = null;
    protected String dbDir = null;
    private String webAdminUri = "/db/manage/";
    private String webAdminDataUri = "/db/data/";
    private final HashMap<String, String> thirdPartyPackages = new HashMap<>();
    private final Properties arbitraryProperties = new Properties();

    private String[] autoIndexedNodeKeys = null;
    private String[] autoIndexedRelationshipKeys = null;
    private String host = null;
    private String[] securityRuleClassNames;
    private Boolean httpsEnabled = false;

    public static ServerBuilder server()
    {
        return new ServerBuilder();
    }

    public CommunityNeoServer build() throws IOException
    {
        if ( dbDir == null )
        {
            this.dbDir = ServerTestUtils.createTempDir().getAbsolutePath();
        }
        File configFile = createPropertiesFiles();

        return new CommunityNeoServer(
                new PropertyFileConfigurator( new Validator( new DatabaseLocationMustBeSpecifiedRule() ), configFile,
                        ConsoleLogger.DEV_NULL ), new DevNullLoggingService()
        );
    }

    public File createPropertiesFiles() throws IOException
    {
        File temporaryConfigFile = ServerTestUtils.createTempPropertyFile();

        createPropertiesFile( temporaryConfigFile );

        return temporaryConfigFile;
    }

    private void createPropertiesFile( File temporaryConfigFile )
    {
        Map<String, String> properties = MapUtil.stringMap(
                Configurator.DATABASE_LOCATION_PROPERTY_KEY, dbDir,
                Configurator.MANAGEMENT_PATH_PROPERTY_KEY, webAdminUri,
                Configurator.REST_API_PATH_PROPERTY_KEY, webAdminDataUri );
        if ( portNo != null )
        {
            properties.put( Configurator.WEBSERVER_PORT_PROPERTY_KEY, portNo );
        }
        if ( host != null )
        {
            properties.put( Configurator.WEBSERVER_ADDRESS_PROPERTY_KEY, host );
        }
        if ( maxThreads != null )
        {
            properties.put( Configurator.WEBSERVER_MAX_THREADS_PROPERTY_KEY, maxThreads );
        }

        if ( thirdPartyPackages.keySet().size() > 0 )
        {
            properties.put( Configurator.THIRD_PARTY_PACKAGES_KEY, asOneLine( thirdPartyPackages ) );
        }

        if ( autoIndexedNodeKeys != null && autoIndexedNodeKeys.length > 0 )
        {
            properties.put( "node_auto_indexing", "true" );
            String propertyKeys = org.apache.commons.lang.StringUtils.join( autoIndexedNodeKeys, "," );
            properties.put( "node_keys_indexable", propertyKeys );
        }

        if ( autoIndexedRelationshipKeys != null && autoIndexedRelationshipKeys.length > 0 )
        {
            properties.put( "relationship_auto_indexing", "true" );
            String propertyKeys = org.apache.commons.lang.StringUtils.join( autoIndexedRelationshipKeys, "," );
            properties.put( "relationship_keys_indexable", propertyKeys );
        }

        if ( securityRuleClassNames != null && securityRuleClassNames.length > 0 )
        {
            String propertyKeys = org.apache.commons.lang.StringUtils.join( securityRuleClassNames, "," );
            properties.put( Configurator.SECURITY_RULES_KEY, propertyKeys );
        }

        if ( httpsEnabled != null )
        {
            if ( httpsEnabled )
            {
                properties.put( Configurator.WEBSERVER_HTTPS_ENABLED_PROPERTY_KEY, "true" );
            }
            else
            {
                properties.put( Configurator.WEBSERVER_HTTPS_ENABLED_PROPERTY_KEY, "false" );
            }
        }

        for ( Object key : arbitraryProperties.keySet() )
        {
            properties.put( String.valueOf( key ), String.valueOf( arbitraryProperties.get( key ) ) );
        }

        for ( Map.Entry<String, String> entry : properties.entrySet() )
        {
            ServerTestUtils.writePropertyToFile( entry.getKey(), entry.getValue(), temporaryConfigFile );
        }
    }

    protected ServerBuilder()
    {
    }

    public ServerBuilder usingDatabaseDir( String dbDir )
    {
        this.dbDir = dbDir;
        return this;
    }


    public ServerBuilder withThirdPartyJaxRsPackage( String packageName, String mountPoint )
    {
        thirdPartyPackages.put( packageName, mountPoint );
        return this;
    }

    public ServerBuilder withSecurityRules( Class... securityRuleClasses )
    {
        ArrayList<String> classNames = new ArrayList<>();
        for ( Class c : securityRuleClasses )
        {
            classNames.add( c.getCanonicalName() );
        }

        this.securityRuleClassNames = classNames.toArray( new String[securityRuleClasses.length] );

        return this;
    }

    private static String asOneLine( Map<String, String> properties )
    {
        StringBuilder builder = new StringBuilder();
        for ( Map.Entry<String, String> property : properties.entrySet() )
        {
            builder.append( (builder.length() > 0 ? "," : "") );
            builder.append( property.getKey() );
            builder.append( "=" );
            builder.append( property.getValue() );
        }
        return builder.toString();
    }
}