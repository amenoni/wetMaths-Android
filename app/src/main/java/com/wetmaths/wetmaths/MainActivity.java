package com.wetmaths.wetmaths;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.wetmaths.wetmaths.io.network.CurrentGamePostRequest;
import com.wetmaths.wetmaths.io.network.CurrentGamePutRequest;
import com.wetmaths.wetmaths.io.network.CurrentGameRequest;
import com.wetmaths.wetmaths.io.network.GameListRequest;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button mPerformRequestbtn;
    private TextView mDeviceUrl;
    private TextView mPlayerName;
    private TextView mGameId;
    private LinearLayout mJoinGame;
    private LinearLayout mCreateGame;
    private Button mCreateNewGame;
    private Button mSetAsPlayer2;
    private Button mSetAsPlayer3;
    private TextView mFirstPlayer;
    private TextView mSecondPlayer;
    private TextView mThirdPlayer;

    protected SpiceManager mSpiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    private Game mGame;
    private int mPlayerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPerformRequestbtn = (Button) findViewById(R.id.requestbtn);
        mDeviceUrl = (TextView) findViewById(R.id.url);
        mPlayerName = (TextView) findViewById(R.id.playerName);
        mJoinGame = (LinearLayout) findViewById(R.id.joinGame);
        mCreateGame = (LinearLayout) findViewById(R.id.createGame);
        mCreateNewGame = (Button) findViewById(R.id.createNewGame);
        mGameId = (TextView) findViewById(R.id.gameid);
        mSetAsPlayer2 = (Button) findViewById(R.id.set_p2);
        mSetAsPlayer2.setOnClickListener(new SetPlayerAsButtonOnClickListener());
        mSetAsPlayer3 = (Button) findViewById(R.id.set_p3);
        mSetAsPlayer3.setOnClickListener(new SetPlayerAsButtonOnClickListener());
        mFirstPlayer = (TextView) findViewById(R.id.firstPlayerName);
        mSecondPlayer = (TextView) findViewById(R.id.secondPlayerName);
        mThirdPlayer = (TextView) findViewById(R.id.thirdPlayerName);

        mSpiceManager.start(this);

        mPerformRequestbtn.setOnClickListener(new ConnectButtonOnClicListener());
        mCreateNewGame.setOnClickListener(new CreateGameButtonOnClickListener());

    }

    @Override
    protected void onStop() {
        //mSpiceManager.shouldStop();
        super.onStop();
    }

    private void CleanScrean(){
        this.mCreateGame.setVisibility(View.GONE);
        this.mGameId.setText("");
        this.mFirstPlayer.setText("");
        this.mSecondPlayer.setText("");
        this.mThirdPlayer.setText("");
        this.mJoinGame.setVisibility(View.GONE);
    }


    private class ConnectButtonOnClicListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            CleanScrean();
            if(!mDeviceUrl.getText().toString().isEmpty() && !mPlayerName.getText().toString().isEmpty()){
                CurrentGameRequest currentGameRequest = new CurrentGameRequest(mDeviceUrl.getText().toString());
                mSpiceManager.execute(currentGameRequest,new ListGamesRequestListener());
            }else {
                Toast.makeText(getApplicationContext(),"You must enter the device url and player name first",Toast.LENGTH_LONG).show();
            }

        }
    }

    private class CreateGameButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(!mDeviceUrl.getText().toString().isEmpty() && !mPlayerName.getText().toString().isEmpty()){
                mGame = new Game();
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                mGame.setStarted_at(sdf.format(date));
                mGame.setStatus("n");
                mGame.setPlayer1(mPlayerName.getText().toString().replace(" ",""));

                mPlayerPosition = 1;

                CurrentGamePostRequest currentGamePostRequest = new CurrentGamePostRequest(mDeviceUrl.getText().toString(),mGame);
                mSpiceManager.execute(currentGamePostRequest,new GamesPostRequestListener());
                CleanScrean();
            }else {
                Toast.makeText(getApplicationContext(),getString(R.string.enterNameAndUrl),Toast.LENGTH_LONG).show();
            }
        }
    }

    private class SetPlayerAsButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
           if(v.getId() == R.id.set_p2){
               mPlayerPosition = 2;
               mGame.setPlayer2(mPlayerName.getText().toString().replace(" ",""));
           }else if(v.getId() == R.id.set_p3){
               mPlayerPosition = 3;
               mGame.setPlayer3(mPlayerName.getText().toString().replace(" ",""));
           }
            //CurrentGamePostRequest currentGamePostRequest = new CurrentGamePostRequest(mDeviceUrl.getText().toString(),mGame);
            //mSpiceManager.execute(currentGamePostRequest,new GamesPostRequestListener());
            CurrentGamePutRequest currentGamePutRequest = new CurrentGamePutRequest(mDeviceUrl.getText().toString(),mGame);
            mSpiceManager.execute(currentGamePutRequest,new GamesPostRequestListener());
            CleanScrean();
        }
    }

    private class GamesPostRequestListener implements RequestListener<Boolean>{
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(),getString(R.string.request_faliure),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(Boolean result) {
            if (result){
                CurrentGameRequest currentGameRequest = new CurrentGameRequest(mDeviceUrl.getText().toString());
                mSpiceManager.execute(currentGameRequest,new GameCreatedRequestListener());

            }else {
                Toast.makeText(getApplicationContext(),getString(R.string.request_faliure),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ListGamesRequestListener implements RequestListener<GameList>{

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(),getString(R.string.request_faliure),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(GameList gamelist) {
            mDeviceUrl.setEnabled(false);
            mPlayerName.setEnabled(false);

            if(gamelist.getGames().isEmpty()){
                mCreateGame.setVisibility(View.VISIBLE);

            }else{
                Game game = gamelist.getGames().get(0);
                mCreateGame.setVisibility(View.GONE);
                mJoinGame.setVisibility(View.VISIBLE);




                mGame = game;

                mGameId.setText(String.valueOf(mGame.getId()));
                mFirstPlayer.setText(mGame.getPlayer1());
                if(mGame.getPlayer2().isEmpty()){
                    mSetAsPlayer2.setVisibility(View.VISIBLE);
                }else {
                    mSecondPlayer.setText(mGame.getPlayer2());
                }

                if(mGame.getPlayer3().isEmpty()){
                    mSetAsPlayer3.setVisibility(View.VISIBLE);
                }else {
                    mThirdPlayer.setText(mGame.getPlayer3());
                }


            }


        }
    }

    private class GameCreatedRequestListener implements RequestListener<GameList>{
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(),getString(R.string.request_faliure),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(GameList gamelist) {
            if( !gamelist.getGames().isEmpty()){
                Game game = gamelist.getGames().get(0);
                mGame = game;
                //Start game activity
                GameActivity gameActivity = new GameActivity();
                Intent gameIntent = gameActivity.newIntent(getApplicationContext(), mGame, mPlayerName.getText().toString().replace(" ",""), mPlayerPosition,mDeviceUrl.getText().toString());
                startActivity(gameIntent);
            }


        }
    }


}
