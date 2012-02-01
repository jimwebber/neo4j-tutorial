package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ContainsOnlySpecificSpecies extends TypeSafeMatcher<Iterable<Node>>
{

    private final Set<String> species;
    private String failedToFindSpecies;
    private boolean matchedSize;

    public ContainsOnlySpecificSpecies(String... speciesNames)
    {
        this.species = new HashSet<String>();
        Collections.addAll(species, speciesNames);
    }

    public void describeTo(Description description)
    {
        if (failedToFindSpecies != null)
        {
            description.appendText(String.format("Failed to find species [%s] in the given species names",
                                                 failedToFindSpecies));
        }

        if (!matchedSize)
        {
            description.appendText(String.format("Mismatched number of species, expected [%d]", species.size()));
        }
    }

    @Override
    public boolean matchesSafely(Iterable<Node> nodes)
    {

        for (Node n : nodes)
        {
            String speciesName = String.valueOf(n.getProperty("species"));

            if (!species.contains(speciesName))
            {
                failedToFindSpecies = speciesName;
                return false;
            }
            species.remove(speciesName);
        }

        return matchedSize = species.size() == 0;
    }

    @Factory
    public static <T> Matcher<Iterable<Node>> containsOnlySpecies(String... speciesNames)
    {
        return new ContainsOnlySpecificSpecies(speciesNames);
    }
}
