package com.company.brand.alarousguide.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Util;

public class ChooseLanguageActivity extends AppCompatActivity implements View.OnClickListener {

    Button bt_arabic , bt_english;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private void mSetupRefs(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        bt_arabic = (Button) findViewById(R.id.bt_arabic);
        bt_english = (Button) findViewById(R.id.bt_english);
        bt_arabic.setTypeface(Fonts.mSetupFontBold(this));
        bt_english.setTypeface(Fonts.mSetupFontBold(this));
    }

    private void mSetupListener(){
        bt_arabic.setOnClickListener(this);
        bt_english.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        mSetupRefs();
        mSetupListener();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_arabic:
                Util.MakeLanguageAabic(this);
                startActivity(new Intent(this , LoginActivity.class));
                editor.putString("language" , "ar");
                editor.apply();
                finish();
                break;
            case R.id.bt_english:
                Util.MakeLanguageEnglish(this);
                startActivity(new Intent(this , LoginActivity.class));
                editor.putString("language" , "en");
                editor.apply();
                finish();
                break;
        }
    }
}
