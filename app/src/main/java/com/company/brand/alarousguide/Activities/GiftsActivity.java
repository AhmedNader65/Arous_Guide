package com.company.brand.alarousguide.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.company.brand.alarousguide.Adapters.GiftsAdapter;
import com.company.brand.alarousguide.Models.Gift;
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

public class GiftsActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tv_Gifts, tv_notFoundOffers, tv_no_internet;
    Button bt_retry;
    RecyclerView rv_recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar pb_progressBar;
    Toolbar tb_toolBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Gift> giftArrayList;
    private int userId;
    ImageView backIV;

    private SpannableString uploadingMessage, error,done;

    private void mSetupRefs(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", 0);
        editor = sharedPreferences.edit();
        tb_toolBar = (Toolbar) findViewById(R.id.tb_toolBar);
        tv_notFoundOffers = (TextView) findViewById(R.id.tv_notFoundOffers);
        tv_no_internet = (TextView) findViewById(R.id.tv_no_internet);
        tv_Gifts = (TextView) findViewById(R.id.tv_Gifts);
        bt_retry = (Button) findViewById(R.id.bt_retry);
        rv_recyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pb_progressBar = (ProgressBar) findViewById(R.id.pb_progressBar);
        backIV = (ImageView) findViewById(R.id.iv_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftsActivity.this.finish();
            }
        });
        tv_notFoundOffers.setTypeface(Fonts.mSetupFontBold(this));
        tv_no_internet.setTypeface(Fonts.mSetupFontMedium(this));
        bt_retry.setTypeface(Fonts.mSetupFontMedium(this));
        tv_Gifts.setTypeface(Fonts.mSetupFontBold(this));
        mSetupToasts();
    }

    private void mSetupListener(){
        bt_retry.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
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
        StringRequest request = new StringRequest(Request.Method.POST, Urls.GIFTS, new Response.Listener<String>() {
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
                        giftArrayList = new ArrayList<>();
                        for(int i = 0 ; i <dataArray.length();i++){
                            Gift gift = new Gift();
                            JSONObject giftObj = dataArray.getJSONObject(i);
                            gift.setTitle(giftObj.getString("title"));
                            gift.setLink(giftObj.getString("url"));
                            gift.setImg(giftObj.getString("image"));
                            giftArrayList.add(gift);
                        }
                        GiftsAdapter adapter = new GiftsAdapter(giftArrayList);
                        rv_recyclerView.setLayoutManager(new LinearLayoutManager(GiftsActivity.this));
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
                    Toast.makeText(GiftsActivity.this,
                            "No Connection",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(GiftsActivity.this,
                            "Check username & password",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(GiftsActivity.this,
                            "Server error! try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(GiftsActivity.this,
                            "Network Error try again",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(GiftsActivity.this,
                            "ooo we have a problem",
                            Toast.LENGTH_LONG).show();
                }
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> params = new HashMap<>();

                params.put("language" , sharedPreferences.getString("language","en"));
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
                startActivity(new Intent(this, GiftsActivity.class));
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
