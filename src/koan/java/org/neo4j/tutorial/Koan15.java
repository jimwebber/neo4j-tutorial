package org.neo4j.tutorial;

import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import scala.collection.convert.Wrappers;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.helpers.collection.IteratorUtil.asSet;

/**
 * In this koan we learn about breaking apart queries using WITH
 */
public class Koan15
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldSortCompanionsAlphabeticallyIntoACollection() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = "MATCH (c:Character)-[:COMPANION_OF]->(:Character {character: 'Doctor'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "WITH c\n" +
                "ORDER BY c.character\n" +
                "RETURN collect(c.character) AS characters\n";

        // SNIPPET_END

        Result result = db.execute( cql );

        assertThat( result, containsOrderedList( "Ace", "Adam Mitchell", "Adelaide Brooke", "Adric", "Amy Pond",
                "Astrid Peth", "Barbara Wright", "Ben Jackson", "Clara Oswald", "Craig Owens", "Dodo Chaplet",
                "Donna Noble", "Grace Holloway", "Hamish Wilson", "Harry Sullivan", "Ian Chesterton",
                "Jack Harkness", "Jackson Lake", "Jamie McCrimmon", "Jo Grant", "K9", "Kamelion", "Katarina",
                "Lady Christina de Souza", "Leela", "Liz Shaw", "Martha Jones", "Melanie Bush", "Mickey Smith",
                "Nyssa", "Peri Brown", "Polly", "River Song", "Romana", "Rory Williams", "Rose Tyler",
                "Rosita Farisi", "Sara Kingdom", "Sarah Jane Smith", "Steven Taylor", "Susan Foreman",
                "Tegan Jovanka", "Vicki", "Victoria Waterfield", "Vislor Turlough", "Wilfred Mott", "Zoe Heriot" ) );
    }

    @Test
    public void shouldFindThePopularCompanionsWhoApprearedMoreThanTwentyTimes() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = "MATCH (ep:Episode)<-[:APPEARED_IN]-(companion:Character)" +
                "-[:COMPANION_OF]->(:Character {character: 'Doctor'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "WITH companion, count(ep) AS numberOfEpisodes\n" +
                "WHERE numberOfEpisodes > 20\n" +
                "RETURN companion.character AS companions";

        // SNIPPET_END

        Result result = db.execute( cql );

        assertThat( result, containsOnlyCompanions( "Rory Williams", "Amy Pond", "Sarah Jane Smith", "Rose Tyler",
                "Jamie McCrimmon" ) );
    }

    @Test
    public void shouldFindTheNumberOfRegenerationsInTotalForTheDoctor() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        /* Can't just count (actor)-[:PLAYED]->(doctor) relationships because of Richard Hurndall */

        // Hint: think about the structure of the first and last doctors

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (doc:Character {character:'Doctor'})<-[:PLAYED]-(first:Actor)-[:REGENERATED_TO]->(),\n" +
                "(doc)<-[:PLAYED]-(last:Actor)<-[:REGENERATED_TO]-() \n" +
                "WHERE not((first)<-[:REGENERATED_TO]-()) AND not(last-[:REGENERATED_TO]->())\n" +
                "WITH first,last\n" +
                "MATCH path = (first)-[:REGENERATED_TO*]->(last)\n" +
                "RETURN LENGTH(path) as regenerations";

        // SNIPPET_END

        Result result = db.execute( cql );

        assertEquals( 12, result.columnAs( "regenerations" ).next() );
    }

    private TypeSafeMatcher<Result> containsOrderedList( final String... companions )
    {
        return new TypeSafeMatcher<Result>()
        {
            @Override
            protected boolean matchesSafely( Result result )
            {
                Wrappers.SeqWrapper characters = (Wrappers.SeqWrapper) result.columnAs( "characters" ).next();

                for ( int i = 0; i < companions.length; i++ )
                {
                    if ( !companions[i].equals( characters.get( i ) ) )
                    {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "" );
            }
        };
    }

    private TypeSafeMatcher<Result> containsOnlyCompanions( final String... companions )
    {
        return new TypeSafeMatcher<Result>()
        {
            private final Set<String> theCompanions = asSet( companions );

            @Override
            protected boolean matchesSafely( Result result )
            {

                for ( Object o : asIterable( result.columnAs( "companions" ) ) )
                {
                    if ( !theCompanions.remove( o ) )
                    {
                        return false;
                    }
                }

                return theCompanions.size() == 0;
            }

            @Override
            public void describeTo( Description description )
            {

            }
        };
    }
}
