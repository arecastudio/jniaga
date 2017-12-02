package io.github.arecastudio.jniaga.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import io.github.arecastudio.jniaga.MainActivity;
import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.adapters.KategoriAdapter;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataKategori;

/**
 * Created by android on 12/1/17.
 */

public class Kategori extends Fragment {
    private GridView grid;

    private Integer[] icons={
            R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher
            ,R.mipmap.ic_launcher

    };

    private ArrayList<DataKategori> data;

    public Kategori(){
        data=new ArrayList<>();
        for (int i=0;i<5;i++){
            DataKategori dk=new DataKategori();
            dk.setId(i);
            dk.setNama("Kategori "+(i+1));
            dk.setIcon(R.mipmap.ic_terbaru);

            data.add(dk);
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
