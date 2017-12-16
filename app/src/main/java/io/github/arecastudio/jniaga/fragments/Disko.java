package io.github.arecastudio.jniaga.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.arecastudio.jniaga.R;

/**
 * Created by android on 12/16/17.
 */

public class Disko extends Fragment {
    private final String TAG="Disko";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frame_notkonek,container,false);

        return view;
    }
}
