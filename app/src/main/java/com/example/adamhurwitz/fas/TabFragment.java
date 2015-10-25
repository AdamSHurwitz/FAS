package com.example.adamhurwitz.fas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment class to be reused by all tabs in the application.
 */
public class TabFragment extends android.support.v4.app.Fragment {

    public static final ArrayList<String> FAKE_STRINGS = new ArrayList<>(
            Arrays.asList(
                    "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta",
                    "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omnicron", "Pi", "Rho",
                    "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega"
            )
    );

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), FAKE_STRINGS));
        return recyclerView;
    }

}
