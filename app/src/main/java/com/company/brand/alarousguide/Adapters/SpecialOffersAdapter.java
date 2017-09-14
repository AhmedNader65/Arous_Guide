package com.company.brand.alarousguide.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.MyHolder>{

    ArrayList<Offer> mValues;
    Context context;
    int mType = 0;
    private SpannableString uploadingMessage, error,done,SureDelete,yes,cancel;
    public SpecialOffersAdapter(ArrayList<Offer> arrayList, int type) {
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OfferDetails.class).putExtra("offerId",mValues.get(position).getOfferId()));
            }
        });
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
