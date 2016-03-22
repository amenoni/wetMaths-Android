package com.wetmaths.wetmaths;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private static final String GAME_ID_EXTRA = "gameId";
    private static final String PLAYER_POSITION_EXTRA = "playerPosition";
    private static final String PLAYER_NAME_EXTRA = "playerName";

    private Firebase mFirebase;


    private TextView mNamePlayer1;
    private TextView mNamePlayer2;
    private TextView mNamePlayer3;

    private String mPlayerName;
    private int mGameId;
    private int mPlayerPosition;



    public Intent newIntent (Context context,Game game, String playerName, int playerPosition){
        Intent intent = new Intent(context,GameActivity.class);
        intent.putExtra(GAME_ID_EXTRA, game.getId() );
        intent.putExtra(PLAYER_POSITION_EXTRA,playerPosition);
        intent.putExtra(PLAYER_NAME_EXTRA,playerName);
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

        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://resplendent-torch-6152.firebaseio.com/");

        mFirebase.child("game_"+ String.valueOf(mGameId)).addValueEventListener(new gameValueEventListener());

        mNamePlayer1 = (TextView) findViewById(R.id.namePlayer1);
        mNamePlayer2 = (TextView) findViewById(R.id.namePlayer2);
        mNamePlayer3 = (TextView) findViewById(R.id.namePlayer3);

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

            if(dataSnapshot.hasChildren()){
                Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                for (DataSnapshot children : childrens){
                    //aca esta el tipo de mensaje que tenemos que evaluar en un case
                    String ChildrenKey = children.getKey();
                    Log.d(TAG,"Children KEY " +ChildrenKey);
                    //aca tiene que estar el objeto formateado en json
                    String ChildrenValue = children.getValue().toString();
                    Log.d(TAG,"Children VALUE " + ChildrenValue);

                }
            }


            /*try {
                JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                jsonObject.get("pk");
            }catch (Exception e){
                Log.e(TAG,e.getMessage());
            }*/



        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

}
