package io.github.arecastudio.jniaga.trans;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.github.arecastudio.jniaga.MainActivity;
import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.KirimFoto;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.fragments.BuatBaru;
import io.github.arecastudio.jniaga.model.DataFoto;

/**
 * Created by android on 12/8/17.
 */

public class KirimIklan extends AsyncTask<JSONObject,JSONObject,JSONObject> {
    private Context context;
    private String url;
    private JSONObject jsonResponse;
    private JSONObject json;
    private JSONArray array;
    private ProgressDialog dialog;

    public KirimIklan(Context context){
        this.context=context;
        this.url= StaticUtil.getWebUrl()+"api/kirim_iklan.php";
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        final KirimFoto kirimFoto=new KirimFoto();
        Object object=params[0];
        final JSONObject object1=params[1];
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DataFoto[] dataFotos=(DataFoto[]) object1.get("data_foto");
                        for (DataFoto dfs:dataFotos){
                            if (dfs!=null){
                                System.out.println("upload --> "+dfs.getNama());
                                kirimFoto.uploadFile(dfs.getFis(),dfs.getUuidNama()+".jpg");
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

            //=======================
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

            jsonResponse = null;
            HttpPost post = new HttpPost(url);


            //=======================
            StringEntity se = new StringEntity("kirim="+object.toString());
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

            jsonResponse=new JSONObject(resFromServer);

            String hasil=jsonResponse.getString("hasil");
            String msg="Gagal memposting iklan ke JNiaga.";
            if (hasil.equals("true")){
                msg="Berhasil memposting iklan ke JNiaga.";
            }

            //System.out.println("Hasil : "+msg);
            //Toast.makeText(StaticUtil.getContext(),msg,Toast.LENGTH_SHORT).show();
            //tampilkan return value di parent class BuatBaru.java, ketika pemanggilan child class ini

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
