package com.company.brand.alarousguide.Models;

/**
 * Created by ahmed on 28/08/17.
 */

public class ImageAndTextModel {
    private String id;
    private String text;
    private String imgLink;

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}
