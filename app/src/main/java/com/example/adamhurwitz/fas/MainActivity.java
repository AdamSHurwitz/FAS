package com.example.adamhurwitz.fas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    // DrawerView: Initialize DrawerLayout - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private DrawerLayout mDrawerLayout;
    // ---------------------------------------------------------------------------------------------
    private InterstitialAd mInterstitialAd;
    private Button mLoadInterstitialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START instantiate_interstitial_ad]
        // Create an InterstitialAd object. This same object can be re-used whenever you want to
        // show an interstitial.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6447516466910649/2278196412");
        // [END instantiate_interstitial_ad]

        // [START create_interstitial_ad_listener]
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                beginSecondActivity();
            }
        });
        // [END create_interstitial_ad_listener]

        // [START display_interstitial_ad]
       /* mLoadInterstitialButton = (Button) findViewById(R.id.load_interstitial_button);
        mLoadInterstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    beginSecondActivity();
                }
            }
        });*/
        // [END display_interstitial_ad]

        /**
         * Create Firebase references
         */
        //Firebase refListName = new Firebase(Constants.FIREBASE_URL).child(Constants.FIREBASE_PROPERTY_QTY);

        /**
         * Add ValueEventListeners to Firebase references
         * to control get data and control behavior and visibility of elements
         */
       /* refListName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // You can get the text using getValue. Since the DataSnapshot is of the exact
                // data you asked for (the node listName), when you use getValue you know it
                // will return a String.
                long qty = (long) dataSnapshot.getValue();
                // Now take the TextView for the list name
                // and set it's value to listName.
                Log.v(LOG_TAG, "Firebase test to get qty - " + qty);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/

        // Initialize Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(new SampleDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        // DrawerView: Build Action Bar - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ----------------------------------------------------------------------------------------

        // Navigation View
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Click Listeners for DrawerView Menu Items
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        //Closing drawer on item click
                        mDrawerLayout.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {

                            // For rest of the options we just show a toast on click

                            case R.id.search_menu_itm:
                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    beginSecondActivity();
                                }
                                /*Toast.makeText(getApplicationContext(), "OPEN SEARCH BOX IN " +
                                        "ACTION BAR", Toast.LENGTH_SHORT).show();*/
                                return true;

                            case R.id.favorites_menu_itm:
                                Intent favoriteIntent = new Intent(getApplicationContext(),
                                        FavoritesActivity.class);
                                startActivity(favoriteIntent);
                                return true;

                            case R.id.cart_menu_itm:
                                Intent cartActivity = new Intent(getApplicationContext(),
                                        CartActivity.class);
                                startActivity(cartActivity);
                                return true;

                            default:
                                Toast.makeText(getApplicationContext(), "Somethings Wrong",
                                        Toast.LENGTH_SHORT).show();
                                return true;

                        }
                    }
                });

        // Initialize Drawer Layout and ActionBarToggle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to
                // happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to
                // happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        // ----------------------------------------------------------------------------------------

        // NavTabs: This loads viewPager for multiple tab views

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    /**
     * Load a new interstitial ad asynchronously.
     */
    // [START request_new_interstitial]
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
    // [END request_new_interstitial]

    private void beginSecondActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // NavTabs: Add Fragments to the TabsAdapter, TabsAdapter recycles views - - - - - - - - - - - -

    private void setupViewPager(ViewPager viewPager) {
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(new PopularFragment(), "Popular");
        adapter.addFragment(new RecentFragment(), "Recent");
        adapter.addFragment(new VintageFragment(), "Vintage");
        viewPager.setAdapter(adapter);
    }
    // ---------------------------------------------------------------------------------------------

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

    // Create class for Stetho
    private static class SampleDumperPluginsProvider implements DumperPluginsProvider {
        private final Context mContext;

        public SampleDumperPluginsProvider(Context context) {
            mContext = context;
        }

        @Override
        public Iterable<DumperPlugin> get() {
            ArrayList<DumperPlugin> plugins = new ArrayList<>();
            for (DumperPlugin defaultPlugin : Stetho.defaultDumperPluginsProvider(mContext).get()) {
                plugins.add(defaultPlugin);
            }
            //plugins.add(new SyncAdapterFragment());
            return plugins;
        }
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }
    }
}
