package com.example.pexelimages;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * a class that stores the image details
 * the class implements parcelable to allow passing of this class' object between activities
 */
public class PexelImage implements Parcelable {
    private int id;
    private String originalUrl;
    private String mediumUrl;
    private int photographerId;
    private String photographer;

    public PexelImage(int id, String originalUrl, String mediumUrl, int photographerId, String photographer) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.mediumUrl = mediumUrl;
        this.photographerId = photographerId;
        this.photographer = photographer;
    }

    //compulsory parcelable constructor
    protected PexelImage(Parcel in) {
        id = in.readInt();
        originalUrl = in.readString();
        mediumUrl = in.readString();
        photographerId = in.readInt();
        photographer = in.readString();
    }

    public static final Creator<PexelImage> CREATOR = new Creator<PexelImage>() {
        @Override
        public PexelImage createFromParcel(Parcel in) {
            return new PexelImage(in);
        }

        @Override
        public PexelImage[] newArray(int size) {
            return new PexelImage[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public int getPhotographerId() {
        return photographerId;
    }

    public void setPhotographerId(int photographerId) {
        this.photographerId = photographerId;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //write the variable values to the destination activity
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalUrl);
        dest.writeString(mediumUrl);
        dest.writeInt(photographerId);
        dest.writeString(photographer);
    }
}
