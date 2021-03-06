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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adamhurwitz.fas.data.Contract;
import com.example.adamhurwitz.fas.utils.Constants;
import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class CartActivity extends AppCompatActivity {
    public static final String LOG_TAG = CartActivity.class.getSimpleName();

    CartCursorAdapter recyclerAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // set the order summary
    int price;
    int totalPrice = 0;
    int qty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_cart_layout);

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
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview_cart_id);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        // set LinearLayoutManager
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // query data
        Cursor cursor = this.getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                Contract.ProductData.COLUMN_NAME_CART + "= ?", // The columns for the WHERE clause
                new String[]{"2"},                            // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );
        Log.v(LOG_TAG, "onCreate() - cursor.getCount() " + cursor.getCount());

        // attach adapter to data
        recyclerAdapter = new CartCursorAdapter(this, cursor);
        rv.setAdapter(recyclerAdapter);

        while (cursor.moveToNext()) {
            price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.ProductData
                    .COLUMN_NAME_PRICE)));
            totalPrice = totalPrice + price;
            qty = qty + 1;
        }

        TextView totalPriceId = (TextView) findViewById(R.id.totalprice_id);
        TextView totalQtyId = (TextView) findViewById(R.id.totalqty_id);
        totalPriceId.setText("$" + String.valueOf(totalPrice));
        totalQtyId.setText(String.valueOf(qty));

        Button completeBtn = (Button) findViewById(R.id.complete_btn);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Launch Android Pay", Toast.LENGTH_SHORT)
                        .show();
                // Get the reference to the root node in Firebase
                Firebase ref = new Firebase(Constants.FIREBASE_URL);
                // Get the string that the user entered into the EditText
                // Go to the "item" child node of the root node.
                // This will create the node for you if it doesn't already exist.
                // Then using the setValue menu it will set value the node to a String value.
                ref.child("Qty").setValue(qty);
            }
        });
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Favorites Page",
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
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
                Action.TYPE_VIEW, //
                "Favorites Page", //
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.adamhurwitz.fas/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}