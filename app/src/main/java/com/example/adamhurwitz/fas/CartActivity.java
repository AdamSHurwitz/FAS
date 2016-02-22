package com.example.adamhurwitz.fas;

import android.content.Intent;
import android.database.Cursor;
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

import com.example.adamhurwitz.fas.data.Contract;


public class CartActivity extends AppCompatActivity {
    public static final String LOG_TAG = CartActivity.class.getSimpleName();
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

        // Get a reference to the grid view layout and attach the adapter to it.
        GridView gridView = (GridView) this.findViewById(R.id.grid_view_layout);
        //gridView.setAdapter(asyncCursorAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String title = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                        .COLUMN_NAME_TITLE));
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


                String[] detailArray = {image, title, price, release_date,
                        description, favorite};
                startActivity(new Intent(getApplicationContext(), DetailActivity.class)
                        .putExtra("recylerAdapterExtra", detailArray));
                Log.v(LOG_TAG, "onClick() - title " + title);
            }
        });

                /*Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                intent.putExtra("Cursor Doodle Attributes", doodleDataItems);

                startActivity(intent);*/


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
    public void onStart() {
        super.onStart();

        // If you are querying entire table, can leave everything as Null
        Cursor cursor = this.getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_CART + " = ?",
                new String[]{"2"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"      // The sort order
        );

        asyncCursorAdapter = new AsyncCursorAdapter(this, cursor, 0);

        // Get a reference to the grid view layout and attach the adapter to it.
        GridView gridView = (GridView) this.findViewById(R.id.grid_view_layout);
        gridView.setAdapter(asyncCursorAdapter);

    }
}