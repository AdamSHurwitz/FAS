package com.example.adamhurwitz.fas;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.adamhurwitz.fas.data.Contract;
import com.google.android.gms.common.api.GoogleApiClient;


public class FavoritesActivity extends AppCompatActivity {
    public static final String LOG_TAG = FavoritesActivity.class.getSimpleName();

    FavoriteCursorAdapter recyclerAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_favorite_layout);

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

        // Back Button To Go Home
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e(LOG_TAG, "The getSupportActionBar() method threw a null pointer exception.");
        }
        // If your minSdkVersion is 11 or higher, instead use:
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // create RecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview_favorite_id);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        // set LinearLayoutManager
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // query data
        Cursor cursor = this.getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_FAVORITE + "= ?", // The columns for the WHERE clause
                new String[]{"2"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );
        Log.v(LOG_TAG, "onCreate() - cursor.getCount() " + cursor.getCount());

        // attach adapter to data
        recyclerAdapter = new FavoriteCursorAdapter(this, cursor);
        rv.setAdapter(recyclerAdapter);

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }
}