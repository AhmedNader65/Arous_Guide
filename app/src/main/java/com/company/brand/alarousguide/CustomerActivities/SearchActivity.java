package com.company.brand.alarousguide.CustomerActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Adapters.SpinnerAdapter;
import com.company.brand.alarousguide.Classess.FetchCities;
import com.company.brand.alarousguide.Classess.FetchCountries;
import com.company.brand.alarousguide.Models.City;
import com.company.brand.alarousguide.Models.Country;
import com.company.brand.alarousguide.Models.ImageAndTextModel;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.databinding.ActivitySearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding ;
    private ArrayList<ImageAndTextModel> sectionsList;
    private ArrayList<String> sectionsNameList;
    SharedPreferences sp;
    private ArrayList<String> cityNameArrayList;
    private ArrayList<City> cityArrayList;
    ArrayList<String> countryNAmeArrayList = null;
    ArrayList<Country> countryArrayList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        cityArrayList = new ArrayList<>();
        cityNameArrayList = new ArrayList<>();
        countryArrayList = new ArrayList<>();
        countryNAmeArrayList = new ArrayList<>();
        setSpinner();
        setSections();
        setTypeFaces();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean canSearch = false;
                Intent searchIntent = new Intent(SearchActivity.this,SearchActivityResult.class);
                if(!binding.ETSearchName.getText().toString().trim().isEmpty()){
                    searchIntent.putExtra("word",binding.ETSearchName.getText().toString());
                    canSearch = true;
                }
                if(binding.spinnerCountry.getSelectedItemPosition()!=0){
                    searchIntent.putExtra("country_id", ""+countryArrayList.get(binding.spinnerCountry.getSelectedItemPosition()).getCountryId());
                    canSearch = true;
                    if(binding.spinnerCity.getSelectedItemPosition()!=0){
                        searchIntent.putExtra("city_id",""+ cityArrayList.get(binding.spinnerCity.getSelectedItemPosition()).getCityId());
                        canSearch = true;
                    }
                }

                if(binding.spinnerSections.getSelectedItemPosition()!=0){
                    searchIntent.putExtra("section_id",""+ sectionsList.get(binding.spinnerSections.getSelectedItemPosition()-1).getId());
                    canSearch = true;
                }
                if(!binding.from.getText().toString().trim().isEmpty()){
                    searchIntent.putExtra("price_from",binding.from.getText().toString());
                    canSearch = true;
                }
                if(!binding.to.getText().toString().trim().isEmpty()){
                    searchIntent.putExtra("price_to",binding.to.getText().toString());
                    canSearch = true;
                }
                if(canSearch)
                    startActivity(searchIntent);
            }
        });
    }

    private void setSpinner() {
        countryArrayList.add(new Country(0,getString(R.string.select_country)));
        countryNAmeArrayList.add(getString(R.string.select_country));
        new FetchCountries(this , binding.spinnerCountry , countryNAmeArrayList , countryArrayList).mGetCountries(sp.getString("language", "en"));
        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String countryName = countryArrayList.get(position).getCountryName();
                int countryId = countryArrayList.get(position).getCountryId();
                Log.e("COUNTRY_NAME   =    ", countryName +"    COUNTRY_ID    =    "+ countryId);
                cityNameArrayList.clear();
                cityArrayList.clear();
                cityArrayList.add(new City(0,getString(R.string.select_city)));
                cityNameArrayList.add(getString(R.string.select_city));
                new FetchCities(SearchActivity.this, binding.spinnerCity, countryId , cityNameArrayList, cityArrayList)
                        .mGetCities(Urls.CITIES_URL,sp.getString("language", "en"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setSections() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.MAIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")){
                        sectionsList = new ArrayList<>();
                        sectionsNameList = new ArrayList<>();
                        sectionsNameList.add(getString(R.string.select_section));
                        JSONArray sections = jsonObject.getJSONArray("sections");
                        for(int i = 0;i < sections.length();i++){
                            JSONObject obj = sections.getJSONObject(i);
                            ImageAndTextModel model = new ImageAndTextModel();
                            model.setId(obj.getString("section_id"));
                            model.setImgLink(obj.getString("section_image"));
                            model.setText(obj.getString("section_name"));
                            sectionsList.add(model);
                            sectionsNameList.add(obj.getString("section_name"));
                        }

                        SpinnerAdapter adapter = new SpinnerAdapter(SearchActivity.this, R.layout.spinner_item, sectionsNameList);
                        binding.spinnerSections.setAdapter(adapter);
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
                params.put("language" , sp.getString("language","en"));
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void setTypeFaces(){

        binding.tvMyProfile.setTypeface(Fonts.mSetupFontBold(this));
        binding.tvDeterminePrice.setTypeface(Fonts.mSetupFontRegular(this));
        binding.from.setTypeface(Fonts.mSetupFontRegular(this));
        binding.fromTV.setTypeface(Fonts.mSetupFontRegular(this));
        binding.to.setTypeface(Fonts.mSetupFontRegular(this));
        binding.toTV.setTypeface(Fonts.mSetupFontRegular(this));
        binding.ETSearchName.setTypeface(Fonts.mSetupFontRegular(this));
    }
}
