package com.wetmaths.wetmaths;




import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amenoni on 20/3/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameList {
    @JsonProperty("objects")
    private List<Game> games;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
