package com.company.brand.alarousguide.TraderActivities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.Utils.Util;
import com.company.brand.alarousguide.databinding.ActivityAddOfferDataBinding;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.company.brand.alarousguide.Utils.Util.mEncodeToBase64;

public class AddOfferDataActivity extends AppCompatActivity implements IPickResult, View.OnClickListener {
    ActivityAddOfferDataBinding dataBinding ;
    View currentImgView;
    String[] imgArray = new String[4];
    boolean fPic= false,sPic=false,thPic= false,frPic= false;
    SharedPreferences sp ;
    private ArrayList<String> countryNameArrayList;
    private ArrayList<Country> countryArrayList;
    private ArrayList<String> cityNameArrayList;
    private ArrayList<City> cityArrayList;
    private int countryId = 0;
    private ArrayList<String> sectionsList;
    private ArrayList<ImageAndTextModel> sectionsListAll;
    private SpinnerAdapter sectionsAdapter;
    private ProgressDialog progressDialog;
    private SpannableString uploadingMessage, error,done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_offer_data);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        dataBinding.civAdv1.setOnClickListener(this);
        dataBinding.civAdv2.setOnClickListener(this);
        dataBinding.civAdv3.setOnClickListener(this);
        dataBinding.civAdv4.setOnClickListener(this);
        dataBinding.btAdd.setOnClickListener(this);
        countryNameArrayList = new ArrayList<>();
        countryArrayList = new ArrayList<>();
        cityNameArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        mSetupSpinners();
        getSectionData();
        mSetupToasts();
        setTypeFaces();
    }
    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            Bitmap bitmap = null;
            if(currentImgView.getId() == dataBinding.civAdv1.getId()) {
                if(imgArray[0]==null){
                    fPic = true;
                }
                String path = Util.resizeAndCompressImageBeforeSend(this,r.getPath(),r.getUri().getLastPathSegment());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                 bitmap = BitmapFactory.decodeFile(path,bmOptions);
                imgArray[0] =mEncodeToBase64(bitmap, Bitmap.CompressFormat.PNG,70);
            }
            if(currentImgView.getId() == dataBinding.civAdv2.getId()) {
                if(imgArray[1]==null){
                    sPic = true;
                }
                String path = Util.resizeAndCompressImageBeforeSend(this,r.getPath(),r.getUri().getLastPathSegment());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                 bitmap = BitmapFactory.decodeFile(path,bmOptions);
                imgArray[1] =mEncodeToBase64(bitmap, Bitmap.CompressFormat.PNG,70);
            }
            if(currentImgView.getId() == dataBinding.civAdv3.getId()) {
                if(imgArray[2]==null){
                    thPic = true;
                }
                String path = Util.resizeAndCompressImageBeforeSend(this,r.getPath(),r.getUri().getLastPathSegment());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                 bitmap = BitmapFactory.decodeFile(path,bmOptions);
                imgArray[2] =mEncodeToBase64(bitmap, Bitmap.CompressFormat.PNG,70);
            }
            if(currentImgView.getId() == dataBinding.civAdv4.getId()) {
                if(imgArray[3]==null){
                    frPic = true;
                }
                String path = Util.resizeAndCompressImageBeforeSend(this,r.getPath(),r.getUri().getLastPathSegment());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                 bitmap = BitmapFactory.decodeFile(path,bmOptions);
                imgArray[3] =mEncodeToBase64(bitmap, Bitmap.CompressFormat.PNG,70);
            }
            //If you want the Bitmap.
            ((ImageView)currentImgView).setImageBitmap(bitmap);

            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        if(v instanceof ImageView) {
            currentImgView = v;
            PickImageDialog.build(new PickSetup()).show(AddOfferDataActivity.this);
        }else if (v.getId() == R.id.bt_add){
            String name = null;
            String from = null;
            String to = null;
            String description = null;
            int traderId;
            int cityId = 0;
            String sectionId;
            double lat = getIntent().getDoubleExtra("latitude",0);
            double lng = getIntent().getDoubleExtra("longitude",0);
            if(!(fPic&&sPic&&thPic&&frPic)){
                Toast.makeText(this, R.string.atleast4pic, Toast.LENGTH_SHORT).show();
            }else{
                if(!dataBinding.from.getText().toString().isEmpty()){
                    from = dataBinding.from.getText().toString();
                    if(!dataBinding.to.getText().toString().isEmpty()){
                        to = dataBinding.to.getText().toString();
                        if(!dataBinding.etOfferName.getText().toString().isEmpty()){
                            name = dataBinding.etOfferName.getText().toString();
                            if(!dataBinding.etDescription.getText().toString().isEmpty()){
                                description = dataBinding.etDescription.getText().toString();

                                traderId = sp.getInt("userId",0);
                                if(countryId==0){
                                    Toast.makeText(this, getString(R.string.choose_country_and_city_first), Toast.LENGTH_SHORT).show();
                                }else{
                                    for(int i = 0 ; i < cityArrayList.size();i++){
                                        if(cityArrayList.get(i).getCityName()
                                                .equals(String.valueOf(dataBinding.spCity.getSelectedItem()))){

                                            cityId = cityArrayList.get(i).getCityId();
                                        }
                                    }
                                    if(cityId==0){
                                        Toast.makeText(this, getString(R.string.choose_country_and_city_first), Toast.LENGTH_SHORT).show();
                                    }else{
                                        Log.e("section",dataBinding.spCategory.getSelectedItemPosition()+"");
                                        sectionId = sectionsListAll.get(dataBinding.spCategory.getSelectedItemPosition()).getId();
                                        postData(name,from,to,description,traderId,cityId,countryId,sectionId,lat,lng);
                                    }
                                }

                            }else{
                                dataBinding.etDescription.setError(getString(R.string.required));
                            }
                        }else{
                            dataBinding.etOfferName.setError(getString(R.string.required));
                        }
                    }else{
                        dataBinding.to.setError(getString(R.string.required));
                    }
                }else{
                    dataBinding.from.setError(getString(R.string.required));
                }
            }



        }
    }

    private void postData(final String name,final String from, final String to, final String description, final int traderId, final int cityId, final int countryId, final String sectionId, final double lat, final double lng) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(uploadingMessage);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.ADD_OFFER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){

                        progressDialog.dismiss();
                        Toast.makeText(AddOfferDataActivity.this, error, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){

                        progressDialog.dismiss();
                        Toast.makeText(AddOfferDataActivity.this, done, Toast.LENGTH_SHORT).show();
                        AddOfferDataActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(AddOfferDataActivity.this,
                            "No Connection",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(AddOfferDataActivity.this,
                            "Check username & password",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(AddOfferDataActivity.this,
                            "Server error! try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(AddOfferDataActivity.this,
                            "Network Error try again",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(AddOfferDataActivity.this,
                            "ooo we have a problem",
                            Toast.LENGTH_LONG).show();
                }
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> params = new HashMap<>();
                Log.e("image[0]" , imgArray[0]);
                Log.e("image[1]" , imgArray[1]);
                Log.e("image[2]" , imgArray[2]);
                Log.e("image[3]" , imgArray[3]);
                Log.e("name" , String.valueOf(name));
                Log.e("trader_id" , String.valueOf(traderId));
                Log.e("from" , String.valueOf(from));
                Log.e("to" , String.valueOf(to));
                Log.e("description" , String.valueOf(description));
                Log.e("country_id" , String.valueOf(countryId));
                Log.e("city_id" , String.valueOf(cityId));
                Log.e("section_id" , String.valueOf(sectionId));
                Log.e("lat" , String.valueOf(lat));
                Log.e("lng" , String.valueOf(lng));
                params.put("image[0]" , imgArray[0]);
                params.put("image[1]" , imgArray[1]);
                params.put("image[2]" , imgArray[2]);
                params.put("image[3]" , imgArray[3]);
                params.put("name" , String.valueOf(name));
                params.put("trader_id" , String.valueOf(traderId));
                params.put("from" , String.valueOf(from));
                params.put("to" , String.valueOf(to));
                params.put("description" , String.valueOf(description));
                params.put("country_id" , String.valueOf(countryId));
                params.put("city_id" , String.valueOf(cityId));
                params.put("section_id" , String.valueOf(sectionId));
                params.put("lat" , String.valueOf(lat));
                params.put("lng" , String.valueOf(lng));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0,0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }

    private void mSetupSpinners(){
        new FetchCountries(this , dataBinding.spCountry , countryNameArrayList , countryArrayList).mGetCountries(sp.getString("language", "en"));
        dataBinding.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("hiii","checkedddd");
                String countryName = countryArrayList.get(position).getCountryName();
                countryId = countryArrayList.get(position).getCountryId();
                Log.e("COUNTRY_NAME   =    ", countryName +"    COUNTRY_ID    =    "+ countryId);
                cityNameArrayList.clear();
                new FetchCities(AddOfferDataActivity.this, dataBinding.spCity, countryId , cityNameArrayList, cityArrayList)
                        .mGetCities(Urls.CITIES_URL,sp.getString("language", "en"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                        sectionsListAll = new ArrayList<>();
                        JSONArray sections = jsonObject.getJSONArray("sections");
                        for(int i = 0;i < sections.length();i++){
                            ImageAndTextModel model = new ImageAndTextModel();
                            JSONObject obj = sections.getJSONObject(i);
                            String  section ;
                            section = obj.getString("section_name");
                            model.setText(section);
                            model.setId(obj.getString("section_id"));
                            sectionsListAll.add(model);
                            sectionsList.add(section);
                        }
                        sectionsAdapter = new SpinnerAdapter(AddOfferDataActivity.this, R.layout.spinner_item, sectionsList);
                        dataBinding.spCategory.setAdapter(sectionsAdapter);
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
                Log.e("languagge",sp.getString("language","en"));
                params.put("language" , sp.getString("language","en"));
                return params;
            }
        };
        requestQueue.add(request);
    }
    private void mSetupToasts(){
        uploadingMessage = new SpannableString(this.getString(R.string.wait));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(this)) , 0 , getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        done = new SpannableString(this.getString(R.string.done_saving));
        error = new SpannableString(this.getString(R.string.error));
        done.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.done_saving).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(this)) , 0 , this.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }
    private void setTypeFaces(){

        dataBinding.tvAddOfferData.setTypeface(Fonts.mSetupFontBold(this));
        dataBinding.tvAddAdv.setTypeface(Fonts.mSetupFontBold(this));
        dataBinding.tvDeterminePrice.setTypeface(Fonts.mSetupFontBold(this));
        dataBinding.toTV.setTypeface(Fonts.mSetupFontBold(this));
        dataBinding.fromTV.setTypeface(Fonts.mSetupFontBold(this));
        dataBinding.etOfferName.setTypeface(Fonts.mSetupFontRegular(this));
        dataBinding.to.setTypeface(Fonts.mSetupFontRegular(this));
        dataBinding.from.setTypeface(Fonts.mSetupFontRegular(this));
        dataBinding.etDescription.setTypeface(Fonts.mSetupFontRegular(this));
    }
}
