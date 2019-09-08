package com.example.tanushree.bakingtime.model;

// Date: 20/05/19

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable
{
    private int id;
    private String name;
    private String description;
    private String videoUrl;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(imageUrl);
    }

    public Step()
    {}

    private Step(Parcel parcel)
    {
        id = parcel.readInt();
        name = parcel.readString();
        description = parcel.readString();
        videoUrl = parcel.readString();
        imageUrl = parcel.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}