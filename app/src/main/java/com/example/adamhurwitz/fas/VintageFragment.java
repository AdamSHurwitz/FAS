package com.example.adamhurwitz.fas;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adamhurwitz.fas.data.Contract;
import com.example.adamhurwitz.fas.sync.SyncAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class VintageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Empty constructor for the PopularFragment class.
     */
    public VintageFragment() {
    }
    MyListCursorAdapter recyclerAdapter;

    private final String LOG_TAG = PopularFragment.class.getSimpleName();
    private static final int LOADER_FRAGMENT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDoodleData();

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.recycler_layout, container, false);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        setupRecyclerView(rv);

        return rv;
    }

    // helper method to create LayoutManager and Adapter
    private void setupRecyclerView(RecyclerView recyclerView) {
        // set LinearLayoutManager
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // If you are querying entire table, can leave everything as Null
        Cursor cursor = getContext().getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_VINTAGE + "= ?", // The columns for the WHERE clause
                new String[] {"1"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );
        Log.v(LOG_TAG, "setupRecyclerView() - cursor.getCount() " + cursor.getCount());
        // set GridLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerAdapter = new MyListCursorAdapter(getActivity(), cursor);

        recyclerView.setAdapter(recyclerAdapter);
    }

    private void getDoodleData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            SyncAdapter.syncImmediately(getActivity());
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_FRAGMENT, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                Contract.ProductData.CONTENT_URI, null,
                Contract.ProductData.COLUMN_NAME_VINTAGE + " = ? AND "
                        + Contract.ProductData.COLUMN_NAME_RECENT + " = ?",
                new String[] {"1","0"}, Contract.ProductData.COLUMN_NAME_RELEASEDATE+ " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        recyclerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        recyclerAdapter.swapCursor(null);
    }

}
