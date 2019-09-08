package com.example.tanushree.bakingtime.model;

// Date: 20/05/19

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable
{
    private String name;
    private double quantity;
    private  String measure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(quantity);
        dest.writeString(measure);
    }

    public Ingredient()
    {}

    private Ingredient(Parcel parcel)
    {
        name = parcel.readString();
        quantity = parcel.readDouble();
        measure = parcel.readString();
    }

    // To create from Parcel
    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
