package io.github.arecastudio.jniaga.fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.activities.ProdukActivity;
import io.github.arecastudio.jniaga.adapters.TerbaruAdapter;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataIklan;
import io.github.arecastudio.jniaga.trans.TerimaTerbaru;

/**
 * Created by android on 12/1/17.
 */

public class Terbaru extends Fragment implements TerimaTerbaru.TerimaTerbaruDelegate {
    private final String TAG="Terbaru";
    private final int REQ_CODE=666;
    private GridView grid;
    private ArrayList data;
    private Context context;
    private boolean isConnected;
    private SwipeRefreshLayout refreshLayout;
    private Intent intent;
    private Fungsi fungsi;

    public Terbaru(){
        context= StaticUtil.getContext();
        fungsi=new Fungsi(context);
        isConnected=true;
        GetData();
    }

    private void GetData() {
        JSONObject object=new JSONObject();
        TerimaTerbaru tt=new TerimaTerbaru(context,this);
        tt.execute(object);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=null;
        if (fungsi.cekKoneksi()){
            view=inflater.inflate(R.layout.frame_terbaru,container,false);

            grid=(GridView)view.findViewById(R.id.gridTerbaru);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    TextView tx_judul=(TextView)v.findViewById(R.id.tx_judul);
                    TextView tx_harga=v.findViewById(R.id.tx_harga);

                    String judul=tx_judul.getText().toString();
                    String idIklan=tx_judul.getTag().toString();
                    String harga=tx_harga.getText().toString();
                    String urlFoto=tx_harga.getTag().toString();

                    if (fungsi.cekKoneksi()){
                        intent=new Intent(getActivity(), ProdukActivity.class);
                        intent.putExtra("idIklan",idIklan);
                        intent.putExtra("judul",judul);
                        intent.putExtra("harga",harga);
                        intent.putExtra("urlFoto",urlFoto);
                        startActivityForResult(intent,REQ_CODE);
                    }else {
                        Toast.makeText(context, "Koneksi terputus, silahkan refresh halaman.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.refreshLayout);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    RefreshListView();
                }
            });
        }else {
            view=inflater.inflate(R.layout.frame_notkonek,container,false);
        }
        return view;
    }

    private void RefreshListView(){
        grid.setAdapter(null);
        GetData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                grid.setAdapter(new TerbaruAdapter(context,data));
                refreshLayout.setRefreshing(false);
            }
        },2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_CODE){
            if (resultCode== Activity.RESULT_OK){
                Log.w(TAG,"XXXXXXXXXXXXXXXXXXXXX: "+data.getStringExtra("juduls"));
            }
        }
    }

    @Override
    public void onDelegateFinish(ArrayList<DataIklan> arrayIklan) {
        if (fungsi.cekKoneksi()){
            this.data=arrayIklan;
            grid.setAdapter(new TerbaruAdapter(context,data));
        }
    }
}
