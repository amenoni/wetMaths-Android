package com.wetmaths.wetmaths;


import android.support.annotation.NonNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by amenoni on 20/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {

    private int id;
    private String player1;
    private String player2;
    private String player3;
    private String started_at;
    private String ended_at;
    private String status;
    private String winner;


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
            map.put("id",String.valueOf(this.id));
        }

        if(this.started_at != null){
            map.put("started_at",this.started_at);
        }

        if(this.player1 != null){
            map.put("player1",this.player1);
        }

        if(player2 != null){
            map.put("player2",this.player2);
        }

        if (player3 != null){
            map.put("player3",this.player3);
        }

        if (ended_at != null){
            map.put("ended_at",this.ended_at);
        }

        if(status != null){
            map.put("status",this.status);
        }

        if(winner != null){
            map.put("winner",this.winner);
        }


        return map;
    }
}
