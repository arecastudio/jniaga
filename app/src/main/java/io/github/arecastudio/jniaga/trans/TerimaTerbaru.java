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
import io.github.arecastudio.jniaga.model.DataIklan;

/**
 * Created by android on 12/9/17.
 */

public class TerimaTerbaru extends AsyncTask<JSONObject, Void, ArrayList<DataIklan>> {
    private final String TAG="TerimaTerbaru";
    private Fungsi fungsi;
    private Context context;
    private ProgressDialog dialog;
    private ArrayList<DataIklan> iklans;
    private JSONArray array=null;
    private JSONObject jsonResponse;
    private String url;

    private TerimaTerbaruDelegate delegate;

    public interface TerimaTerbaruDelegate{
        void onDelegateFinish(ArrayList<DataIklan> arrayIklan);
    }

    public TerimaTerbaru(Context context,TerimaTerbaruDelegate delegate){
        this.context=context;
        this.delegate=delegate;
        url= StaticUtil.getWebUrl()+"api/terbaru.php";
        fungsi=new Fungsi(context);
    }

    @Override
    protected ArrayList<DataIklan> doInBackground(JSONObject... params) {
        Object json=params[0];
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

        jsonResponse = null;
        HttpPost post = new HttpPost(url);

        ///iklans=new ArrayList<>();

        try {
            StringEntity se = new StringEntity(json.toString());
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response;
            response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

            jsonResponse=new JSONObject(resFromServer);

            array=jsonResponse.getJSONArray("hasil");

            iklans=new ArrayList<DataIklan>();
            JSONObject jos=new JSONObject();
            for (int i=0;i<array.length();i++){
                jos=array.getJSONObject(i);
                DataIklan di=new DataIklan();
                di.setIdIklan(jos.getInt("id"));;
                di.setIdKategri(jos.getInt("id_kategori"));
                di.setJudul(jos.getString("judul"));
                di.setIsi(jos.getString("isi"));
                di.setIdUser(jos.getString("id_user"));
                di.setHarga(Double.parseDouble(jos.getString("harga")));
                if(jos.getString("nama_gambar")!="null"){
                    String url_icon=StaticUtil.getWebUrl()+"assets/foto/"+jos.getString("nama_gambar");
                    di.setNamaGambar(url_icon);
                    Log.w(TAG,di.getJudul());
                }
                iklans.add(di);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return iklans;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

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
    protected void onPostExecute(ArrayList<DataIklan> dataIklans) {
        super.onPostExecute(dataIklans);
        if (delegate!=null){
            delegate.onDelegateFinish(dataIklans);
        }
        dialog.dismiss();
    }
}
