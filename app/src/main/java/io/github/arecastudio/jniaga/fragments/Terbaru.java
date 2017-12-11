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
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.adapters.TerbaruAdapter;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataIklan;
import io.github.arecastudio.jniaga.trans.TerimaTerbaru;

/**
 * Created by android on 12/1/17.
 */

public class Terbaru extends Fragment {
    private GridView grid;
    private ArrayList data;
    private Context context;
    private boolean isConnected;

    public Terbaru(){
        context= StaticUtil.getContext();
        isConnected=true;
        GetData();
    }

    private void GetData() {
        JSONObject object=new JSONObject();
        TerimaTerbaru tt=new TerimaTerbaru(context);
        tt.execute(new JSONObject[]{object});

        JSONObject json=new JSONObject();

        try {
            JSONArray array=tt.get();
            if (array!=null){
                data=new ArrayList<DataIklan>();
                for (int i=0;i<array.length();i++){
                    json=array.getJSONObject(i);
                    DataIklan di=new DataIklan();
                    di.setIdIklan(json.getInt("id"));;
                    di.setIdKategri(json.getInt("id_kategori"));
                    di.setJudul(json.getString("judul"));
                    di.setIsi(json.getString("isi"));
                    di.setIdUser(json.getString("id_user"));
                    di.setHarga(Double.parseDouble(json.getString("harga")));
                    if(json.getString("nama_gambar")!="null"){
                        String url_icon=StaticUtil.getWebUrl()+"assets/foto/"+json.getString("nama_gambar");
                        di.setNamaGambar(url_icon);
                    }
                    data.add(di);
                }
            }else {
                isConnected=false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=null;
        if (isConnected){
            view=inflater.inflate(R.layout.frame_terbaru,container,false);
            grid=(GridView)view.findViewById(R.id.gridTerbaru);
            grid.setAdapter(new TerbaruAdapter(context,data));
        }else {
            view=inflater.inflate(R.layout.frame_notkonek,container,false);
        }
        return view;
    }
}
