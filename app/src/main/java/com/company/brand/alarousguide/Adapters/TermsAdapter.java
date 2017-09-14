package com.company.brand.alarousguide.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Fonts;

import java.util.ArrayList;

/**
 * Created by ahmed on 28/08/17.
 */

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.MyHolder>{

    ArrayList<String> mValues;
    Context context;
    public TermsAdapter(ArrayList<String> arrayList) {
        mValues = arrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.condition_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.name.setText(mValues.get(position));
        holder.name.setTypeface(Fonts.mSetupFontMedium(context));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        final TextView name;
        public MyHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.txt);
        }
    }
}
