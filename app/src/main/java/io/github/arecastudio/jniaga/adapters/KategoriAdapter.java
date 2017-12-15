package io.github.arecastudio.jniaga.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.model.DataKategori;

/**
 * Created by android on 12/2/17.
 */

public class KategoriAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<DataKategori> data;
    private LayoutInflater inflater;
    private final String TAG="KategoriAdapter";
    private String urls=null;

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

        //imageView.setImageResource(icons[position]);

        //imageView.setImageResource(dk.getIcon());
        //if (dk.getIcon()!=null) imageView.setImageBitmap(dk.getIcon());

        //new GetImageFromURL(imageView).execute(dk.getIcon());

        if (dk!=null){
            tx_title.setText(dk.getNama());
            if (dk.getIcon()!=null){
                Picasso.with(context).load(dk.getIcon()).resize(100,100).centerCrop().into(imageView);
            }else {
                Picasso.with(context).load(R.mipmap.ic_noimage).resize(100,100).centerCrop().into(imageView);
            }
        }

        return convertView;
    }
}
