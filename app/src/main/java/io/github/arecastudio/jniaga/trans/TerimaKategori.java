package io.github.arecastudio.jniaga.trans;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataKategori;

/**
 * Created by android on 12/2/17.
 */

public class TerimaKategori extends AsyncTask<Object, Object, ArrayList<DataKategori>> {
    private final String TAG="TerimaKategori";
    private Context context;
    private ProgressDialog dialog;
    private ArrayList<DataKategori> kategoriList;
    public JSONArray array=null;
    public JSONObject jsonResponse;

    private Fungsi fungsi;
    private String url;

    private TerimaKategoriDelegate delegate;

    public interface TerimaKategoriDelegate{
        void onFinishGetData(ArrayList<DataKategori> data);
    }

    public TerimaKategori(Context context,TerimaKategoriDelegate delegate) {
        this.context = context;
        this.delegate=delegate;
        fungsi=new Fungsi(context);
        url=StaticUtil.getWebUrl()+"api/kategori.php";
    }

    @Override
    protected ArrayList<DataKategori> doInBackground(Object... params) {
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

            JSONObject jos=new JSONObject();
            for (int i=0;i<array.length();i++){
                jos=array.getJSONObject(i);
                //id=jos.getString("id").toString();
                DataKategori dk=new DataKategori();
                dk.setId(jos.getInt("id"));
                dk.setNama(jos.getString("nama"));
                String url_icon=StaticUtil.getWebUrl()+"assets/icons/kategori/"+jos.getString("id")+".png";

                Log.d(TAG,url_icon);
                dk.setIcon(url_icon);

                kategoriList.add(dk);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return kategoriList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog=new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(context.getString(R.string.tunggu_progress));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.side_nav_bar);
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<DataKategori> dataArray) {
        super.onPostExecute(dataArray);
        dialog.dismiss();
        if (delegate!=null){
            delegate.onFinishGetData(dataArray);
        }
    }
}