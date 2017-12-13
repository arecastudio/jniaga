package io.github.arecastudio.jniaga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.GetImageFromURL;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.imgs.ImageLoader;
import io.github.arecastudio.jniaga.model.DataKategori;

/**
 * Created by android on 12/2/17.
 */

public class KategoriAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<DataKategori> data;
    private LayoutInflater inflater;

    public KategoriAdapter(Context context,ArrayList<DataKategori> data) {
        this.context = context;
        this.data=data;
        inflater=LayoutInflater.from(context);
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
        if (convertView == null) {
            convertView=inflater.inflate(R.layout.adapter_kategori,null);
        }

        DataKategori dk=data.get(position);


        ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);

        //imageView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(100, 100));
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setPadding(2, 2, 2, 2);

        TextView tx_title=(TextView)convertView.findViewById(R.id.tx_title);
        tx_title.setText(dk.getNama());

        //imageView.setImageResource(icons[position]);

        //imageView.setImageResource(dk.getIcon());
        //if (dk.getIcon()!=null) imageView.setImageBitmap(dk.getIcon());

        new GetImageFromURL(imageView).execute(dk.getIcon());

        return convertView;
    }
}
