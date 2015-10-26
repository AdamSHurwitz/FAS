package com.example.adamhurwitz.fas;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecentFragment extends Fragment {

    private final Integer[] dummyData = {R.drawable.recent1,R.drawable.recent2,R.drawable.recent3,
            R.drawable.recent4, R.drawable.recent5, R.drawable.recent6, R.drawable.recent7,
            R.drawable.recent8, R.drawable.recent9, R.drawable.recent10, R.drawable.recent11,
            R.drawable.recent12, R.drawable.recent13, R.drawable.recent14, R.drawable.recent15,
            R.drawable.recent16, R.drawable.recent17, R.drawable.recent18};

    // used for Array of Integers when using dummy data
    // private class GridViewAdapter extends ArrayAdapter<Integer> {
    private class GridViewAdapter extends ArrayAdapter<Integer> {
        private final String LOG_TAG = GridViewAdapter.class.getSimpleName();
        // declare Context variable
        Context context;

        /**
         * @param context  is the Context
         * @param resource is the grid_view_layout
         */
        // creates contructor to create GridViewAdapter object
        public GridViewAdapter(Context context, int resource) {

            super(context, resource, dummyData);
            this.context = context;
        }

        // get view to create view, telling Adapter what's included in the grid_item_layout
        @Override
        public View getView(int position, View view, ViewGroup parent) {

            // new method to only use memory when view is being used
            // layout inflater
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            // holder will hold the references to your views
            ViewHolder holder;

            // first clutter of views when nothing is loaded
            if (view == null) {
                // need inflator to inflate the grid_item_layout
                view = inflater.inflate(R.layout.grid_item_layout, parent, false);
                holder = new ViewHolder();
                // once view is inflated we can grab elements, getting and saving grid_item_imageview
                // as ImageView
                holder.gridItem = (ImageView) view.findViewById(R.id.grid_item_imageview);
                view.setTag(holder);
                // if view is not empty, re-use view to repopulate w/ data
            } else {
                holder = (ViewHolder) view.getTag();
            }

            // use setter method setImageResource() to set ImageView image from dummyData Array
            holder.gridItem.setImageResource(dummyData[position]);

            return view;
        }

        class ViewHolder {
            // declare your views here
            ImageView gridItem;
        }
    }

    private ArrayAdapter dummyDataAdapter;

    // ArrayAdapter
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_view_layout, container, false);

        dummyDataAdapter = new GridViewAdapter(
                // current context (this fragment's containing activity)
                getActivity(),
                // ID of view item layout, not needed since we get it in getView()
                R.layout.grid_item_layout);


        // Get a reference to GridView, and attach this adapter to it
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_layout);
        gridView.setAdapter(dummyDataAdapter);
        return view;
    }
}
