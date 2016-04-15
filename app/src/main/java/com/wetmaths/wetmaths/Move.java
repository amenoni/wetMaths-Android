package com.wetmaths.wetmaths;

import android.util.Log;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amenoni on 4/4/16.
 */
public class Move {

    public static final Integer FIRST_PLAYER = 1;
    public static final Integer SECOND_PLAYER = 2;
    public static final Integer THIRD_PLAYER = 3;
    private static final String TAG = "Move";

    private Game mGame;
    private Integer mPlayer;
    private String mDate;
    private Integer mValue;
    private Integer mScoreP1;
    private Integer mScoreP2;
    private Integer mScoreP3;

    public Move(){
    }

    public Move(Game game,JSONObject moveJson) {
        this.setGame(game);
        try {
            this.setPlayer(moveJson.getInt("player"));
            this.setDate(moveJson.getString("time"));
            this.setValue(moveJson.getInt("value"));
            this.setScoreP1(moveJson.getInt("scoreP1"));
            this.setScoreP2(moveJson.getInt("scoreP2"));
            this.setScoreP3(moveJson.getInt("scoreP3"));
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }



    }

    public Game getGame() {
        return mGame;
    }

    public Integer getPlayer() {
        return mPlayer;
    }

    public String getDate() {
        return mDate;
    }

    public Integer getValue() {
        return mValue;
    }

    public Integer getScoreP1() {
        return mScoreP1;
    }

    public Integer getScoreP2() {
        return mScoreP2;
    }

    public Integer getScoreP3() {
        return mScoreP3;
    }

    public void setGame(Game game) {
        this.mGame = game;
    }

    public void setPlayer(Integer player) {
        this.mPlayer = player;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public void setValue(Integer value) {
        this.mValue = value;
    }

    public void setScoreP1(Integer scoreP1) {
        this.mScoreP1 = scoreP1;
    }

    public void setScoreP2(Integer scoreP2) {
        this.mScoreP2 = scoreP2;
    }

    public void setScoreP3(Integer scoreP3) {
        this.mScoreP3 = scoreP3;
    }


    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String, String>();

        if(this.mGame != null){
            map.put("game_id",String.valueOf(this.mGame.getId()));
        }

        if(this.mPlayer != null){
            map.put("player",String.valueOf(this.mPlayer));
        }

        if(this.mDate != null){
            map.put("time",mDate);
        }

        if(this.mValue != null){
            map.put("value",String.valueOf(mValue));
        }

        if(this.mScoreP1 != null){
            map.put("scoreP1",String.valueOf(this.mScoreP1));
        }

        if(this.mScoreP2 != null){
            map.put("scoreP2",String.valueOf(this.mScoreP2));
        }

        if(this.mScoreP3 != null){
            map.put("scoreP3",String.valueOf(this.mScoreP3));
        }

        return map;
    }

}
