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
import com.company.brand.alarousguide.Models.City;
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

public class FetchCities {

    private Context context;
    private Spinner sp_city;
    private int countryId = 0;
    private ArrayList<String> cityNameArrayList = new ArrayList<>();
    private ArrayList<City> cityArrayList = new ArrayList<>();
    private int selectionIndexCity = 0;
    private int selectionIndexCountry = 0;

    public FetchCities(Context context, Spinner sp_city, int countryId, ArrayList<String> cityNameArrayList ,ArrayList<City> cityArrayList){
        this.context = context;
        this.sp_city = sp_city;
        this.countryId = countryId;
        this.cityNameArrayList = cityNameArrayList;
        this.cityArrayList = cityArrayList;
    }

    public void mGetCities(final String url, final String lang){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("CITIES_RESPONSE" , response);
                try {
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");


                    if (success.equals("0")){

                        SpannableString noCities = new SpannableString(context.getString(R.string.no_cities));
                        noCities.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontMedium(context)) , 0 , context.getString(R.string.no_cities).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        Toast.makeText(context, noCities, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){

                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0 ; i < data.length() ; i++){

                            JSONObject countries = data.getJSONObject(i);
                            int city_id = countries.getInt("city_id");
                            String city_name = countries.getString("city_name");
                            try {
                                if (city_name.equals(EditProfileActivity.city)) {
                                    Log.e("i > " + i, "  city > " + city_name);
                                    selectionIndexCity = i;
                                }
                            }catch (NullPointerException e){

                            }
                            cityNameArrayList.add(city_name);
                            cityArrayList.add(new City(city_id , city_name));
                        }
                        SpinnerAdapter adapter;
                        if(url.equals(Urls.ALL_CITIES_URL)) {
                            adapter = new SpinnerAdapter(context, R.layout.spinner_item2, cityNameArrayList);
                        }else{
                            adapter = new SpinnerAdapter(context, R.layout.spinner_item, cityNameArrayList);
                        }
                            sp_city.setAdapter(adapter);
                        sp_city.setSelection(selectionIndexCity);

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
                if(countryId!=0)
                  params.put("country_id" , String.valueOf(countryId));
                return params;
            }
        };

        requestQueue.add(request);
    }
}
