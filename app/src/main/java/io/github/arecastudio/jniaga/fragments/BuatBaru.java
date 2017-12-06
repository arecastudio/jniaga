package io.github.arecastudio.jniaga.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.imgs.ImageLoader;

/**
 * Created by android on 12/6/17.
 */

public class BuatBaru extends Fragment {
    private Context context;
    private Fungsi fungsi;

    private TextView txTitle;
    private Bundle bundle;



    public BuatBaru(){
        context= StaticUtil.getContext();
        fungsi=new Fungsi(context);

        bundle=new Bundle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frame_baru,container,false);
        txTitle=(TextView)view.findViewById(R.id.tx_title);
        Inits();
        return view;
    }

    private void Inits(){
        //T
        if (fungsi.isFbLoggedIn()==true){
            //txTitle.setText("Fb su masuk.\n"+ AccessToken.getCurrentAccessToken().getToken());

            bundle.putString("fields", "id,email,gender,cover,picture.type(large)");
            String userId=AccessToken.getCurrentAccessToken().getUserId();
            txTitle.setText(userId);
        }else {
            txTitle.setText("FB blum masuk.");
        }
    }
}
