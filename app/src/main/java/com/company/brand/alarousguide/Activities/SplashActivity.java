package com.company.brand.alarousguide.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.company.brand.alarousguide.CustomerActivities.HomeActivity;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.TraderActivities.TraderOffersActivity;
import com.company.brand.alarousguide.Utils.Util;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int userId = sharedPreferences.getInt("userId", 0);
        final int roleId = sharedPreferences.getInt("roleId", 0);
        final String lang = sharedPreferences.getString("language", "ar");

        if(lang.equals("ar")){
            Util.MakeLanguageAabic(this);
        }else{
            Util.MakeLanguageEnglish(this);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (userId !=0){

                    if (roleId == 1){

                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();

                    } else {

                        startActivity(new Intent(SplashActivity.this, TraderOffersActivity.class));
                        finish();
                    }

                } else {

                    startActivity(new Intent(SplashActivity.this , ChooseLanguageActivity.class));
                    finish();
                }
            }

        }, 2 * 1000);
    }
}
