package com.company.brand.alarousguide.CustomerActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Activities.LoginActivity;
import com.company.brand.alarousguide.Activities.ProfileActivity;
import com.company.brand.alarousguide.Activities.TermsAndConditionsActivity;
import com.company.brand.alarousguide.Adapters.ImageAndTextAdapter;
import com.company.brand.alarousguide.Classess.FetchCities;
import com.company.brand.alarousguide.Fragments.Navigation_Drawer_Fragment;
import com.company.brand.alarousguide.Models.City;
import com.company.brand.alarousguide.Models.ImageAndTextModel;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Activities.AlertsActivity;
import com.company.brand.alarousguide.Activities.GiftsActivity;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.Utils.Util;
import com.company.brand.alarousguide.databinding.ActivityHomeBinding;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.company.brand.alarousguide.Utils.Urls.SLIDER_IMAGES_BASE;


public class HomeActivity extends AppCompatActivity implements Navigation_Drawer_Fragment.FragmentDrawerListener {
    ActivityHomeBinding binding;
    private ArrayList<ImageAndTextModel> sectionsList;
    SharedPreferences sp;
    private ArrayList<String> cityNameArrayList;
    private ArrayList<City> cityArrayList;
    Navigation_Drawer_Fragment drawerFragment;
    private View containerView;
    SpannableString error,loginFirst;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private int userId;
    public static int cityId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        containerView = findViewById(R.id.fragment_navigation_drawer);
        drawerFragment = (Navigation_Drawer_Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, binding.dlDrawerLayout, binding.tbToolBar);
        drawerFragment.setDrawerListener(this);
        setToasts();
        if(PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getInt("roleId",5)==5){
            binding.dlDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        binding.ivNavigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getInt("roleId",5)==5){
                    Toast.makeText(HomeActivity.this, loginFirst, Toast.LENGTH_SHORT).show();
                }else {
                    binding.dlDrawerLayout.openDrawer(containerView);
                }
            }
        });
        if (Util.isOnline(this)){

            binding.pbProgressBar.setVisibility(View.VISIBLE);
            getMainData();
            setSlider();
            setSpinner();
            setSections();
        } else {

            binding.tvNoInternet.setVisibility(View.VISIBLE);
            binding.btRetry.setVisibility(View.VISIBLE);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", 0);
        editor = sharedPreferences.edit();

        binding.searchBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getInt("roleId",5)==5){
                    Toast.makeText(HomeActivity.this, loginFirst, Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                }
            }
        });
        binding.btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                HomeActivity.this.finish();
            }
        });


        if(PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getInt("roleId",5)!=5) {
            Util.updateToken(HomeActivity.this,sp);
        }
        setTypeFaces();
    }



    private void setSections() {
        sectionsList = new ArrayList<>();
        binding.sections.setLayoutManager(new GridLayoutManager(this,2));

        getSectionData();
        setTypeFaces();
    }

    private void getSectionData() {
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
                         JSONArray sections = jsonObject.getJSONArray("sections");
                         for(int i = 0;i < sections.length();i++){
                             JSONObject obj = sections.getJSONObject(i);
                             ImageAndTextModel model = new ImageAndTextModel();
                             model.setId(obj.getString("section_id"));
                             model.setImgLink(obj.getString("section_image"));
                             model.setText(obj.getString("section_name"));
                             sectionsList.add(model);
                             binding.pbProgressBar.setVisibility(View.GONE);
                         }
                         binding.sections.setAdapter(new ImageAndTextAdapter(R.layout.sections_item,sectionsList));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                binding.tvNoInternet.setVisibility(View.VISIBLE);
                binding.btRetry.setVisibility(View.VISIBLE);
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

    private void setSpinner() {
        cityNameArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        new FetchCities(HomeActivity.this, binding.spinner,0 , cityNameArrayList, cityArrayList)
                .mGetCities(Urls.ALL_CITIES_URL,sp.getString("language", "en"));
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = cityArrayList.get(position).getCityId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMainData() {

    }

    private void setSlider() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.SLIDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){

                        Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){
                        JSONArray data = jsonObject.getJSONArray("data");

                        HashMap<String,String> file_maps = new HashMap<String, String>();
                        for(int i = 0;i<data.length();i++){
                            JSONObject slide = data.getJSONObject(i);
                            SpannableString des  = new SpannableString(slide.getString("description"));
                            des.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(HomeActivity.this)) , 0 , slide.getString("description").length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                            file_maps.put(des.toString(),SLIDER_IMAGES_BASE+slide.getString("image"));
                        }

                        for(String name : file_maps.keySet()){
                            TextSliderView textSliderView = new TextSliderView(HomeActivity.this);
                            // initialize a SliderLayout
                            textSliderView
                                    .description(name)
                                    .image(file_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);

                            binding.slider.addSlider(textSliderView);
                        }
                        binding.slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        binding.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        binding.slider.setCustomAnimation(new DescriptionAnimation());
                        binding.slider.setDuration(4000);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                binding.tvNoInternet.setVisibility(View.VISIBLE);
                binding.btRetry.setVisibility(View.VISIBLE);
                Log.e("ERROR" , ""+error.getMessage());
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> params = new HashMap<>();
                params.put("language" , sp.getString("language", "en"));
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
            switch (position) {
                case 0:
                    startActivity(new Intent(this, ProfileActivity.class)
                            .putExtra("userId",sharedPreferences.getInt("userId", 0))
                            .putExtra("roleId",sharedPreferences.getInt("roleId", 0)));
                    break;
                case 1:
                    startActivity(new Intent(this, AlertsActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(this, SpecialOffersActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(this, GiftsActivity.class));
                    break;
                case 4:
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, "Download Bride and groom App From This link : http://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(i);
                    break;
                case 5:
                    startActivity(new Intent(this, TermsAndConditionsActivity.class));
                    break;
                case 6:
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
    private void setTypeFaces(){

        binding.tvMyProfile.setTypeface(Fonts.mSetupFontBold(this));
        binding.tvNotFoundOffers.setTypeface(Fonts.mSetupFontRegular(this));
        binding.tvNoInternet.setTypeface(Fonts.mSetupFontRegular(this));
        binding.btRetry.setTypeface(Fonts.mSetupFontRegular(this));
        }
    private void setToasts(){
        error = new SpannableString(getString(R.string.error));
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        loginFirst = new SpannableString(getString(R.string.login_first));
        loginFirst.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.login_first).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

    }
}
