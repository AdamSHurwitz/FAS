package com.example.adamhurwitz.fas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Vector;

/**
 * An asynchronous task object to fetch data about the Google doodles.
 */
public class FetchDoodleDataTask extends AsyncTask<String, Void, Void> {

    public static final String LOG_TAG = FetchDoodleDataTask.class.getSimpleName();
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

    private final Context context;
    private AsyncCursorAdapter asyncCursorAdapter;
    Vector<ContentValues> cVVector;

    String mParams1;

    /**
     * Constructor for the AsyncParcelableFetchDoodleDataTask object.
     *
     * @param context            Context of Activity
     * @param asyncCursorAdapter An adapter to recycle items correctly in the grid view.
     */
    public FetchDoodleDataTask(Context context, AsyncCursorAdapter asyncCursorAdapter) {
        this.asyncCursorAdapter = asyncCursorAdapter;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        mParams1 = params[1];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // This variable will contain the raw JSON response as a string.
        String doodleDataJsonResponse = null;

        try {
            if (!params[1].equals("popular")) {
                // Construct the URL to fetch data from and make the connection.
                Uri builtUri = Uri.parse(FAS_API_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAMETER, params[0])
                        .appendQueryParameter(params[1], "true")
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            } else {    // Construct the URL to fetch data from and make the connection.
                Uri builtUri = Uri.parse(FAS_API_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAMETER, params[0])
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            }

            // See if the input stream is not null and a connection could be made. If it is null, do
            // not process any further.
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
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
                return null;
            }

            doodleDataJsonResponse = buffer.toString();

        } catch (IOException e) {
            // If there was no valid Google doodle data returned, there is no point in attempting to
            // parse it.
            Log.e(LOG_TAG, "Error, IOException.", e);
            return null;
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
                    doodleDataJsonResponse);
            parseDoodleDataJsonResponse(doodleDataJsonResponse);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // Any other case that gets here is an error that was not caught, so return null.
        return null;
    }

    /**
     * Parses the JSON response for information about the Google doodles.
     *
     * @param doodleDataJsonResponse A JSON string which needs to be parsed for data about the
     *                               Google doodles.
     */
    private void parseDoodleDataJsonResponse(String doodleDataJsonResponse)
            throws JSONException {
        try {
            JSONArray doodlesInfo = new JSONArray(doodleDataJsonResponse);
            // Initialize ArrayList of Content Values size of data Array length
            cVVector = new Vector<>(doodlesInfo.length());
            for (int index = 0; index < doodlesInfo.length(); index++) {
                JSONObject doodleDataJson = doodlesInfo.getJSONObject(index);
                putDoodleDataIntoDb(
                        doodleDataJson.getString(ID_PARAMETER),
                        doodleDataJson.getString(TITLE_PARAMETER),
                        doodleDataJson.getString(RELEASE_DATE_PARAMETER),
                        doodleDataJson.getString(DESCRIPTION_PARAMETER),
                        doodleDataJson.getString(SEARCH_STRINGS),
                        doodleDataJson.getInt(PRICE_PARAMETER),
                        doodleDataJson.getString(IMAGE_URL_PARAMETER),
                        doodleDataJson.getDouble(POPULARITY),
                        doodleDataJson.getBoolean(IS_RECENT_BOOLEAN),
                        doodleDataJson.getBoolean(IS_VINTAGE_BOOLEAN));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void putDoodleDataIntoDb(String item_id, String title, String date, String description,
                                    String search_strings, int price, String image,
                                    Double popularity, Boolean recent, Boolean vintage) {
        Log.v("putInfoIntoDatabase", "called here");

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

        cVVector.add(values);

        // Insert the new row, returning the primary key value of the new row
        long thisRowID;

        // If you are querying entire table, can leave everything as Null
        // Querying when Item ID Exists
        Cursor cursor = context.getContentResolver().query(
                Contract.ProductData.CONTENT_URI,  // The table to query
                null,                                // The columns to return
                Contract.ProductData.COLUMN_NAME_ITEMID + "= ?", // The columns for the WHERE clause
                new String[] {item_id}, // The values for the WHERE clause
                Contract.ProductData._ID + " DESC"                                 // The sort order
        );

       /* if (cursor.getCount() == 0) {
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                context.getContentResolver().bulkInsert(Contract.ProductData.CONTENT_URI, cvArray);
            }
        }*/
        // If the Item ID Does Not Exist, Insert All Values
        if (cursor.getCount() == 0) {
            Uri uri;
            uri = context.getContentResolver().insert(
                    Contract.ProductData.CONTENT_URI, values);
        }

    }
}