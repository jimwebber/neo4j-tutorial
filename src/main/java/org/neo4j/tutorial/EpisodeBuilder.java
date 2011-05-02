package org.neo4j.tutorial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class EpisodeBuilder {

    private String title;
    private List<String> companionNames = new ArrayList<String>();
    private int episodeNumber = 0;
    private String actorFirstname;
    private String actorLastname;
    private List<String> enemySpecies = new ArrayList<String>();
    private List<String> enemies = new ArrayList<String>();

    private EpisodeBuilder(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public static EpisodeBuilder episode(int episodeNumber) {
        return new EpisodeBuilder(episodeNumber);
    }

    public EpisodeBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EpisodeBuilder doctor(String actorfirstName, String actorLastname) {
        this.actorFirstname = actorfirstName;
        this.actorLastname = actorLastname;
        return this;
    }

    public EpisodeBuilder companion(String... namesOfCompanions) {
        for (String str : namesOfCompanions) {
            companionNames.add(str);
        }
        return this;
    }

    public EpisodeBuilder enemySpecies(String... enemySpecies) {
        for(String str : enemySpecies) {
            this.enemySpecies.add(str);
        }
        return this;
    }

    public EpisodeBuilder enemy(String... enemies) {
        for(String str : enemies) {
            this.enemies.add(str);
        }
        return this;
    }

    public void fact(DoctorWhoUniverse universe) {
        checkEpisodeNumberAndTitle();

        GraphDatabaseService db = universe.getDatabase();

        Node episode = db.createNode();
        episode.setProperty("episode", episodeNumber);
        episode.setProperty("title", title);

        Node theDoctorActor = setDoctorActor(universe);

        theDoctorActor.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);

        Set<Node> companions = setCompanions(universe);
        for (Node companion : companions) {
            companion.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
        }

        if (enemySpecies != null) {
            for (String es : enemySpecies) {
                Node enemySpeciesNode = ensureEnemySpeciesInDb(es, universe);
                enemySpeciesNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
            }
        }

        if (enemies != null) {
            for (String enemy : enemies) {
                Node enemyNode = ensureEnemyNodeInDb(enemy, universe);
                enemyNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
            }
        }
    }

    private Node ensureEnemyNodeInDb(String enemy, DoctorWhoUniverse universe) {
        GraphDatabaseService db = universe.getDatabase();
        Iterable<Node> allNodes = db.getAllNodes();

        for (Node n : allNodes) {
            if (n.hasRelationship(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING)) {
                for (Relationship r : n.getRelationships(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING)) {
                    if (r.getEndNode().equals(universe.theDoctor()) && r.getStartNode().hasProperty("name")
                            && r.getStartNode().getProperty("name").equals(enemy)) {
                        return r.getStartNode();
                    }
                }
            }
        }

        Node newEnemyNode = db.createNode();
        newEnemyNode.setProperty("name", enemy);
        newEnemyNode.createRelationshipTo(universe.theDoctor(), DoctorWhoUniverse.ENEMY_OF);
        universe.theDoctor().createRelationshipTo(newEnemyNode, DoctorWhoUniverse.ENEMY_OF);
        return newEnemyNode;
    }

    private Node ensureEnemySpeciesInDb(String es, DoctorWhoUniverse universe) {
        GraphDatabaseService db = universe.getDatabase();
        Iterable<Node> allNodes = db.getAllNodes();
        Node enemySpeciesNode = null;
        for (Node n : allNodes) {
            if (n.hasProperty("species") && n.getProperty("species").equals(es)) {
                enemySpeciesNode = n;
                break;
            }
        }

        if (enemySpeciesNode == null) {
            enemySpeciesNode = db.createNode();
            enemySpeciesNode.setProperty("species", es);
            enemySpeciesNode.createRelationshipTo(universe.theDoctor(), DoctorWhoUniverse.ENEMY_OF);
            universe.theDoctor().createRelationshipTo(enemySpeciesNode, DoctorWhoUniverse.ENEMY_OF);
        }

        return enemySpeciesNode;
    }

    private void checkEpisodeNumberAndTitle() {
        if (title == null) {
            throw new RuntimeException("Episodes must have a title");
        }

        if (episodeNumber < 1) {
            throw new RuntimeException("Episodes must have a number");
        }
    }

    private Set<Node> setCompanions(DoctorWhoUniverse universe) {
        HashSet<Node> result = new HashSet<Node>();
        for (String companionName : companionNames) {
            result.add(ensureCompanionExists(companionName, universe));
        }

        return result;
    }

    private Node ensureCompanionExists(String companionName, DoctorWhoUniverse universe) {
        if (companionExists(companionName, universe)) {
            return getCompanion(companionName, universe);
        } else {
            return createCompanion(companionName, universe);
        }
    }

    private Node createCompanion(String companion, DoctorWhoUniverse universe) {
        GraphDatabaseService db = universe.getDatabase();
        Node companionNode = db.createNode();
        companionNode.setProperty("name", companion);
        companionNode.createRelationshipTo(universe.theDoctor(), DoctorWhoUniverse.COMPANION_OF);
        universe.companionsIndex.add(companionNode, "name", companion);
        return companionNode;
    }

    private boolean companionExists(String companion, DoctorWhoUniverse universe) {
        return getCompanion(companion, universe) != null ? true : false;
    }

    private Node getCompanion(String companion, DoctorWhoUniverse universe) {
        Iterable<Relationship> relationships = universe.theDoctor().getRelationships(DoctorWhoUniverse.COMPANION_OF, Direction.INCOMING);
        for (Relationship r : relationships) {
            Node currentNode = r.getStartNode();
            if (currentNode.getProperty("name").equals(companion)) {
                return currentNode;
            }
        }
        return null;
    }

    private Node setDoctorActor(DoctorWhoUniverse universe) {
        Node theDoctor = universe.theDoctor();
        Iterable<Relationship> relationships = theDoctor.getRelationships(DoctorWhoUniverse.PLAYED, Direction.INCOMING);
        for (Relationship r : relationships) {
            Node current = r.getStartNode();
            if (current.getProperty("firstname").equals(actorFirstname) && current.getProperty("lastname").equals(actorLastname)) {
                return current;
            }
        }

        Node doctorActor = universe.getDatabase().createNode();
        doctorActor.setProperty("firstname", actorFirstname);
        doctorActor.setProperty("lastname", actorLastname);
        doctorActor.createRelationshipTo(theDoctor, DoctorWhoUniverse.PLAYED);
        return doctorActor;
    }
}
