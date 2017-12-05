package io.github.arecastudio.jniaga.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.arecastudio.jniaga.R;
import io.github.arecastudio.jniaga.ctrl.StaticUtil;

/**
 * Created by android on 12/6/17.
 */

public class BuatBaru extends Fragment {
    private Context context;

    public BuatBaru(){
        context= StaticUtil.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frame_baru,container,false);
        return view;
    }
}
