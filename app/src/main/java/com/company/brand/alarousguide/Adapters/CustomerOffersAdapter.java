package com.company.brand.alarousguide.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.company.brand.alarousguide.Models.Offer;
import com.company.brand.alarousguide.Activities.OfferDetails;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by ahmed on 28/08/17.
 */

public class CustomerOffersAdapter extends RecyclerView.Adapter<CustomerOffersAdapter.MyHolder>{

    ArrayList<Offer> mValues;
    Context context;
    private SpannableString uploadingMessage, error,done;
    public CustomerOffersAdapter(ArrayList<Offer> arrayList) {
        mValues = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        mSetupToasts();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.customer_offers_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.name.setText(mValues.get(position).getName());
        holder.name.setTypeface(Fonts.mSetupFontRegular(context));
        holder.ratingBar.setRating(mValues.get(position).getRate());
        holder.offerPrice.setText(context.getString(R.string.price) +": "+ mValues.get(position).getPriceFrom());
        holder.offerPrice.setTypeface(Fonts.mSetupFontRegular(context));
        holder.details.setTypeface(Fonts.mSetupFontRegular(context));
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
        if(mValues.get(position).isSpecial()){
            holder.specialOffer.setVisibility(View.VISIBLE);
            holder.details.setBackgroundResource(R.drawable.special_details_btn);
        }else{
            holder.specialOffer.setVisibility(View.GONE);
            holder.details.setBackgroundResource(R.drawable.details_btn);
        }
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OfferDetails.class).putExtra("offerId",mValues.get(position).getOfferId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        final TextView name, offerPrice;
        final ImageView img;
        final RatingBar ratingBar;
        final ImageView specialOffer;
        final Button details;
        public MyHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.name);
            offerPrice = (TextView) itemView.findViewById(R.id.offer_num);
            img= (ImageView) itemView.findViewById(R.id.content);
            ratingBar= (RatingBar) itemView.findViewById(R.id.starts);
            specialOffer= (ImageView) itemView.findViewById(R.id.special);
            details= (Button) itemView.findViewById(R.id.details_btn);
        }
    }

    private void mSetupToasts(){
        uploadingMessage = new SpannableString(context.getString(R.string.wait));
        uploadingMessage.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(context)) , 0 , context.getString(R.string.wait).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        done = new SpannableString(context.getString(R.string.done_saving));
        error = new SpannableString(context.getString(R.string.error));
        done.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(context)) , 0 , context.getString(R.string.done_saving).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        error.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontRegular(context)) , 0 , context.getString(R.string.error).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }
}
