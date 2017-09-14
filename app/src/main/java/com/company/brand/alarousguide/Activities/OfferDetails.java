package com.company.brand.alarousguide.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import com.company.brand.alarousguide.Models.CustomerOffer;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.company.brand.alarousguide.databinding.ActivityOfferDetailsBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.company.brand.alarousguide.R.string.error;

public class OfferDetails extends AppCompatActivity {
    ActivityOfferDetailsBinding binding;
    private CustomerOffer offer;
    String offerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer_details);
        binding.progress.setVisibility(View.VISIBLE);
        binding.content.setVisibility(View.GONE);
        offerId = String.valueOf(getIntent().getIntExtra("offerId", 0));
        getData();
        setTypeFaces();
        setClicks();

    }

    private void setClicks() {
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + offer.getWhatsapp());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
            }
        });
        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + offer.getPhone()));
                if (ActivityCompat.checkSelfPermission(OfferDetails.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(OfferDetails.this,
                            new String[]{android.Manifest.permission.CALL_PHONE},
                            1);
                    return;
                }
                startActivity(intent);
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferDetails.this.finish();
            }
        });
        binding.viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OfferDetails.this, ViewOfferLocationActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + offer.getPhone()));
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(OfferDetails.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void setTypeFaces() {
        binding.errorTV.setTypeface(Fonts.mSetupFontBold(this));
        binding.tvMyProfile.setTypeface(Fonts.mSetupFontBold(this));
        binding.offerDescription.setTypeface(Fonts.mSetupFontRegular(this));
        binding.tvMyProfile.setTypeface(Fonts.mSetupFontRegular(this));
        binding.offerFrom.setTypeface(Fonts.mSetupFontBold(this));
        binding.callTV.setTypeface(Fonts.mSetupFontRegular(this));
        binding.whatsappTV.setTypeface(Fonts.mSetupFontRegular(this));
        binding.photosTV.setTypeface(Fonts.mSetupFontBold(this));
        binding.offerName.setTypeface(Fonts.mSetupFontBold(this));
        binding.phoneTV.setTypeface(Fonts.mSetupFontBold(this));
        binding.priceTV.setTypeface(Fonts.mSetupFontBold(this));
        binding.viewOnMap.setTypeface(Fonts.mSetupFontRegular(this));
    }

    private void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Urls.OFFER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE_REGISTRATION" , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("0")){

                        Toast.makeText(OfferDetails.this, error, Toast.LENGTH_SHORT).show();

                    } else if (success.equals("1")){
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        for(int i = 0 ; i <dataArray.length();i++){
                            offer = new CustomerOffer();
                            JSONObject offerObj = dataArray.getJSONObject(i);
                            offer.setImage1(offerObj.getString("image1"));;
                            offer.setImage2(offerObj.getString("image2"));;
                            offer.setImage3(offerObj.getString("image3"));;
                            offer.setImage4(offerObj.getString("image4"));;
                            offer.setOfferLat(offerObj.getString("offer_lat"));;
                            offer.setOfferLng(offerObj.getString("offer_lng"));;
                            offer.setOfferName(offerObj.getString("offer_name"));;
                            offer.setTraderName(offerObj.getString("trader_name"));;
                            offer.setDescription(offerObj.getString("description"));;
                            offer.setPriceFrom(offerObj.getString("price_from"));;
                            offer.setPriceTo(offerObj.getString("price_to"));;
                            offer.setWhatsapp(offerObj.getString("whatsup"));;
                            offer.setPhone(offerObj.getString("phone"));;
                            offer.setRate(Integer.parseInt(offerObj.getString("rate")));;
                            offer.setTraderId(offerObj.getString("trader_id"));;
                            setDateInfields();
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
                params.put("offer_id" , offerId);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void setDateInfields() {
        binding.progress.setVisibility(View.GONE);
        binding.content.setVisibility(View.VISIBLE);
        binding.offerDescription.setText(offer.getDescription());
        binding.phone.setText(offer.getPhone());
        String name = "<font color='#089DFA'>"+offer.getTraderName()+"</font>";

        binding.offerFrom.setText(Html.fromHtml(getString(R.string.offerFrom)+" "+name));
        binding.offerName.setText(offer.getOfferName());
        binding.price.setText(offer.getPriceFrom());
        binding.offerFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OfferDetails.this, ProfileActivity.class)
                        .putExtra("userId",Integer.valueOf(offer.getTraderId()))
                        .putExtra("roleId",2)
                        .putExtra("customer",true)
                );
            }
        });
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                binding.image1.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        binding.image1.setTag(target);
        Picasso.with(this).load(Urls.OFFER_IMG_BASE+offer.getImage1()).into((Target) binding.image1.getTag());
        Target target2 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                binding.image2.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        binding.image2.setTag(target2);
        Picasso.with(this).load(Urls.OFFER_IMG_BASE+offer.getImage2()).into((Target) binding.image2.getTag());

        Target target3 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                binding.image3.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        binding.image3.setTag(target3);
        Picasso.with(this).load(Urls.OFFER_IMG_BASE+offer.getImage3()).into((Target) binding.image3.getTag());

        Target target4 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                binding.image4.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        binding.image4.setTag(target4);
        Picasso.with(this).load(Urls.OFFER_IMG_BASE+offer.getImage4()).into((Target) binding.image4.getTag());
    }
}
