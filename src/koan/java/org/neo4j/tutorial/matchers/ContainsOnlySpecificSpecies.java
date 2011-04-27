package org.neo4j.tutorial.matchers;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

public class ContainsOnlySpecificSpecies extends TypeSafeMatcher<IndexHits<Node>> {

	private final Map<String, Boolean> species;
	
	public ContainsOnlySpecificSpecies(String[] speciesNames) {
		this.species = new HashMap<String, Boolean>();
		for (String name : speciesNames){
			species.put(name, false);
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Checks whether each member in the supplied list of species is present in the presented arguments.");
	}

	@Override
	public boolean matchesSafely(IndexHits<Node> indexHits) {

        for (Node n : indexHits) {
            String property = (String) n.getProperty("species");
            
            if (!species.containsKey(property)){
            	return false;
            }
        
        	species.put(property, true);
        }

        return !species.containsValue(false);
	}
	
	@Factory
    public static <T> Matcher<IndexHits<Node>> containsOnly(String... speciesNames) {
      return new ContainsOnlySpecificSpecies(speciesNames);
    }

}
