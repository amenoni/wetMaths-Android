package com.wetmaths.wetmaths.io.network;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import com.wetmaths.wetmaths.GameList;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.Random;

/**
 * Created by amenoni on 20/3/16.
 */
public class GameListRequest extends SpringAndroidSpiceRequest<GameList> {

    String mArduinoUrl;

    public GameListRequest(String arduinoUrl){
        super(GameList.class);
        this.mArduinoUrl = arduinoUrl;
    }

    @Override
    public GameList loadDataFromNetwork() throws Exception {
        String url = String.format("%s/api/game/?format=json", mArduinoUrl);
        return getRestTemplate().getForObject(url,GameList.class);
    }


    /**
     * This method generates a unique cache key for this request. In this case
     * our cache key depends just on the keyword.
     * @return
     */
    public String createCacheKey() {
        return "game." + mArduinoUrl;
    }
}
