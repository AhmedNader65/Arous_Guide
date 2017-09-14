package com.company.brand.alarousguide.Models;

/**
 * Created by waheed_manii on 08/09/2015.
 */

public class Navigation_Drawer_Item {

    private boolean showNotify;
    private String title;


    public Navigation_Drawer_Item() {

    }

    public Navigation_Drawer_Item(boolean showNotify, String title) {

        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

