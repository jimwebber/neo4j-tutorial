package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

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
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.tutorial.server.rest.BatchCommandBuilder;
import org.neo4j.tutorial.server.rest.RelationshipDescription;
import org.neo4j.tutorial.server.rest.TraversalDescription;
import org.neo4j.tutorial.server.rest.domain.EpisodeSearchResult;
import org.neo4j.tutorial.server.rest.domain.EpisodeSearchResults;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * In this Koan we use the REST API to explore the Doctor Who universe.
 */
public class Koan09 {

	private static ServerDoctorWhoUniverse universe;

	@BeforeClass
	public static void createDatabase() throws Exception {
		universe = new ServerDoctorWhoUniverse();
	}

	@AfterClass
	public static void closeTheDatabase() {
		universe.stop();
	}

	@Test
	public void shouldCountTheEnemiesOfTheDoctor() throws Exception {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		String response = null;

		// SNIPPET_START

		WebResource resource = client.resource(universe.theDoctor().get("incoming_relationships") + "/ENEMY_OF");
		response = resource.accept(MediaType.APPLICATION_JSON).get(String.class);

		// SNIPPET_END

		List<Map<String, Object>> json = JsonHelper.jsonToList(response);
		assertEquals(140, json.size());
	}

	@Test
	public void shouldIdentifyWhichDoctorsTookPartInInvasionStories()
			throws Exception {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		String response = null;

		// SNIPPET_START

		TraversalDescription traversal = new TraversalDescription();
		traversal.setOrder("depth first");
		traversal.setUniqueness("node path");
		traversal.setRelationships(
				new RelationshipDescription("PLAYED", RelationshipDescription.IN),
				new RelationshipDescription("APPEARED_IN", RelationshipDescription.OUT));
		traversal.setReturnFilter("position.endNode().hasProperty('title') && position.endNode().getProperty('title').contains('Invasion')");
		traversal.setMaxDepth(3);

		WebResource resource = client.resource(universe.theDoctor().get("traverse").toString().replace("{returnType}", "fullpath"));
		response = resource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(String.class, traversal.toJson());

		// SNIPPET_END

		EpisodeSearchResults results = new EpisodeSearchResults(JsonHelper.jsonToList(response));
		for (EpisodeSearchResult result : results) {
			System.out.println(result.getActor() + ": " + result.getEpisode());
		}
	}

	@Test
	public void canAddFirstAndSecondIncarnationInformationForTheDoctor() {
		String PLAYED = "PLAYED";
		String INCARNATION_OF = "INCARNATION_OF";

		String theDoctorUri = universe.theDoctor().get("self").toString();
		
		Map<String,Object> williamHartnell = universe.getJsonFor(universe.getUriFromIndex("actors", "actor", "William Hartnell"));
		Map<String,Object> richardHurdnall = universe.getJsonFor(universe.getUriFromIndex("actors", "actor", "Richard Hurdnall"));
		Map<String,Object> patrickTroughton = universe.getJsonFor(universe.getUriFromIndex("actors", "actor", "Patrick Troughton"));
		
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		BatchCommandBuilder cmds = new BatchCommandBuilder();

		cmds
				.createNode(0, MapUtil.stringMap("incarnation", "First Doctor"))
				.createNode(1, MapUtil.stringMap("incarnation", "Second Doctor"))
				.createRelationship("{0}/relationships", theDoctorUri, INCARNATION_OF)
				.createRelationship("{1}/relationships", theDoctorUri, INCARNATION_OF)
				.createRelationship(williamHartnell.get("create_relationship").toString(), "{0}", PLAYED)
				.createRelationship(richardHurdnall.get("create_relationship").toString(), "{0}", PLAYED)
				.createRelationship(patrickTroughton.get("create_relationship").toString(), "{1}", PLAYED);

		WebResource resource = client.resource("http://localhost:7474/db/data/batch");
		resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(String.class, cmds.build());

		assertFirstAndSecondDoctorCreatedAndLinkedToActors(universe.getServer().getDatabase().graph);

	}

	private void assertFirstAndSecondDoctorCreatedAndLinkedToActors(AbstractGraphDatabase db) {
		Node doctorNode = db.getNodeById(1);

		final PatternNode theDoctor = new PatternNode();
		theDoctor.addPropertyConstraint("name", CommonValueMatchers.exact("Doctor"));

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

		firstDoctor.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("INCARNATION_OF"), Direction.OUTGOING);
		secondDoctor.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("INCARNATION_OF"), Direction.OUTGOING);
		williamHartell.createRelationshipTo(firstDoctor, DoctorWhoUniverse.PLAYED, Direction.OUTGOING);
		richardHurdnall.createRelationshipTo(firstDoctor, DoctorWhoUniverse.PLAYED, Direction.OUTGOING);
		patrickTroughton.createRelationshipTo(secondDoctor, DoctorWhoUniverse.PLAYED, Direction.OUTGOING);

		PatternMatcher matcher = PatternMatcher.getMatcher();
		final Iterable<PatternMatch> matches = matcher.match(theDoctor, doctorNode);

		assertTrue(matches.iterator().hasNext());
	}
}
