package com.example.adamhurwitz.fas;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adamhurwitz.fas.data.CursorContract;
import com.example.adamhurwitz.fas.data.CursorDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularFragment extends Fragment {

    private AsyncCursorAdapter asyncCursorAdapter;

    /**
     * Empty constructor for the PopularFragment class.
     */
    public PopularFragment() {
    }

    String doodleTitle = "";
    String doodleFavortie = "";
    Cursor itemCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview_layout, container, false);

        // Access database
        CursorDbHelper mDbHelper = new CursorDbHelper(getContext());
        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CursorContract.ProductData._ID + " DESC";

        String[] whereValues = {"0", "0"};

        // If you are querying entire table, can leave everything as Null
        Cursor cursor = db.query(
                CursorContract.ProductData.TABLE_NAME,  // The table to query
                null, // The columns to return
                CursorContract.ProductData.COLUMN_NAME_VINTAGE + " = ? AND " + CursorContract.ProductData.
                        COLUMN_NAME_RECENT + " = ? ", // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        asyncCursorAdapter = new AsyncCursorAdapter(getActivity(), cursor, 0);

        // Get a reference to the grid view layout and attach the adapter to it.
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_layout);
        gridView.setAdapter(asyncCursorAdapter);

        //TODO: Add favorite button to main views
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView favoriteButton = (ImageView) view.findViewById(
                        R.id.gridItem_favorite_button);

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                itemCursor = cursor;
                String item_id = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_ITEMID));
                String title = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_TITLE));
                doodleTitle = title;
                String image = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_IMAGEURL));
                String description = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_PRICE));
                String release_date = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_RELEASEDATE));
                String favorite = cursor.getString(cursor.getColumnIndex((
                        CursorContract.ProductData.COLUMN_NAME_FAVORITE)));
                doodleFavortie = favorite;

                String[] doodleDataItems = {item_id, title, image, description, price, release_date,
                        favorite};

                Intent intent = new Intent(getActivity(),
                        DetailActivity.class);

                intent.putExtra("Cursor Doodle Attributes", doodleDataItems);

                startActivity(intent);

                favoriteButton.setOnClickListener(new View.OnClickListener() {
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
                });
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        asyncCursorAdapter.notifyDataSetChanged();
        getDoodleData();
    }

    private void getDoodleData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // Make sure that the device is actually connected to the internet before trying to get data
        // about the Google doodles.
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            FetchDoodleDataTask doodleTask = new FetchDoodleDataTask(asyncCursorAdapter,
                    getContext());
            doodleTask.execute("popularity.desc", "popular");
        }
    }
}
