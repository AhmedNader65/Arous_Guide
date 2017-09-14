package com.company.brand.alarousguide.Activities;

import android.app.Application;
import android.content.Context;

import com.company.brand.alarousguide.Utils.LocaleHelper;

/**
 * Created by ahmed on 13/09/17.
 */

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}