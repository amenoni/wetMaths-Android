package com.wetmaths.wetmaths;

import java.util.Date;

/**
 * Created by amenoni on 4/4/16.
 */
public class Move {

    public static final Integer FIRST_PLAYER = 1;
    public static final Integer SECOND_PLAYER = 2;
    public static final Integer THIRD_PLAYER = 3;

    private Game mGame;
    private Integer mPlayer;
    private Date mDate;
    private Integer mValue;
    private Integer mScoreP1;
    private Integer mScoreP2;
    private Integer mScoreP3;



    public Game getGame() {
        return mGame;
    }

    public Integer getPlayer() {
        return mPlayer;
    }

    public Date getDate() {
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

    public void setDate(Date date) {
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
}
