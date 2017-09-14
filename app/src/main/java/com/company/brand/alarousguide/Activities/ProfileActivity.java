package com.company.brand.alarousguide.Activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.CustomerActivities.ViewTraderLocationActivity;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back, iv_pSetting, iv_editProfile;
    TextView tv_myProfile, tv_name, tv_phone, tv_email, tv_address, tv_whatsApp, tv_snapChat, tv_facebook, tv_instagram,tv_openOnMap;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private SpannableString uploadingMessage, error;
    private String name,country,city,phone,whatsup,facebook,snapchat,instagram,email;

    private void mSetupRefs(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_pSetting = (ImageView) findViewById(R.id.iv_pSetting);
        iv_editProfile = (ImageView) findViewById(R.id.iv_editProfile);
        tv_myProfile = (TextView) findViewById(R.id.tv_myProfile);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_whatsApp = (TextView) findViewById(R.id.tv_whatsApp);
        tv_snapChat = (TextView) findViewById(R.id.tv_snapChat);
        tv_facebook = (TextView) findViewById(R.id.tv_facebook);
        tv_instagram = (TextView) findViewById(R.id.tv_instagram);
        tv_openOnMap = (TextView) findViewById(R.id.tv_openOnMap);

        tv_myProfile.setTypeface(Fonts.mSetupFontMedium(this));
        tv_name.setTypeface(Fonts.mSetupFontMedium(this));
        tv_phone.setTypeface(Fonts.mSetupFontRegular(this));
        tv_email.setTypeface(Fonts.mSetupFontRegular(this));
        tv_address.setTypeface(Fonts.mSetupFontRegular(this));
        tv_whatsApp.setTypeface(Fonts.mSetupFontRegular(this));
        tv_snapChat.setTypeface(Fonts.mSetupFontRegular(this));
        tv_facebook.setTypeface(Fonts.mSetupFontRegular(this));
        tv_instagram.setTypeface(Fonts.mSetupFontRegular(this));
        tv_openOnMap.setTypeface(Fonts.mSetupFontRegular(this));
        tv_whatsApp.setOnClickListener(this);
        tv_snapChat.setOnClickListener(this);
        tv_facebook.setOnClickListener(this);
        tv_instagram.setOnClickListener(this);
    }

    private void mSetupListener(){
        iv_back.setOnClickListener(this);
        iv_editProfile.setOnClickListener(this);
        iv_pSetting.setOnClickListener(this);
        tv_openOnMap.setOnClickListener(this);
    }

    private void mSetupToasts(){
        uploadingMessage = new SpannableString(this.getString(R.string.wait));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error = new SpannableString(this.getString(R.string.error));
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mSetupRefs();
        mSetupListener();
        mSetupToasts();
        if(getIntent().hasExtra("customer")){
            iv_editProfile.setVisibility(View.GONE);
        }
        int userId = getIntent().getIntExtra("userId", 0);
        int roleId = getIntent().getIntExtra("roleId", 0);
        String language = sharedPreferences.getString("language", "en");
        mGetProfileData(userId, roleId,language);
    }

    private void mGetProfileData(final int userId, final int roleId,final String language) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(uploadingMessage);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){

                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){
                        try {
                            progressDialog.dismiss();
                        }catch (Exception e){}
                        name = jsonObject.getString("name");
                        country = jsonObject.getString("country");
                        city = jsonObject.getString("city");
                        phone = jsonObject.getString("phone");
                        email = jsonObject.getString("email");
                        facebook = jsonObject.getString("facebook");
                        whatsup = jsonObject.getString("whatsup");
                        snapchat = jsonObject.getString("snapchat");
                        instagram = jsonObject.getString("instagram");
                        tv_address.setText(country+", "+city);
                        tv_email.setText(email);
                        tv_facebook.setText(facebook);
                        tv_instagram.setText(instagram);
                        tv_name.setText(name);
                        tv_phone.setText(phone);
                        tv_snapChat.setText(snapchat);
                        tv_whatsApp.setText(whatsup);
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
                params.put("role_id" , String.valueOf(roleId));
                params.put("user_id" , String.valueOf(userId));
                params.put("language" , language);
                return params;
            }
        };
        requestQueue.add(request);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_back:
                    finish();
                    break;
            case R.id.iv_editProfile:
                startActivityForResult(new Intent(this , EditProfileActivity.class)
                .putExtra("name",name)
                .putExtra("phone",phone)
                .putExtra("country",country)
                .putExtra("city",city)
                .putExtra("email",email)
                .putExtra("facebook",facebook)
                .putExtra("whatsup",whatsup)
                .putExtra("snapchat",snapchat)
                .putExtra("instagram",instagram)
                ,0);
                break;
            case R.id.iv_pSetting:
                startActivity(new Intent(this , Account_settings.class));
                break;
            case R.id.tv_openOnMap:

                startActivity(new Intent(this , ViewTraderLocationActivity.class).putExtra("location",city));
                break;
            case R.id.tv_whatsApp:
                Log.e("whatsapp",whatsup);
                Uri uri = Uri.parse("smsto:" + whatsup);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
                break;
            case R.id.tv_snapChat:
                Intent nativeAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://snapchat.com/add/" + snapchat));
                startActivity(nativeAppIntent);
                break;
            case R.id.tv_facebook:
                String face = facebook;
                if(!facebook.startsWith("https://")) {
                    if (facebook.contains("/")) {
                        face = facebook.split("/")[1];
                    }
                    face = "https://facebook.com/"+face;
                }
                Uri urii = Uri.parse(face);
                try {
                    ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);
                    if (applicationInfo.enabled) {
                        urii = Uri.parse("fb://facewebmodal/f?href=" + face);
                    }
                } catch (PackageManager.NameNotFoundException ignored) {
                }
                startActivity(new Intent(Intent.ACTION_VIEW, urii));
                Log.e("faceboook",face);
                break;
            case R.id.tv_instagram:
                Uri urri = Uri.parse("http://instagram.com/_u/"+instagram);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, urri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/"+instagram)));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==0 && resultCode == RESULT_OK){
            name = data.getStringExtra("name");
            phone = data.getStringExtra("phone");
            country = data.getStringExtra("country");
            city = data.getStringExtra("city");
            email =data.getStringExtra("email");
            facebook = data.getStringExtra("facebook");
            whatsup = data.getStringExtra("whatsup");
            snapchat = data.getStringExtra("snapchat");
            instagram = data.getStringExtra("instagram");
            tv_address.setText(country+", "+city);
            tv_email.setText(email);
            tv_facebook.setText(facebook);
            tv_instagram.setText(instagram);
            tv_name.setText(name);
            tv_phone.setText(phone);
            tv_snapChat.setText(snapchat);
            tv_whatsApp.setText(whatsup);
        }
    }
}
