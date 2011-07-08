package org.neo4j.tutorial;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;

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
	
	public DalekPropBuilder addProp(String shoulder, String skirt, String name){
		props.add(new Prop(shoulder, skirt, name));
		return this;
	}
	
	private class Prop{
		private String shoulder;
		private String skirt;
		private String name;
		public Prop(String shoulder, String skirt, String name) {
			super();
			this.shoulder = shoulder;
			this.skirt = skirt;
			this.name = name;
		}
		public String getShoulder() {
			return shoulder;
		}
		public String getSkirt() {
			return skirt;
		}
		public String getName() {
			return name;
		}
	}
	
	public void fact(GraphDatabaseService db) {
		Node dalekSpeciesNode = db.index().forNodes("species").get("species", "Dalek").getSingle();
        Node episodeNode = ensureEpisodeIsInDb(episode, db);
        
        Node episodePropsNode = db.createNode();
        episodePropsNode.setProperty("props", "Daleks");
        episodePropsNode.createRelationshipTo(episodeNode, DoctorWhoUniverse.USED_IN);
        
        for (Prop prop : props){
        	if (isFullProp(prop)){
        		Node currentDalekPropNode = ensurePropAppearsInDb(prop.getName(), db);
            	currentDalekPropNode.createRelationshipTo(dalekSpeciesNode, DoctorWhoUniverse.IS_A);
            	currentDalekPropNode.createRelationshipTo(episodePropsNode, DoctorWhoUniverse.MEMBER_OF);
            	
            	if (shoulderExists(prop)){
            		createPartAttachedToProp(prop.getShoulder(), "shoulder", currentDalekPropNode, db);	
            	}
            	
            	if (skirtExists(prop)){
            		createPartAttachedToProp(prop.getSkirt(), "skirt", currentDalekPropNode, db);
            	}
        	}
        	else {
        		if (shoulderExists(prop)){
        			createPartAttachedToPropGroup(prop.getShoulder(), "shoulder", episodePropsNode, db);
            	}
            	
            	if (skirtExists(prop)){
            		createPartAttachedToPropGroup(prop.getSkirt(), "skirt", episodePropsNode, db);
            	}
        	}
        	
        }
    }

	private void createPartAttachedToProp(String originalPropName, String part, Node currentDalekPropNode, GraphDatabaseService db) {
		Node partNode = ensurePartExistsInDb(originalPropName, part, db);
		if (!relationshipExists(currentDalekPropNode, partNode, DoctorWhoUniverse.COMPOSED_OF, Direction.OUTGOING)){
			currentDalekPropNode.createRelationshipTo(partNode, DoctorWhoUniverse.COMPOSED_OF);
		}
	}
	
	private void createPartAttachedToPropGroup(String originalPropName, String part, Node propGroupNode, GraphDatabaseService db) {
		Node partNode = ensurePartExistsInDb(originalPropName, part, db);
		if (!relationshipExists(partNode, propGroupNode, DoctorWhoUniverse.MEMBER_OF, Direction.OUTGOING)){
			partNode.createRelationshipTo(propGroupNode, DoctorWhoUniverse.MEMBER_OF);
		}
	}
	
	private boolean relationshipExists(Node startNode, Node endNode, RelationshipType relationship, Direction direction){
		Iterable<Relationship> rels = startNode.getRelationships(direction, relationship);
		for (Relationship rel: rels){
			if (rel.getOtherNode(startNode).equals(endNode)){
				return true;
			}
		}
		return false;
	}

	private boolean skirtExists(Prop prop) {
		return prop.getSkirt() != null;
	}

	private boolean shoulderExists(Prop prop) {
		return prop.getShoulder() != null;
	}

	private boolean isFullProp(Prop prop) {
		return prop.getName() != null;
	}
	
	private Node ensurePartExistsInDb(String originalPropName, String part, GraphDatabaseService db) {
		Index<Node> index = db.index().forNodes("props");
		Node shoulderNode = index.get(part, originalPropName).getSingle();
		if (shoulderNode == null){
			shoulderNode = db.createNode();
			shoulderNode.setProperty("part", part);
			index.add(shoulderNode, part, originalPropName);
			
			Node originalDalekPropNode = ensurePropAppearsInDb(originalPropName, db);
			shoulderNode.createRelationshipTo(originalDalekPropNode, DoctorWhoUniverse.ORIGINAL_PROP);
		}
		return shoulderNode;
	}

	private Node ensurePropAppearsInDb(String name, GraphDatabaseService db) {
		Index<Node> index = db.index().forNodes("props");
		Node dalekPropNode = index.get("prop", name).getSingle();
		if (dalekPropNode == null){
			dalekPropNode = db.createNode();
			dalekPropNode.setProperty("prop", name);
			index.add(dalekPropNode, "prop", name);
		}
		return dalekPropNode;
	}

	private Node ensureEpisodeIsInDb(String episode, GraphDatabaseService db){
		Index<Node> index = db.index().forNodes("episodes");
		Node episodeNode = index.get("title", episode).getSingle();
		if (episodeNode == null){
			throw new RuntimeException("Episode '" + episode + "' missing from database.");
		}
		return episodeNode;
	}
}
