package io.github.arecastudio.jniaga.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.arecastudio.jniaga.R;

/**
 * Created by android on 12/1/17.
 */

public class Terbaru extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*View view=inflater.inflate(R.layout.frame_terbaru,container,false);
        return view;*/
        return inflater.inflate(R.layout.frame_terbaru,container,false);
    }
}
