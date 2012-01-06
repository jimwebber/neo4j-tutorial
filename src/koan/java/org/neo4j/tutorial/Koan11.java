package org.neo4j.tutorial;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
import org.neo4j.server.NeoServerWithEmbeddedWebServer;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.tutorial.server.ServerBuilder;
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
 * In this Koan we use the default REST API to explore the Doctor Who universe.
 */
public class Koan11
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
    public void shouldCountTheEnemiesOfTheDoctor() throws Exception
    {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        String response = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        WebResource resource = client.resource(universe.theDoctor().get("incoming_relationships") + "/ENEMY_OF");
        response = resource.accept(MediaType.APPLICATION_JSON).get(String.class);

        // SNIPPET_END

        List<Map<String, Object>> json = JsonHelper.jsonToList(response);
        int numberOfEnemiesOfTheDoctor = 146;
        assertEquals(numberOfEnemiesOfTheDoctor, json.size());
    }

    @Test
    public void shouldIdentifyWhichDoctorsTookPartInInvasionStories()
            throws Exception
    {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        ClientResponse response = null;
        TraversalDescription traversal = new TraversalDescription();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        traversal.setOrder("depth_first");
        traversal.setUniqueness("node_path");
        traversal.setRelationships(
                new RelationshipDescription("PLAYED", RelationshipDescription.IN),
                new RelationshipDescription("APPEARED_IN", RelationshipDescription.OUT));
        traversal.setReturnFilter(
                "position.endNode().hasProperty('title') && position.endNode().getProperty('title').contains('Invasion')");
        traversal.setMaxDepth(3);

        WebResource resource = client.resource(
                universe.theDoctor().get("traverse").toString().replace("{returnType}", "fullpath"));
        String requestJson = traversal.toJson();

        response = resource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, requestJson);

        // SNIPPET_END

        String responseJson = response.getEntity(String.class);

        EpisodeSearchResults results = new EpisodeSearchResults(JsonHelper.jsonToList(responseJson));
        assertActorsAndInvasionEpisodes(results);
    }

    @Test
    public void canAddFirstAndSecondIncarnationInformationForTheDoctor()
    {

        // We'd like to update the model to add a new domain entity - "incarnation".
        // Timelords have one or more incarnations. In the TV series, an incarnation is played by one or
        // more actors (usually one). Here we're going to use the REST batch API to add a bit of this new
        // model. See the presentation for an example of the target graph structure.

        String PLAYED = "PLAYED";
        String INCARNATION_OF = "INCARNATION_OF";

        Map<String, Object> theDoctorJson = universe.theDoctor();
        String theDoctorUri = theDoctorJson.get("self").toString();

        Map<String, Object> williamHartnellJson = universe.getJsonFor(
                universe.getUriFromIndex("actors", "actor", "William Hartnell"));
        Map<String, Object> richardHurdnallJson = universe.getJsonFor(
                universe.getUriFromIndex("actors", "actor", "Richard Hurdnall"));
        Map<String, Object> patrickTroughtonJson = universe.getJsonFor(
                universe.getUriFromIndex("actors", "actor", "Patrick Troughton"));

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        BatchCommandBuilder cmds = new BatchCommandBuilder();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cmds
                .createNode(0, MapUtil.stringMap("incarnation", "First Doctor"))
                .createNode(1, MapUtil.stringMap("incarnation", "Second Doctor"))
                .createRelationship("{0}/relationships", theDoctorUri, INCARNATION_OF)
                .createRelationship("{1}/relationships", theDoctorUri, INCARNATION_OF)
                .createRelationship(williamHartnellJson.get("create_relationship").toString(), "{0}", PLAYED)
                .createRelationship(richardHurdnallJson.get("create_relationship").toString(), "{0}", PLAYED)
                .createRelationship(patrickTroughtonJson.get("create_relationship").toString(), "{1}", PLAYED);

        WebResource resource = client.resource("http://localhost:7474/db/data/batch");
        resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(String.class, cmds.build());

        // SNIPPET_END

        assertFirstAndSecondDoctorCreatedAndLinkedToActors(universe.getServer().getDatabase().graph);

    }

    private void assertActorsAndInvasionEpisodes(EpisodeSearchResults results)
    {

        Map<String, String> episodesAndActors = MapUtil.stringMap("The Christmas Invasion", "David Tennant",
                                                                  "The Invasion of Time", "Tom Baker",
                                                                  "The Android Invasion", "Tom Baker",
                                                                  "Invasion of the Dinosaurs", "Jon Pertwee",
                                                                  "The Invasion", "Patrick Troughton",
                                                                  "The Dalek Invasion of Earth", "William Hartnell");

        int count = 0;
        for (EpisodeSearchResult result : results)
        {
            assertTrue(episodesAndActors.containsKey(result.getEpisode()));
            assertEquals(episodesAndActors.get(result.getEpisode()), result.getActor());
            count++;
        }

        assertEquals(episodesAndActors.keySet().size(), count);
    }

    private void assertFirstAndSecondDoctorCreatedAndLinkedToActors(AbstractGraphDatabase db)
    {
        Node doctorNode = db.getNodeById(1);

        final PatternNode theDoctor = new PatternNode();
        theDoctor.addPropertyConstraint("character", CommonValueMatchers.exact("Doctor"));

        final PatternNode firstDoctor = new PatternNode();
        firstDoctor.addPropertyConstraint("incarnation", CommonValueMatchers.exact("First Doctor"));

        final PatternNode secondDoctor = new PatternNode();
        secondDoctor.addPropertyConstraint("incarnation", CommonValueMatchers.exact("Second Doctor"));

        final PatternNode williamHartell = new PatternNode();
        williamHartell.addPropertyConstraint("actor", CommonValueMatchers.exact("William Hartnell"));

        final PatternNode richardHurdnall = new PatternNode();
        richardHurdnall.addPropertyConstraint("actor", CommonValueMatchers.exact("Richard Hurdnall"));

        final PatternNode patrickTroughton = new PatternNode();
        patrickTroughton.addPropertyConstraint("actor", CommonValueMatchers.exact("Patrick Troughton"));

        firstDoctor.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("INCARNATION_OF"),
                                         Direction.OUTGOING);
        secondDoctor.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("INCARNATION_OF"),
                                          Direction.OUTGOING);
        williamHartell.createRelationshipTo(firstDoctor, DoctorWhoRelationships.PLAYED, Direction.OUTGOING);
        richardHurdnall.createRelationshipTo(firstDoctor, DoctorWhoRelationships.PLAYED, Direction.OUTGOING);
        patrickTroughton.createRelationshipTo(secondDoctor, DoctorWhoRelationships.PLAYED, Direction.OUTGOING);

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match(theDoctor, doctorNode);

        assertTrue(matches.iterator().hasNext());
    }
}
