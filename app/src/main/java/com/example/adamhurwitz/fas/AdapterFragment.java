package com.example.adamhurwitz.fas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adamhurwitz.fas.model.Item;
import com.example.adamhurwitz.fas.sync.SyncAdapter;
import com.example.adamhurwitz.fas.utils.Constants;
import com.example.adamhurwitz.fas.utils.FirebaseRecyclerAdapter;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
// public class AdapterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
public class AdapterFragment extends Fragment {
    private final String LOG_TAG = AdapterFragment.class.getSimpleName();
    private Firebase mRef;
    private Query mImageRef;
    // private RecyclerView mImages;
    // private FirebaseRecyclerAdapter<Item, ItemHolder> mRecyclerViewAdapter;

    /**
     * Empty constructor for the PopularFragment class.
     */
    public AdapterFragment() {
        /* Required empty public constructor */
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDoodleData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Initialize UI elements
         */
        View rootView = inflater.inflate(R.layout.adapter_view_layout, container, false);
        /*RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.adapter_view_layout, container, false);

        Log.v(LOG_TAG, "rv is " + isRvFilled(rv));

        Firebase mRef = new Firebase(Constants.FIREBASE_URL);

        mImageRef = mRef.limitToLast(50);

        rv = (RecyclerView) getActivity().findViewById(
                R.id.firebaserecyclerview_id);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerAdapter<Item, ItemHolder> mRecyclerViewAdapter = new FirebaseRecyclerAdapter<Item, ItemHolder>(Item.class,
                R.layout.adapter_item_layout, ItemHolder.class, mImageRef) {
            @Override
            public void populateViewHolder(ItemHolder itemHolder, Item item, int position) {
                itemHolder.setImage(item.getImageUrl());
            }
        };

        rv.setAdapter(mRecyclerViewAdapter);*/

        /**
         *  Create Firebase references
         */
        Firebase mRef = new Firebase(Constants.FIREBASE_URL);

        mImageRef = mRef.limitToLast(50);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(
                R.id.firebaserecyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerAdapter<Item, ItemHolder> mRecyclerViewAdapter = new FirebaseRecyclerAdapter<Item, ItemHolder>(Item.class,
                R.layout.adapter_item_layout, ItemHolder.class, mImageRef) {
            @Override
            public void populateViewHolder(ItemHolder itemHolder, Item item, int position) {
                itemHolder.setImage(item.getImageUrl());
            }
        };

        recyclerView.setAdapter(mRecyclerViewAdapter);

        return rootView;
    }


    //@Override
    public Firebase getFirebaseRef() {
        return mRef;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        View mView;
        Context mContext;

        public ItemHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setImage(String imageUrl) {
            ImageView imageView = (ImageView) mView.findViewById(R.id.adapter_item_imageview);
            Picasso.with(mContext).load(imageUrl).noFade()
                    .into(imageView);
        }
    }

    private void getDoodleData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            SyncAdapter.syncImmediately(getActivity());
            Log.v(LOG_TAG, "getDoodleData() - syncImmediately()");
        }
    }

    public boolean isRvFilled(RecyclerView view) {
        boolean x = false;
        if (view != null) {
            x = true;
        } else {
            x = false;
        }
        return x;
    }

    public boolean isRootFilled(View view) {
        boolean x = false;
        if (view != null) {
            x = true;
        } else {
            x = false;
        }
        return x;
    }
}

