package io.github.arecastudio.jniaga.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.arecastudio.jniaga.Manifest;
import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataFoto;
import io.github.arecastudio.jniaga.model.DataKategori;
import io.github.arecastudio.jniaga.trans.KirimIklan;
import io.github.arecastudio.jniaga.trans.TerimaKategori;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by android on 12/6/17.
 */

public class BuatBaru extends Fragment implements View.OnClickListener,TerimaKategori.TerimaKategoriDelegate {
    private final String TAG="BuatBaru";
    private Context context;
    private Fungsi fungsi;
    private EditText edit_isi_iklan;
    private EditText edit_judul_iklan;
    private EditText edit_harga;
    //private TextView txTitle;
    private Spinner cbx_jenis_iklan;
    private int id_jenis_iklan;
    private Intent intent;
    private Uri globalUri;

    private Button bt_post;
    //private Button bt_cam1;
    private int baseColor;

    private TextView tx_gambar1, tx_gambar2, tx_gambar3, tx_gambar4;
    private ImageView imageView,imageView2,imageView3,imageView4;

    //picture takken
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GAMBAR1 = 11;

    private static final int PERMISSION_REQUEST_CODE = 100;

    private int cameraPermission;
    private int readPermission;
    private int writePermission;

    private String[] permissions;

    private DataFoto df1;

    private FirebaseUser user;

    private static String tempUri=null;

