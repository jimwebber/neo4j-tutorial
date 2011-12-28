package org.neo4j.tutorial;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphmatching.CommonValueMatchers;
import org.neo4j.graphmatching.PatternMatch;
import org.neo4j.graphmatching.PatternMatcher;
import org.neo4j.graphmatching.PatternNode;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.tutorial.server.rest.BatchCommandBuilder;
import org.neo4j.tutorial.server.rest.RelationshipDescription;
import org.neo4j.tutorial.server.rest.TraversalDescription;
import org.neo4j.tutorial.server.rest.domain.EpisodeSearchResult;
import org.neo4j.tutorial.server.rest.domain.EpisodeSearchResults;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * In this Koan we enhance the default REST API with managed and unmanaged (JAX-RS) extensions
 * to provide a domain-specific set of Doctor Who resources inside the Neo4j server.
 */
@Ignore
public class Koan12
{

    private static ServerDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new ServerDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }


}
