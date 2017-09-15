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
import com.company.brand.alarousguide.Adapters.ImageAndTextAdapter;
import com.company.brand.alarousguide.Models.ImageAndTextModel;
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

public class AlertsActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tv_notFoundOffers, tv_no_internet,tv_Gifts;
    ImageView backIV;
    Button bt_retry;
    RecyclerView rv_recyclerView;
    LinearLayoutManager layoutManager;
    ProgressBar pb_progressBar;
    Toolbar tb_toolBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<ImageAndTextModel> alertArrayList;
    private int userId;

    private SpannableString uploadingMessage, error,done;

    private void mSetupRefs(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", 0);
        editor = sharedPreferences.edit();
       tv_notFoundOffers = (TextView) findViewById(R.id.tv_notFoundOffers);
        backIV = (ImageView) findViewById(R.id.iv_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertsActivity.this.finish();
            }
        });
        tv_Gifts = (TextView) findViewById(R.id.tv_Gifts);
        tv_Gifts.setText(getString(R.string.alerts));
        tv_no_internet = (TextView) findViewById(R.id.tv_no_internet);
        bt_retry = (Button) findViewById(R.id.bt_retry);
        rv_recyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pb_progressBar = (ProgressBar) findViewById(R.id.pb_progressBar);
        tv_notFoundOffers.setTypeface(Fonts.mSetupFontBold(this));
        tv_Gifts.setTypeface(Fonts.mSetupFontBold(this));
        tv_no_internet.setTypeface(Fonts.mSetupFontMedium(this));
        bt_retry.setTypeface(Fonts.mSetupFontMedium(this));
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
            mFetchTraderOffers();

        } else {

            tv_no_internet.setVisibility(View.VISIBLE);
            bt_retry.setVisibility(View.VISIBLE);
        }
    }

    private void mFetchTraderOffers() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.ALERTS, new Response.Listener<String>() {
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
                        alertArrayList = new ArrayList<>();
                        for(int i = 0 ; i <dataArray.length();i++){
                            ImageAndTextModel alert = new ImageAndTextModel();
                            JSONObject alertObj = dataArray.getJSONObject(i);
                            alert.setText(alertObj.getString("title"));
                            alert.setImgLink(alertObj.getString("image"));
                            alertArrayList.add(alert);
                        }
                        ImageAndTextAdapter adapter = new ImageAndTextAdapter(R.layout.alert_item,alertArrayList);
                        rv_recyclerView.setLayoutManager(new LinearLayoutManager(AlertsActivity.this));
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
                    Toast.makeText(AlertsActivity.this,
                            "No Connection",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(AlertsActivity.this,
                            "Check username & password",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(AlertsActivity.this,
                            "Server error! try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(AlertsActivity.this,
                            "Network Error try again",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(AlertsActivity.this,
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
                startActivity(new Intent(this, AlertsActivity.class));
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
