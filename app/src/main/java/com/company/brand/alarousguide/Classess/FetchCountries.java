package com.company.brand.alarousguide.Classess;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Activities.EditProfileActivity;
import com.company.brand.alarousguide.Adapters.SpinnerAdapter;
import com.company.brand.alarousguide.Models.Country;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by waheed_manii on 8/26/2017.
 */

public class FetchCountries {

     Context context;
     Spinner sp_country;
     ArrayList<String> countryNameArrayList = new ArrayList<>();
    ArrayList<Country> countryArrayList = new ArrayList<>();
    private int selectionIndexCountry = 0;

    public FetchCountries(Context context, Spinner sp_country, ArrayList<String> countryNameArrayList, ArrayList<Country> countryArrayList){
        this.context = context;
        this.sp_country = sp_country;
        this.countryNameArrayList = countryNameArrayList;
        this.countryArrayList = countryArrayList;
    }

    public void mGetCountries(final String lang){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.COUNTRIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("COUNTRIES_RESPONSE" , response);
                try {
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");


                    if (success.equals("0")){

                        SpannableString noCountries = new SpannableString(context.getString(R.string.no_countries));
                        noCountries.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontMedium(context)) , 0 , context.getString(R.string.no_countries).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        Toast.makeText(context, noCountries , Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0 ; i < data.length() ; i++){

                            JSONObject countries = data.getJSONObject(i);
                            int country_id = countries.getInt("country_id");
                            String country_name = countries.getString("country_name");
                            try {
                                if (country_name.equals(EditProfileActivity.country)){
                                    selectionIndexCountry = i;
                                }
                            }catch (NullPointerException e){};
                            countryArrayList.add(new Country(country_id , country_name));
                            countryNameArrayList.add(country_name);
                        }
                        SpinnerAdapter adapter = new SpinnerAdapter(context , R.layout.spinner_item , countryNameArrayList);
                        sp_country.setAdapter(adapter);
                        sp_country.setSelection(selectionIndexCountry);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR_RESPONSE" , ""+error);

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> params = new HashMap<>();
                params.put("language" , lang);
                return params;
            }
        };

        requestQueue.add(request);
    }
}
