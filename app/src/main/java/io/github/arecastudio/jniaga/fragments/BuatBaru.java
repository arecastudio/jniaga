package io.github.arecastudio.jniaga.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bolts.Task;
import io.github.arecastudio.jniaga.MainActivity;
import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.activities.PermissionActivity;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataKategori;
import io.github.arecastudio.jniaga.trans.KirimIklan;
import io.github.arecastudio.jniaga.trans.TerimaKategori;

/**
 * Created by android on 12/6/17.
 */

public class BuatBaru extends Fragment implements View.OnClickListener {
    private Context context;
    private Fungsi fungsi;
    private EditText edit_isi_iklan;
    private EditText edit_judul_iklan;
    //private TextView txTitle;
    private Spinner cbx_jenis_iklan;
    private int id_jenis_iklan;
    private Bundle bundle;

    private ImageButton bt_post;
    private Button bt_cam1;

    //picture takken
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private final List<String> lists=Arrays.asList("publish_actions");
    private AccessToken accToken=null;

    public BuatBaru(){
        context= StaticUtil.getContext();
        fungsi=new Fungsi(context);

        bundle=new Bundle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=null;
        if (fungsi.cekKoneksi()){
            view=inflater.inflate(R.layout.frame_baru,container,false);
            //txTitle=(TextView)view.findViewById(R.id.tx_title);
            edit_isi_iklan=(EditText)view.findViewById(R.id.edit_isi_iklan);
            edit_judul_iklan=(EditText)view.findViewById(R.id.edit_judul_iklan);

            cbx_jenis_iklan=(Spinner)view.findViewById(R.id.spinner_jenis_iklan);

            bt_post=(ImageButton)view.findViewById(R.id.bt_post);
            bt_post.setOnClickListener(this);

            bt_cam1=(Button)view.findViewById(R.id.button_camera1);
            bt_cam1.setOnClickListener(this);

            Inits();
        }else {
            view=inflater.inflate(R.layout.frame_diskonek,container,false);
        }
        return view;
    }

    private void Inits(){
        //T
        /*if (fungsi.isFbLoggedIn()==true){
            //txTitle.setText("Fb su masuk.\n"+ AccessToken.getCurrentAccessToken().getToken());

            bundle.putString("fields", "id,email,gender,cover,picture.type(large)");
            String userId=AccessToken.getCurrentAccessToken().getUserId();
            txTitle.setText(userId);
        }else {
            txTitle.setText("FB blum masuk.");
        }*/

        //publish_actions
        //LoginManager.getInstance().logInWithPublishPermissions((Fragment) this, Arrays.asList("publish_actions"));

        //=========================================================

        final ArrayList<Integer> sid=new ArrayList<>();
        final ArrayList<String> snama=new ArrayList<>();

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object=new JSONObject();
                TerimaKategori trs=new TerimaKategori(context);
                trs.execute(new JSONObject[]{object});

                JSONObject json=new JSONObject();
                JSONArray array= null;
                try {
                    array = trs.get();
                    if (array!=null){
                        for (int i=0;i<array.length();i++){
                            json=array.getJSONObject(i);
                            sid.add(json.getInt("id"));
                            snama.add(json.getString("nama"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String>adapter=new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,snama);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                cbx_jenis_iklan.setAdapter(adapter);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //=========================================================

        //if ()
        cbx_jenis_iklan.setSelection(0,true);
        id_jenis_iklan=sid.get(0);//id dipilih sesuai position
        View v=cbx_jenis_iklan.getSelectedView();
        ((TextView)v).setTextColor(Color.BLACK);

        cbx_jenis_iklan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)view).setTextColor(Color.BLACK);
                id_jenis_iklan=sid.get(position);
                System.out.println("Kategori dipilih: "+id_jenis_iklan+"\n"+((TextView) view).getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_post:
                if (AccessToken.getCurrentAccessToken()!=null){
                    if (isValid()) {
                        /*Bundle params = new Bundle();
                        String isi = "";
                        isi += edit_judul_iklan.getText().toString().toUpperCase().trim() + "\n\n";
                        isi += edit_isi_iklan.getText().toString().trim() + "\n\n\n\n";
                        isi += "#jniaga " + cbx_jenis_iklan.getSelectedItem().toString();
                        params.putString("message", isi);

                        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/feed", params, HttpMethod.POST, new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null) {
                                    Toast.makeText(context, "Berhasil\n" + response.getRawResponse(), Toast.LENGTH_SHORT).show();
                                    System.out.println(response + "");
                                }
                            }
                        });
                        request.executeAsync();
                        */

                        //System.out.println("tombol");

                        Thread thread=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //
                                try {
                                    JSONObject jo = new JSONObject();
                                    jo.put("sql_iklan", "INSERT IGNORE INTO");
                                    jo.put("judul",edit_judul_iklan.getText().toString().trim());
                                    jo.put("isi",edit_isi_iklan.getText().toString().trim());
                                    jo.put("id_kategori",id_jenis_iklan);
                                    jo.put("user_id",AccessToken.getCurrentAccessToken().getUserId()+"");
                                    KirimIklan ki = new KirimIklan(context);
                                    ki.execute(jo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }else {
                        Toast.makeText(context,"Isian belum lengkap.",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(context,"Silahkan login terlebih dahulu.",Toast.LENGTH_SHORT).show();
                    //Intent intent=new Intent(getContext(),PermissionActivity.class);
                    //startActivity(intent);
                }
                break;
            case R.id.button_camera1:
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(context.getPackageManager())!=null){
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            default:
        }
    }

    private boolean isValid(){
        boolean ret=false;
        if (edit_judul_iklan.getText().toString().trim().length()>0
                && edit_isi_iklan.getText().toString().trim().length()>0
                && id_jenis_iklan>0){
            ret=true;
        }
        return ret;
    }
}
