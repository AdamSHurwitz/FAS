package com.example.adamhurwitz.fas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularFragment extends Fragment {

    private ArrayList<DoodleData> mDoodleDataList = new ArrayList<>();
    private GridViewAdapter mGridViewAdapter;

    /**
     * Empty constructor for the PopularFragment class.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_view_layout, container, false);

        mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout,
                mDoodleDataList);

        // Get a reference to the grid view layout and attach the adapter to it.
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_layout);
        gridView.setAdapter(mGridViewAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDoodleDataList.clear();
        mGridViewAdapter.notifyDataSetChanged();
        getDoodleData();
    }

    private void getDoodleData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // Make sure that the device is actually connected to the internet before trying to get data
        // about the Google doodles.
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            FetchDoodleDataTask doodleTask = new FetchDoodleDataTask(mGridViewAdapter,
                    mDoodleDataList);
            doodleTask.execute("popularity.desc");
        }
    }
}
