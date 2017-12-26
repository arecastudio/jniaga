package io.github.arecastudio.jniaga.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;


import java.util.Arrays;
import java.util.List;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;

/**
 * Created by android on 12/13/17.
 */

public class LoginUser extends Fragment implements View.OnClickListener {
    private Context context;
    private final String TAG="LoginUser.java";
    private static final int RC_SIGN_IN = 123;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabase;

    public LoginUser(){
        context= StaticUtil.getContext();

        providers = Arrays.asList(
                //new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()//,
                //new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
        );

    }

    @Override
    public void onClick(View v) {
        final View views=v;

        switch (v.getId()){
            case R.id.bt_logout:
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e(TAG,"Logout berhasil.");
                                ReloadFragment();
                            }
                        });
                break;
            case R.id.sign_in_button:
                Log.e(TAG,"Login.");
                break;
            default:
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=null;

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            view=inflater.inflate(R.layout.frame_logoutuser,container,false);

            TextView tx_username=(TextView)view.findViewById(R.id.tx_username);

            String userName=null;
            if(user.getDisplayName()!=null) {
                userName=user.getDisplayName();
                if (user.getPhoneNumber()!=null) userName+="\n"+user.getPhoneNumber();
            }else userName=user.getPhoneNumber();
            tx_username.setText(userName);

            //Log.w(TAG,user.getPhoneNumber());

            ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
            if (user.getPhotoUrl()!=null) Picasso.with(context).load(user.getPhotoUrl()).resize(200,200).centerCrop().into(imageView);

            Button bt_logout=(Button)view.findViewById(R.id.bt_logout);
            bt_logout.setOnClickListener(this);
        }else {
            view=inflater.inflate(R.layout.frame_notlogin,container,false);

            //SignInButton signInButton = view.findViewById(R.id.sign_in_button);
            //signInButton.setSize(SignInButton.SIZE_STANDARD);
            //signInButton.setOnClickListener(this);
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.mipmap.ic_launcher)
                            .setTheme(R.style.AppTheme_NoActionBar)
                            .build(),
                    RC_SIGN_IN);

        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN && resultCode== ResultCodes.OK){
            if (data!=null){
                //refresh fragment
                ReloadFragment();
            }
        }
    }

    private void ReloadFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
