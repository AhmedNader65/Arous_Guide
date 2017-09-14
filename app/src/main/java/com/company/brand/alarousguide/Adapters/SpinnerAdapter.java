package com.company.brand.alarousguide.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.brand.alarousguide.Utils.Fonts;

import java.util.ArrayList;

/**
 * Created by waheed_manii on 22/04/17.
 */



 public class SpinnerAdapter extends ArrayAdapter<String> {


    public SpinnerAdapter(Context context, int resource, ArrayList<String> citiesName) {
        super(context, resource, citiesName);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(Fonts.mSetupFontRegular(getContext()));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(Fonts.mSetupFontRegular(getContext()));
        return view;
    }


}
