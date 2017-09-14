package com.company.brand.alarousguide.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import com.company.brand.alarousguide.Classess.FetchCities;
import com.company.brand.alarousguide.Classess.FetchCountries;
import com.company.brand.alarousguide.Models.City;
import com.company.brand.alarousguide.Models.Country;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.databinding.ActivityEditProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    public static String country,city;
    private String name,phone,whatsup,facebook,snapchat,instagram,email;
    ActivityEditProfileBinding binding;
    private ArrayList<String> countryNameArrayList;
    private ArrayList<Country> countryArrayList;
    private ArrayList<String> cityNameArrayList;
    private ArrayList<City> cityArrayList;
    private ProgressDialog progressDialog;
    private SpannableString uploadingMessage, error,done,emailExist,mobileExist,mobileAndEmailEx;
    SharedPreferences sharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        countryNameArrayList = new ArrayList<>();
        countryArrayList = new ArrayList<>();
        cityNameArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        getUserData();
        mSetupToasts();
        setTypeFaces();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileActivity.this.finish();
            }
        });
        binding.BTSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int userId = sharedPreferences.getInt("userId", 0);
                int roleId = sharedPreferences.getInt("roleId", 0);
                saveProfileData(userId,roleId);
            }
        });
    }

    private void mSetupSpinners(){
        new FetchCountries(this , binding.spinnerCountry , countryNameArrayList , countryArrayList).mGetCountries(sharedPreferences.getString("language", "en"));
        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String countryName = countryArrayList.get(position).getCountryName();
                int countryId = countryArrayList.get(position).getCountryId();
                Log.e("COUNTRY_NAME   =    ", countryName +"    COUNTRY_ID    =    "+ countryId);
                cityNameArrayList.clear();
                new FetchCities(EditProfileActivity.this, binding.spinnerCity, countryId , cityNameArrayList, cityArrayList)
                        .mGetCities(Urls.CITIES_URL,sharedPreferences.getString("language", "en"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getUserData() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        country = intent.getStringExtra("country");
        city = intent.getStringExtra("city");
        phone = intent.getStringExtra("phone");
        whatsup = intent.getStringExtra("whatsup");
        facebook = intent.getStringExtra("facebook");
        snapchat = intent.getStringExtra("snapchat");
        instagram = intent.getStringExtra("instagram");
        email = intent.getStringExtra("email");
        setEditTextx();
    }

    private void setEditTextx() {
        binding.ETEmail.setText(email);
        binding.ETPhone.setText(phone);
        binding.ETFacebook.setText(facebook);
        binding.ETInsta.setText(instagram);
        binding.ETSnapchat.setText(snapchat);
        binding.ETWhatsapp.setText(whatsup);
        mSetupSpinners();

    }

    private void mSetupToasts(){
        uploadingMessage = new SpannableString(this.getString(R.string.wait));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        done = new SpannableString(this.getString(R.string.done_saving));
        error = new SpannableString(this.getString(R.string.error));
        done.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.done_saving).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        emailExist = new SpannableString(this.getString(R.string.email_already_exist));
        emailExist.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.email_already_exist).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        mobileExist = new SpannableString(this.getString(R.string.phone_already_exist));
        mobileExist.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.phone_already_exist).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
       mobileAndEmailEx = new SpannableString(this.getString(R.string.mobileAndEmailExist));
        mobileAndEmailEx.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.mobileAndEmailExist).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    private void saveProfileData(final int userId, final int roleId) {
        phone = binding.ETPhone.getText().toString();
        email = binding.ETEmail.getText().toString();
        whatsup = binding.ETWhatsapp.getText().toString();
        facebook = binding.ETFacebook.getText().toString();
        instagram = binding.ETInsta.getText().toString();
        snapchat = binding.ETSnapchat.getText().toString();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(uploadingMessage);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    progressDialog.dismiss();
                    if (success.equals("0")){

                        Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){

                        Intent returnIntent = new Intent()
                                .putExtra("name",name)
                                .putExtra("phone",phone)
                                .putExtra("country",country)
                                .putExtra("city",city)
                                .putExtra("email",email)
                                .putExtra("facebook",facebook)
                                .putExtra("whatsup",whatsup)
                                .putExtra("snapchat",snapchat)
                                .putExtra("instagram",instagram);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    } else if (success.equals("2")){
                        Toast.makeText(EditProfileActivity.this, emailExist, Toast.LENGTH_SHORT).show();
                    } else if (success.equals("3")){
                        Toast.makeText(EditProfileActivity.this, mobileAndEmailEx, Toast.LENGTH_SHORT).show();
                    } else if (success.equals("4")){
                        Toast.makeText(EditProfileActivity.this, mobileAndEmailEx, Toast.LENGTH_SHORT).show();
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
                for(int i = 0 ; i < countryArrayList.size();i++){
                    if(countryArrayList.get(i).getCountryName()
                            .equals(String.valueOf(binding.spinnerCountry.getSelectedItem()))){
                        country = String.valueOf(countryArrayList.get(i).getCountryName());
                        params.put("country" , String.valueOf(countryArrayList.get(i).getCountryId()));
                    }
                }
                for(int i = 0 ; i < cityArrayList.size();i++){
                    if(cityArrayList.get(i).getCityName()
                            .equals(String.valueOf(binding.spinnerCity.getSelectedItem()))){
                        city = String.valueOf(cityArrayList.get(i).getCityName());
                        params.put("city" , String.valueOf(cityArrayList.get(i).getCityId()));
                    }
                }
                Log.e("role_id" , String.valueOf(roleId));
                Log.e("user_id" , String.valueOf(userId));
                Log.e("phone" , phone);
                Log.e("email" , email);
                Log.e("whatsup" , whatsup);
                Log.e("facebook" , facebook);
                Log.e("instagram" , instagram);
                Log.e("snapchat" , snapchat);
                params.put("role_id" , String.valueOf(roleId));
                params.put("user_id" , String.valueOf(userId));
                params.put("phone" , phone);
                params.put("email" , email);
                params.put("whatsup" , whatsup);
                params.put("facebook" , facebook);
                params.put("instagram" , instagram);
                params.put("snapchat" , snapchat);
                return params;
            }
        };
        requestQueue.add(request);

    }

    private void setTypeFaces(){

        binding.tvMyProfile.setTypeface(Fonts.mSetupFontBold(this));
        binding.ETPhone.setTypeface(Fonts.mSetupFontRegular(this));
        binding.ETEmail.setTypeface(Fonts.mSetupFontRegular(this));
        binding.ETWhatsapp.setTypeface(Fonts.mSetupFontRegular(this));
        binding.ETSnapchat.setTypeface(Fonts.mSetupFontRegular(this));
        binding.ETFacebook.setTypeface(Fonts.mSetupFontRegular(this));
        binding.ETInsta.setTypeface(Fonts.mSetupFontRegular(this));
        binding.BTSave.setTypeface(Fonts.mSetupFontRegular(this));
    }
}
