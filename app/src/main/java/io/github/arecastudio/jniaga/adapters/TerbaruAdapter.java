package io.github.arecastudio.jniaga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.imgs.ImageLoader;
import io.github.arecastudio.jniaga.model.DataIklan;

/**
 * Created by android on 12/9/17.
 */

public class TerbaruAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<DataIklan> data;
    private LayoutInflater inflater;
    private Fungsi fungsi;

    public TerbaruAdapter(Context context,ArrayList<DataIklan>data){
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
        fungsi=new Fungsi(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=inflater.inflate(R.layout.adapter_terbaru,null);
        }
        DataIklan di=data.get(position);

        TextView tx_judul=(TextView)convertView.findViewById(R.id.tx_judul);
        tx_judul.setText(di.getJudul());

        String harga=fungsi.FormatIDR(di.getHarga());

        TextView tx_harga=convertView.findViewById(R.id.tx_harga);
        tx_harga.setText(""+harga);

        ImageView imageView=(ImageView) convertView.findViewById(R.id.imageView);

        if (di.getNamaGambar()!=null){
            ImageLoader loader=new ImageLoader(context);
            loader.DisplayImage(di.getNamaGambar(),imageView);
        }else {
            imageView.setImageResource(R.mipmap.ic_noimage);
        }

        return convertView;
    }
}
