package io.github.arecastudio.jniaga.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataIklan;
import io.github.arecastudio.jniaga.trans.TerimaDetail;

public class ProdukActivity extends AppCompatActivity implements TerimaDetail.TerimaDetailDelegate {
    private final String TAG="ProdukActivity";
    private Context context;
    private TextView tx_judul,tx_isi,tx_harga,tx_kategori;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=StaticUtil.getContext();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#660066")));


        tx_judul=(TextView)findViewById(R.id.tx_judul);
        tx_harga=(TextView)findViewById(R.id.tx_harga);
        tx_isi=(TextView)findViewById(R.id.tx_isi);
        tx_kategori=(TextView)findViewById(R.id.tx_kategori);
        imageView=(ImageView)findViewById(R.id.imageView);

        Intent intent=getIntent();
        if (intent.getExtras()!=null){
            setTitle(intent.getStringExtra("judul").toString().toUpperCase());

            setGui(intent);
        }
        //onBackPressed() result to parent fragment
        intent = new Intent();
        intent.putExtra("juduls", getTitle().toString());
        setResult(RESULT_OK, intent);
    }

    private void setGui(Intent intent) {
        tx_judul.setText("");
        tx_harga.setText("");
        tx_isi.setText("");
        tx_kategori.setText("");
        //imageView.setImageResource(R.mipmap.ic_noimage);

        final  String id=intent.getStringExtra("idIklan").toString();
        DataIklan di=null;
        try {
            JSONObject jo=new JSONObject();
            jo.put("id",id);
            TerimaDetail terimaDetail=new TerimaDetail(this);
            terimaDetail.execute(jo);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // or finish();
                return true;
        }
        return false;//super.onOptionsItemSelected(item);
    }


    @Override
    public void onTaskEndWithResult(int success) {

    }

    @Override
    public void onTaskFinishGettingData(DataIklan data) {
        tx_judul.setText(data.getJudul());
        tx_harga.setText(new Fungsi(context).FormatIDR(data.getHarga()));
        tx_isi.setText(data.getIsi());
        tx_kategori.setText(data.getNamaKategori());
        Uri uri=Uri.parse(data.getNamaGambar());
        //Log.e(TAG,data.getNamaGambar());
        Picasso.with(getApplicationContext()).load(uri).resize(400,400).centerCrop().into(imageView);
    }
}
