package com.example.adamhurwitz.fas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab3 extends android.support.v4.app.Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Tab3 newInstance(int sectionNumber) {
        Tab3 fragment = new Tab3();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab3, container, false);
        return v;
    }
}
