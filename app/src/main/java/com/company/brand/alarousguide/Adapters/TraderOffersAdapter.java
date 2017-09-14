package com.company.brand.alarousguide.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.brand.alarousguide.Models.Offer;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.TraderActivities.EditOfferDataActivity;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed on 28/08/17.
 */

public class TraderOffersAdapter extends RecyclerView.Adapter<TraderOffersAdapter.MyHolder>{

    ArrayList<Offer> mValues;
    Context context;
    int mType = 0;
    private SpannableString uploadingMessage, error,done,SureDelete,yes,cancel;
    public TraderOffersAdapter(ArrayList<Offer> arrayList,int type) {
        mValues = arrayList;
        mType = type;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        mSetupToasts();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.my_offers_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        if(mType ==1 ){
            holder.editOffer.setVisibility(View.GONE);
            holder.deleteOffer.setVisibility(View.GONE);
        }
        holder.name.setText(mValues.get(position).getName());
        holder.name.setTypeface(Fonts.mSetupFontBold(context));
        holder.ratingBar.setRating(mValues.get(position).getRate());

        String ends = "<font color='#089DFA'>"+mValues.get(position).getOfferEnd()+" " +context.getString(R.string.days)+"</font>";
        holder.offerEnds.setText(Html.fromHtml(context.getString(R.string.endsIn)+ends));
        holder.offerEnds.setTypeface(Fonts.mSetupFontRegular(context));
        String dur = "<font color='#089DFA'>"+mValues.get(position).getOfferDuration()+" " +context.getString(R.string.days)+"</font>";
        holder.offerDuration.setText(Html.fromHtml(context.getString(R.string.offerDuration)+dur));
        holder.offerDuration.setTypeface(Fonts.mSetupFontRegular(context));
        String num = "<font color='#999999'>"+mValues.get(position).getOfferNum()+"</font>";
        holder.offerNum.setText(Html.fromHtml(context.getString(R.string.offerNum)+num));
        holder.offerNum.setTypeface(Fonts.mSetupFontRegular(context));
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                holder.img.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        holder.img.setTag(target);
        Picasso.with(context).load(Urls.OFFER_IMG_BASE+mValues.get(position).getImg1()).into((Target) holder.img.getTag());
        holder.editOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).startActivityForResult(new Intent(context, EditOfferDataActivity.class).putExtra("offer",mValues.get(position)),0);
            }
        });
        holder.deleteOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(SureDelete);
                dialog.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        StringRequest request = new StringRequest(Request.Method.POST, Urls.OFFER_DELETE, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("RESPONSE_DELETE_OFFER" , response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    if (success.equals("1")){
                                        Toast.makeText(context, done, Toast.LENGTH_SHORT).show();
                                        mValues.remove(position);
                                        notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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
                                params.put("offer_id" , String.valueOf(mValues.get(position).getOfferId()));
                                return params;
                            }
                        };
                        requestQueue.add(request);
                    }
                });
                dialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                Button btn1 = (Button) alertDialog.findViewById(android.R.id.button1);
                btn1.setTypeface(Fonts.mSetupFontRegular(context));

                Button btn2 = (Button) alertDialog.findViewById(android.R.id.button2);
                btn2.setTypeface(Fonts.mSetupFontRegular(context));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        final TextView name,offerNum,offerDuration,offerEnds;
        final ImageView img;
        final ImageView deleteOffer;
        final ImageView editOffer;
        final RatingBar ratingBar;
        public MyHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.name);
            offerNum= (TextView) itemView.findViewById(R.id.offer_num);
            offerDuration= (TextView) itemView.findViewById(R.id.offer_duration);
            offerEnds= (TextView) itemView.findViewById(R.id.offer_ends);
            img= (ImageView) itemView.findViewById(R.id.content);
            deleteOffer= (ImageView) itemView.findViewById(R.id.delete);
            editOffer= (ImageView) itemView.findViewById(R.id.edit);
            ratingBar= (RatingBar) itemView.findViewById(R.id.starts);
        }
    }

    private void mSetupToasts(){
        uploadingMessage = new SpannableString(context.getString(R.string.wait));
        SureDelete = new SpannableString(context.getString(R.string.confirmDeleteOffer));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(context)) , 0 , context.getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SureDelete.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(context)) , 0 , context.getString(R.string.confirmDeleteOffer).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        yes = new SpannableString(context.getString(R.string.yesSure));
        yes.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(context)) , 0 , context.getString(R.string.yesSure).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        cancel = new SpannableString(context.getString(R.string.cancel));
        cancel.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(context)) , 0 , context.getString(R.string.cancel).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        done = new SpannableString(context.getString(R.string.done_saving));
        error = new SpannableString(context.getString(R.string.error));
        done.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(context)) , 0 , context.getString(R.string.done_saving).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(context)) , 0 , context.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
       done.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(context)) , 0 , context.getString(R.string.done_saving).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(context)) , 0 , context.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }
}
