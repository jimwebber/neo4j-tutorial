package org.neo4j.tutorial.server.rest.domain;

import java.util.List;
import java.util.Map;

public class EpisodeSearchResult
{
    private final List<Map<String, Object>> nodes;

    public EpisodeSearchResult(List<Map<String, Object>> nodes)
    {
        super();
        this.nodes = nodes;
    }

    @SuppressWarnings("unchecked")
    public String getActor()
    {
        Map<String, Object> actor = (Map<String, Object>) nodes.get(1)
                                                               .get("data");
        return (String) actor.get("actor");
    }

    @SuppressWarnings("unchecked")
    public String getEpisode()
    {
        Map<String, Object> actor = (Map<String, Object>) nodes.get(2)
                                                               .get("data");
        return (String) actor.get("title");
    }

}
