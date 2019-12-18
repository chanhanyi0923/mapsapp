package com.hanyi.mapsapp;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("user_name")
    public String username;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("signal_type")
    public int signalType;

    @SerializedName("signal_level")
    public int signalLevel;

    @SerializedName("people_num")
    public String peopleNum;

    @SerializedName("message")
    public String message;

    @Override
    public int hashCode() {
        String hashString = username + "$" + latitude + "$" + longitude + "$" + signalType + "$" +
                signalLevel + "$" + peopleNum + "$" + message;
        return hashString.hashCode();
    }

    public String getUsername() {
        return username;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public int getSignalLevel() {
        return signalLevel;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public String getMessage() {
        return message;
    }

    public String getSignalTypeName(Context context) {
        String[] signalTypeArray = context.getResources().getStringArray(R.array.signal_type_array);
        return signalTypeArray[this.signalType];
    }
}
