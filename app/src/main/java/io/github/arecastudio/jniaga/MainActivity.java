package io.github.arecastudio.jniaga;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

//import com.facebook.CallbackManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.apache.http.auth.AUTH;

import java.util.Arrays;
import java.util.List;

import io.github.arecastudio.jniaga.ctrl.Fungsi;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;
import io.github.arecastudio.jniaga.fragments.BuatBaru;
import io.github.arecastudio.jniaga.fragments.Cari;
import io.github.arecastudio.jniaga.fragments.Disko;
import io.github.arecastudio.jniaga.fragments.Kategori;
import io.github.arecastudio.jniaga.fragments.LihatPost;
import io.github.arecastudio.jniaga.fragments.LoginFB;
import io.github.arecastudio.jniaga.fragments.LoginUser;
import io.github.arecastudio.jniaga.fragments.Terbaru;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean login_stat;
    private FrameLayout fMain;
    private FragmentManager fm;
    //private Fungsi fungsi;

    //private CallbackManager callbackManager;
    private String fireToken;

    private final String TAG="MainActivity";
    private final int RC_SIGN_IN = 123;

    private List<AuthUI.IdpConfig>providers;
    private FirebaseUser user;

    private static ImageView imageView;

    public MainActivity(){
        providers= Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
        );

        user=FirebaseAuth.getInstance().getCurrentUser();
    }

    public static ImageView getImageView() {
        return imageView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticUtil.setContext(this);
        //fungsi=new Fungsi(this);

        //firebase messager stuff
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.e(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]



        //firebase messager stuff

//-------------------------------------------------------------
        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();*/


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        StaticUtil.setIsLogin(true);

        login_stat= StaticUtil.isLogin();
        //Toast.makeText(getApplicationContext(),"Status: "+login_stat,Toast.LENGTH_SHORT).show();
        //----------------------------------------------

        fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.MainFrame,new Terbaru()).commit();
        setTitle("Postingan Terbaru");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder
                    .setTitle("Konfirmasi")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("Yakin untuk tutup?")
                    .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.w(TAG,"ya");
                            //onBackPressed();
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                            Log.w(TAG,"tidak");
                        }
                    })
                    .setCancelable(true);
            final AlertDialog dialog=builder.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.side_nav_bar);
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final Fungsi fungsi=new Fungsi(this);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_terbaru:
                setTitle("Postingan Terbaru");
                if (fungsi.cekKoneksi()){
                    fm.beginTransaction().replace(R.id.MainFrame,new Terbaru()).commit();
                }else {
                    fm.beginTransaction().replace(R.id.MainFrame,new Disko()).commit();
                }
                break;
            case R.id.nav_kategori:
                setTitle("Kategori");
                if (fungsi.cekKoneksi()){
                    fm.beginTransaction().replace(R.id.MainFrame,new Kategori()).commit();
                }else {
                    fm.beginTransaction().replace(R.id.MainFrame,new Disko()).commit();
                }
                break;
            case R.id.nav_pencarian:
                setTitle("Pencarian");
                fm.beginTransaction().replace(R.id.MainFrame,new Cari()).commit();
                break;
            case R.id.nav_login:
                setTitle("Login Akun");
                if (fungsi.cekKoneksi()){
                    fm.beginTransaction().replace(R.id.MainFrame,new LoginUser()).commit();
                    /*startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        String phoneVer=user.getPhoneNumber();
                        boolean emailVer=user.isEmailVerified();
                        phoneVer+=" , "+emailVer;
                        Log.w(TAG,phoneVer);
                    } else {
                        // No user is signed in
                    }*/

                }else {
                    fm.beginTransaction().replace(R.id.MainFrame,new Disko()).commit();
                }
                break;
            case R.id.nav_buat:
                setTitle("Buat Iklan baru");
                fm.beginTransaction().replace(R.id.MainFrame,new BuatBaru()).commit();
                break;
            case R.id.nav_list:
                setTitle("List Postingan");
                fm.beginTransaction().replace(R.id.MainFrame,new LihatPost()).commit();
                break;
            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Aplikasi Forum Jual Beli untuk wilayah Jayapura, lebih mudah menemukan produk yang diinginkan.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Bagikan"));
                break;
            default:
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goFragment(FragmentManager manager, Fragment new_fragment,String new_title){
        if (new Fungsi(this).cekKoneksi()){
            manager.beginTransaction().replace(R.id.MainFrame,new_fragment).commit();
            setTitle(new_title);
        }else{
            manager.beginTransaction().replace(R.id.MainFrame,new Disko()).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }

    }
}
