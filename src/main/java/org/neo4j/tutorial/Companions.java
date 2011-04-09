package org.neo4j.tutorial;

import java.io.File;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

public class Companions {

    private List<String> companionsList;

    @SuppressWarnings("unchecked")
    public Companions(File data) {
        ObjectMapper m = new ObjectMapper();
        try {
            companionsList = m.readValue(data, List.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertAndIndex(GraphDatabaseService db, Index<Node> friendliesIndex, Index<Node> companionsIndex) {
        Transaction tx = db.beginTx();
        try {

            Node theDoctor = friendliesIndex.get("name", "Doctor").getSingle();

            for (String companion : companionsList) {
                Node companionNode = friendliesIndex.get("name", companion).getSingle();
                if (companionNode != null) {
                    companionNode.createRelationshipTo(theDoctor, DoctorWhoUniverse.COMPANION_OF);
                    companionsIndex.add(companionNode, "name", companion);
                } else {
                    throw new RuntimeException(String.format(
                            "Companion [%s] is not know as a friendly in the Doctor Who universe, unable to add COMPANION_OF to the Doctor node with id [%d]",
                            companion, theDoctor.getId()));
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }
}
