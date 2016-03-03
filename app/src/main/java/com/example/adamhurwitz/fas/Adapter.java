package com.example.adamhurwitz.fas;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.adamhurwitz.fas.model.Item;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.squareup.picasso.Picasso;

/**
 * Populates the list_view_active_lists inside ItemsFragment
 */
public abstract class Adapter extends FirebaseListAdapter<Item> {
    Context mContext;

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public Adapter(Activity activity, Class<Item> modelClass, int modelLayout,
                   Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_active_lists)
     * with items inflated from single_active_list.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, Item list) {

        /**
         * Grab the needed views and set data
         */
        ImageView imageView = (ImageView) view.findViewById(R.id.adapter_item_imageview);

       /* Set the list name and owner */
        Picasso.with(mContext).load(Item.getImageUrl()).noFade()
                .into(imageView);
        // textViewCreatedByUser.setText(list.getOwner());
    }
}

