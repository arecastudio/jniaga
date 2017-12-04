package io.github.arecastudio.jniaga.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import io.github.arecastudio.jniaga.MainActivity;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;

import static com.facebook.FacebookSdk.getApplicationContext;


import io.github.arecastudio.jniaga.R;

/**
 * Created by android on 12/4/17.
 */

public class LoginFB extends Fragment {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView tx_status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frame_login,container,false);

        FacebookSdk.sdkInitialize(StaticUtil.getContext());

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        tx_status=(TextView)view.findViewById(R.id.tx_status);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        ProsesLogin();
        return view;
    }

    private void ProsesLogin(){

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                tx_status.setText("Login berhasil.\n"+loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //tx_status.setText("Login gagal.");
            }

            @Override
            public void onError(FacebookException error) {
                //tx_status.setText(error.toString());
            }
        });




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
