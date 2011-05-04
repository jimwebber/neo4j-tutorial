package org.neo4j.tutorial;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class EpisodeBuilder {

    private String title;
    private List<String> companionNames = new ArrayList<String>();
    private int episodeNumber = 0;
    private List<String> doctorActors = new ArrayList<String>();
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

    public EpisodeBuilder doctor(String actorName) {
        doctorActors.add(actorName);
        return this;
    }

    public EpisodeBuilder companion(String... namesOfCompanions) {
        for (String str : namesOfCompanions) {
            companionNames.add(str);
        }
        return this;
    }

    public EpisodeBuilder enemySpecies(String... enemySpecies) {
        for (String str : enemySpecies) {
            this.enemySpecies.add(str);
        }
        return this;
    }

    public EpisodeBuilder enemy(String... enemies) {
        for (String str : enemies) {
            this.enemies.add(str);
        }
        return this;
    }

    public void fact(DoctorWhoUniverse universe) {
        checkEpisodeNumberAndTitle();

        GraphDatabaseService db = universe.getDatabase();

        Node episode = ensureEpisodeNodeInDb(db);

        ensureDoctorActorsAreInDb(universe, episode);

        if (this.companionNames != null) {
            for (String companionName : companionNames) {
                Node companionNode = CharacterBuilder.ensureCharacterIsInDb(companionName, universe);
                companionNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
            }
        }

        if (this.enemySpecies != null) {
            for (String eSpecies : enemySpecies) {
                Node speciesNode = SpeciesBuilder.ensureSpeciesInDb(eSpecies, universe);
                speciesNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
            }
        }

        if (this.enemies != null) {
            for (String enemy : enemies) {
                Node enemyNode = CharacterBuilder.ensureCharacterIsInDb(enemy, universe);
                enemyNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
            }
        }
    }

    private void ensureDoctorActorsAreInDb(DoctorWhoUniverse universe, Node episode) {
        if (doctorActors != null) {
            for(String actor : doctorActors) {
                Node actorNode = ensureDoctorActorInDb(actor, universe);
                actorNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
            }
        }
    }

    private Node ensureEpisodeNodeInDb(GraphDatabaseService db) {
        Iterable<Node> allNodes = db.getAllNodes();
        Node episode = null;
        for (Node n : allNodes) {
            if (n.hasProperty("episode") && n.hasProperty("title")) {
                if (n.getProperty("title").equals(this.title) && n.getProperty("episode").equals(this.episodeNumber)) {
                    episode = n;
                    break;
                }
            }
        }

        if (episode == null) {
            episode = db.createNode();
            episode.setProperty("episode", episodeNumber);
            episode.setProperty("title", title);
        }

        return episode;
    }

//    private Node ensureEnemyNodeInDb(String enemy, DoctorWhoUniverse universe) {
//        GraphDatabaseService db = universe.getDatabase();
//
//        Node enemyNode = CharacterBuilder.ensureCharacterIsInDb(enemy, universe);
//        if (enemyNode == null) {
//
//            enemyNode = db.createNode();
//            enemyNode.setProperty("name", enemy);
//
//            enemyNode.createRelationshipTo(universe.theDoctor(), DoctorWhoUniverse.ENEMY_OF);
//            universe.theDoctor().createRelationshipTo(enemyNode, DoctorWhoUniverse.ENEMY_OF);
//        }
//        return enemyNode;
//    }

    private void checkEpisodeNumberAndTitle() {
        if (title == null) {
            throw new RuntimeException("Episodes must have a title");
        }

        if (episodeNumber < 1) {
            throw new RuntimeException("Episodes must have a number");
        }
    }

//    private Set<Node> setCompanions(DoctorWhoUniverse universe) {
//        HashSet<Node> result = new HashSet<Node>();
//        for (String companionName : companionNames) {
//            result.add(ensureCompanionExists(companionName, universe));
//        }
//
//        return result;
//    }

//    private Node ensureCompanionExists(String companionName, DoctorWhoUniverse universe) {
//        if (companionExists(companionName, universe)) {
//            return getCompanion(companionName, universe);
//        } else {
//            return createCompanion(companionName, universe);
//        }
//    }

//    private Node createCompanion(String companion, DoctorWhoUniverse universe) {
//        GraphDatabaseService db = universe.getDatabase();
//        Node companionNode = db.createNode();
//        companionNode.setProperty("name", companion);
//        companionNode.createRelationshipTo(universe.theDoctor(), DoctorWhoUniverse.COMPANION_OF);
//        universe.characterIndex.add(companionNode, "name", companion);
//        return companionNode;
//    }
//
//    private boolean companionExists(String companion, DoctorWhoUniverse universe) {
//        return getCompanion(companion, universe) != null ? true : false;
//    }
//
//    private Node getCompanion(String companion, DoctorWhoUniverse universe) {
//        Iterable<Relationship> relationships = universe.theDoctor().getRelationships(DoctorWhoUniverse.COMPANION_OF, Direction.INCOMING);
//        for (Relationship r : relationships) {
//            Node currentNode = r.getStartNode();
//            if (currentNode.getProperty("name").equals(companion)) {
//                return currentNode;
//            }
//        }
//        return null;
//    }

    private Node ensureDoctorActorInDb(String doctorActor, DoctorWhoUniverse universe) {
        Node theDoctor = universe.theDoctor();
        Iterable<Relationship> relationships = theDoctor.getRelationships(DoctorWhoUniverse.PLAYED, Direction.INCOMING);

        for (Relationship r : relationships) {
            Node current = r.getStartNode();
            if (current.getProperty("actor").equals(doctorActor)) {
                return current;
            }
        }

        Node doctorActorNode = universe.getDatabase().createNode();
        doctorActorNode.setProperty("actor", doctorActor);
        doctorActorNode.createRelationshipTo(theDoctor, DoctorWhoUniverse.PLAYED);
        return doctorActorNode;
    }
}
