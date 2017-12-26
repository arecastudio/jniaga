package io.github.arecastudio.jniaga.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    private ViewPager mPager;
    private PagerAdapter adapter;
    private DataIklan dataIklan;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPager=(ViewPager)findViewById(R.id.pager);


        tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager,true);

        context=StaticUtil.getContext();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#660066")));


        tx_judul=(TextView)findViewById(R.id.tx_judul);
        tx_harga=(TextView)findViewById(R.id.tx_harga);
        tx_isi=(TextView)findViewById(R.id.tx_isi);
        tx_kategori=(TextView)findViewById(R.id.tx_kategori);
        //imageView=(ImageView)findViewById(R.id.imageView);

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
        this.dataIklan=data;
        tx_judul.setText(data.getJudul());
        tx_harga.setText(new Fungsi(context).FormatIDR(data.getHarga()));
        tx_isi.setText(data.getIsi());
        tx_kategori.setText(data.getNamaKategori());
        Uri uri=Uri.parse(data.getNamaGambar());
        //Log.e(TAG,data.getNamaGambar());
        //collapsTBar.setTitle(data.getJudul()+"");
        //Picasso.with(getApplicationContext()).load(uri).resize(400,400).centerCrop().into(imageView);;

        final Uri[] iconPath={
                uri,
                Uri.parse("https://examples.javacodegeeks.com/wp-content/uploads/2013/01/create-new-project6.jpg"),
                Uri.parse("http://st1.vchensubeswogfpjoq.netdna-cdn.com/wp-content/uploads/2013/08/Android-GridView-Example.jpg"),
                Uri.parse("http://st1.vchensubeswogfpjoq.netdna-cdn.com/wp-content/uploads/2013/11/Android-ImageView-Example.jpg")
        };
        adapter=new ScreenPagerAdapter(this,iconPath);
        mPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        /*if (mPager.getCurrentItem()==0 && mPager.isScrollbarFadingEnabled()){
            super.onBackPressed();
        }else {
            mPager.setCurrentItem(mPager.getCurrentItem()-1);
        }*/
        super.onBackPressed();
    }

    private class ScreenPagerAdapter extends PagerAdapter{
        Context context1;
        LayoutInflater mLayoutInflater;
        Uri[]iPath;

        public ScreenPagerAdapter(Context context1,Uri[] iPath) {
            this.context1=context1;
            this.iPath=iPath;
            mLayoutInflater=(LayoutInflater)context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return iPath.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==((RelativeLayout)object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //return super.instantiateItem(container, position);
            final Uri imageUri=iPath[position];
            View itemView = mLayoutInflater.inflate(R.layout.frame_pager_items, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //imageView.setImageResource(iPath[position]);

            Picasso.with(context1).load(imageUri).into(imageView);
            container.addView(itemView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewClicked(imageUri.toString());
                }
            });

            TextView tx_harga=(TextView)itemView.findViewById(R.id.tx_harga);
            if (dataIklan!=null){
                tx_harga.setText(""+new Fungsi(context).FormatIDR(dataIklan.getHarga()));
            }

            return itemView;
        }

        private void imageViewClicked(String id){
            //
            Log.w(TAG,"Item clicked. "+id);
            Intent intent=new Intent(getApplicationContext(),ImageItem.class);
            intent.putExtra("uri_string",id);
            startActivity(intent);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((RelativeLayout) object);
        }
    }
}
