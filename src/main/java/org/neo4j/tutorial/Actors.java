package org.neo4j.tutorial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

public class Actors {

    private Node character;
    protected List<Actor> actors = new ArrayList<Actor>();

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
        insertAndIndex(db, actorIndex, null);
    }
    
    public void insertAndIndex(GraphDatabaseService db, Index<Node> actorIndex, RelationshipType linkBetweenActors) {
        Transaction tx = db.beginTx();
        try {
            Node previousActor = null;
            for (Actor actor : actors) {
                Node currentActor = db.createNode();
                currentActor.setProperty("firstname", actor.firstName);
                currentActor.setProperty("lastname", actor.lastName);

                currentActor.createRelationshipTo(character, DoctorWhoUniverse.PLAYED);

                actorIndex.add(currentActor, "lastname", actor.lastName);
                
                if(linkBetweenActors != null && previousActor != null) {
                    previousActor.createRelationshipTo(currentActor, linkBetweenActors);
                }
                
                previousActor = currentActor;
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    static class Actor {
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
