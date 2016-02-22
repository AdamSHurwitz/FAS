package com.example.adamhurwitz.fas;

import android.database.Cursor;
import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class FavoritesActivity extends AppCompatActivity {
    public static final String LOG_TAG = FavoritesActivity.class.getSimpleName();

    MyListCursorAdapter recyclerAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_recycler_layout);

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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // create RecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        //setupRecyclerView(rv);

        // set LinearLayoutManager
        rv.setLayoutManager(new LinearLayoutManager(this));

        // query data
        Cursor cursor = this.getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_VINTAGE + "= ? AND "
                        + Contract.ProductData.COLUMN_NAME_RECENT + " = ? ", // The columns for the WHERE clause
                new String[]{"0", "0"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );
        Log.v(LOG_TAG, "onCreate() - cursor.getCount() " + cursor.getCount());

        // attach adapter to data
        recyclerAdapter = new MyListCursorAdapter(this, cursor);
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

        // If you are querying entire table, can leave everything as Null
        Cursor cursor = this.getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_FAVORITE + " = ?",
                new String[]{"2"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"      // The sort order
        );

        //asyncCursorAdapter = new AsyncCursorAdapter(this, cursor, 0);

        // Get a reference to the grid view layout and attach the adapter to it.
        /*GridView gridView = (GridView) this.findViewById(R.id.grid_view_layout);
        gridView.setAdapter(asyncCursorAdapter);*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Favorites Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.adamhurwitz.fas/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Favorites Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.adamhurwitz.fas/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}