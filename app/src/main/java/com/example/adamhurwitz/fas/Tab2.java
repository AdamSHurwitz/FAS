package com.example.adamhurwitz.fas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab2 extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Tab2 newInstance(int sectionNumber) {
        Tab2 fragment = new Tab2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab2,container,false);
        return v;
    }
}
