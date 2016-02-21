package com.example.adamhurwitz.fas;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adamhurwitz.fas.data.Contract;
import com.example.adamhurwitz.fas.service.Service;

/**
 * A placeholder fragment containing a simple view.
 */
//public class PopularFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
public class PopularFragment extends Fragment {

    //private AsyncCursorAdapter asyncCursorAdapter;

    /**
     * Empty constructor for the PopularFragment class.
     */
    public PopularFragment() {
    }

    /* String doodleTitle = "";
    String doodleFavortie = "";
    Cursor itemCursor;*/
    private final String LOG_TAG = PopularFragment.class.getSimpleName();
    Cursor cursor;
    Context context;
    //private static final int LOADER_FRAGMENT = 0;

    /*public void setCursorOnAttach() {
        context = getContext();
        onAttach(context);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDoodleData();
        cursor = getContext().getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_VINTAGE + "= ? AND "
                        + Contract.ProductData.COLUMN_NAME_RECENT + " = ? ", // The columns for the WHERE clause
                new String[]{"0", "0"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );
        Log.v(LOG_TAG, "onCreateView() - " + cursor.getCount());

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.recycler_layout, container, false);
        setupRecyclerView(rv);

        //TODO: Add favorite button to main views
        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView favoriteButton = (ImageView) view.findViewById(
                        R.id.gridItem_favorite_button);

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                itemCursor = cursor;
                String item_id = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                        .COLUMN_NAME_ITEMID));
                String title = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                        .COLUMN_NAME_TITLE));
                doodleTitle = title;
                String image = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                        .COLUMN_NAME_IMAGEURL));
                String description = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                        .COLUMN_NAME_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                        .COLUMN_NAME_PRICE));
                String release_date = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                        .COLUMN_NAME_RELEASEDATE));
                String favorite = cursor.getString(cursor.getColumnIndex((
                        Contract.ProductData.COLUMN_NAME_FAVORITE)));
                doodleFavortie = favorite;

                String[] doodleDataItems = {item_id, title, image, description, price, release_date,
                        favorite};

                Intent intent = new Intent(getActivity(),
                        DetailActivity.class);

                intent.putExtra("Cursor Doodle Attributes", doodleDataItems);

                startActivity(intent);

               *//* favoriteButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "MEOW", Toast.LENGTH_SHORT).show();

                        CursorDbHelper cursorDbHelper = new CursorDbHelper(getContext());
                        SQLiteDatabase db = cursorDbHelper.getReadableDatabase();
                        Cursor cursor = db.query(
                                CursorContract.ProductData.TABLE_NAME, null,
                                CursorContract.ProductData.COLUMN_NAME_TITLE + "= ?",
                                new String[]{doodleTitle}, null, null,
                                CursorContract.ProductData._ID + " DESC");

                        ContentValues values = new ContentValues();

                        if (doodleFavortie.equals("1")) {
                            favoriteButton.setImageResource(R.drawable.star_pressed_18dp);
                            cursor.moveToFirst();
                            values.put(CursorContract.ProductData.COLUMN_NAME_FAVORITE, 2);
                        } else if (doodleFavortie.equals("2")){
                            favoriteButton.setImageResource(R.drawable.star_default_18dp);
                            cursor.moveToFirst();
                            values.put(CursorContract.ProductData.COLUMN_NAME_FAVORITE, 1);
                        }

                        db.update(
                                CursorContract.ProductData.TABLE_NAME, values,
                                CursorContract.ProductData.COLUMN_NAME_TITLE + "= ?",
                                new String[]{doodleTitle});
                        //cursor.close();
                    }
                });*//*
            }
        });*/
        //return view;
        return rv;
    }

    // helper method to create LayoutManager and Adapter
    private void setupRecyclerView(RecyclerView recyclerView) {
        // set LinearLayoutManager
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cursor = getContext().getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_VINTAGE + "= ? AND "
                        + Contract.ProductData.COLUMN_NAME_RECENT + " = ? ", // The columns for the WHERE clause
                new String[]{"0", "0"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );
        Log.v(LOG_TAG, "setupRecyclerView() - cursor count: " + cursor.getCount());
        // set GridLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerView.setAdapter(new MyListCursorAdapter(
                getActivity(),
                cursor));
    }

    private void getDoodleData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // Make sure that the device is actually connected to the internet before trying to get data
        // about the Google doodles.
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            String[] serviceArray = {"popularity.desc", "popular"};
            getActivity().startService(new Intent(getContext(), Service.class)
                    .putExtra("service_extra", serviceArray));
        }
        cursor = getContext().getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_VINTAGE + "= ? AND "
                        + Contract.ProductData.COLUMN_NAME_RECENT + " = ? ", // The columns for the WHERE clause
                new String[]{"0", "0"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );
        Log.v(LOG_TAG, "getDoodleData() - cursor count " + cursor.getCount());
    }
}
