package io.github.arecastudio.jniaga.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.github.arecastudio.jniaga.R;

/**
 * Created by android on 12/21/17.
 */

public class ImageItem extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageitem);

        Intent intent=getIntent();
        String extra=intent.getStringExtra("uri_string");
        ImageView imageView=(ImageView)findViewById(R.id.imageView);

        Picasso.with(this).load(Uri.parse(extra))
                .error(R.mipmap.ic_noimage)
                .into(imageView);

        FloatingActionButton xButton=(FloatingActionButton)findViewById(R.id.xButton);

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
