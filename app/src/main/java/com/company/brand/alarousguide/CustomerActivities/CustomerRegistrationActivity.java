package com.company.brand.alarousguide.CustomerActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Activities.LoginActivity;
import com.company.brand.alarousguide.Classess.FetchCities;
import com.company.brand.alarousguide.Classess.FetchCountries;
import com.company.brand.alarousguide.Models.City;
import com.company.brand.alarousguide.Models.Country;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back;
    TextView tv_have_account , tv_login;
    EditText et_name , et_email , et_phone , et_password , et_rePassword , et_weddingDate;
    Spinner sp_country , sp_city;
    Button bt_create;
    ArrayList<String> countryNAmeArrayList = null;
    ArrayList<Country> countryArrayList = null;
    ArrayList<String> cityNameArrayList = null;
    ArrayList<City> cityArrayList = null;
    private ProgressDialog progressDialog;
    private int countryId;
    private int cityId;
    private SpannableString chooseCityAndCountryFirst, emptyField, invalidEmail, invalidPhone, weakPassword, pNotMatch, noInternet,
            uploadingMessage, error, registerSuccessfully, emailExist, phoneExist, phoneAndEmailExist;

    private void mSetupRef(){
        countryNAmeArrayList = new ArrayList<>();
        countryArrayList = new ArrayList<>();
        cityNameArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_have_account = (TextView) findViewById(R.id.tv_have_account);
        tv_login = (TextView) findViewById(R.id.tv_login);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        et_rePassword = (EditText) findViewById(R.id.et_rePassword);
        et_weddingDate = (EditText) findViewById(R.id.et_weddingDate);
        sp_country = (Spinner) findViewById(R.id.sp_country);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        bt_create = (Button) findViewById(R.id.bt_create);
        tv_have_account.setTypeface(Fonts.mSetupFontMedium(this));
        tv_login.setTypeface(Fonts.mSetupFontMedium(this));
        et_name.setTypeface(Fonts.mSetupFontRegular(this));
        et_email.setTypeface(Fonts.mSetupFontRegular(this));
        et_phone.setTypeface(Fonts.mSetupFontRegular(this));
        et_password.setTypeface(Fonts.mSetupFontRegular(this));
        et_rePassword.setTypeface(Fonts.mSetupFontRegular(this));
        bt_create.setTypeface(Fonts.mSetupFontMedium(this));
    }

    private void mSetupListener(){
        iv_back.setOnClickListener(this);
        bt_create.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    private void mSetupSpinners(){
        new FetchCountries(this , sp_country , countryNAmeArrayList , countryArrayList).mGetCountries(PreferenceManager.getDefaultSharedPreferences(CustomerRegistrationActivity.this).getString("language", "en"));
        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String countryName = countryArrayList.get(position).getCountryName();
                int countryId = countryArrayList.get(position).getCountryId();
                Log.e("COUNTRY_NAME   =    ", countryName +"    COUNTRY_ID    =    "+ countryId);
                cityNameArrayList.clear();
                new FetchCities(CustomerRegistrationActivity.this, sp_city, countryId , cityNameArrayList, cityArrayList)
                        .mGetCities(Urls.CITIES_URL, PreferenceManager.getDefaultSharedPreferences(CustomerRegistrationActivity.this).getString("language", "en"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void mSetupToasts(){
        chooseCityAndCountryFirst = new SpannableString(this.getString(R.string.choose_country_and_city_first));
        chooseCityAndCountryFirst.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.choose_country_and_city_first).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        emptyField = new SpannableString(this.getString(R.string.empty_field));
        emptyField.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.empty_field).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        invalidEmail = new SpannableString(this.getString(R.string.invalid_email));
        invalidEmail.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.invalid_email).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        invalidPhone = new SpannableString(this.getString(R.string.invalid_phone));
        invalidPhone.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.invalid_phone).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        weakPassword = new SpannableString(this.getString(R.string.weak_password));
        weakPassword.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.weak_password).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        pNotMatch = new SpannableString(this.getString(R.string.password_not_match));
        pNotMatch.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.password_not_match).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        noInternet = new SpannableString(this.getString(R.string.no_internet_connection));
        noInternet.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.no_internet_connection).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        uploadingMessage = new SpannableString(this.getString(R.string.wait));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error = new SpannableString(CustomerRegistrationActivity.this.getString(R.string.error));
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(CustomerRegistrationActivity.this)) , 0 , CustomerRegistrationActivity.this.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        registerSuccessfully = new SpannableString(CustomerRegistrationActivity.this.getString(R.string.register_successfully));
        registerSuccessfully.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(CustomerRegistrationActivity.this)) , 0 , CustomerRegistrationActivity.this.getString(R.string.register_successfully).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        emailExist = new SpannableString(CustomerRegistrationActivity.this.getString(R.string.email_already_exist));
        emailExist.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(CustomerRegistrationActivity.this)) , 0 , CustomerRegistrationActivity.this.getString(R.string.email_already_exist).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        phoneExist = new SpannableString(CustomerRegistrationActivity.this.getString(R.string.phone_already_exist));
        phoneExist.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(CustomerRegistrationActivity.this)) , 0 , CustomerRegistrationActivity.this.getString(R.string.phone_already_exist).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        phoneAndEmailExist = new SpannableString(CustomerRegistrationActivity.this.getString(R.string.phone_and_email_already_exist));
        phoneAndEmailExist.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(CustomerRegistrationActivity.this)) , 0 , CustomerRegistrationActivity.this.getString(R.string.phone_and_email_already_exist).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);
        mSetupRef();
        mSetupListener();
        mSetupToasts();
        mSetupSpinners();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_back:
                startActivity(new Intent(this , LoginActivity.class));
                finish();
                break;
            case R.id.bt_create:
                mSetupValidation();
                break;
            case R.id.tv_login:
                startActivity(new Intent(this , LoginActivity.class));
                finish();
        }
    }

    private void mSetupValidation() {
        String name = et_name.getText().toString();
        String email = et_email.getText().toString().trim();
        String phone = et_phone.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_rePassword.getText().toString();
        String weddingDate = et_weddingDate.getText().toString();

        try {

            countryId = countryArrayList.get(sp_country.getSelectedItemPosition()).getCountryId();
            cityId = cityArrayList.get(sp_city.getSelectedItemPosition()).getCityId();

        } catch (Exception ex){

            Toast.makeText(this, chooseCityAndCountryFirst, Toast.LENGTH_SHORT).show();
        }


        if (name.equals("") || name.isEmpty()){

            et_name.setError(emptyField);

        } else if (email.equals("") || email.isEmpty()){

            et_email.setError(emptyField);

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            et_email.setError(invalidEmail);

        } else if (phone.equals("") || phone.isEmpty()){

            et_phone.setError(emptyField);

        } else if (!Patterns.PHONE.matcher(phone).matches()){

            et_phone.setError(invalidPhone);

        } else if (password.equals("") || password.isEmpty()){

            et_password.setError(emptyField);

        } else if (password.length() < 8) {

            et_password.setError(weakPassword);

        } else if (!password.equals(confirmPassword)){

            et_password.setError(pNotMatch);
            et_rePassword.setError(pNotMatch);

        } else if(weddingDate.equals("") || weddingDate.isEmpty()) {

            et_weddingDate.setError(emptyField);

        } else {

            if (!Util.isOnline(this)){

                Toast.makeText(this, noInternet, Toast.LENGTH_LONG).show();

            } else {

                mSaveUser(name, countryId, cityId, email, phone, password, weddingDate);
            }
        }
    }

    private void mSaveUser(final String name, final int countryId, final int cityId, final String email, final String phone, final String password, final String weddingDate) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(uploadingMessage);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.REGISTRATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){

                        progressDialog.dismiss();
                        Toast.makeText(CustomerRegistrationActivity.this, error, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){

                        progressDialog.dismiss();
                        Toast.makeText(CustomerRegistrationActivity.this, registerSuccessfully, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CustomerRegistrationActivity.this , LoginActivity.class));
                        finish();

                    } else if (success.equals("2")){

                        progressDialog.dismiss();
                        Toast.makeText(CustomerRegistrationActivity.this, emailExist, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("3")){

                        progressDialog.dismiss();
                        Toast.makeText(CustomerRegistrationActivity.this, phoneExist, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("4")){

                        progressDialog.dismiss();
                        Toast.makeText(CustomerRegistrationActivity.this, phoneAndEmailExist, Toast.LENGTH_SHORT).show();
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
                params.put("role_id" , "1");
                params.put("name" , name);
                params.put("country" , String.valueOf(countryId));
                params.put("city" , String.valueOf(cityId));
                params.put("email" , email);
                params.put("phone" , phone);
                params.put("password" , password);
                params.put("wedding_date" , weddingDate);
                params.put("device_type" , "0");
                params.put("token" , "token");
                return params;
            }
        };
        requestQueue.add(request);
    }
}
