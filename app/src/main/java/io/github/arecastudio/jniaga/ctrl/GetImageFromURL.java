package io.github.arecastudio.jniaga.ctrl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import io.github.arecastudio.jniaga.R;

/**
 * Created by android on 12/12/17.
 */

public class GetImageFromURL extends AsyncTask<String,Void,Bitmap> {
    private ImageView imageView;

    public GetImageFromURL(ImageView imageView){
        this.imageView=imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urlString) {
        final int MAX_SIZE=200;
        final BitmapFactory.Options options=new BitmapFactory.Options();
        Bitmap bmp=null;
        try {
            URL url=new URL(urlString[0]);
            InputStream is=url.openStream();
            //bmp=BitmapFactory.decodeStream(is);
            options.inScaled=true;
            options.inSampleSize=4;
            options.inDensity=400;
            options.inTargetDensity=100*options.inSampleSize;
            bmp=BitmapFactory.decodeStream(is,null,options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else {
            imageView.setImageResource(R.mipmap.ic_noimage);
        }
    }

    //kecilkan ukuran gambar saat tampil di adapter untuk menghemat memory/quota/waktu. semacam thumbnail
    private Bitmap DecodeBitmap(InputStream is,int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeStream(is,null,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is,null, options);
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
