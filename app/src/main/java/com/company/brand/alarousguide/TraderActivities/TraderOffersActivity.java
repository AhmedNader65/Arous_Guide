package com.company.brand.alarousguide.TraderActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Activities.AlertsActivity;
import com.company.brand.alarousguide.Activities.GiftsActivity;
import com.company.brand.alarousguide.Activities.LoginActivity;
import com.company.brand.alarousguide.Activities.ProfileActivity;
import com.company.brand.alarousguide.Activities.TermsAndConditionsActivity;
import com.company.brand.alarousguide.Adapters.TraderOffersAdapter;
import com.company.brand.alarousguide.Fragments.Navigation_Drawer_Fragment;
import com.company.brand.alarousguide.Models.Offer;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.Utils.Util;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TraderOffersActivity extends AppCompatActivity implements View.OnClickListener, Navigation_Drawer_Fragment.FragmentDrawerListener {


    TextView tv_myOffers, tv_notFoundOffers, tv_no_internet;
    ImageView iv_navigationIcon;
    Button bt_retry;
    RecyclerView rv_recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar pb_progressBar;
    Toolbar tb_toolBar;
    DrawerLayout dl_drawerLayout;
    Navigation_Drawer_Fragment drawerFragment;
    View containerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Offer> offerArrayList;
    private int userId;

    private SpannableString uploadingMessage, error,done;

    private void mSetupRefs(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", 0);
        editor = sharedPreferences.edit();
        tb_toolBar = (Toolbar) findViewById(R.id.tb_toolBar);
        dl_drawerLayout = (DrawerLayout) findViewById(R.id.dl_drawerLayout);
        drawerFragment = (Navigation_Drawer_Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, dl_drawerLayout, tb_toolBar);
        containerView = findViewById(R.id.fragment_navigation_drawer);
        tv_myOffers = (TextView) findViewById(R.id.tv_myOffers);
        tv_notFoundOffers = (TextView) findViewById(R.id.tv_notFoundOffers);
        tv_no_internet = (TextView) findViewById(R.id.tv_no_internet);
        iv_navigationIcon = (ImageView) findViewById(R.id.iv_navigationIcon);
        bt_retry = (Button) findViewById(R.id.bt_retry);
        rv_recyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pb_progressBar = (ProgressBar) findViewById(R.id.pb_progressBar);

        tv_myOffers.setTypeface(Fonts.mSetupFontMedium(this));
        tv_notFoundOffers.setTypeface(Fonts.mSetupFontBold(this));
        tv_no_internet.setTypeface(Fonts.mSetupFontMedium(this));
        bt_retry.setTypeface(Fonts.mSetupFontMedium(this));
        mSetupToasts();
            Util.updateToken(TraderOffersActivity.this,sharedPreferences);

    }

    private void mSetupListener(){
        iv_navigationIcon.setOnClickListener(this);
        bt_retry.setOnClickListener(this);
        drawerFragment.setDrawerListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader_offers);
        mSetupRefs();
        mSetupListener();

        if (Util.isOnline(this)){

            pb_progressBar.setVisibility(View.VISIBLE);
            mFetchTraderOffers(sharedPreferences.getInt("userId",0));

        } else {

            tv_no_internet.setVisibility(View.VISIBLE);
            bt_retry.setVisibility(View.VISIBLE);
        }
    }

    private void mFetchTraderOffers(final int traderId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.TRADER_OFFER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){
                        tv_notFoundOffers.setVisibility(View.VISIBLE);
                        pb_progressBar.setVisibility(View.GONE);
                    } else if (success.equals("1")){
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        offerArrayList = new ArrayList<>();
                        for(int i = 0 ; i <dataArray.length();i++){
                            Offer offer = new Offer();
                            JSONObject offerObj = dataArray.getJSONObject(i);
                            offer.setName(offerObj.getString("name"));
                            offer.setCityId(Integer.parseInt(offerObj.getString("city_id")));
                            offer.setCountryId(Integer.parseInt(offerObj.getString("country_id")));
                            offer.setOfferDuration(Integer.parseInt(offerObj.getString("offer_duration")));
                            offer.setOfferEnd(Integer.parseInt(offerObj.getString("offer_end")));
                            offer.setOfferId(Integer.parseInt(offerObj.getString("offer_id")));
                            offer.setOfferNum(Integer.parseInt(offerObj.getString("offer_num")));
                            offer.setPriceFrom(Integer.parseInt(offerObj.getString("price_from")));
                            offer.setPriceTo(Integer.parseInt(offerObj.getString("price_to")));
                            offer.setRate(Integer.parseInt(offerObj.getString("rate")));
                            offer.setSectionId(Integer.parseInt(offerObj.getString("section_id")));
                            offer.setLat(Double.parseDouble(offerObj.getString("lat")));
                            offer.setLng(Double.parseDouble(offerObj.getString("lng")));
                            offer.setImg1(offerObj.getString("image1"));
                            offer.setImg2(offerObj.getString("image2"));
                            offer.setImg3(offerObj.getString("image3"));
                            offer.setImg4(offerObj.getString("image4"));
                            offer.setDescription(offerObj.getString("description"));
                            offerArrayList.add(offer);
                        }
                        TraderOffersAdapter adapter = new TraderOffersAdapter(offerArrayList,2);
                        rv_recyclerView.setLayoutManager(new LinearLayoutManager(TraderOffersActivity.this));
                        rv_recyclerView.setAdapter(adapter);
                        pb_progressBar.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(TraderOffersActivity.this,
                            "No Connection",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(TraderOffersActivity.this,
                            "Check username & password",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(TraderOffersActivity.this,
                            "Server error! try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(TraderOffersActivity.this,
                            "Network Error try again",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(TraderOffersActivity.this,
                            "ooo we have a problem",
                            Toast.LENGTH_LONG).show();
                }
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> params = new HashMap<>();

                params.put("trader_id" , String.valueOf(traderId));
                return params;
            }
        };
        requestQueue.add(request);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_navigationIcon:
                dl_drawerLayout.openDrawer(containerView);
                break;
            case R.id.bt_retry:
                startActivity(new Intent(this, TraderOffersActivity.class));
                finish();
                break;

        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        switch (position){

            case 0:
                startActivity(new Intent(this , ProfileActivity.class)
                        .putExtra("userId",sharedPreferences.getInt("userId", 0))
                        .putExtra("roleId",sharedPreferences.getInt("roleId", 0)));
                break;
            case 1:
                startActivity(new Intent(this , AdvertisingMethodActivity.class));
                break;
            case 2:
                startActivity(new Intent(this , AddOfferDataActivity.class));
                break;
            case 3:
                startActivity(new Intent(this , GiftsActivity.class));
                break;
            case 4:
                startActivity(new Intent(this , AlertsActivity.class));
                break;
            case 5:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "Download Bride and groom App From This link : http://play.google.com/store/apps/details?id=" + getPackageName());
               startActivity(i);
                break;
            case 6:
                startActivity(new Intent(this , TermsAndConditionsActivity.class));
                break;
            case 7:
                Intent i_exit = new Intent(this, LoginActivity.class);
                i_exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i_exit);
                finish();
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editor.clear();
                editor.apply();
                break;
        }
    }
    private void mSetupToasts(){
        uploadingMessage = new SpannableString(this.getString(R.string.wait));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        done = new SpannableString(this.getString(R.string.done_saving));
        error = new SpannableString(this.getString(R.string.error));
        done.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.done_saving).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFetchTraderOffers(sharedPreferences.getInt("userId",0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK){
            mFetchTraderOffers(sharedPreferences.getInt("userId",0));
        }
    }
}
