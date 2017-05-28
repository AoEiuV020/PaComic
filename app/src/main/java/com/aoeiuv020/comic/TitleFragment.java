package com.aoeiuv020.comic;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by AoEiuV020 on 2016/04/06 - 21:19:41
 */
public class TitleFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_title, container, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

}  
