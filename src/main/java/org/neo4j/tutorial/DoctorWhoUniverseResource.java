package org.neo4j.tutorial;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;


/**
 * @author Stefan Armbruster
 */
public class DoctorWhoUniverseResource extends Neo4jEmbeddedResource {

    public DoctorWhoUniverseResource() {
        super(new DoctorWhoUniverseGenerator());
    }

    public Node theDoctor() {
        try (Transaction tx = getGraphDatabaseService().beginTx()) {
            Node doctor = getGraphDatabaseService().findNode(CHARACTER, "character", "Doctor");
            tx.success();
            return doctor;
        }
    }

}
