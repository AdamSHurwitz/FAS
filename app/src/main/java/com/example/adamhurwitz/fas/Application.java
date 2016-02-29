package com.example.adamhurwitz.fas;

import com.firebase.client.Firebase;

/**
 * Includes one-time initialization of Firebase related code
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        Firebase.setAndroidContext(this);
    }

}
