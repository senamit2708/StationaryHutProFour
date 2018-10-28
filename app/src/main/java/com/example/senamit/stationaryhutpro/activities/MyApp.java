package com.example.senamit.stationaryhutpro.activities;

import android.app.Application;

public class MyApp extends Application {

    //I thinks its good not to use this class.. there is no need for me
    //to have a class which extends application class.
    @Override
    public void onCreate() {
        super.onCreate();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Rubik-BoldItalic.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

    }
}
