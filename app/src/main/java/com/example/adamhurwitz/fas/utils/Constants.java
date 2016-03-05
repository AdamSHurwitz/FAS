package com.example.adamhurwitz.fas.utils;
import com.example.adamhurwitz.fas.BuildConfig;

/**
 * Constants class store most important strings and paths of the app
 */
public final class Constants {

    /**
     * Constants related to locations in Firebase, such as the name of the node
     * where active lists are stored (ie "activeLists")
     */
    public static final String FIREBASE_POPULAR_LIST = "popularList";
    public static final String FIREBASE_RECENT_LIST = "recentList";
    public static final String FIREBASE_VINTAGE_LIST = "vintageList";


    /**
     * Constants for Firebase object properties
     */
    public static final String FIREBASE_PROPERTY_QTY = "Qty";


    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_POPULAR_LIST = FIREBASE_URL + "/" + FIREBASE_POPULAR_LIST;
    public static final String FIREBASE_URL_RECENT_LIST = FIREBASE_URL + "/" + FIREBASE_RECENT_LIST;
    public static final String FIREBASE_URL_VINTAGE_LIST = FIREBASE_URL + "/" + FIREBASE_VINTAGE_LIST;


    /**
     * Constants for bundles, extras and shared preferences keys
     */


}
