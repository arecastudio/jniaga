package io.github.arecastudio.jniaga.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private Bundle bundle;

    private Button bt_post;

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
        View view=inflater.inflate(R.layout.frame_baru,container,false);
        //txTitle=(TextView)view.findViewById(R.id.tx_title);
        edit_isi_iklan=(EditText)view.findViewById(R.id.edit_isi_iklan);
        edit_judul_iklan=(EditText)view.findViewById(R.id.edit_judul_iklan);

        cbx_jenis_iklan=(Spinner)view.findViewById(R.id.spinner_jenis_iklan);

        bt_post=(Button)view.findViewById(R.id.bt_post);
        bt_post.setOnClickListener(this);



        Inits();
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
        JSONObject object=new JSONObject();
        TerimaKategori trs=new TerimaKategori(context);
        trs.execute(new JSONObject[]{object});

        JSONObject json=new JSONObject();
        JSONArray array= null;
        ArrayList<String> data=new ArrayList<>();
        try {
            array = trs.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (array!=null){
            try {
                for (int i=0;i<array.length();i++){
                    json=array.getJSONObject(i);

                    //DataKategori dk=new DataKategori();
                    //dk.setId(json.getInt("id"));
                    //dk.setNama(json.getString("nama"));

                    //String url_icon=StaticUtil.getWebUrl()+"assets/icons/kategori/"+json.getString("id")+".png";
                    //dk.setIcon(url_icon);
                    data.add(json.getString("nama"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //List<JSONArray> list=Arrays.asList(array);
        }

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cbx_jenis_iklan.setAdapter(adapter);
        //=========================================================

        cbx_jenis_iklan.setSelection(0,true);
        View v=cbx_jenis_iklan.getSelectedView();
        ((TextView)v).setTextColor(Color.BLACK);

        cbx_jenis_iklan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)view).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bt_post){
            if (AccessToken.getCurrentAccessToken()!=null){
                Bundle params = new Bundle();
                String isi="";
                isi+=edit_judul_iklan.getText().toString().toUpperCase().trim()+"\n\n";
                isi+=edit_isi_iklan.getText().toString().trim()+"\n\n\n\n";
                isi+="#jniaga "+cbx_jenis_iklan.getSelectedItem().toString();
                params.putString("message", isi);
                //params.putString("tags","Test");

                GraphRequest request=new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/feed", params, HttpMethod.POST, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response!=null){
                            Toast.makeText(context,"Berhasil "+response.getRawResponse(),Toast.LENGTH_SHORT).show();
                            System.out.println(response+"");
                        }
                    }
                });
                request.executeAsync();


                System.out.println("tombol");
                try {
                    JSONObject jo=new JSONObject();
                    jo.put("sql_iklan","INSERT INTO");
                    KirimIklan ki=new KirimIklan(context);
                    ki.execute(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Intent intent=new Intent(getContext(),PermissionActivity.class);
                startActivity(intent);
            }
        }
    }
}