    public BuatBaru(){
        context= StaticUtil.getContext();
        fungsi=new Fungsi(context);

        permissions=new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        cameraPermission=ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        writePermission=ContextCompat.checkSelfPermission(context,android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        readPermission=ContextCompat.checkSelfPermission(context,android.Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=null;
        if (fungsi.cekKoneksi()){
            user= FirebaseAuth.getInstance().getCurrentUser();
            //if (AccessToken.getCurrentAccessToken()!=null){
            if (user!=null){
                view=inflater.inflate(R.layout.frame_baru,container,false);
                //txTitle=(TextView)view.findViewById(R.id.tx_title);
                edit_isi_iklan=(EditText)view.findViewById(R.id.edit_isi_iklan);
                edit_judul_iklan=(EditText)view.findViewById(R.id.edit_judul_iklan);
                edit_harga=(EditText)view.findViewById(R.id.edit_harga);

                cbx_jenis_iklan=(Spinner)view.findViewById(R.id.spinner_jenis_iklan);

                bt_post=(Button)view.findViewById(R.id.bt_post);
                bt_post.setOnClickListener(this);

                //bt_cam1=(Button)view.findViewById(R.id.button_camera1);
                //bt_cam1.setOnClickListener(this);

                //Drawable drawable = (Drawable) bt_cam1.getBackground();
                //baseColor=bt_cam1.getCurrentTextColor();

                tx_gambar1=(TextView)view.findViewById(R.id.tx_gambar1);
                tx_gambar2=(TextView)view.findViewById(R.id.tx_gambar2);
                tx_gambar3=(TextView)view.findViewById(R.id.tx_gambar3);
                tx_gambar4=(TextView)view.findViewById(R.id.tx_gambar4);

                imageView=(ImageView)view.findViewById(R.id.imageView);
                imageView.setOnClickListener(this);

                imageView2=(ImageView)view.findViewById(R.id.imageView2);
                imageView2.setOnClickListener(this);

                imageView3=(ImageView)view.findViewById(R.id.imageView3);
                imageView3.setOnClickListener(this);

                imageView4=(ImageView)view.findViewById(R.id.imageView4);
                imageView4.setOnClickListener(this);

                Inits();
            }else {
                view=inflater.inflate(R.layout.frame_notlogin,container,false);
            }
        }else {
            view=inflater.inflate(R.layout.frame_notkonek,container,false);
        }

        return view;
    }

    private void Inits(){
        JSONObject object=new JSONObject();
        TerimaKategori trs=new TerimaKategori(context,this);
        trs.execute(object);
    }

    @Override
    public void onClick(View v) {
        //cek ulang
        user=FirebaseAuth.getInstance().getCurrentUser();
        switch (v.getId()){
            case R.id.bt_post:
                if (user!=null){
                    if (isValid()) {
                        DataFoto[]dataFotos={df1};
                        //==============================
                        try {
                            //List<DataFoto>list=new ArrayList<>();
                            JSONArray array=new JSONArray();
                            for (DataFoto dfs:dataFotos){
                                if (dfs!=null){
                                    //list.add(dfs);
                                    JSONObject  job=new JSONObject();
                                    job.put("nama",dfs.getUuidNama()+".jpg");
                                    Log.e(TAG,"Gen name: "+dfs.getUuidNama()+".jpg");
                                    array.put(job);
                                }
                            }
                            //=====================================================================

                            JSONObject jo=new JSONObject();
                            JSONObject jl=new JSONObject();

                            jo.put("judul",edit_judul_iklan.getText().toString().trim().toUpperCase());
                            jo.put("isi",edit_isi_iklan.getText().toString().trim());
                            jo.put("id_kategori",id_jenis_iklan);
                            jo.put("user_id",user.getUid());
                            jo.put("harga",Double.parseDouble(edit_harga.getText().toString().trim()));
                            jo.put("foto2",array);

                            jl.put("data_foto",dataFotos);
                            KirimIklan ki = new KirimIklan(context);
                            ki.execute(jo,jl);

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
                }
                break;
            case R.id.imageView:
                takePictures();
                break;
            //case R.id.button_camera1:
                //takePictures();
                /*final String[] items={"Dari Kamera","Dari Gallery","Batal"};
                final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Ambil Gambar")
                        .setIcon(R.drawable.ic_menu_camera)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                Log.d(TAG,pos+". "+items[pos]);
                                switch (pos){
                                    case 0:
                                        //kamera

                                        if (cameraPermission!=PackageManager.PERMISSION_GRANTED ||
                                                readPermission!=PackageManager.PERMISSION_GRANTED ||
                                                writePermission!=PackageManager.PERMISSION_GRANTED){
                                            ActivityCompat.requestPermissions(getActivity(),
                                                    permissions,
                                                    PERMISSION_REQUEST_CODE
                                                    );
                                        }else{
                                            try {
                                                String imageFileName =System.currentTimeMillis()+".jpg";
                                                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"jniaga" );
                                                storageDir.mkdirs();
                                                File images=new File(storageDir,imageFileName);
                                                Uri photoURI=Uri.fromFile(images);
                                                globalUri=photoURI;
                                                if (images!=null){
                                                    Log.e(TAG,photoURI+"");
                                                    PackageManager pm=context.getPackageManager();
                                                    if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                                                        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                        if (intent.resolveActivity(context.getPackageManager())!=null){
                                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        break;
                                    case 1:
                                        //gallery
                                        intent = new Intent();
                                        intent.setType("image/jpeg");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar (*.JPG)"), GAMBAR1);
                                        break;
                                    default:
                                }
                            }
                        })
                        .create()
                        .show();*/
            //    break;
            default:
        }
    }

    private void takePictures(){
        user=FirebaseAuth.getInstance().getCurrentUser();
        final String[] items={"Dari Kamera","Dari Gallery","Batal"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Ambil Gambar")
                .setIcon(R.drawable.ic_menu_camera)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        Log.d(TAG,pos+". "+items[pos]);
                        switch (pos){
                            case 0:
                                //kamera

                                if (cameraPermission!=PackageManager.PERMISSION_GRANTED ||
                                        readPermission!=PackageManager.PERMISSION_GRANTED ||
                                        writePermission!=PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions,
                                            PERMISSION_REQUEST_CODE
                                    );
                                }else{
                                    try {
                                        String imageFileName =System.currentTimeMillis()+".jpg";
                                        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"jniaga" );
                                        storageDir.mkdirs();
                                        File images=new File(storageDir,imageFileName);
                                        Uri photoURI=Uri.fromFile(images);
                                        globalUri=photoURI;
                                        if (images!=null){
                                            Log.e(TAG,photoURI+"");
                                            PackageManager pm=context.getPackageManager();
                                            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                                                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                if (intent.resolveActivity(context.getPackageManager())!=null){
                                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case 1:
                                //gallery
                                intent = new Intent();
                                intent.setType("image/jpeg");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar (*.JPG)"), GAMBAR1);
                                break;
                            default:
                        }
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        final int LIMIT_SIZE=1000000;
        final String LIMITED_SIZE="Ukuran gambar terlalu besar.";
        String uuid=UUID.randomUUID().toString();
        Log.e(TAG,requestCode+"");
        if (resultCode==Activity.RESULT_OK){
            if (requestCode==GAMBAR1) {
                try {
                    //InputStream instr = context.getContentResolver().openInputStream(data.getData());
                    Uri imgUri = data.getData();
                    //String picturePath = imgUri.getPath();

                    CropImage.activity(imgUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(context, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (requestCode==REQUEST_IMAGE_CAPTURE){
                try {

                    Log.e(TAG,"xxxxx "+globalUri);

                    CropImage.activity(globalUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(context, this);
                    globalUri=null;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.with(context).load(resultUri).resize(200,200).centerCrop().into(imageView);
                Log.e(TAG,resultUri.toString());

                df1=new DataFoto();
                df1.setUuidNama("img-"+UUID.randomUUID().toString()+"-"+System.currentTimeMillis());
                File fl=new File(resultUri.getPath());
                df1.setNama(fl.getName());
                //Log.e(TAG,fl.getName());
                try {
                    InputStream is=new FileInputStream(resultUri.getPath());
                    df1.setFis((FileInputStream)is);
                    //bt_cam1.setTextColor(Color.BLUE);
                    tx_gambar1.setText(df1.getNama()+"");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG,error.toString());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_REQUEST_CODE){
            if (grantResults.length>0 &&
                    (grantResults[0]==PackageManager.PERMISSION_GRANTED ||
                    grantResults[1]==PackageManager.PERMISSION_GRANTED ||
                    grantResults[2]==PackageManager.PERMISSION_GRANTED)
                    ){
                Log.w(TAG,"All permission granted.");
            }else {
                Log.e(TAG,"Permission denied.");
            }
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

        //bt_cam1.setTextColor(baseColor);

        tx_gambar1.setText("");

        df1=null;

        globalUri=null;

        imageView.setImageResource(R.mipmap.ic_noimage);

        //sembunyikan keyboard
        try  {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinishGetData(ArrayList<DataKategori> data) {
        final ArrayList<String> snama=new ArrayList<>();
        final ArrayList<Integer> sid=new ArrayList<>();

        List<DataKategori>list=data;

        for(int i=0;i<data.size();i++){
            sid.add(data.get(i).getId());
            snama.add(data.get(i).getNama());
        }

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,snama);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cbx_jenis_iklan.setAdapter(adapter);
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
}
