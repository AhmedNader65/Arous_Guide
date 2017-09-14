package com.company.brand.alarousguide.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_email;
    Button bt_send;
    String email;
    private void mSetupRefs(){
        et_email = (EditText) findViewById(R.id.et_email);
        bt_send = (Button) findViewById(R.id.bt_send);

        et_email.setTypeface(Fonts.mSetupFontRegular(this));
        bt_send.setTypeface(Fonts.mSetupFontMedium(this));
    }

    private void mSetupListener(){
        bt_send.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mSetupRefs();
        mSetupListener();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_send:
                mSetupValidation();
                break;
        }
    }

    private void mSetupValidation() {
        if(et_email.getText().toString().contains("@")&&et_email.getText().toString().endsWith(".com")) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, Urls.TOKEN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("RESPONSE_TOKEN", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            SpannableString reset = new SpannableString(getString(R.string.password_sent));
                            reset.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(ResetPasswordActivity.this)) , 0 , getString(R.string.password_sent).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                            Toast.makeText(ResetPasswordActivity.this,reset, Toast.LENGTH_SHORT).show();
                            ResetPasswordActivity.this.finish();;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("ERROR", "" + error.getMessage());
                }

            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    return params;
                }
            };
            requestQueue.add(request);
        }else{
            SpannableString wrongEmail = new SpannableString(getString(R.string.invalid_email));
            wrongEmail.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.invalid_email).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

            Toast.makeText(this,wrongEmail, Toast.LENGTH_SHORT).show();
        }
    }
}
