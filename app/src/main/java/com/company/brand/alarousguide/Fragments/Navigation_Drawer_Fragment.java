package com.company.brand.alarousguide.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.brand.alarousguide.Adapters.Navigation_Drawer_Adapter;
import com.company.brand.alarousguide.Models.Navigation_Drawer_Item;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.Utils.Fonts;
import com.company.brand.alarousguide.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by waheed_manii on 26/03/17.
 */

public class Navigation_Drawer_Fragment extends Fragment {


    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Navigation_Drawer_Adapter adapter;
    private View containerView;
    private static String[] TITLES = null;
    private FragmentDrawerListener drawerListener;
    private int roleId;



    public Navigation_Drawer_Fragment() {

    }


    private void mSetupRefr(View layout){
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new Navigation_Drawer_Adapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    private void mSetupListener(){

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        roleId = sharedPreferences.getInt("roleId", 0);
        Log.e("TEST_ROLE_ID" , String.valueOf(roleId));

      if (roleId == 1){

          TITLES = getResources().getStringArray(R.array.navCustomerTitles);

      } else {

          TITLES = getResources().getStringArray(R.array.navTraderTitles);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mSetupRefr(layout);
        mSetupListener();
        CircleImageView civ_shopImage = (CircleImageView) layout.findViewById(R.id.civ_shopImage);
        TextView tv_shop_name = (TextView) layout.findViewById(R.id.tv_shop_name);
        tv_shop_name.setTypeface(Fonts.mSetupFontBold(getActivity()));

        if (roleId != 0){

            String profileImage = sharedPreferences.getString("profileImage", "null");
            String name = sharedPreferences.getString("name", "null");
            Log.e("NAVIGATION_IMAGE" , profileImage);
            Log.e("NAVIGATION_NAME" , name);
            tv_shop_name.setText(name);

            if (profileImage.equals("null")){

                civ_shopImage.setImageResource(R.drawable.image_profile_menu);

            } else {

                String imgPath = Urls.PROFILE_IMAGE_PATH + profileImage;
                Picasso.with(getActivity()).load(imgPath).into(civ_shopImage);
            }



        }

        return layout;
    }


    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<Navigation_Drawer_Item> getData() {

        List<Navigation_Drawer_Item> data = new ArrayList<>();


        for (int i = 0; i < TITLES.length; i++) {
            Navigation_Drawer_Item navItem = new Navigation_Drawer_Item();
            navItem.setTitle(TITLES[i]);
            data.add(navItem);
        }
        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mDrawerToggle.syncState();
//            }
//        });

    }

    public static interface ClickListener {

        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}