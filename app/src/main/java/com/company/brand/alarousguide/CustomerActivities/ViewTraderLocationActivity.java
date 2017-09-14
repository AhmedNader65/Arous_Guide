package com.company.brand.alarousguide.CustomerActivities;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class ViewTraderLocationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    TextView tv_addOfferLocation;
    ImageView iv_back;
    private GoogleMap gMap;
    private String location;



    private void mSetupRefs(){
        tv_addOfferLocation = (TextView) findViewById(R.id.tv_OfferLocation);
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
        setContentView(R.layout.activity_mark_offer_location);mSetupRefs();
        mSetupListener();
        if (!mGoogleServicesAvailable()){
            SpannableString ss = new SpannableString(this.getString(R.string.google_play_not_active));
            ss.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.google_play_not_active).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            Toast.makeText(this, ss , Toast.LENGTH_SHORT).show();

        } else {

            mInitializeMap();
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

        location = getIntent().getStringExtra("location");
        Geocoder geocoder = new Geocoder(ViewTraderLocationActivity.this);

        try {

            List<Address> address = geocoder.getFromLocationName(location, 1);
            Address currentLocation = address.get(0);
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();
            mGetMyLocationZoom(latitude , longitude , 10);

        } catch (IOException e) {
            Log.e("IOException" , ""+e.getMessage());
        } catch (IndexOutOfBoundsException e){
            Log.e("EDITTEXT_MAP_EXCEPTION" , ""+e.getMessage());
        }

    }

    private void mGetMyLocationZoom(double latitude , double longitude , float zoom) {
        LatLng latLng = new LatLng(latitude , longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        gMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
