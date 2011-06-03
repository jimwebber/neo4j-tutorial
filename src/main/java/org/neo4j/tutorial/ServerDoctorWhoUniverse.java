package org.neo4j.tutorial;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.neo4j.server.NeoServerWithEmbeddedWebServer;
import org.neo4j.server.modules.DiscoveryModule;
import org.neo4j.server.modules.ManagementApiModule;
import org.neo4j.server.modules.RESTApiModule;
import org.neo4j.server.modules.ThirdPartyJAXRSModule;
import org.neo4j.server.modules.WebAdminModule;
import org.neo4j.server.rest.domain.JsonHelper;
import org.neo4j.server.rest.domain.JsonParseException;
import org.neo4j.tutorial.server.ServerBuilder;
import org.neo4j.tutorial.server.rest.FunctionalTestHelper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ServerDoctorWhoUniverse extends DoctorWhoUniverse<Map<String,Object>> {

	private final NeoServerWithEmbeddedWebServer server;

	@SuppressWarnings("unchecked")
	public ServerDoctorWhoUniverse() throws Exception {
		super();
		server = ServerBuilder
				.server()
				.usingDatabaseDir(getDatabaseDirectory())
				.withPassingStartupHealthcheck()
				.withDefaultDatabaseTuning()
				.withSpecificServerModules(DiscoveryModule.class,
						RESTApiModule.class, ManagementApiModule.class,
						ThirdPartyJAXRSModule.class, WebAdminModule.class)
				.build();
		server.start();
	}

	@Override
	Map<String,Object> theDoctor() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource resource = client.resource(getDoctorUri());
		String response = resource.accept(MediaType.APPLICATION_JSON).get(
				String.class);
		try {
			return JsonHelper.jsonToMap(response);
		} catch (JsonParseException e) {
			throw new RuntimeException("Invalid response when looking up Doctor node");
		}
	}

	private String getDoctorUri() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource resource = client.resource(new FunctionalTestHelper(server)
				.indexNodeUri("characters", "name", "Doctor"));
		String response = resource.accept(MediaType.APPLICATION_JSON).get(
				String.class);
		try {
			return JsonHelper.jsonToList(response).get(0).get("self").toString();
		} catch (JsonParseException e) {
			throw new RuntimeException("Invalid response when looking up Doctor node");
		}
	}

	@Override
	void stop() {
		server.stop();
	}

	public NeoServerWithEmbeddedWebServer getServer() {
		return server;
	}

}
