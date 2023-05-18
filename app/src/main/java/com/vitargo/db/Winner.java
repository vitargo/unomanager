package com.vitargo.db;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.io.Serializable;

public class Winner implements Serializable {

    private int id;
    private String name;
    private String score;
    private String date;

    public Winner() {
    }

    public Winner(String name, String score, String date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(@NonNull Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(name);
//        dest.writeString(score);
//    }
}
