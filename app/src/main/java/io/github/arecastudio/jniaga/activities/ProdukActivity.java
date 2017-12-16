package io.github.arecastudio.jniaga.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import io.github.arecastudio.jniaga.R;

public class ProdukActivity extends AppCompatActivity {
    private final String TAG="ProdukActivity";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#660066")));

        intent=getIntent();
        if (intent.getExtras()!=null){
            setTitle(intent.getStringExtra("judul").toString().toUpperCase());
        }





        //passback result to parent fragment
        intent = new Intent();
        intent.putExtra("juduls", getTitle().toString());
        setResult(RESULT_OK, intent);
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
}
