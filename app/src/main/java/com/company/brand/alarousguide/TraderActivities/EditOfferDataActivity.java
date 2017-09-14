package com.company.brand.alarousguide.TraderActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.company.brand.alarousguide.Models.ImageAndTextModel;
import com.company.brand.alarousguide.Models.Offer;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.Utils.Util;
import com.company.brand.alarousguide.databinding.ActivityEditOfferDataBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
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

public class EditOfferDataActivity extends AppCompatActivity implements IPickResult, View.OnClickListener {
    ActivityEditOfferDataBinding dataBinding ;
    View currentImgView;
    Bitmap[] imgArray = new Bitmap[4];
    boolean doneLoading = false;
    SharedPreferences sp ;
    private int countryId = 0;
    private ArrayList<String> sectionsList;
    private ArrayList<ImageAndTextModel> sectionsListAll;
    private SpinnerAdapter sectionsAdapter;
    private ProgressDialog progressDialog;
    private SpannableString uploadingMessage, error,done;
    private Offer offer;
    private int cityId;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_offer_data);
        dataBinding.btAdd.setOnClickListener(this);
        dataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditOfferDataActivity.this.finish();
            }
        });
        getSectionData();
        mSetupToasts();
        setTypeFaces();
    }

    private void mGetData() {
        offer = getIntent().getParcelableExtra("offer");
        dataBinding.etDescription.setText(offer.getDescription());
        dataBinding.etOfferName.setText(offer.getName());
        dataBinding.from.setText(String.valueOf(offer.getPriceFrom()));
        dataBinding.to.setText(String.valueOf(offer.getPriceTo()));
        for(int i = 0 ; i < sectionsListAll.size();i++){
            if(Integer.parseInt(sectionsListAll.get(i).getId()) == offer.getSectionId()){
                dataBinding.spCategory.setSelection(i);
            }
        }
        cityId = offer.getCityId();
        countryId = offer.getCountryId();
        lat = offer.getLat();
        lng = offer.getLng();
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                dataBinding.civAdv1.setImageBitmap(bitmap);
                dataBinding.civAdv1.setOnClickListener(EditOfferDataActivity.this);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        dataBinding.civAdv1.setTag(target);
        Picasso.with(this).load(Urls.OFFER_IMG_BASE+offer.getImg1()).into((Target) dataBinding.civAdv1.getTag());
        Target target2 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                dataBinding.civAdv2.setImageBitmap(bitmap);
                dataBinding.civAdv2.setOnClickListener(EditOfferDataActivity.this);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        dataBinding.civAdv2.setTag(target2);
        Picasso.with(this).load(Urls.OFFER_IMG_BASE+offer.getImg2()).into((Target) dataBinding.civAdv2.getTag());

        Target target3 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                dataBinding.civAdv3.setImageBitmap(bitmap);
                dataBinding.civAdv3.setOnClickListener(EditOfferDataActivity.this);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        dataBinding.civAdv3.setTag(target3);
        Picasso.with(this).load(Urls.OFFER_IMG_BASE+offer.getImg3()).into((Target) dataBinding.civAdv3.getTag());

        Target target4 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                dataBinding.civAdv4.setImageBitmap(bitmap);
                dataBinding.civAdv4.setOnClickListener(EditOfferDataActivity.this);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        dataBinding.civAdv4.setTag(target4);
        Picasso.with(this).load(Urls.OFFER_IMG_BASE+offer.getImg4()).into((Target) dataBinding.civAdv4.getTag());

        doneLoading = true;
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Bitmap.
            Bitmap bitmap = null;
            if(currentImgView.getId() == dataBinding.civAdv1.getId()){
                String path = Util.resizeAndCompressImageBeforeSend(this,r.getPath(),r.getUri().getLastPathSegment());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(path,bmOptions);
                imgArray[0] =bitmap;

            }
            if(currentImgView.getId() == dataBinding.civAdv2.getId()){

                String path = Util.resizeAndCompressImageBeforeSend(this,r.getPath(),r.getUri().getLastPathSegment());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(path,bmOptions);
                imgArray[1] =bitmap;
            }
            if(currentImgView.getId() == dataBinding.civAdv3.getId()){

                String path = Util.resizeAndCompressImageBeforeSend(this,r.getPath(),r.getUri().getLastPathSegment());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(path,bmOptions);
                imgArray[2] =bitmap;
            }
            if(currentImgView.getId() == dataBinding.civAdv4.getId()){

                String path = Util.resizeAndCompressImageBeforeSend(this,r.getPath(),r.getUri().getLastPathSegment());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(path,bmOptions);
                imgArray[3] =bitmap;
            }
            ((ImageView)currentImgView).setImageBitmap(r.getBitmap());

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
            PickImageDialog.build(new PickSetup()).show(EditOfferDataActivity.this);
        }else if (v.getId() == R.id.bt_add){
            if(!doneLoading){
            }else {
                String name = null;
                String from = null;
                String to = null;
                String description = null;
                int traderId;
                String sectionId;
                String offerId;
                    if (!dataBinding.from.getText().toString().isEmpty()) {
                        from = dataBinding.from.getText().toString();
                        if (!dataBinding.to.getText().toString().isEmpty()) {
                            to = dataBinding.to.getText().toString();
                            if (!dataBinding.etOfferName.getText().toString().isEmpty()) {
                                name = dataBinding.etOfferName.getText().toString();
                                if (!dataBinding.etDescription.getText().toString().isEmpty()) {
                                    description = dataBinding.etDescription.getText().toString();
                                    traderId = sp.getInt("userId", 0);

                                    Log.e("section", dataBinding.spCategory.getSelectedItemPosition() + "");
                                    sectionId = sectionsListAll.get(dataBinding.spCategory.getSelectedItemPosition()).getId();
                                    postData(String.valueOf(offer.getOfferId()),name, from, to, description, traderId, cityId, countryId, sectionId, lat, lng);


                                } else {
                                    dataBinding.etDescription.setError(getString(R.string.required));
                                }
                            } else {
                                dataBinding.etOfferName.setError(getString(R.string.required));
                            }
                        } else {
                            dataBinding.to.setError(getString(R.string.required));
                        }
                    } else {
                        dataBinding.from.setError(getString(R.string.required));
                    }
            }
        }
    }

    private void postData(final String offer_id,final String name,final String from, final String to, final String description, final int traderId, final int cityId, final int countryId, final String sectionId, final double lat, final double lng) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(uploadingMessage);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.EDIT_OFFER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){

                        progressDialog.dismiss();
                        Toast.makeText(EditOfferDataActivity.this, error, Toast.LENGTH_SHORT).show();
                    } else if (success.equals("1")){

                        progressDialog.dismiss();
                        Toast.makeText(EditOfferDataActivity.this, done, Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK,new Intent());
                        EditOfferDataActivity.this.finish();
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
                    Toast.makeText(EditOfferDataActivity.this,
                            "No Connection",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(EditOfferDataActivity.this,
                            "Check username & password",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(EditOfferDataActivity.this,
                            "Server error! try again later",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(EditOfferDataActivity.this,
                            "Network Error try again",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(EditOfferDataActivity.this,
                            "ooo we have a problem",
                            Toast.LENGTH_LONG).show();
                }
            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> params = new HashMap<>();
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
                if(imgArray[0]==null){
                    params.put("image[0]" , offer.getImg1());
                }else {
                    params.put("image[0]", mEncodeToBase64(imgArray[0], Bitmap.CompressFormat.PNG, 70));
                }
                if(imgArray[1]==null){
                    params.put("image[1]" , offer.getImg2());
                }else {
                    params.put("image[1]", mEncodeToBase64(imgArray[1], Bitmap.CompressFormat.PNG, 70));
                }
                if(imgArray[2]==null){
                    params.put("image[2]" , offer.getImg3());
                }else {
                    params.put("image[2]", mEncodeToBase64(imgArray[2], Bitmap.CompressFormat.PNG, 70));
                }
                if(imgArray[3]==null){
                    params.put("image[3]" , offer.getImg4());
                }else {
                    params.put("image[3]", mEncodeToBase64(imgArray[3], Bitmap.CompressFormat.PNG, 70));
                }
                params.put("name" , String.valueOf(name));
                params.put("trader_id" , String.valueOf(traderId));
                params.put("offer_id" , String.valueOf(offer_id));
                params.put("from" , String.valueOf(from));
                params.put("to" , String.valueOf(to));
                params.put("description" , String.valueOf(description));
                params.put("country_id" , String.valueOf(countryId));
                params.put("city_id" , String.valueOf(cityId));
                params.put("section_id" , String.valueOf(sectionId));
                params.put("lat" , String.valueOf(lat));
                params.put("lng" , String.valueOf(lng));
                params.put("lng" , String.valueOf(lng));
                return params;
            }
        };
        requestQueue.add(request);

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
                        sectionsAdapter = new SpinnerAdapter(EditOfferDataActivity.this, R.layout.spinner_item, sectionsList);
                        dataBinding.spCategory.setAdapter(sectionsAdapter);
                        mGetData();
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
        dataBinding.btAdd.setTypeface(Fonts.mSetupFontRegular(this));
        dataBinding.to.setTypeface(Fonts.mSetupFontRegular(this));
        dataBinding.from.setTypeface(Fonts.mSetupFontRegular(this));
        dataBinding.etDescription.setTypeface(Fonts.mSetupFontRegular(this));
    }
}
