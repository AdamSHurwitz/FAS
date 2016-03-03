package com.example.adamhurwitz.fas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.adamhurwitz.fas.utils.Constants;;
import com.example.adamhurwitz.fas.model.Item;
import com.firebase.client.Firebase;

/**
 * A placeholder fragment containing a simple view.
 */
// public class AdapterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
public class AdapterFragment extends Fragment {
    private ListView mListView;
    private Adapter mAdapter;

    /**
     * Empty constructor for the PopularFragment class.
     */
    public AdapterFragment() {
        /* Required empty public constructor */
    }

    //MyListCursorAdapter recyclerAdapter;

    private final String LOG_TAG = AdapterFragment.class.getSimpleName();
    // private static final int LOADER_FRAGMENT = 0;

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
        initializeScreen(rootView);

        /**
         * Create Firebase references
         */
        Firebase activeListsRef = new Firebase(Constants.FIREBASE_URL);

        /**
         * Add ValueEventListeners to Firebase references
         * to control get data and control behavior and visibility of elements
         */
        mAdapter = new Adapter(getActivity(), Item.class,
                R.layout.adapter_item_layout, activeListsRef);

        /**
         * Set the adapter to the mListView
         */
        mListView.setAdapter(mAdapter);


        /**
         * Set interactive bits, such as click events and adapters
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }

    /**
     * Link list view from XML
     */
    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.listview_id);
    }
}

