package io.github.arecastudio.jniaga.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import io.github.arecastudio.jniaga.R;

/**
 * Created by android on 12/13/17.
 */

public class LihatPost extends Fragment implements View.OnClickListener {
    private final String TAG="LihatPost";

    public LihatPost(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frame_lihatpost,container,false);

        Button bt_send=(Button)view.findViewById(R.id.bt_send_msg);
        bt_send.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_send_msg:
                //FirebaseMessaging.getInstance().subscribeToTopic("news");
                //Log.e(TAG,"Kirim pesan Firebase Msg.");
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                //String msg = getString(R.string.msg_token_fmt, token);
                Log.e(TAG, token);
                break;
            default:
        }
    }
}
