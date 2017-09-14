package com.company.brand.alarousguide.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.company.brand.alarousguide.CustomerActivities.CustomerOffersActivity;
import com.company.brand.alarousguide.Models.ImageAndTextModel;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Custom_Type_face_Span;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import static com.company.brand.alarousguide.CustomerActivities.HomeActivity.cityId;

/**
 * Created by ahmed on 28/08/17.
 */

public class ImageAndTextAdapter extends RecyclerView.Adapter<ImageAndTextAdapter.MyHolder>{
    SpannableString chooseCountry , loginFirst;
    ArrayList<ImageAndTextModel> mValues;
    Context context;
    int mRes ;
    public ImageAndTextAdapter(int resource , ArrayList<ImageAndTextModel> arrayList) {
        mRes = resource;
        mValues = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(mRes,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        setToasts();
        holder.content.setText(mValues.get(position).getText());
        holder.content.setTypeface(Fonts.mSetupFontRegular(context));
        holder.progress.setVisibility(View.VISIBLE);
        holder.img.setVisibility(View.GONE);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.img.setVisibility(View.VISIBLE);
                holder.progress.setVisibility(View.GONE);
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
        if(mRes == R.layout.alert_item){

            Picasso.with(context).load(Urls.ALERTS_BASE_IMG + mValues.get(position).getImgLink()).into((Target) holder.img.getTag());
        }
        else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cityId!=0) {
                        if(PreferenceManager.getDefaultSharedPreferences(context).getInt("roleId",5)==5){
                            Toast.makeText(context, loginFirst, Toast.LENGTH_SHORT).show();
                        }else {
                            context.startActivity(new Intent(context, CustomerOffersActivity.class)
                                    .putExtra("secId", mValues.get(position).getId())
                                    .putExtra("secName", mValues.get(position).getText())
                                    .putExtra("cityId", cityId + ""));
                        }
                    }else{
                        Toast.makeText(context, chooseCountry, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Picasso.with(context).load(Urls.SECTION_IMAGES_BASE + mValues.get(position).getImgLink()).into((Target) holder.img.getTag());
        }
    }
    private void setToasts(){
        loginFirst = new SpannableString(context.getString(R.string.login_first));
        loginFirst.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(context)) , 0 ,context.getString(R.string.login_first).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        loginFirst = new SpannableString(context.getString(R.string.login_first));
        loginFirst.setSpan(new Custom_Type_face_Span("" , Fonts.mSetupFontBold(context)) , 0 , context.getString(R.string.login_first).length() , Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        final TextView content;
        final ImageView img;
        final ProgressBar progress;
        public MyHolder(View itemView) {
            super(itemView);
            content= (TextView) itemView.findViewById(R.id.content);
            img= (ImageView) itemView.findViewById(R.id.section_img);
            progress= (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }
}
