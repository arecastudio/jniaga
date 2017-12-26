package io.github.arecastudio.jniaga.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.activities.ScrollingActivity;

/**
 * Created by android on 12/13/17.
 */

public class LihatPost extends Fragment implements View.OnClickListener {
    private final String TAG="LihatPost";

    public LihatPost(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frame_lihatpost,container,false);

        Button bt_send=(Button)view.findViewById(R.id.bt_send_msg);
        bt_send.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_send_msg:
                /*final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("test");
                final FirebaseAuth mAuth=FirebaseAuth.getInstance();

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String currentDateandTime = sdf.format(new Date());
                        try {
                            FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference current_user_db=mDatabase.child(mUser.getUid());
                            current_user_db.child("id").setValue(System.currentTimeMillis()+"");
                            current_user_db.child("name").setValue(currentDateandTime);
                            //current_user_db.child("photo_url").setValue(mAuth.getCurrentUser().getPhotoUrl());

                            //StorageReference mStorage= FirebaseStorage.getInstance().getReference().child(mUser.getUid());
                            //mStorage.child("")
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                Intent intent=new Intent(getContext(), ScrollingActivity.class);
                startActivity(intent);

                Log.w(TAG, "Simpan database.");
                break;
            default:
        }
    }
}
