package org.neo4j.tutorial;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class DalekPropBuilder {
	private final String episode;
	private List<Prop> props;
	
	public static DalekPropBuilder dalekProps(String episode){
		return new DalekPropBuilder(episode);
	}
	
	private DalekPropBuilder(String episode) {
		super();
		this.episode = episode;
		props = new ArrayList<Prop>();
	}
	
	public DalekPropBuilder addProp(String shoulder, String skirt){
		props.add(new Prop(shoulder, skirt));
		return this;
	}
	
	private class Prop{
		private String shoulder;
		private String skirt;
		public Prop(String shoulder, String skirt) {
			super();
			this.shoulder = shoulder;
			this.skirt = skirt;
		}
		public String getShoulder() {
			return shoulder;
		}
		public String getSkirt() {
			return skirt;
		}
	}
	
	public void fact(GraphDatabaseService db) {
		Node daleksNode = db.index().forNodes("species").get("species", "Dalek").getSingle();
        Node episodeNode = ensureEpisodeIsInDb(episode, db);
        for (Prop prop : props){
        	Node propNode = db.createNode();
        	propNode.setProperty("prop", "Dalek");
        	propNode.createRelationshipTo(episodeNode, DoctorWhoUniverse.APPEARED_IN);
        	propNode.createRelationshipTo(daleksNode, DoctorWhoUniverse.IS_A);
        	
        	if (prop.getShoulder() != null){
        		Node shoulderNode = ensurePropExistsInDb(prop.getShoulder(), db);
        		shoulderNode.createRelationshipTo(propNode, DoctorWhoUniverse.SHOULDER);
        	}
        	
        	if (prop.getSkirt() != null){
        		Node skirtNode = ensurePropExistsInDb(prop.getSkirt(), db);
        		skirtNode.createRelationshipTo(propNode, DoctorWhoUniverse.SKIRT);
        	}
        }
    }
	
	private Node ensureEpisodeIsInDb(String episode, GraphDatabaseService db){
		Node episodeNode = db.index().forNodes("episodes").get("title", episode).getSingle();
		if (episodeNode == null){
			throw new RuntimeException("Episode '" + episode + "' missing from database.");
		}
		return episodeNode;
	}
	
	private Node ensurePropExistsInDb(String part, GraphDatabaseService db){
		Node partNode = db.index().forNodes("props").get("part", part).getSingle();
		if (partNode == null){
			partNode = db.createNode();
			partNode.setProperty("part", part);
		}
		return partNode;
	}
}
