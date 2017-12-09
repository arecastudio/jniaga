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
import io.github.arecastudio.jniaga.model.DataIklan;

/**
 * Created by android on 12/9/17.
 */

public class TerimaTerbaru extends AsyncTask<JSONObject, Void, JSONArray> {
    private Context context;
    private ProgressDialog dialog;
    private List<DataIklan> iklans;
    private JSONArray array=null;
    private JSONObject jsonResponse;
    private String url;

    public TerimaTerbaru(Context context){
        this.context=context;
        url= StaticUtil.getWebUrl()+"api/terbaru.php";
    }

    @Override
    protected JSONArray doInBackground(JSONObject... params) {
        Object json=params[0];
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

        jsonResponse = null;
        HttpPost post = new HttpPost(url);

        iklans=new ArrayList<>();

        try {
            StringEntity se = new StringEntity(json.toString());
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

            jsonResponse=new JSONObject(resFromServer);

            array=jsonResponse.getJSONArray("hasil");

            if (array.length()>0){
                //
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
    protected void onPostExecute(JSONArray jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
