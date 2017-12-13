package io.github.arecastudio.jniaga.srv;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by android on 12/13/17.
 */

public class FirebaseInstanceID extends FirebaseInstanceIdService {
    private final String TAG = "FirebaseInstanceID";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG,"Token: "+token);

        SendRegToServer(token);
    }

    private void SendRegToServer(String token) {
    }
}
