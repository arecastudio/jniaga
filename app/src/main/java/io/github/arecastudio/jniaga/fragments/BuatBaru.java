package io.github.arecastudio.jniaga.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private EditText edit_harga;
    //private TextView txTitle;
    private Spinner cbx_jenis_iklan;
    private int id_jenis_iklan;
    private Bundle bundle;

    private Button bt_post;
    private Button bt_cam1;
    private Button bt_cam2;
    private Button bt_cam3;
    private int baseColor;

    private TextView tx_gambar1;
    private TextView tx_gambar2;
    private TextView tx_gambar3;

    //picture takken
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GAMBAR1 = 11;
    private static final int GAMBAR2 = 12;
    private static final int GAMBAR3 = 13;


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
            edit_harga=(EditText)view.findViewById(R.id.edit_harga);

            cbx_jenis_iklan=(Spinner)view.findViewById(R.id.spinner_jenis_iklan);

            bt_post=(Button)view.findViewById(R.id.bt_post);
            bt_post.setOnClickListener(this);

            bt_cam1=(Button)view.findViewById(R.id.button_camera1);
            bt_cam1.setOnClickListener(this);

            //Drawable drawable = (Drawable) bt_cam1.getBackground();
            baseColor=bt_cam1.getCurrentTextColor();

            bt_cam2=(Button)view.findViewById(R.id.button_camera2);
            bt_cam2.setOnClickListener(this);

            bt_cam3=(Button)view.findViewById(R.id.button_camera3);
            bt_cam3.setOnClickListener(this);

            tx_gambar1=(TextView)view.findViewById(R.id.tx_gambar1);
            tx_gambar2=(TextView)view.findViewById(R.id.tx_gambar2);
            tx_gambar3=(TextView)view.findViewById(R.id.tx_gambar3);

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
        Intent intent=null;
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

                        try {
                            JSONObject jo = new JSONObject();
                            jo.put("sql_iklan", "INSERT IGNORE INTO");
                            jo.put("judul",edit_judul_iklan.getText().toString().trim().toUpperCase());
                            jo.put("isi",edit_isi_iklan.getText().toString().trim());
                            jo.put("id_kategori",id_jenis_iklan);
                            jo.put("user_id",AccessToken.getCurrentAccessToken().getUserId()+"");
                            jo.put("harga",Double.parseDouble(edit_harga.getText().toString().trim()));
                            KirimIklan ki = new KirimIklan(context);
                            ki.execute(jo);

                            JSONObject ret=ki.get();
                            String hasil=ret.getString("hasil");
                            if (hasil.equals("true")){
                                Toast.makeText(context,"Data berhasil dipublish.",Toast.LENGTH_SHORT).show();
                                Refresh();
                            }else {
                                Toast.makeText(context,"Data gagal dipublish.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
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
                /*Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(context.getPackageManager())!=null){
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }*/
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GAMBAR1);
                break;
            case R.id.button_camera2:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GAMBAR2);
                break;
            case R.id.button_camera3:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GAMBAR3);
                break;
            default:
        }
    }

    private boolean isValid(){
        boolean ret=false;
        if (edit_judul_iklan.getText().toString().trim().length()>0
                && edit_isi_iklan.getText().toString().trim().length()>0
                && edit_harga.getText().toString().trim().length()>0
                && id_jenis_iklan>0){
            ret=true;
        }
        return ret;
    }

    private void Refresh(){
        id_jenis_iklan=0;
        edit_judul_iklan.setText("");
        edit_isi_iklan.setText("");
        edit_harga.setText("");
        edit_judul_iklan.requestFocus();

        bt_cam1.setTextColor(baseColor);
        bt_cam2.setTextColor(baseColor);
        bt_cam3.setTextColor(baseColor);

        tx_gambar1.setText("");
        tx_gambar2.setText("");
        tx_gambar3.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GAMBAR1 && resultCode== Activity.RESULT_OK){
            if (data!=null){
                try{
                    InputStream instr = context.getContentResolver().openInputStream(data.getData());
                    Uri imgUri = data.getData();
                    String picturePath = imgUri.getPath();

                    bt_cam1.setTextColor(Color.BLUE);
                    tx_gambar1.setText("Gambar 1 dipilih");
                    System.out.println(data+"");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        if (requestCode==GAMBAR2 && resultCode== Activity.RESULT_OK){
            if (data!=null){
                try{
                    InputStream instr = context.getContentResolver().openInputStream(data.getData());
                    bt_cam2.setTextColor(Color.BLUE);
                    tx_gambar2.setText("Gambar 2 dipilih");
                    System.out.println(data+"");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        if (requestCode==GAMBAR3 && resultCode== Activity.RESULT_OK){
            if (data!=null){
                try{
                    InputStream instr = context.getContentResolver().openInputStream(data.getData());
                    bt_cam3.setTextColor(Color.BLUE);
                    tx_gambar3.setText("Gambar 3 dipilih");
                    System.out.println(data+"");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
