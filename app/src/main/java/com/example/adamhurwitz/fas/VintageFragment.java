package com.example.adamhurwitz.fas;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.adamhurwitz.fas.data.Contract;

/**
 * A placeholder fragment containing a simple view.
 */
public class VintageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private AsyncCursorAdapter asyncCursorAdapter;

    /**
     * Empty constructor for the PopularFragment class.
     */
    public VintageFragment() {
    }

    String doodleTitle = "";
    String doodleFavortie = "";
    Cursor itemCursor;
    private static final int LOADER_FRAGMENT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview_layout, container, false);

        // If you are querying entire table, can leave everything as Null
        Cursor cursor = getContext().getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_VINTAGE + "= ?", // The columns for the WHERE clause
                new String[] {"1"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
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

               /* favoriteButton.setOnClickListener(new View.OnClickListener() {
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
                });*/
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
            FetchDoodleDataTask doodleTask = new FetchDoodleDataTask(getContext(),
                    asyncCursorAdapter);
            doodleTask.execute("release_date.desc", "vintage");
        }
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
        asyncCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        asyncCursorAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_FRAGMENT, null, this);
        super.onActivityCreated(savedInstanceState);
    }


}
