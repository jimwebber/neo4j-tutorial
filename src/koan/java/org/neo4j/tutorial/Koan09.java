package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.server.rest.domain.JsonParseException;
import org.neo4j.tutorial.server.rest.FunctionalTestHelper;

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
		
		List<Map<String,Object>> json = JsonHelper.jsonToList(response);
		assertEquals(139, json.size());
	}
	

}
		