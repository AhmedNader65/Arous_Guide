package com.company.brand.alarousguide.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.company.brand.alarousguide.CustomerActivities.CustomerRegistrationActivity;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.TraderActivities.TraderRegistrationActivity;
import com.company.brand.alarousguide.Utils.Fonts;

public class ChooseRegistrationTypeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back;
    Button bt_customerAccount , bt_traderAccount;

    private void mSetupRefs(){
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_customerAccount = (Button) findViewById(R.id.bt_customerAccount);
        bt_traderAccount = (Button) findViewById(R.id.bt_traderAccount);
        bt_customerAccount.setTypeface(Fonts.mSetupFontMedium(this));
        bt_traderAccount.setTypeface(Fonts.mSetupFontMedium(this));
    }

    private void mSetupListener(){
        iv_back.setOnClickListener(this);
        bt_customerAccount.setOnClickListener(this);
        bt_traderAccount.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_registration_type);
        mSetupRefs();
        mSetupListener();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_back:
                startActivity(new Intent(this , LoginActivity.class));
                finish();
                break;
            case R.id.bt_customerAccount:
                startActivity(new Intent(this , CustomerRegistrationActivity.class));
                break;
            case R.id.bt_traderAccount:
                startActivity(new Intent(this , TraderRegistrationActivity.class));
                break;
        }
    }
}
