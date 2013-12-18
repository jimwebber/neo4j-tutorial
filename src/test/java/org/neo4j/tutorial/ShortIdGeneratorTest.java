package org.neo4j.tutorial;

import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShortIdGeneratorTest
{
    @Test
    public void shouldGenerateUniqueIdsWithinReason() throws Exception
    {
        // given
        final int numberOfIds = 32;
        HashSet<String> ids = new HashSet<>();

        // when
        for ( int i = 0; i < numberOfIds; i++ )
        {
            ids.add( ShortIdGenerator.shortId( "some-prefix" ) );
        }

        // then
        assertEquals(numberOfIds, ids.size());
    }
}
