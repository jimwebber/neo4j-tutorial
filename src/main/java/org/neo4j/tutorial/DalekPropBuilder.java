package org.neo4j.tutorial;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;

import java.util.ArrayList;
import java.util.List;

public class DalekPropBuilder
{
    private static final String PROP = "prop";
    private static final String PROPS = "props";
    private final String episode;
    private List<Prop> props;

    public static DalekPropBuilder dalekProps(String episode)
    {
        return new DalekPropBuilder(episode);
    }

    private DalekPropBuilder(String episode)
    {
        super();
        this.episode = episode;
        props = new ArrayList<Prop>();
    }

    public DalekPropBuilder addProp(String shoulder, String skirt, String name)
    {
        props.add(new Prop(shoulder, skirt, name));
        return this;
    }

    private class Prop
    {
        private String shoulder;
        private String skirt;
        private String name;

        public Prop(String shoulder, String skirt, String name)
        {
            super();
            this.shoulder = shoulder;
            this.skirt = skirt;
            this.name = name;
        }

        public String getShoulder()
        {
            return shoulder;
        }

        public String getSkirt()
        {
            return skirt;
        }

        public String getName()
        {
            return name;
        }
    }

    public void fact(GraphDatabaseService db)
    {
        Node dalekSpeciesNode = db.index()
                                  .forNodes("species")
                                  .get("species", "Dalek")
                                  .getSingle();

        Node episodeNode = ensureEpisodeIsInDb(episode, db);
        ensureEpisodeIsConnectedToDalekSpecies(episodeNode, dalekSpeciesNode);

        Node episodePropsNode = db.createNode();
        episodePropsNode.setProperty(PROPS, "Daleks");
        episodePropsNode.createRelationshipTo(episodeNode, DoctorWhoRelationships.USED_IN);

        for (Prop prop : props)
        {
            if (isFullProp(prop))
            {
                Node currentDalekPropNode = ensurePropAppearsInDb(prop.getName(), db);
                currentDalekPropNode.createRelationshipTo(episodePropsNode, DoctorWhoRelationships.MEMBER_OF);

                if (shoulderExists(prop))
                {
                    createPartAttachedToProp(prop.getShoulder(), "shoulder", currentDalekPropNode, db);
                }

                if (skirtExists(prop))
                {
                    createPartAttachedToProp(prop.getSkirt(), "skirt", currentDalekPropNode, db);
                }
            }
            else
            {
                if (shoulderExists(prop))
                {
                    createPartAttachedToPropGroup(prop.getShoulder(), "shoulder", episodePropsNode, db);
                }

                if (skirtExists(prop))
                {
                    createPartAttachedToPropGroup(prop.getSkirt(), "skirt", episodePropsNode, db);
                }
            }

        }
    }

    private void createPartAttachedToProp(String originalPropName, String part, Node currentDalekPropNode,
                                          GraphDatabaseService db)
    {
        Node partNode = ensurePartExistsInDb(originalPropName, part, db);
        if (!relationshipExists(currentDalekPropNode, partNode, DoctorWhoRelationships.COMPOSED_OF, Direction.OUTGOING))
        {
            currentDalekPropNode.createRelationshipTo(partNode, DoctorWhoRelationships.COMPOSED_OF);
        }
    }

    private void createPartAttachedToPropGroup(String originalPropName, String part, Node propGroupNode,
                                               GraphDatabaseService db)
    {
        Node partNode = ensurePartExistsInDb(originalPropName, part, db);
        if (!relationshipExists(partNode, propGroupNode, DoctorWhoRelationships.MEMBER_OF, Direction.OUTGOING))
        {
            partNode.createRelationshipTo(propGroupNode, DoctorWhoRelationships.MEMBER_OF);
        }
    }

    private boolean relationshipExists(Node startNode, Node endNode, RelationshipType relationship, Direction direction)
    {
        Iterable<Relationship> rels = startNode.getRelationships(direction, relationship);
        for (Relationship rel : rels)
        {
            if (rel.getOtherNode(startNode)
                   .equals(endNode))
            {
                return true;
            }
        }
        return false;
    }

    private boolean skirtExists(Prop prop)
    {
        return prop.getSkirt() != null;
    }

    private boolean shoulderExists(Prop prop)
    {
        return prop.getShoulder() != null;
    }

    private boolean isFullProp(Prop prop)
    {
        return prop.getName() != null;
    }

    private Node ensurePartExistsInDb(String originalPropName, String part, GraphDatabaseService db)
    {
        Index<Node> index = db.index()
                              .forNodes(PROPS);
        Node shoulderNode = index.get(part, originalPropName)
                                 .getSingle();
        if (shoulderNode == null)
        {
            shoulderNode = db.createNode();
            shoulderNode.setProperty("part", part);
            index.add(shoulderNode, part, originalPropName);

            Node originalDalekPropNode = ensurePropAppearsInDb(originalPropName, db);
            shoulderNode.createRelationshipTo(originalDalekPropNode, DoctorWhoRelationships.ORIGINAL_PROP);
        }
        return shoulderNode;
    }

    private Node ensurePropAppearsInDb(String prop, GraphDatabaseService db)
    {
        Index<Node> index = db.index()
                              .forNodes(PROPS);
        Node dalekPropNode = index.get(PROP, prop)
                                  .getSingle();
        if (dalekPropNode == null)
        {
            dalekPropNode = db.createNode();
            dalekPropNode.setProperty(PROP, prop);
            index.add(dalekPropNode, PROP, prop);
        }
        return dalekPropNode;
    }

    private void ensureEpisodeIsConnectedToDalekSpecies(Node episodeNode, Node speciesNode)
    {
        boolean isConnected = false;
        for (Relationship rel : episodeNode.getRelationships(DoctorWhoRelationships.APPEARED_IN, Direction.INCOMING))
        {
            if (rel.getStartNode()
                   .equals(speciesNode))
            {
                isConnected = true;
                break;
            }
        }
        if (!isConnected)
        {
            throw new RuntimeException("Episode '" + episodeNode.getProperty("title")
                                               + "' not connected to Dalek species.");
        }
    }

    private Node ensureEpisodeIsInDb(String episode, GraphDatabaseService db)
    {
        Index<Node> index = db.index()
                              .forNodes("episodes");
        Node episodeNode = index.get("title", episode)
                                .getSingle();
        if (episodeNode == null)
        {
            throw new RuntimeException("Episode '" + episode + "' missing from database.");
        }
        return episodeNode;
    }
}
