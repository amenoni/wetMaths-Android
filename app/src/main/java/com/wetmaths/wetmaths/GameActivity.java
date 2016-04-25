package com.wetmaths.wetmaths;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.wetmaths.wetmaths.io.network.MovePostRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private static final String GAME_ID_EXTRA = "gameId";
    private static final String PLAYER_POSITION_EXTRA = "playerPosition";
    private static final String PLAYER_NAME_EXTRA = "playerName";
    private static final String DEVICE_URL_EXTRA = "deviceURL";
    private static final int TIMER_VALUE = 5;
    public static final String FIREBASE_GAME_KEY = "game_update";
    public static final String FIREBASE_MOVE_KEY = "moves";

    private Firebase mFirebase;

    protected SpiceManager mSpiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    private TextView mNamePlayer1;
    private TextView mNamePlayer2;
    private TextView mNamePlayer3;
    private TextView mFirstOperand;
    private TextView mSecondOperand;
    private TextView mTimer;
    private TextView mResult;
    private TextView mScorePlayer1;
    private TextView mScorePlayer2;
    private TextView mScorePlayer3;
    private ArrayList<TextView> mScoreBoard;
    private Button mStartButton;
    private Button mSendButton;
    private Button mPad0;
    private Button mPad1;
    private Button mPad2;
    private Button mPad3;
    private Button mPad4;
    private Button mPad5;
    private Button mPad6;
    private Button mPad7;
    private Button mPad8;
    private Button mPad9;
    private Button mBack;

    private String mPlayerName;
    private int mGameId;
    private int mPlayerPosition;
    private String mDeviceURL;
    private Game mGame;
    private String mCurrentStatus = Game.STATUS_NOT_STARTED;

    private Handler mTimeHandler;
    private Runnable mTimerRunable;

    private PadButtonOnClickListener mPadButtonOnClickListener = new PadButtonOnClickListener();

    public Intent newIntent (Context context,Game game, String playerName, int playerPosition,String deviceURL){
        Intent intent = new Intent(context,GameActivity.class);
        intent.putExtra(GAME_ID_EXTRA, game.getId() );
        intent.putExtra(PLAYER_POSITION_EXTRA,playerPosition);
        intent.putExtra(PLAYER_NAME_EXTRA,playerName);
        intent.putExtra(DEVICE_URL_EXTRA,deviceURL);
        return intent;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        mPlayerName = bundle.getString(PLAYER_NAME_EXTRA);
        mGameId = bundle.getInt(GAME_ID_EXTRA);
        mPlayerPosition = bundle.getInt(PLAYER_POSITION_EXTRA);
        mDeviceURL = bundle.getString(DEVICE_URL_EXTRA);
        mNamePlayer1 = (TextView) findViewById(R.id.namePlayer1);
        mNamePlayer2 = (TextView) findViewById(R.id.namePlayer2);
        mNamePlayer3 = (TextView) findViewById(R.id.namePlayer3);
        mFirstOperand = (TextView) findViewById(R.id.firstOperand);
        mSecondOperand = (TextView) findViewById(R.id.secondOperand);
        mTimer = (TextView) findViewById(R.id.timer);
        mResult = (TextView) findViewById(R.id.result);
        mScorePlayer1 = (TextView) findViewById(R.id.scorePlayer1);
        mScorePlayer2 = (TextView) findViewById(R.id.scorePlayer2);
        mScorePlayer3 = (TextView) findViewById(R.id.scorePlayer3);
        mScoreBoard = new ArrayList<TextView>();
        mScoreBoard.add(mScorePlayer1);
        mScoreBoard.add(mScorePlayer2);
        mScoreBoard.add(mScorePlayer3);
        mStartButton = (Button) findViewById(R.id.start);
        mSendButton = (Button) findViewById(R.id.send);
        mPad0 = (Button) findViewById(R.id.zero);
        mPad1 = (Button) findViewById(R.id.one);
        mPad2 = (Button) findViewById(R.id.two);
        mPad3 = (Button) findViewById(R.id.three);
        mPad4 = (Button) findViewById(R.id.four);
        mPad5 = (Button) findViewById(R.id.five);
        mPad6 = (Button) findViewById(R.id.six);
        mPad7 = (Button) findViewById(R.id.seven);
        mPad8 = (Button) findViewById(R.id.eight);
        mPad9 = (Button) findViewById(R.id.nine);
        mBack = (Button)findViewById(R.id.back);

        mSpiceManager.start(this);

        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://resplendent-torch-6152.firebaseio.com/");

        mFirebase.child("game_" + String.valueOf(mGameId)).addValueEventListener(new gameValueEventListener());

        mTimeHandler = new Handler();
        mTimerRunable = new Runnable() {
            @Override
            public void run() {
                int time =  Integer.parseInt(mTimer.getText().toString());
                if(time == 0){
                    timeIsOver();
                }else {
                    mTimer.setText(String.valueOf(time - 1));
                    mTimeHandler.postDelayed(mTimerRunable,1000);
                }
            }
        };

        mStartButton.setOnClickListener(new StartGameOnClickListener());

        switch (mPlayerPosition){
            case 1:
                mNamePlayer1.setText(mPlayerName);
                break;
            case 2:
                mNamePlayer2.setText(mPlayerName);
                break;
            case 3:
                mNamePlayer3.setText(mPlayerName);
                break;
        }

        mPad0.setOnClickListener(mPadButtonOnClickListener);
        mPad1.setOnClickListener(mPadButtonOnClickListener);
        mPad2.setOnClickListener(mPadButtonOnClickListener);
        mPad3.setOnClickListener(mPadButtonOnClickListener);
        mPad4.setOnClickListener(mPadButtonOnClickListener);
        mPad5.setOnClickListener(mPadButtonOnClickListener);
        mPad6.setOnClickListener(mPadButtonOnClickListener);
        mPad7.setOnClickListener(mPadButtonOnClickListener);
        mPad8.setOnClickListener(mPadButtonOnClickListener);
        mPad9.setOnClickListener(mPadButtonOnClickListener);
        mBack.setOnClickListener(new BackButtonOnClickListener());

        mSendButton.setOnClickListener(new SendButtonOnClickListener());
    }

    private class gameValueEventListener implements ValueEventListener{
        private static final String STARTED_AT_KEY = "started_at";
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //String FirstKey = dataSnapshot.getKey();
            //String FirstValue = dataSnapshot.getValue().toString();

            Log.d(TAG,"New Message recived");

            if(dataSnapshot.hasChildren()){
                Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                for (DataSnapshot children : childrens){
                    //aca esta el tipo de mensaje que tenemos que evaluar en un case
                    String ChildrenKey = children.getKey();
                    Log.d(TAG,"Children KEY " +ChildrenKey);
                    //aca tiene que estar el objeto formateado en json
                    String ChildrenValue = children.getValue().toString();
                    Log.d(TAG, "Children VALUE " + ChildrenValue);

                    //second level of childrens
                    for(DataSnapshot secondChild : children.getChildren()){
                        Log.d(TAG, "Second Children Key" + secondChild.getKey());
                        Log.d(TAG, "Second Children Value" + secondChild.getValue());
                        try {
                            JSONArray jsonArray = new JSONArray(secondChild.getValue().toString());
                            JSONObject object = jsonArray.getJSONObject(0);
                            JSONObject json = (JSONObject) object.get("fields");
                            json.put("pk",object.get("pk"));
                            switch (ChildrenKey){
                                case FIREBASE_GAME_KEY:
                                    ProcessGameUpdate(json);
                                    break;
                                case FIREBASE_MOVE_KEY:
                                    ProcessMoveUpdate(json);
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }
            }


        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }


    private void ProcessGameUpdate(JSONObject gameJson){
        mGame = new Game(gameJson);
        mNamePlayer1.setText(mGame.getPlayer1());
        mNamePlayer2.setText(mGame.getPlayer2());
        mNamePlayer3.setText(mGame.getPlayer3());

        if(mGame.getStatus().equals(Game.STATUS_FINISHED)){
            endGame();
        }else if (mCurrentStatus.equals(Game.STATUS_NOT_STARTED)){
            //I am the first player?
            if(mPlayerName.equals(mGame.getPlayer1())){
                mSendButton.setVisibility(View.GONE);
                mStartButton.setVisibility(View.VISIBLE);
                if(!mGame.getPlayer2().equals("null") && !mGame.getPlayer3().equals("null") && !mGame.getPlayer2().isEmpty() && !mGame.getPlayer3().isEmpty()){
                    mStartButton.setEnabled(true);
                }
            }else {
                mStartButton.setVisibility(View.GONE);
                mSendButton.setVisibility(View.VISIBLE);
                mSendButton.setEnabled(false);
            }
        }


        //GAME STARTED RECIVED!!
        if(mCurrentStatus.equals(Game.STATUS_NOT_STARTED) && mGame.getStatus().equals(Game.STATUS_STARTED)){
            mStartButton.setVisibility(View.GONE);
            mSendButton.setVisibility(View.VISIBLE);
            mSendButton.setEnabled(true);
            mCurrentStatus = Game.STATUS_STARTED;
            startMove();
        }


    }

    private void ProcessMoveUpdate(JSONObject moveJson){
        Move move = new Move(mGame,moveJson);
        this.mScorePlayer1.setText(move.getScoreP1().toString());
        this.mScorePlayer2.setText(move.getScoreP2().toString());
        this.mScorePlayer3.setText(move.getScoreP3().toString());
        UpdateScoreBoard();

        //If this player lose we finish the activity
        if (Integer.parseInt(mScoreBoard.get(mPlayerPosition-1).getText().toString()) <= 0){
            endGame();
        }

    }


    private void startMove(){
        Random random = new Random();
        int firstOperand = random.nextInt(9);
        int secondOperand = random.nextInt(9);
        mFirstOperand.setText(String.valueOf(firstOperand));
        mSecondOperand.setText(String.valueOf(secondOperand));
        mResult.setText("");
        resetTimer();
    }

    private void resetTimer(){
        mTimer.setText(String.valueOf(TIMER_VALUE));
        mTimeHandler.removeCallbacks(mTimerRunable);
        mTimeHandler.postDelayed(mTimerRunable, 1000);
    }

    private void timeIsOver(){
        Move move = new Move();
        move.setGame(mGame);
        move.setPlayer(mPlayerPosition);
        move.setValue(TIMER_VALUE * 3);
        MovePostRequest movePostRequest = new MovePostRequest(mDeviceURL,move);
        mSpiceManager.execute(movePostRequest,new MovePostRequestListener());
        startMove();
    }

    private class StartGameOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            mGame.setStatus(Game.STATUS_STARTED);
            CurrentGamePutRequest currentGamePutRequest = new CurrentGamePutRequest(mDeviceURL,mGame);
            mSpiceManager.execute(currentGamePutRequest, new GamesPutRequestListener());
        }
    }


    private class GamesPutRequestListener implements RequestListener<Boolean> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), getString(R.string.request_faliure), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(Boolean result) {

        }
    }

    private class PadButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            mResult.setText(mResult.getText().toString() + button.getText());
        }
    }

    private class BackButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String resultText = mResult.getText().toString();
            if(! resultText.isEmpty()){
                mResult.setText(resultText.substring(0, resultText.length()-1));
            }
        }
    }

    private class SendButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(!mResult.getText().toString().isEmpty()){
                int firstOperand = Integer.parseInt(mFirstOperand.getText().toString());
                int secondOperand = Integer.parseInt(mSecondOperand.getText().toString());
                int answer = Integer.parseInt(mResult.getText().toString());

                int operationResult = firstOperand * secondOperand;
                if (operationResult == answer){
                    Toast.makeText(getApplicationContext(),getString(R.string.nice),Toast.LENGTH_SHORT).show();
                    Move move = new Move();
                    move.setGame(mGame);
                    move.setPlayer(mPlayerPosition);
                    move.setValue(TIMER_VALUE - Integer.parseInt(mTimer.getText().toString()));

                    MovePostRequest movePostRequest = new MovePostRequest(mDeviceURL,move);
                    mSpiceManager.execute(movePostRequest,new MovePostRequestListener());

                    startMove();
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.wrong),Toast.LENGTH_SHORT).show();
                    mResult.setText("");
                }
            }
        }
    }

    private class MovePostRequestListener implements RequestListener<Boolean>{
        @Override
        public void onRequestSuccess(Boolean aBoolean) {

        }

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(),getString(R.string.request_faliure),Toast.LENGTH_SHORT).show();

        }
    }

    private void UpdateScoreBoard(){
        for (TextView textView : mScoreBoard){
            int score = Integer.parseInt(textView.getText().toString());
            if (score <= 0){
                textView.setBackgroundColor(Color.RED);
            }else if (score <= 20){
                textView.setBackgroundColor(Color.YELLOW);
            }
        }
    }

    private void endGame(){
        mTimeHandler.removeCallbacks(mTimerRunable);
        finish();
    }
}
