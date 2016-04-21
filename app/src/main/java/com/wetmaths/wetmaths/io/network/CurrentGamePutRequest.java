package com.wetmaths.wetmaths.io.network;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import com.wetmaths.wetmaths.Game;

import java.util.Map;

/**
 * Created by amenoni on 4/4/16.
 */
public class CurrentGamePutRequest extends SpringAndroidSpiceRequest<Boolean> {
    Game mGame;
    String mArduinoUrl;

    public CurrentGamePutRequest(String arduinoUrl, Game game) {
        super(Boolean.class);
        this.mArduinoUrl = arduinoUrl;
        this.mGame = game;
    }

    @Override
    public Boolean loadDataFromNetwork() throws Exception {


        if (mGame != null) {
            String url = String.format("%s/api/game/%s/", mArduinoUrl,mGame.getId());
            Map<String, String> record = mGame.toMap();
            getRestTemplate().put(url, record);

            return true;
        } else {

            return false;
        }


    }
}
