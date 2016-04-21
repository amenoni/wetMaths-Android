package com.wetmaths.wetmaths.io.network;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;
import com.octo.android.robospice.retry.RetryPolicy;
import com.wetmaths.wetmaths.Move;

import java.util.Map;

/**
 * Created by amenoni on 14/4/16.
 */
public class MovePostRequest extends SpringAndroidSpiceRequest<Boolean>{

    Move mMove;
    String mArduinoUrl;

    public MovePostRequest(String arduinoUrl, Move move) {
        super(Boolean.class);
        this.mMove = move;
        this.mArduinoUrl = arduinoUrl;
        this.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getRetryCount() {
                return 0;
            }

            @Override
            public void retry(SpiceException e) {

            }

            @Override
            public long getDelayBeforeRetry() {
                return 0;
            }
        });
    }

    @Override
    public Boolean loadDataFromNetwork() throws Exception {
        String url = String.format("%s/api/move/", mArduinoUrl);

        if(mMove != null){
            Map<String,String> record = mMove.toMap();
            getRestTemplate().postForLocation(url,record);
            return true;
        }else {
            return false;
        }
    }
}
