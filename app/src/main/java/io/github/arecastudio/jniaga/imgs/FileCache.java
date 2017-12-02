package io.github.arecastudio.jniaga.imgs;

/**
 * Created by rail on 10/19/16.
 */

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        if(android.os.Environment.getDownloadCacheDirectory().equals(Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getDownloadCacheDirectory(),"JsonParseTutorialCache");
        else
            cacheDir=context.getCacheDir();
        if (!cacheDir.exists()) cacheDir.mkdirs();
        // Find the dir to save cached images
        /*if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "JsonParseTutorialCache");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();*/
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}