package com.company.brand.alarousguide.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.brand.alarousguide.Models.Gift;
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

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.MyHolder>{

    ArrayList<Gift> mValues;
    Context context;
    private SpannableString uploadingMessage, error,done;
    public GiftsAdapter(ArrayList<Gift> arrayList) {
        mValues = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        mSetupToasts();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.gifts_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.name.setText(mValues.get(position).getTitle());
        holder.name.setTypeface(Fonts.mSetupFontRegular(context));
        holder.url.setTypeface(Fonts.mSetupFontRegular(context));
        holder.url.setText(mValues.get(position).getLink());
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
        Picasso.with(context).load(Urls.GIFTS_IMG_BASE+mValues.get(position).getImg()).into((Target) holder.img.getTag());
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mValues.get(position).getLink()));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        final TextView name,url;
        final ImageView img;
        public MyHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.name);
            url= (TextView) itemView.findViewById(R.id.link);
            img= (ImageView) itemView.findViewById(R.id.content);
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
