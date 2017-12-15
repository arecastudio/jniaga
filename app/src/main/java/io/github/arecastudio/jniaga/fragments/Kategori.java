package io.github.arecastudio.jniaga.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bolts.Task;
import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.adapters.KategoriAdapter;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataKategori;
import io.github.arecastudio.jniaga.trans.TerimaKategori;

/**
 * Created by android on 12/1/17.
 */

public class Kategori extends Fragment {
    private GridView grid;
    private ArrayList data;
    private Context context;
    private boolean isConnected;
    //private StorageReference kategoriRef;
    private final String TAG="Kategori.java";
    private String urls=null;
    private String id=null;

    public Kategori(){
        context=StaticUtil.getContext();
        isConnected=true;

        GetData();

        //FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReference();
        //kategoriRef = storageRef.child("icons/kategori");
    }

    private void GetData(){
        JSONObject object=new JSONObject();
        TerimaKategori trs=new TerimaKategori(context);
        trs.execute(new JSONObject[]{object});

        JSONObject json=new JSONObject();
        JSONArray array= null;
        try {
            array = trs.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (array!=null){
            data=new ArrayList<DataKategori>();
            try {
                for (int i=0;i<array.length();i++){
                    json=array.getJSONObject(i);
                    id=json.getString("id").toString();
                    DataKategori dk=new DataKategori();
                    dk.setId(json.getInt("id"));
                    dk.setNama(json.getString("nama"));
                    String url_icon=StaticUtil.getWebUrl()+"assets/icons/kategori/"+json.getString("id")+".png";
                    /*
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            iconPath.child("icons/kategori/"+json.getString("id")+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urls=uri.toString();
                                }
                            });
                        }
                    }).start();*/

                    Log.d(TAG,url_icon);
                    dk.setIcon(url_icon);

                    data.add(dk);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //List<JSONArray> list=Arrays.asList(array);


        }else {
            /*data=new ArrayList<DataKategori>();
            for (int i=1;i<=3;i++){
                DataKategori dk=new DataKategori();
                dk.setId(i);
                dk.setNama("Kategori "+i);

                data.add(dk);
            }*/

            isConnected=false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layId=0;
        View view=null;
        if(isConnected){
            layId=R.layout.frame_kategori;
            view=(View)inflater.inflate(layId,container,false);
            grid=(GridView)view.findViewById(R.id.gridKategori);
            grid.setAdapter(new KategoriAdapter(StaticUtil.getContext(),data));
        }else {
            layId=R.layout.frame_notkonek;
            view=(View)inflater.inflate(layId,container,false);
        }
        return view;
    }
}
