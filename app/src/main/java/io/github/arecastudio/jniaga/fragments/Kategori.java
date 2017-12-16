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
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataKategori;
import io.github.arecastudio.jniaga.trans.TerimaKategori;

/**
 * Created by android on 12/1/17.
 */

public class Kategori extends Fragment implements TerimaKategori.TerimaKategoriDelegate {
    private GridView grid;
    private ArrayList data;
    private Context context;
    private boolean isConnected;
    //private StorageReference kategoriRef;
    private final String TAG="Kategori.java";
    private String urls=null;
    private String id=null;
    private Fungsi fungsi;

    public Kategori(){
        context=StaticUtil.getContext();
        fungsi=new Fungsi(context);
        isConnected=true;

        GetData();

        //FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReference();
        //kategoriRef = storageRef.child("icons/kategori");
    }

    private void GetData(){
        JSONObject object=new JSONObject();
        TerimaKategori trs=new TerimaKategori(context,this);
        trs.execute(object);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layId=0;
        View view=null;
        if(fungsi.cekKoneksi()){
            layId=R.layout.frame_kategori;
            view=(View)inflater.inflate(layId,container,false);
            grid=(GridView)view.findViewById(R.id.gridKategori);
        }else {
            layId=R.layout.frame_notkonek;
            view=(View)inflater.inflate(layId,container,false);
        }
        return view;
    }

    @Override
    public void onFinishGetData(ArrayList<DataKategori> data) {
        if (fungsi.cekKoneksi()){
            this.data=data;
            grid.setAdapter(new KategoriAdapter(StaticUtil.getContext(),data));
        }
    }
}
