package io.github.arecastudio.jniaga.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.arecastudio.jniaga.MainActivity;
import io.github.arecastudio.jniaga.activities.PermissionActivity;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.GetImageFromURL;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;

import static com.facebook.FacebookSdk.getApplicationContext;


import io.github.arecastudio.jniaga.R;

/**
 * Created by android on 12/4/17.
 */

public class LoginFB extends Fragment implements View.OnClickListener {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView tx_status;
    private Context context;
    private ProfilePictureView pictureView;
    private String userId;
    private final String[] permissions={"publish_actions","email","user_status","public_profile"};
    private GoogleSignInClient mGoogleSignInClient;
    private final int GOOGLE_LOGIN=666;
    private final String TAG="LoginFB.java";

    public LoginFB(){
        //konstruktor
        context=StaticUtil.getContext();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.sign_in_button){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLE_LOGIN);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frame_login,container,false);

        FacebookSdk.sdkInitialize(StaticUtil.getContext());
        AppEventsLogger.activateApp(context);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        tx_status=(TextView)view.findViewById(R.id.tx_status);

        TelephonyManager telemamanger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        TextView tx_telp=view.findViewById(R.id.tx_telp);
        tx_telp.setText("No hp.");

        pictureView=(ProfilePictureView) view.findViewById(R.id.facebook_picture);

        callbackManager = CallbackManager.Factory.create();
        //loginButton.setReadPermissions("publish_actions");
        //loginButton.setReadPermissions("user_status");
        //loginButton.setReadPermissions(Arrays.asList(permissions));
        // If using in a fragment
        loginButton.setReadPermissions(Arrays.asList("email","user_status","public_profile","user_friends"));//user_friends

        //loginButton.setPublishPermissions(Arrays.asList("publish_actions"));
        ///////

        ///////

        loginButton.setFragment(this);
        // Other app specific specialization

        ProsesLogin();

        ifLogin();

        /*String url="https://img.okezone.com/content/2011/04/07/340/443471/J1647XEN5J.jpg";
        ImageView im=(ImageView)view.findViewById(R.id.imageView);
        GetImageFromURL gifURL=new GetImageFromURL(im);
        gifURL.execute(url);*/

        //LoginWithGoogle
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        SignInButton signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        updateUI(GoogleSignIn.getLastSignedInAccount(getActivity()));

        return view;
    }

    private void ifLogin() {
        if (new Fungsi(context).isFbLoggedIn()){

            pictureView.setProfileId(AccessToken.getCurrentAccessToken().getUserId());

            GraphRequest request=new GraphRequest().newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        String nama=object.getString("name");
                        //String email=object.getString("email");
                        System.out.println("Nama Facebook "+nama);
                        tx_status.setText(nama);

                        StaticUtil.setUserId(object.getString("id"));
                        StaticUtil.setUserName(object.getString("name"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }else {
            StaticUtil.ResetAll();
        }
    }

    private void ProsesLogin(){

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult!=null){
                    //String accessToken=loginResult.getAccessToken().getToken();
                    //tx_status.setText("Login success\n"+loginResult.getAccessToken().getToken());

                    GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                tx_status.setText(object.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                tx_status.setText("Sukses.");
                            }
                        }
                    });
                    request.executeAsync();

                    userId=loginResult.getAccessToken().getUserId();
                    pictureView.setProfileId(userId);

                    StaticUtil.setUserId(userId);
                    //StaticUtil.setUserName(loginResult.getAccessToken().));

                    //Intent intent=new Intent(getContext(), PermissionActivity.class);
                    //startActivity(intent);
                }
            }

            @Override
            public void onCancel() {
                tx_status.setText("Login gagal.");
                StaticUtil.ResetAll();
            }

            @Override
            public void onError(FacebookException error) {
                tx_status.setText("Error :\n"+error.toString());
            }
        });

        //otomatis cek perubahan akses token.
        AccessTokenTracker att=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken currentToken) {
                if (currentToken==null){
                    pictureView.setProfileId(null);
                    tx_status.setText("Status");
                }else {
                    //
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

        if (requestCode==GOOGLE_LOGIN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount acct){
        //GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            Log.e(TAG,acct.zzaap());
        }
    }
}
