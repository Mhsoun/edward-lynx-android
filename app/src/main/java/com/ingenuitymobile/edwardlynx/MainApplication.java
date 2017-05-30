package com.ingenuitymobile.edwardlynx;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mEmEnG-sKi on 13/12/2016.
 */

public class MainApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .setDefaultFontPath("fonts/Lato-Regular.ttf")
        .setFontAttrId(R.attr.fontPath)
        .build()
    );
  }
}
