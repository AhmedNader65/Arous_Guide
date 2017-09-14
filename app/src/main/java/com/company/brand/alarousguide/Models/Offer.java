package com.company.brand.alarousguide.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmed on 05/09/17.
 */

public class Offer implements Parcelable{

    private int offerId;
    private String name;
    private int rate;
    private int offerNum;
    private int offerDuration;
    private int offerEnd;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private double lat;
    private double lng;
    private int countryId;
    private int cityId;
    private int sectionId;
    private int priceFrom;
    private int priceTo;
    private String description;
    private boolean isSpecial;

    public Offer() {
    }

    protected Offer(Parcel in) {
        offerId = in.readInt();
        name = in.readString();
        rate = in.readInt();
        offerNum = in.readInt();
        offerDuration = in.readInt();
        offerEnd = in.readInt();
        img1 = in.readString();
        img2 = in.readString();
        img3 = in.readString();
        img4 = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        countryId = in.readInt();
        cityId = in.readInt();
        sectionId = in.readInt();
        priceFrom = in.readInt();
        priceTo = in.readInt();
        description = in.readString();
        isSpecial = in.readByte() != 0;
    }

    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getOfferNum() {
        return offerNum;
    }

    public void setOfferNum(int offerNum) {
        this.offerNum = offerNum;
    }

    public int getOfferDuration() {
        return offerDuration;
    }

    public void setOfferDuration(int offerDuration) {
        this.offerDuration = offerDuration;
    }

    public int getOfferEnd() {
        return offerEnd;
    }

    public void setOfferEnd(int offerEnd) {
        this.offerEnd = offerEnd;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(offerId);
        dest.writeString(name);
        dest.writeInt(rate);
        dest.writeInt(offerNum);
        dest.writeInt(offerDuration);
        dest.writeInt(offerEnd);
        dest.writeString(img1);
        dest.writeString(img2);
        dest.writeString(img3);
        dest.writeString(img4);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeInt(countryId);
        dest.writeInt(cityId);
        dest.writeInt(sectionId);
        dest.writeInt(priceFrom);
        dest.writeInt(priceTo);
        dest.writeString(description);
        dest.writeByte((byte) (isSpecial ? 1 : 0));
    }
}
