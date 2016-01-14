package com.example.adamhurwitz.fas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.adamhurwitz.fas.data.CursorContract;
import com.example.adamhurwitz.fas.data.CursorDbHelper;


public class FavoritesActivity extends AppCompatActivity {
    public static final String LOG_TAG = FavoritesActivity.class.getSimpleName();
    private AsyncCursorAdapter asyncCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_gridview_layout);

        // Status Bar: Add Color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.status_bar));
        }

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CursorDbHelper mDbHelper = new CursorDbHelper(getApplicationContext());
        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // How you want the results sorted in the resulting Cursor
        String sortOrder = CursorContract.ProductData._ID + " DESC";

        // If you are querying entire table, can leave everything as Null
        Cursor cursor = db.query(
                CursorContract.ProductData.TABLE_NAME,  // The table to query
                null, // The columns to return
                CursorContract.ProductData.COLUMN_NAME_FAVORITE + " = ?",
                new String[]{"2"},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        asyncCursorAdapter = new AsyncCursorAdapter(this, cursor, 0);

        // Get a reference to the grid view layout and attach the adapter to it.
        GridView gridView = (GridView) this.findViewById(R.id.grid_view_layout);
        gridView.setAdapter(asyncCursorAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String item_id = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_ITEMID));
                String title = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_TITLE));
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

                String[] doodleDataItems = {item_id, title, image, description, price, release_date,
                        favorite};

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                intent.putExtra("Cursor Doodle Attributes", doodleDataItems);

                startActivity(intent);
            }
        });


        // Back Button To Go Home
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(LOG_TAG, "The getSupportActionBar() method threw a null pointer exception.");
        }
        // If your minSdkVersion is 11 or higher, instead use:
        //getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
        asyncCursorAdapter.notifyDataSetChanged();
    }
}