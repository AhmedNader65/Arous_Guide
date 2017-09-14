package com.company.brand.alarousguide.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.brand.alarousguide.Models.Navigation_Drawer_Item;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Fonts;

import java.util.Collections;
import java.util.List;


/**
 * Created by waheed_manii on 27/03/17.
 */

public class Navigation_Drawer_Adapter extends RecyclerView.Adapter<Navigation_Drawer_Adapter.MyViewHolder> {

    private   int [] PICTURES ;
    SharedPreferences sharedPreferences;
    List<Navigation_Drawer_Item> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;



    public Navigation_Drawer_Adapter(Context context, List<Navigation_Drawer_Item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        int roleId = sharedPreferences.getInt("roleId", 0);

        if (roleId == 1){

            PICTURES = new int[] {R.drawable.icon_nav_home, R.drawable.icon_nav_alerts,
                                  R.drawable.icon_nav_cart, R.drawable.icon_nav_gifts,
                                  R.drawable.icon_nav_share, R.drawable.icon_nav_terms,
                                  R.drawable.icon_nav_logout, R.drawable.icon_nav_logout,
                                  R.drawable.icon_nav_logout};

        } else {

            PICTURES = new int[] {R.drawable.icon_nav_home, R.drawable.icon_nav_adv_way,
                                  R.drawable.icon_nav_add, R.drawable.icon_nav_gifts,
                                  R.drawable.icon_nav_alerts, R.drawable.icon_nav_share,
                                  R.drawable.icon_nav_terms, R.drawable.icon_nav_logout,
                                  R.drawable.icon_nav_alerts};
        }

    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_nav, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Navigation_Drawer_Item current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.title.setTypeface(Fonts.mSetupFontMedium(context));
        holder.icon.setImageResource(PICTURES[position]);
        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout ll_linearLayout;
        TextView title;
        ImageView icon;

         MyViewHolder(View itemView) {
            super(itemView);

            ll_linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_linearLayout);
            title = (TextView) itemView.findViewById(R.id.textRow);
            icon = (ImageView) itemView.findViewById(R.id.imageView);

        }

    }
}
