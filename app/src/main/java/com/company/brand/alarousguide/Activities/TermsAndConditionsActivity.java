package com.company.brand.alarousguide.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Adapters.TermsAdapter;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.databinding.ActivityTermsAndConditionsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.company.brand.alarousguide.R.string.error;

public class TermsAndConditionsActivity extends AppCompatActivity {
    ActivityTermsAndConditionsBinding binding;
    ArrayList<String> conditions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_terms_and_conditions);
        binding.progress.setVisibility(View.VISIBLE);
        setTypeFaces();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.TERMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_TERMS" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){
                        binding.errorTV.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);
                        Toast.makeText(TermsAndConditionsActivity.this, error, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){
                        binding.errorTV.setVisibility(View.GONE);
                        binding.progress.setVisibility(View.GONE);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        conditions = new ArrayList<>();
                        for(int i = 0 ; i < dataArray.length();i++){
                            conditions.add(dataArray.getJSONObject(i).getString("title"));
                        }
                        binding.listView.setLayoutManager(new LinearLayoutManager(TermsAndConditionsActivity.this));
                        binding.listView.setAdapter(new TermsAdapter(conditions));
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
                params.put("language" , PreferenceManager.getDefaultSharedPreferences(TermsAndConditionsActivity.this)
                        .getString("language","en"));
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void setTypeFaces(){

        binding.tvMyProfile.setTypeface(Fonts.mSetupFontBold(this));
        binding.errorTV.setTypeface(Fonts.mSetupFontRegular(this));
    }
}
