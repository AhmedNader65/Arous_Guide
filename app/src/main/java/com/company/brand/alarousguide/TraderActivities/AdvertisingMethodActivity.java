package com.company.brand.alarousguide.TraderActivities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.databinding.ActivityAdvertisingMethodBinding;

public class AdvertisingMethodActivity extends AppCompatActivity {
    ActivityAdvertisingMethodBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_advertising_method);
        binding.txt1.setTypeface(Fonts.mSetupFontBold(this));
        binding.txt2.setTypeface(Fonts.mSetupFontBold(this));
        binding.txt3.setTypeface(Fonts.mSetupFontBold(this));
        binding.txt4.setTypeface(Fonts.mSetupFontBold(this));
        binding.txt5.setTypeface(Fonts.mSetupFontBold(this));
        binding.txt6.setTypeface(Fonts.mSetupFontBold(this));
        binding.txt7.setTypeface(Fonts.mSetupFontBold(this));
        binding.txt8.setTypeface(Fonts.mSetupFontBold(this));
        binding.tvMyProfile.setTypeface(Fonts.mSetupFontBold(this));
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
