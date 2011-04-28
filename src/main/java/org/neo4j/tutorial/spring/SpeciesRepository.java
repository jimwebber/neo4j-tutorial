package org.neo4j.tutorial.spring;

import org.springframework.data.graph.neo4j.repository.GraphRepository;

public interface SpeciesRepository extends GraphRepository<SpeciesEntity> {

}
