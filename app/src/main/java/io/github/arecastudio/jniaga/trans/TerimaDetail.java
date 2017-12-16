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

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.model.DataIklan;

/**
 * Created by android on 12/16/17.
 */

public class TerimaDetail extends AsyncTask<JSONObject,Void,DataIklan> {
    private final String TAG="TerimaDetail";
    private ProgressDialog dialog;
    private Context context;

    private TerimaDetailDelegate delegate;

    public interface TerimaDetailDelegate {
        void onTaskEndWithResult(int success);
        void onTaskFinishGettingData(DataIklan dataIklan);
    }

    public TerimaDetail(TerimaDetailDelegate delegate){
        context=StaticUtil.getContext();
        this.delegate=delegate;
    }

    @Override
    protected DataIklan doInBackground(JSONObject... params) {
        Object param=params[0];
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

        String url= StaticUtil.getWebUrl()+"api/detail.php";

        JSONObject jsonResponse = null;
        HttpPost post = new HttpPost(url);

        DataIklan iklan=new DataIklan();

        try {
            StringEntity se = new StringEntity("terima="+param.toString());
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

            jsonResponse=new JSONObject(resFromServer);

            //JSONArray array=jsonResponse.getJSONArray("hasil");
            Object status=jsonResponse.get("status");
            if (status!="0"){
                JSONArray produk=jsonResponse.getJSONArray("produk");
                //String judul=produk.getJSONObject(0).getString("judul_full");
                //Log.w(TAG,judul);
                iklan.setIdIklan(Integer.parseInt(getValue(produk,"id")));
                iklan.setJudul(getValue(produk,"judul_full"));
                iklan.setIdKategri(Integer.parseInt(getValue(produk,"id_kategori")));
                iklan.setNamaKategori(getValue(produk,"nama_kategori"));
                iklan.setIsi(getValue(produk,"isi"));
                iklan.setHarga(Double.parseDouble(getValue(produk,"harga")));
                iklan.setIdUser(getValue(produk,"id_user"));

                String urlFoto=StaticUtil.getWebUrl()+"assets/foto/"+getValue(produk,"nama_gambar");
                iklan.setNamaGambar(urlFoto);
            }
            //Log.w(TAG,"DataIklan-> "+iklan.getJudul());
            /*
            if (array.length()>0){
                Log.w(TAG,array.getString(0));
            }else {
                Log.e(TAG,"Response array 0");
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }

        int rets=0;
        if (iklan!=null)rets=1;

        if (delegate!=null){
            delegate.onTaskEndWithResult(rets);
        }

        return iklan;
    }

    private String getValue(JSONArray produk,String field){
        String ret="";
        try{
            ret = produk.getJSONObject(0).getString(field);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
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
    protected void onPostExecute(DataIklan iklan) {
        super.onPostExecute(iklan);

        dialog.dismiss();
        if (delegate!=null){
            delegate.onTaskFinishGettingData(iklan);
        }
    }
}
