package io.github.arecastudio.jniaga.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.GetImageFromURL;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;

/**
 * Created by android on 12/13/17.
 */

public class LoginUser extends Fragment implements View.OnClickListener {
    private Context context;
    private final String TAG="LoginUser.java";
    private final int GOOGLE_LOGIN=666;
    private GoogleSignInClient mGoogleSignInClient;

    public LoginUser(){
        context= StaticUtil.getContext();

        //LoginWithGoogle
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    @Override
    public void onClick(View v) {
        final View views=v;

        switch (v.getId()){
            case R.id.bt_logout:
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(LoginUser.this).attach(LoginUser.this).commit();
                    }
                });
                Log.e(TAG,"Logout.");
                break;
            case R.id.sign_in_button:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_LOGIN);
                Log.e(TAG,"Login.");
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GOOGLE_LOGIN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            if (data!=null){
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
            }
        }
    }

    private GoogleUserInfo updateUI(GoogleSignInAccount acct){
        //GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        GoogleUserInfo gui=new GoogleUserInfo();
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            gui.setPersonEmail(personEmail);
            gui.setPersonFamilyName(personFamilyName);
            gui.setPersonId(personId);
            gui.setPersonPhoto(personPhoto);
            gui.setPersonGivenName(personGivenName);
            gui.setPersonName(personName);

            //Log.e(TAG,acct.getDisplayName());
        }
        return gui;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=null;

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(getActivity());
        GoogleUserInfo gui=updateUI(account);
        if (account!=null){
            view=inflater.inflate(R.layout.frame_logoutuser,container,false);

            TextView tx_username=(TextView)view.findViewById(R.id.tx_username);
            tx_username.setText(gui.getPersonName());

            ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
            if (gui.getPersonPhoto()!=null){
                new GetImageFromURL(imageView).execute(gui.getPersonPhoto().toString());
            }

            Button bt_logout=(Button)view.findViewById(R.id.bt_logout);
            bt_logout.setOnClickListener(this);
        }else {
            view=inflater.inflate(R.layout.frame_loginuser,container,false);

            SignInButton signInButton = view.findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setOnClickListener(this);
        }

        //updateUI(GoogleSignIn.getLastSignedInAccount(getActivity()));

        return view;
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

    private class GoogleUserInfo{
        private String personName,personGivenName,personFamilyName,personEmail,personId;
        private Uri personPhoto;

        public GoogleUserInfo(){}

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public String getPersonGivenName() {
            return personGivenName;
        }

        public void setPersonGivenName(String personGivenName) {
            this.personGivenName = personGivenName;
        }

        public String getPersonFamilyName() {
            return personFamilyName;
        }

        public void setPersonFamilyName(String personFamilyName) {
            this.personFamilyName = personFamilyName;
        }

        public String getPersonEmail() {
            return personEmail;
        }

        public void setPersonEmail(String personEmail) {
            this.personEmail = personEmail;
        }

        public String getPersonId() {
            return personId;
        }

        public void setPersonId(String personId) {
            this.personId = personId;
        }

        public Uri getPersonPhoto() {
            return personPhoto;
        }

        public void setPersonPhoto(Uri personPhoto) {
            this.personPhoto = personPhoto;
        }
    }
}
