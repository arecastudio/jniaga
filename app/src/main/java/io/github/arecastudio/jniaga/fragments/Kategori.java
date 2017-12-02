package io.github.arecastudio.jniaga.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.adapters.KategoriAdapter;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataKategori;
import io.github.arecastudio.jniaga.trans.KategoriTransmitter;

/**
 * Created by android on 12/1/17.
 */

public class Kategori extends Fragment {
    private GridView grid;
    private ArrayList data;
    private Context context;

    public Kategori(){
        context=StaticUtil.getContext();
        GetData();
    }

    private void GetData(){
        JSONObject object=new JSONObject();
        KategoriTransmitter trs=new KategoriTransmitter(context);
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

                    DataKategori dk=new DataKategori();
                    dk.setId(i);
                    dk.setNama(json.getString("nama"));
                    //dk.setIcon(R.mipmap.ic_terbaru);

                    System.out.println(json.getString("icon"));

                    //URL urlImg=new URL(json.getString("icon"));
                    //Bitmap bitmap= BitmapFactory.decodeStream(urlImg.openConnection().getInputStream());

                    //Bitmap bitmap=new Fungsi(context).DownloadFullFromUrl(json.getString("icon"));

                    dk.setIcon(json.getString("icon"));

                    data.add(dk);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //List<JSONArray> list=Arrays.asList(array);


        }else {
            data=new ArrayList<DataKategori>();
            for (int i=1;i<=3;i++){
                DataKategori dk=new DataKategori();
                dk.setId(i);
                dk.setNama("Kategori "+i);
                //dk.setIcon(R.mipmap.ic_cari);

                data.add(dk);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=(View)inflater.inflate(R.layout.frame_kategori,container,false);
        grid=(GridView)view.findViewById(R.id.gridKategori);
        grid.setAdapter(new KategoriAdapter(StaticUtil.getContext(),data));
        return view;
    }
}
