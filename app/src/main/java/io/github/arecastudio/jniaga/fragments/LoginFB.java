package io.github.arecastudio.jniaga.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.arecastudio.jniaga.MainActivity;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
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
    private Context context;
    private ProfilePictureView pictureView;

    private final String[] permissions={"publish_actions","email","user_status","public_profile"};

    public LoginFB(){
        context=StaticUtil.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frame_login,container,false);

        FacebookSdk.sdkInitialize(StaticUtil.getContext());
        AppEventsLogger.activateApp(context);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        tx_status=(TextView)view.findViewById(R.id.tx_status);

        pictureView=(ProfilePictureView) view.findViewById(R.id.facebook_picture);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        //loginButton.setReadPermissions("user_status");
        //loginButton.setReadPermissions(Arrays.asList(permissions));
        // If using in a fragment
        loginButton.setReadPermissions(Arrays.asList("user_status","user_friends"));//user_friends

        loginButton.setFragment(this);
        // Other app specific specialization

        ProsesLogin();

        CekStatus();

        return view;
    }

    private void CekStatus() {
        if (new Fungsi(context).isFbLoggedIn()){
            pictureView.setProfileId(AccessToken.getCurrentAccessToken().getUserId());
        }
    }

    private void ProsesLogin(){

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult!=null){
                    //String accessToken=loginResult.getAccessToken().getToken();
                    tx_status.setText("Login success\n"+loginResult.getAccessToken().getToken());

                    String userId= loginResult.getAccessToken().getUserId();
                    pictureView.setProfileId(userId);
                }
            }

            @Override
            public void onCancel() {
                tx_status.setText("Login gagal.");
            }

            @Override
            public void onError(FacebookException error) {
                tx_status.setText("Error :\n"+error.toString());
            }
        });

        AccessTokenTracker att=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken currentToken) {
                if (currentToken==null){
                    pictureView.setProfileId(null);
                    tx_status.setText("Status");
                }
            }
        };

        //otomatis cek login, seperti pada button.OnActionClick()
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
