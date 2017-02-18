package com.three38inc.app.picsmash;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jobith on 8/6/2015.
 */
public class Pixie implements Parcelable{
    private String imgName;
    private String imgUrl;
    private String imgCategory;
    private String imgCourtesy;

    public Pixie(){
        //nothing in here
    }
    public Pixie(Parcel input){
        imgName = input.readString();
        imgCategory = input.readString();
        imgUrl = input.readString();
        imgCourtesy = input.readString();
    }

    public Pixie( String imgCategory, String imgName, String imgUrl, String imgCourtesy) {
        this.imgName = imgName;
        this.imgUrl = imgUrl;
        this.imgCategory = imgCategory;
        this.imgCourtesy = imgCourtesy;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgCategory() {
        return imgCategory;
    }

    public void setImgCategory(String imgCategory) {
        this.imgCategory = imgCategory;
    }

    public String getImgCourtesy() {
        return imgCourtesy;
    }

    public void setImgCourtesy(String imgCourtesy) {
        this.imgCourtesy = imgCourtesy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgName);
        dest.writeString(imgCategory);
        dest.writeString(imgUrl);
        dest.writeString(imgCourtesy);

    }

    public static final Parcelable.Creator<Pixie> CREATOR
            = new Parcelable.Creator<Pixie>() {
        public Pixie createFromParcel(Parcel in) {
            return new Pixie(in);
        }

        public Pixie[] newArray(int size) {
            return new Pixie[size];
        }
    };
}
