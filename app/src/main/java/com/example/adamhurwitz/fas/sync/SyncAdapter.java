package com.example.adamhurwitz.fas.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.adamhurwitz.fas.R;
import com.example.adamhurwitz.fas.data.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = SyncAdapter.class.getSimpleName();
    // Interval at which to sync with the data
    public static final int SYNC_INTERVAL = 0;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL;
    private static final int PRODUCT_NOTIFICATION_ID = 3004;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public static final String FAS_API_BASE_URL = "https://fas-api.appspot.com/";
    public static final String SORT_PARAMETER = "sort_order";
    public static final String ID_PARAMETER = "item_id";
    public static final String TITLE_PARAMETER = "title";
    public static final String RELEASE_DATE_PARAMETER = "release_date";
    public static final String DESCRIPTION_PARAMETER = "description";
    public static final String SEARCH_STRINGS = "search_strings";
    public static final String PRICE_PARAMETER = "price";
    public static final String IMAGE_URL_PARAMETER = "image_url";
    public static final String POPULARITY = "popularity";
    public static final String IS_RECENT_BOOLEAN = "recent";
    public static final String IS_VINTAGE_BOOLEAN = "vintage";

    private static final String[] NOTIFY_PRODUCT_PROJECTION = new String[]{
            Contract.ProductData._ID,
            Contract.ProductData.COLUMN_NAME_TITLE,
            Contract.ProductData.COLUMN_NAME_PRICE,
    };

    // these indices must match the projection
    private static final int INDEX_ID = 0;
    private static final int INDEX_TITLE = 1;
    private static final int INDEX_PRICE = 2;

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        // Could call SharedPreference value here
        Log.d(LOG_TAG, "onPerformSync Called.");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // This variable will contain the raw JSON response as a string.
        String jsonResponse = null;

        try {
            // Construct the URL to fetch data from and make the connection.
            Uri builtUri = Uri.parse(FAS_API_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAMETER, "item_id.desc")
                    .build();
            URL url = new URL(builtUri.toString());
            Log.v("REC/VINT_URL_HERE: ", builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // See if the input stream is not null and a connection could be made. If it is null, do
            // not process any further.
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return;
            }

            // Read the input stream to see if any valid response was give.
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Add new to make debugging easier.
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                // If the stream is empty, do not process any further.
                return;
            }

            jsonResponse = buffer.toString();
            Log.v("JSON_String_here: ", jsonResponse);

        } catch (IOException e) {
            // If there was no valid Google doodle data returned, there is no point in attempting to
            // parse it.
            Log.e(LOG_TAG, "Error, IOException.", e);
            return;
        } finally {
            // Make sure to close the connection and the reader no matter what.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream ", e);
                }
            }
        }

        // If valid data was returned, return the parsed data.
        try {
            Log.i(LOG_TAG, "The Google doodle data that was returned is: " +
                    jsonResponse);
            parseJSONResponse(jsonResponse);
            return;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // Any other case that gets here is an error that was not caught, so return null.
        return;
    }

    /**
     * Parses the JSON response for information about the Google doodles.
     *
     * @param jsonResponse A JSON string which needs to be parsed for data about the
     *                     Google doodles.
     */
    private void parseJSONResponse(String jsonResponse)
            throws JSONException {
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            // Initialize ArrayList of Content Values size of data Array length
            for (int index = 0; index < jsonarray.length(); index++) {
                JSONObject jsonObject = jsonarray.getJSONObject(index);
                putDataIntoDb(
                        jsonObject.getString(ID_PARAMETER),
                        jsonObject.getString(TITLE_PARAMETER),
                        jsonObject.getString(RELEASE_DATE_PARAMETER),
                        jsonObject.getString(DESCRIPTION_PARAMETER),
                        jsonObject.getString(SEARCH_STRINGS),
                        jsonObject.getInt(PRICE_PARAMETER),
                        jsonObject.getString(IMAGE_URL_PARAMETER),
                        jsonObject.getDouble(POPULARITY),
                        jsonObject.getBoolean(IS_RECENT_BOOLEAN),
                        jsonObject.getBoolean(IS_VINTAGE_BOOLEAN));
                Log.v("JSON_RESPONSE", jsonResponse);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void putDataIntoDb(String item_id, String title, String date, String description,
                              String search_strings, int price, String image,
                              Double popularity, Boolean recent, Boolean vintage) {

        // Put Info into Database

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Contract.ProductData.COLUMN_NAME_ITEMID, item_id);
        values.put(Contract.ProductData.COLUMN_NAME_TITLE, title);
        values.put(Contract.ProductData.COLUMN_NAME_RELEASEDATE, date);
        values.put(Contract.ProductData.COLUMN_NAME_DESCRIPTION, description);
        values.put(Contract.ProductData.COLUMN_NAME_SEARCHSTRINGS, search_strings);
        values.put(Contract.ProductData.COLUMN_NAME_PRICE, price);
        values.put(Contract.ProductData.COLUMN_NAME_IMAGEURL, image);
        values.put(Contract.ProductData.COLUMN_NAME_DESCRIPTION, description);
        values.put(Contract.ProductData.COLUMN_NAME_POPULARITY, popularity);
        values.put(Contract.ProductData.COLUMN_NAME_RECENT, recent);
        values.put(Contract.ProductData.COLUMN_NAME_VINTAGE, vintage);
        values.put(Contract.ProductData.COLUMN_NAME_FAVORITE, "1");
        values.put(Contract.ProductData.COLUMN_NAME_CART, "1");

        // If you are querying entire table, can leave everything as Null
        // Querying when Item ID Exists
        Cursor cursor = getContext().getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null,                                // The columns to return
                Contract.ProductData.COLUMN_NAME_ITEMID + "= ?", // The columns for the WHERE clause
                new String[]{item_id}, // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );


        // If the Item ID Does Not Exist, Insert All Values
        if (cursor.getCount() == 0) {
            Uri uri;
            uri = getContext().getContentResolver().insert(
                    Contract.ProductData.CONTENT_URI, values);
        }
    }

    /*private void notifyData() {
        Context context = getContext();

        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        long lastSync = prefs.getLong(lastNotificationKey, 0);
        String displayNotificationsKey = context.getString(R.string.notification_settings_key);
        boolean notificationsPref = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.notification_default)));
        Log.v(LOG_TAG, "Notification SharedPref: " + notificationsPref);

        if (notificationsPref && System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
            Log.v(LOG_TAG, "Notification SharedPref: " + notificationsPref);
            // Last sync was more than 1 day ago, let's send a notification

            Uri uri = Contract.ProductData.CONTENT_URI;

            // we'll query our contentProvider, as always
            Cursor cursor = context.getContentResolver().query(uri, NOTIFY_PRODUCT_PROJECTION,
                    null, null, null);

            if (cursor.moveToFirst()) {
                int product_index = cursor.getInt(INDEX_ID);
                String price = cursor.getString(INDEX_PRICE);
                double product_title = cursor.getDouble(INDEX_TITLE);

                int iconId = R.drawable.and_green;
                String title = context.getString(R.string.app_name);

                // Define the text of the forecast.
                String contentText = "New Doodle Available " + product_title + " | " + price;

                // NotificationCompatBuilder is a very convenient way to build backward-compatible
                // notifications.  Just throw in some data.
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setSmallIcon(iconId)
                                .setContentTitle(title)
                                .setContentText(contentText);

                // Make something interesting happen when the user clicks on the notification.
                // In this case, opening the app is sufficient.
                Intent resultIntent = new Intent(context, MainActivity.class);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                // PRODUCT_NOTIFICATION_ID allows you to update the notification later on.
                mNotificationManager.notify(PRODUCT_NOTIFICATION_ID, mBuilder.build());


                //refreshing last sync
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(lastNotificationKey, System.currentTimeMillis());
                editor.commit();
            }
        }
    }*/


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
   /* public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }*/

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        // SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string
                .content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}