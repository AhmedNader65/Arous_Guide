package com.company.brand.alarousguide.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.CustomerActivities.HomeActivity;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.TraderActivities.TraderOffersActivity;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_email , et_password;
    TextView tv_forgetPassword , tv_have_account , tv_new_account;
    Button bt_login , bt_browseApp;
    private SpannableString emptyField, invalidEmail, wrongPassword, noInternet,
            uploadingMessage, error;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private void mSetupRefs(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);
        tv_have_account = (TextView) findViewById(R.id.tv_have_account);
        tv_new_account = (TextView) findViewById(R.id.tv_new_account);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_browseApp = (Button) findViewById(R.id.bt_browseApp);
        et_email.setTypeface(Fonts.mSetupFontRegular(this));
        et_password.setTypeface(Fonts.mSetupFontRegular(this));
        tv_forgetPassword.setTypeface(Fonts.mSetupFontRegular(this));
        tv_have_account.setTypeface(Fonts.mSetupFontBold(this));
        tv_new_account.setTypeface(Fonts.mSetupFontMedium(this));
        bt_login.setTypeface(Fonts.mSetupFontMedium(this));
        bt_browseApp.setTypeface(Fonts.mSetupFontMedium(this));
    }

    private void mSetupListener(){
        bt_login.setOnClickListener(this);
        bt_browseApp.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        tv_new_account.setOnClickListener(this);
    }

    private void mSetupToasts(){
        emptyField = new SpannableString(this.getString(R.string.empty_field));
        emptyField.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.empty_field).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        invalidEmail = new SpannableString(this.getString(R.string.invalid_email));
        invalidEmail.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.invalid_email).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        wrongPassword = new SpannableString(this.getString(R.string.wrong_password));
        wrongPassword.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , getString(R.string.wrong_password).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        noInternet = new SpannableString(this.getString(R.string.no_internet_connection));
        noInternet.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.no_internet_connection).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        uploadingMessage = new SpannableString(this.getString(R.string.wait));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error = new SpannableString(this.getString(R.string.error));
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSetupRefs();
        mSetupListener();
        mSetupToasts();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_login:
                mSetupValidation();
                break;
            case R.id.bt_browseApp:
                editor.putInt("roleId",5);
                editor.commit();
                startActivity(new Intent(LoginActivity.this , HomeActivity.class));
                break;
            case R.id.tv_forgetPassword:
                startActivity(new Intent(this , ResetPasswordActivity.class));
                break;
            case R.id.tv_new_account:
                startActivity(new Intent(this , ChooseRegistrationTypeActivity.class));
                break;
        }
    }

    private void mSetupValidation() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString();

        if (email.equals("") || email.isEmpty()){

            et_email.setError(emptyField);

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            et_email.setError(invalidEmail);

        } else if (password.equals("") || password.isEmpty()){

            et_password.setError(emptyField);

        } else if (password.length() < 8){

            et_password.setError(wrongPassword);

        } else {

            if (!Util.isOnline(this)){

                Toast.makeText(this, noInternet, Toast.LENGTH_SHORT).show();

            } else {

                mAccessUser(email , password);
            }
        }
    }

    private void mAccessUser(final String email, final String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(uploadingMessage);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.Login_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){

                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){

                        progressDialog.dismiss();
                        int user_id = jsonObject.getInt("user_id");
                        int role_id = jsonObject.getInt("role_id");
                        String name = jsonObject.getString("name");
                        editor.putInt("userId" , user_id);
                        editor.putInt("roleId" , role_id);
                        editor.putString("name" , name);
                        editor.apply();

                        if (role_id == 1){

                            startActivity(new Intent(LoginActivity.this , HomeActivity.class));
                            finish();

                        } else {

                            startActivity(new Intent(LoginActivity.this , TraderOffersActivity.class));
                            finish();
                        }
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
                params.put("email" , email);
                params.put("password" , password);
                return params;
            }
        };
        requestQueue.add(request);
    }
}
