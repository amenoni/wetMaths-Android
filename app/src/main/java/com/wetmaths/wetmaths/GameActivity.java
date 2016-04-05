package com.wetmaths.wetmaths;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private static final String GAME_ID_EXTRA = "gameId";
    private static final String PLAYER_POSITION_EXTRA = "playerPosition";
    private static final String PLAYER_NAME_EXTRA = "playerName";
    private static final String DEVICE_URL_EXTRA = "deviceURL";

    private Firebase mFirebase;

    protected SpiceManager mSpiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    private TextView mNamePlayer1;
    private TextView mNamePlayer2;
    private TextView mNamePlayer3;
    private Button mStartButton;
    private Button mSendButton;

    private String mPlayerName;
    private int mGameId;
    private int mPlayerPosition;
    private String mDeviceURL;
    private Game mGame;
    private String mCurrentStatus = Game.STATUS_NOT_STARTED;



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

        mSpiceManager.start(this);

        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://resplendent-torch-6152.firebaseio.com/");

        mFirebase.child("game_" + String.valueOf(mGameId)).addValueEventListener(new gameValueEventListener());

        mNamePlayer1 = (TextView) findViewById(R.id.namePlayer1);
        mNamePlayer2 = (TextView) findViewById(R.id.namePlayer2);
        mNamePlayer3 = (TextView) findViewById(R.id.namePlayer3);
        mStartButton = (Button) findViewById(R.id.start);
        mSendButton = (Button) findViewById(R.id.send);


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
                            JSONObject gameJson = (JSONObject) object.get("fields");
                            gameJson.put("pk",object.get("pk"));
                            ProcessGameUpdate(gameJson);
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
            this.finish();
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
        }


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
            Toast.makeText(getApplicationContext(), "Request faliure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(Boolean result) {

        }
    }

}
