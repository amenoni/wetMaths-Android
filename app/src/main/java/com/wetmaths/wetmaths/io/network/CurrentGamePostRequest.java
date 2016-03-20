package com.wetmaths.wetmaths.io.network;

import android.net.Uri;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import com.wetmaths.wetmaths.Game;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by amenoni on 20/3/16.
 */
public class CurrentGamePostRequest extends SpringAndroidSpiceRequest<Boolean> {

    Game mGame;
    String mArduinoUrl;

    public CurrentGamePostRequest(String arduinoUrl,Game game){
        super(Boolean.class);
        this.mArduinoUrl = arduinoUrl;
        this.mGame = game;
    }

    @Override
    public Boolean loadDataFromNetwork() throws Exception {
        String url = String.format("%s/api/game/", mArduinoUrl);

        if (mGame != null){
            Map<String, String> record = mGame.toMap();
            getRestTemplate().postForLocation(url, record);

            return true;
        }else {

            return false;
        }

    }






}
