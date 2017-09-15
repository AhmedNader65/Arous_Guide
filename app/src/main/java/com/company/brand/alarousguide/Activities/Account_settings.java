package com.company.brand.alarousguide.Activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Adapters.SpinnerAdapter;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.LocaleHelper;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.Utils.Util;
import com.company.brand.alarousguide.databinding.ActivityAccountSettingsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Account_settings extends AppCompatActivity {
    ActivityAccountSettingsBinding binding;
    private SpannableString  error ,weakPassword, uploadingMessage, pNotMatch, noInternet, registerSuccessfully;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_account_settings);
        ArrayList<String> languages = new ArrayList<>();
        languages.add("العربية");
        languages.add("English");
        sp = PreferenceManager.getDefaultSharedPreferences(Account_settings.this);
        Log.e("language " ,sp.getString("language",""));
        setTypeFaces();
            mSetupToasts();
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this,R.layout.spinner_item,languages);
        binding.spinnerLanguage.setAdapter(spinnerAdapter);
        if(sp.getString("language", "ar").equals("ar")) {
            binding.spinnerLanguage.setSelection(0);
        }else{
            binding.spinnerLanguage.setSelection(1);
        }
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account_settings.this.finish();
            }
        });
        Log.e("language from spinner",binding.spinnerLanguage.getSelectedItemPosition()+"");
        binding.BTSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.ETNewPassword.getText().toString().isEmpty()){
                    if(binding.ETNewPassword.getText().toString().equals(binding.ETPasswordConfirm.getText().toString())){
                        if(binding.ETNewPassword.getText().toString().length()<8){
                            Toast.makeText(Account_settings.this, weakPassword, Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog = new ProgressDialog(Account_settings.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage(uploadingMessage);
                            progressDialog.show();

                            RequestQueue requestQueue = Volley.newRequestQueue(Account_settings.this);
                            StringRequest request = new StringRequest(Request.Method.POST, Urls.EDIT_PASSWORD, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("RESPONSE_edit_password" , response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");

                                        if (success.equals("0")){

                                            progressDialog.dismiss();
                                            Toast.makeText(Account_settings.this, error, Toast.LENGTH_SHORT).show();

                                        } else if (success.equals("1")){

                                            SharedPreferences.Editor editor = sp.edit();
                                            switch (binding.spinnerLanguage.getSelectedItemPosition()) {
                                                case 0:
                                                    if(!sp.getString("language", "ar").equals("ar")) {
                                                        Util.MakeLanguageAabic(Account_settings.this);
                                                        editor.putString("language", "ar");
                                                        editor.apply();
                                                        Context context = LocaleHelper.setLocale(Account_settings.this, "en");
                                                        restart();
                                                    }
                                                    break;
                                                case 1:
                                                    if(!sp.getString("language", "en").equals("en")) {
                                                        Util.MakeLanguageEnglish(Account_settings.this);
                                                        editor.putString("language", "en");
                                                        editor.apply();
                                                        Context context = LocaleHelper.setLocale(Account_settings.this, "en");
                                                        restart();
                                                        break;
                                                    }
                                            }

                                            progressDialog.dismiss();
                                            Toast.makeText(Account_settings.this, registerSuccessfully, Toast.LENGTH_SHORT).show();
                                            finish();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("ERROR" , ""+error.getMessage());
                                    progressDialog.dismiss();
                                    Toast.makeText(Account_settings.this, noInternet, Toast.LENGTH_SHORT).show();
                                }

                            }) {

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String , String> params = new HashMap<>();
                                    params.put("role_id" ,""+ sp.getInt("roleId",1));
                                    Log.e("role_id" , ""+sp.getInt("roleId",1));
                                    params.put("user_id" , ""+sp.getInt("userId",0));
                                    Log.e("user_id" ,""+ sp.getInt("userId",0));
                                    params.put("old_password" , binding.ETOldPassword.getText().toString());
                                    Log.e("old_password" , binding.ETOldPassword.getText().toString());
                                    params.put("new_password" , binding.ETNewPassword.getText().toString());
                                    Log.e("new_password" , binding.ETNewPassword.getText().toString());
                                    return params;
                                }
                            };
                            requestQueue.add(request);
                        }
                    }else{
                        Toast.makeText(Account_settings.this, pNotMatch, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    Log.e("language from shared ",sp.getString("language","mafesh"));
                    Log.e("language from spinner",binding.spinnerLanguage.getSelectedItemPosition()+"");
                    switch (binding.spinnerLanguage.getSelectedItemPosition()) {
                        case 0:
                            if(!sp.getString("language", "ar").equals("ar")) {
                                Util.MakeLanguageAabic(Account_settings.this);
                                editor.putString("language", "ar");
                                editor.apply();
                                Context context = LocaleHelper.setLocale(Account_settings.this, "ar");
                                Toast.makeText(Account_settings.this,registerSuccessfully, Toast.LENGTH_SHORT).show();
                                restart();
                            }
                            break;
                        case 1:
                            if(!sp.getString("language", "en").equals("en")) {
                                Util.MakeLanguageEnglish(Account_settings.this);
                                editor.putString("language", "en");
                                editor.apply();
                                Context context = LocaleHelper.setLocale(Account_settings.this, "en");
                                Toast.makeText(Account_settings.this,registerSuccessfully, Toast.LENGTH_SHORT).show();
                                restart();
                            }
                            break;
                    }

                }
            }
        });
    }
    private void mSetupToasts(){
        weakPassword = new SpannableString(this.getString(R.string.weak_password));
        weakPassword.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.weak_password).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        pNotMatch = new SpannableString(this.getString(R.string.password_not_match));
        pNotMatch.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.password_not_match).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        noInternet = new SpannableString(this.getString(R.string.no_internet_connection));
        noInternet.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.no_internet_connection).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error = new SpannableString(getString(R.string.wrong_password));
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(Account_settings.this)) , 0 , Account_settings.this.getString(R.string.wrong_password).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        registerSuccessfully = new SpannableString(Account_settings.this.getString(R.string.done_saving));
        registerSuccessfully.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(Account_settings.this)) , 0 , Account_settings.this.getString(R.string.done_saving).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        uploadingMessage = new SpannableString(Account_settings.this.getString(R.string.wait));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(Account_settings.this)) , 0 , Account_settings.this.getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        private void setTypeFaces(){

            binding.title.setTypeface(Fonts.mSetupFontBold(this));
            binding.BTSave.setTypeface(Fonts.mSetupFontBold(this));
            binding.ETOldPassword.setTypeface(Fonts.mSetupFontRegular(this));
            binding.ETNewPassword.setTypeface(Fonts.mSetupFontRegular(this));
            binding.ETPasswordConfirm.setTypeface(Fonts.mSetupFontRegular(this));
        }

        public void restart(){
            Intent mStartActivity = new Intent(Account_settings.this, SplashActivity.class);
            mStartActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mStartActivity);
        }
}
