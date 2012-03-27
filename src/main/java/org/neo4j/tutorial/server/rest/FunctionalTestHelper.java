package org.neo4j.tutorial.server.rest;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.server.NeoServerWithEmbeddedWebServer;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.server.rest.domain.JsonParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class FunctionalTestHelper
{
    private final NeoServerWithEmbeddedWebServer server;

    public FunctionalTestHelper(NeoServerWithEmbeddedWebServer server)
    {
        if (server.getDatabase() == null)
        {
            throw new RuntimeException("Server must be started before using " + getClass().getName());
        }
        this.server = server;
    }

    void assertLegalJson(String entity) throws IOException, JsonParseException
    {
        JsonHelper.jsonToMap(entity);
    }

    public String dataUri()
    {
        return server.baseUri()
                     .toString() + "db/data/";
    }

    public String nodeUri()
    {
        return dataUri() + "node";
    }

    public String nodeUri(long id)
    {
        return nodeUri() + "/" + id;
    }

    public String nodePropertiesUri(long id)
    {
        return nodeUri(id) + "/properties";
    }

    public String nodePropertyUri(long id, String key)
    {
        return nodePropertiesUri(id) + "/" + key;
    }

    public String relationshipUri()
    {
        return dataUri() + "relationship";
    }

    public String relationshipUri(long id)
    {
        return relationshipUri() + "/" + id;
    }

    public String relationshipPropertiesUri(long id)
    {
        return relationshipUri(id) + "/properties";
    }

    public String relationshipPropertyUri(long id, String key)
    {
        return relationshipPropertiesUri(id) + "/" + key;
    }

    public String relationshipsUri(long nodeId, String dir, String... types)
    {
        StringBuilder typesString = new StringBuilder();
        for (String type : types)
        {
            typesString.append(typesString.length() > 0 ? "&" : "");
            typesString.append(type);
        }
        return nodeUri(nodeId) + "/relationships/" + dir + "/" + typesString;
    }

    public String indexUri()
    {
        return dataUri() + "index/";
    }

    public String nodeIndexUri()
    {
        return indexUri() + "node/";
    }

    public String relationshipIndexUri()
    {
        return indexUri() + "relationship/";
    }

    public String mangementUri()
    {
        return server.baseUri()
                     .toString() + "db/manage";
    }

    public String indexNodeUri(String indexName)
    {
        return nodeIndexUri() + indexName;
    }

    public String indexRelationshipUri(String indexName)
    {
        return relationshipIndexUri() + indexName;
    }

    public String indexNodeUri(String indexName, String key, Object value)
    {
        return indexNodeUri(indexName) + "/" + key + "/" + value.toString()
                                                                .replace(" ", "%20");
    }

    public String indexRelationshipUri(String indexName, String key, Object value)
    {
        try
        {
            return indexRelationshipUri(indexName) + "/" + key + "/" + URLEncoder.encode(value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            return indexRelationshipUri(indexName) + "/" + key + "/" + value;
        }
    }

    public String extensionUri()
    {
        return dataUri() + "ext";
    }

    public String extensionUri(String name)
    {
        return extensionUri() + "/" + name;
    }

    public String graphdbExtensionUri(String name, String method)
    {
        return extensionUri(name) + "/graphdb/" + method;
    }

    public String nodeExtensionUri(String name, String method, long id)
    {
        return extensionUri(name) + "/node/" + id + "/" + method;
    }

    public String relationshipExtensionUri(String name, String method, long id)
    {
        return extensionUri(name) + "/relationship/" + id + "/" + method;
    }

    public GraphDatabaseService getDatabase()
    {
        return server.getDatabase().graph;
    }

    public String getWebadminUri()
    {
        return server.baseUri()
                     .toString() + "webadmin";
    }
}
