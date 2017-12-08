package io.github.arecastudio.jniaga.trans;

import android.content.Context;
import android.os.AsyncTask;

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

import io.github.arecastudio.jniaga.ctrl.StaticUtil;

/**
 * Created by android on 12/8/17.
 */

public class KirimIklan extends AsyncTask<JSONObject,JSONObject,JSONObject> {
    private Context context;
    private String url;
    private JSONObject jsonResponse;
    private JSONObject json;
    private JSONArray array;

    public KirimIklan(Context context){
        this.context=context;
        this.url= StaticUtil.getWebUrl()+"api/kirim_iklan.php";
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        Object object=params[0];

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

        jsonResponse = null;
        HttpPost post = new HttpPost(url);

        try {
            StringEntity se = new StringEntity("kirim="+object.toString());
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

            jsonResponse=new JSONObject(resFromServer);
            System.out.println("Hasil "+jsonResponse.getString("hasil"));
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
