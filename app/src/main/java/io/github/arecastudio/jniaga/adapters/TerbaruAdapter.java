package io.github.arecastudio.jniaga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.model.DataIklan;

/**
 * Created by android on 12/9/17.
 */

public class TerbaruAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<DataIklan> data;
    private LayoutInflater inflater;
    private Fungsi fungsi;
    private final int IMG_SIZE=100;

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
        tx_judul.setTag(di.getIdIklan()+"");

        String harga=fungsi.FormatIDR(di.getHarga());

        TextView tx_harga=convertView.findViewById(R.id.tx_harga);
        tx_harga.setText(""+harga);
        tx_harga.setTag(di.getNamaGambar());

        ImageView imageView=(ImageView) convertView.findViewById(R.id.imageView);

        if (di.getNamaGambar()!=null){
            //new GetImageFromURL(imageView).execute(di.getNamaGambar());
            Picasso.with(context).load(di.getNamaGambar()).resize(IMG_SIZE,IMG_SIZE).centerCrop().into(imageView);
        }else {
            //imageView.setImageResource(R.mipmap.ic_noimage);
            Picasso.with(context).load(R.mipmap.ic_noimage).resize(IMG_SIZE,IMG_SIZE).centerCrop().into(imageView);
        }

        return convertView;
    }
}
