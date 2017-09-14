package com.company.brand.alarousguide.TraderActivities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MarkOfferLocationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    TextView tv_addOfferLocation;
    ImageView iv_back;
    private GoogleMap gMap;


    private void mSetupPermissions(){
        String permissions [] = {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this , permissions , 1);
    }

    private void mSetupRefs(){
        tv_addOfferLocation = (TextView) findViewById(R.id.tv_OfferLocation);
        tv_addOfferLocation.setText(getString(R.string.add_offer_location));
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_addOfferLocation.setTypeface(Fonts.mSetupFontMedium(this));
    }

    private void mSetupListener(){
        iv_back.setOnClickListener(this);
    }

    private boolean mGoogleServicesAvailable(){

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS){

            return true;

        } else if (api.isUserResolvableError(isAvailable)){

            Dialog errorDialog = api.getErrorDialog(this, isAvailable, 0);
            errorDialog.show();

        } else {

            SpannableString ss = new SpannableString(this.getString(R.string.cannot_connect_to_google_play));
            ss.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.cannot_connect_to_google_play).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            Toast.makeText(this, ss , Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_offer_location);
        mSetupPermissions();
        mSetupRefs();
        mSetupListener();

        if (!mGoogleServicesAvailable()){
            SpannableString ss = new SpannableString(this.getString(R.string.google_play_not_active));
            ss.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.google_play_not_active).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            Toast.makeText(this, ss , Toast.LENGTH_SHORT).show();

        } else {

            mInitializeMap();
        }

        if (!Util.isGPSOn(this)){

            Util.showGPSAlert(this);
        }
    }

    private void mInitializeMap() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {

            gMap.setMyLocationEnabled(true);

        } catch (SecurityException ex){

            Log.e("MAP_EXCEPTION" , "" + ex.getMessage());

        }


        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                double latitude = point.latitude;
                double longitude = point.longitude;
                Log.e("LAT = " , latitude + "      LON = " + longitude);
                Intent i = new Intent(MarkOfferLocationActivity.this , AddOfferDataActivity.class);
                i.putExtra("latitude" , latitude);
                i.putExtra("longitude" , longitude);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_back:
                startActivity(new Intent(this , TraderOffersActivity.class));
                finish();
                break;
        }
    }
}
