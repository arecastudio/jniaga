package io.github.arecastudio.jniaga.fragments;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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

    public Kategori(){
        context=StaticUtil.getContext();
        isConnected=true;
        GetData();
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

                    DataKategori dk=new DataKategori();
                    dk.setId(json.getInt("id"));
                    dk.setNama(json.getString("nama"));

                    String url_icon=StaticUtil.getWebUrl()+"assets/icons/kategori/"+json.getString("id")+".png";
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
