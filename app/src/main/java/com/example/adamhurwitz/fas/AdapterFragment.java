package com.example.adamhurwitz.fas;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adamhurwitz.fas.utils.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

;

/**
 * A placeholder fragment containing a simple view.
 */
// public class AdapterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
public class AdapterFragment extends Fragment {
    private final String LOG_TAG = AdapterFragment.class.getSimpleName();
    //private ListView mListView;
    private Firebase mRef;
    private String string;
    RecyclerView mListView;
    private RecyclerView mImages;
    private FirebaseRecyclerAdapter<Item, ItemHolder> mRecycleViewAdapter;
    private Query mImageRef;
    Context mContext;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Initialize UI elements
         */
        View rootView = inflater.inflate(R.layout.adapter_view_layout, container, false);
        //initializeScreen(rootView);

        /**
         * Create Firebase references
         */
        Firebase mRef = new Firebase(Constants.FIREBASE_URL_POPULAR_LIST);

        mImages = (RecyclerView) getActivity().findViewById(R.id.recyclerview_id);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(false);
        mImages.setLayoutManager(new LinearLayoutManager(getActivity()));
        mImages.setHasFixedSize(true);
        mImages.setLayoutManager(manager);

        mRecycleViewAdapter = new FirebaseRecyclerAdapter<Item, ItemHolder>(Item.class,
                R.layout.adapter_item_layout, ItemHolder.class, mImageRef) {
            @Override
            public void populateViewHolder(ItemHolder imageView, Item image, int position) {
                imageView.setImage(image.getImageUrl());
            }
        };

        mImages.setAdapter(mRecycleViewAdapter);

        /**
         * Add ValueEventListeners to Firebase references
         * to control get data and control behavior and visibility of elements
         */
        /*mAdapter = new Adapter(getActivity(), Item.class,
                R.layout.adapter_item_layout, activeListsRef);*/

        /**
         * Set the adapter to the mListView
         */
        // mListView.setAdapter(mAdapter);


        /**
         * Set interactive bits, such as click events and adapters
         */
        /*mImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Click Listener", Toast.LENGTH_SHORT).show();
            }
        });*/

        return rootView;
    }

    /**
     * Link list view from XML
     */
  /*  private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.listview_id);
    }*/

    @Override
    public Firebase getFirebaseRef() {
        return mRef;
    }

    public static class Item {
        String imageUrl;

        public Item() {

        }

        public Item(String image_url) {
            this.imageUrl = image_url;
        }

        public String getImageUrl() {
            return imageUrl;
        }
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
}

