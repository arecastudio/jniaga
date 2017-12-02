package io.github.arecastudio.jniaga.trans;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataKategori;

/**
 * Created by android on 12/2/17.
 */

public class KategoriTransmitter extends AsyncTask<Object, Object, JSONArray> {

    private Context context;
    private ProgressDialog dialog;
    private List<DataKategori> kategoriList;
    public JSONArray array=null;
    public JSONObject jsonResponse;

    private String retVal;
    private String url;

    public KategoriTransmitter(Context context) {
        this.context = context;
        url=StaticUtil.getWebUrl();
    }

    @Override
    protected JSONArray doInBackground(Object... params) {
        Object json=params[0];
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

        jsonResponse = null;
        HttpPost post = new HttpPost(url);

        kategoriList=new ArrayList<DataKategori>();

        try {
            StringEntity se = new StringEntity(json.toString());
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());
            //ArrayList<DataKategori> resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());;

            jsonResponse=new JSONObject(resFromServer);

            array=jsonResponse.getJSONArray("kategori");
            for (int i=0;i<array.length();i++){
                //json=array.getJSONArray(i);
                //System.out.println(array.get(i));
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return array;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
    }
}