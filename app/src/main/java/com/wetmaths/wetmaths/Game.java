package com.wetmaths.wetmaths;


import android.support.annotation.NonNull;
import android.util.Log;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by amenoni on 20/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {

    private final static String TAG = "Game";

    public final static String STATUS_FINISHED = "f";
    public final static String STATUS_NOT_STARTED = "n";
    public final static String STATUS_STARTED = "s";

    private int id;
    private String player1;
    private String player2;
    private String player3;
    private String started_at;
    private String ended_at;
    private String status;
    private String winner;


    public Game(){

    }

    public Game(JSONObject gameJson) {
        try {
            this.setId(gameJson.getInt("pk"));
            this.setPlayer1(gameJson.getString("player1"));
            this.setPlayer2(gameJson.getString("player2"));
            this.setPlayer3(gameJson.getString("player3"));
            this.setStarted_at(gameJson.getString("started_at"));
            this.setEnded_at(gameJson.getString("ended_at"));
            this.setWinner(gameJson.getString("winner"));
            this.setStatus(gameJson.getString("status"));

        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getEnded_at() {
        return ended_at;
    }

    public String getStatus() {
        return status;
    }

    public String getWinner() {
        return winner;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public void setEnded_at(String ended_at) {
        this.ended_at = ended_at;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }


    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String,String>();
        if(this.id != 0){
            map.put("pk",String.valueOf(this.id));
        }

        if(this.started_at != null){
            if(!this.started_at.equals("null")){
                map.put("started_at",this.started_at);
            }

        }

        if(this.player1 != null){
            if(!this.player1.equals("null")){
                map.put("player1",this.player1);
            }
        }

        if(player2 != null){
            if(!this.player2.equals("null")){
                map.put("player2",this.player2);
            }
        }

        if (player3 != null){
            if(!this.player3.equals("null")){
                map.put("player3",this.player3);
            }
        }

        if (ended_at != null){
            if (!this.ended_at.equals("null")){
                map.put("ended_at",this.ended_at);
            }

        }

        if(status != null){
            if(!this.status.equals("null")){
                map.put("status",this.status);
            }
        }

        if(winner != null){
            if(!this.winner.equals("null")){
                map.put("winner",this.winner);
            }

        }


        return map;
    }

}
