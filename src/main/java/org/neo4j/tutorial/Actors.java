package org.neo4j.tutorial;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

public class Actors {

    private Node character;
    private Set<Actor> actors = new HashSet<Actor>();

    @SuppressWarnings("unchecked")
    public Actors(Node character, File data) {
        this.character = character;
        ObjectMapper m = new ObjectMapper();
        try {
            List<List<String>> actorsList = m.readValue(data, List.class);
            for (List<String> actorCandidate : actorsList) {
                actors.add(new Actor(actorCandidate));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertAndIndex(GraphDatabaseService db, Index<Node> actorIndex) {
        Transaction tx = db.beginTx();
        try {
            for (Actor actor : actors) {
                Node actorNode = db.createNode();
                actorNode.setProperty("firstname", actor.firstName);
                actorNode.setProperty("lastname", actor.lastName);

                actorNode.createRelationshipTo(character, DoctorWhoUniverse.PLAYED);

                actorIndex.add(actorNode, "lastname", actor.lastName);

            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private static class Actor {
        public String firstName;
        public String lastName;

        public Actor(List<String> seralisedActor) {
            if (seralisedActor == null || seralisedActor.size() != 2) {
                throw new RuntimeException(
                        "Actors are only accepted as JSON lists of lists of firstname, lastname. E.g. [[\"Billy\", \"Bragg\"], [\"Joe\", \"Strummer\"]]");
            }
            firstName = seralisedActor.get(0);
            lastName = seralisedActor.get(1);
        }
    }
}
