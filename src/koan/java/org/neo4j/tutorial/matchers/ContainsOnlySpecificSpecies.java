package org.neo4j.tutorial.matchers;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

public class ContainsOnlySpecificSpecies extends TypeSafeMatcher<IndexHits<Node>> {

    private final Set<String> species;

    public ContainsOnlySpecificSpecies(String... speciesNames) {
        this.species = new HashSet<String>();
        for (String name : speciesNames) {
            species.add(name);
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Checks whether each member in the supplied list of species is present in the presented arguments.");
    }

    @Override
    public boolean matchesSafely(IndexHits<Node> indexHits) {
        
        for (Node n : indexHits) {
            String property = String.valueOf(n.getProperty("species"));
 
            if (!species.contains(property)) {
                return false;
            } 
            species.remove(property);
        }

        return species.size() == 0;
    }

    @Factory
    public static <T> Matcher<IndexHits<Node>> containsOnly(String... speciesNames) {
        return new ContainsOnlySpecificSpecies(speciesNames);
    }
}
