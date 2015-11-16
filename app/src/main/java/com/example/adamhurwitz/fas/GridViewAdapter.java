package com.example.adamhurwitz.fas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * An adapter object that efficiently recycles items in a grid view.
 */
public class GridViewAdapter extends ArrayAdapter<DoodleData> {

    private Context mContext;
    private ArrayList<DoodleData> mDoodleDataList;

    /**
     * Constructor for the GridViewAdapter object.
     * @param context The context in which this adapter is called.
     * @param layoutResourceId The id of the resource layout to use.
     * @param doodleDataList An array list of DoodleData objects.
     */
    public GridViewAdapter(Context context, int layoutResourceId,
                           ArrayList<DoodleData> doodleDataList) {
        super(context, layoutResourceId, doodleDataList);
        this.mContext = context;
        this.mDoodleDataList = doodleDataList;
    }

    @Override
    /**
     * Overriding the getView method so that the adapter can recycle views appropriately when using
     * a grid.
     * @param position An integer representing the location of the view to pull from the list.
     * @param view The view to inflate with the different layouts for the grid.
     * @param parent The parent element of the view.
     */
    public View getView(int position, View view, ViewGroup parent) {

        // new method to only use memory when view is being used
        // layout inflater
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        // A view holder object with references to the views.
        ViewHolder viewHolder;

        // When loading the view for the first time actually create it, instead of trying to pull
        // a recycled version of the view.
        if (view == null) {
            view = inflater.inflate(R.layout.grid_item_layout, parent, false);
            viewHolder = new ViewHolder();

            // After the view is inflated, set different layouts for what the view should contain
            // using the viewHolder object.
            viewHolder.imageView = (ImageView) view.findViewById(R.id.grid_item_imageview);
            view.setTag(viewHolder);
        } else {
            // If the view exists, re-use it instead of recreating it from scratch.
            viewHolder = (ViewHolder) view.getTag();
        }

        Picasso.with(mContext).load(mDoodleDataList.get(position).getImageUrl()).noFade()
                .into(viewHolder.imageView);
        return view;
    }

    /**
     * Generic class to hold different views for each grid item.
     */
    class ViewHolder {
        ImageView imageView;
    }
}