package org.neo4j.tutorial.server.rest.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EpisodeSearchResults implements Iterable<EpisodeSearchResult>
{
    private List<EpisodeSearchResult> results;

    @SuppressWarnings("unchecked")
    public EpisodeSearchResults(List<Map<String, Object>> json)
    {
        super();
        this.results = new ArrayList<EpisodeSearchResult>();
        for (Map<String, Object> result : json)
        {
            results.add(new EpisodeSearchResult((List<Map<String, Object>>) result.get("nodes")));
        }
    }

    public Iterator<EpisodeSearchResult> iterator()
    {
        return results.iterator();
    }
}
