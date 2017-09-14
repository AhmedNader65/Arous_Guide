package com.company.brand.alarousguide.CustomerActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Adapters.CustomerOffersAdapter;
import com.company.brand.alarousguide.Models.Offer;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed on 09/09/17.
 */

public class SearchActivityResult extends AppCompatActivity implements View.OnClickListener {


    TextView tv_myOffers, tv_notFoundOffers, tv_no_internet,title;
    Button bt_retry;
    ImageView backBtn;
    RecyclerView rv_recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar pb_progressBar;
    Toolbar tb_toolBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Offer> offerArrayList;
    private int userId;

    private SpannableString uploadingMessage, error,done;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        mSetupRefs();
        mSetupListener();
        mSetupToasts();
        if (Util.isOnline(this)){

            pb_progressBar.setVisibility(View.VISIBLE);
            getResult();
        } else {

            tv_no_internet.setVisibility(View.VISIBLE);
            bt_retry.setVisibility(View.VISIBLE);
        }
    }

    private void mSetupListener(){
        bt_retry.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }
    private void mSetupRefs(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", 0);
        editor = sharedPreferences.edit();
        tb_toolBar = (Toolbar) findViewById(R.id.tb_toolBar);
        tv_myOffers = (TextView) findViewById(R.id.tv_myOffers);
        tv_myOffers.setText(getString(R.string.search_result));
        tv_notFoundOffers = (TextView) findViewById(R.id.tv_notFoundOffers);
        tv_no_internet = (TextView) findViewById(R.id.tv_no_internet);
        bt_retry = (Button) findViewById(R.id.bt_retry);
        backBtn = (ImageView) findViewById(R.id.iv_back);
        rv_recyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pb_progressBar = (ProgressBar) findViewById(R.id.pb_progressBar);

        tv_myOffers.setTypeface(Fonts.mSetupFontMedium(this));
        tv_notFoundOffers.setTypeface(Fonts.mSetupFontBold(this));
        tv_no_internet.setTypeface(Fonts.mSetupFontMedium(this));
        bt_retry.setTypeface(Fonts.mSetupFontMedium(this));
        mSetupToasts();
    }
    private void getResult(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.OFFER_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("0")){
                        tv_notFoundOffers.setVisibility(View.VISIBLE);
                        pb_progressBar.setVisibility(View.GONE);

                    }else if (success.equals("1")){
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        offerArrayList = new ArrayList<>();
                        for(int i = 0 ; i <dataArray.length();i++){
                            Offer offer = new Offer();
                            JSONObject offerObj = dataArray.getJSONObject(i);
                            if(offerObj.getInt("published")==0)
                                continue;
                            offer.setName(offerObj.getString("name"));;
                            offer.setOfferId(Integer.parseInt(offerObj.getString("offer_id")));
                            offer.setPriceFrom(Integer.parseInt(offerObj.getString("price_from")));
                            offer.setPriceTo(Integer.parseInt(offerObj.getString("price_to")));
                            offer.setRate(Integer.parseInt(offerObj.getString("rate")));
                            offer.setSpecial(Integer.parseInt(offerObj.getString("special_offer"))== 1);
                            offer.setImg1(offerObj.getString("image1"));
                            offerArrayList.add(offer);
                        }
                        CustomerOffersAdapter adapter = new CustomerOffersAdapter(offerArrayList);
                        rv_recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivityResult.this));
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
                Log.e("ERROR" , ""+error.getMessage());
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> params = new HashMap<>();
                if(getIntent().hasExtra("word")){
                    Log.e("word",getIntent().getStringExtra("word"));
                    params.put("word",getIntent().getStringExtra("word"));
                }
                if(getIntent().hasExtra("country_id")){
                    Log.e("country_id",getIntent().getStringExtra("country_id"));
                    params.put("country_id",getIntent().getStringExtra("country_id"));
                }
                if(getIntent().hasExtra("city_id")){
                    Log.e("city_id",getIntent().getStringExtra("city_id"));
                    params.put("city_id",getIntent().getStringExtra("city_id"));
                }
                if(getIntent().hasExtra("price_from")){
                    Log.e("price_from",getIntent().getStringExtra("price_from"));
                    params.put("price_from",getIntent().getStringExtra("price_from"));
                }
                if(getIntent().hasExtra("price_to")){
                    Log.e("price_to",getIntent().getStringExtra("price_to"));
                    params.put("price_to",getIntent().getStringExtra("price_to"));
                }
                if(getIntent().hasExtra("section_id")){
                    Log.e("section_id",getIntent().getStringExtra("section_id"));
                    params.put("section_id",getIntent().getStringExtra("section_id"));
                }
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_retry:
                startActivity(new Intent(this, CustomerOffersActivity.class));
                finish();
                break;
            case R.id.iv_back:
                finish();
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
}
