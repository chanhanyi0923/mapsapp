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
        switch (this.signalType) {
            case 1:
                return context.getResources().getString(R.string.signal_type_1);
            case 2:
                return context.getResources().getString(R.string.signal_type_2);
            case 3:
                return context.getResources().getString(R.string.signal_type_3);
            case 4:
                return context.getResources().getString(R.string.signal_type_4);
            case 5:
                return context.getResources().getString(R.string.signal_type_5);
            case 6:
                return context.getResources().getString(R.string.signal_type_6);
            case 7:
                return context.getResources().getString(R.string.signal_type_7);
            case 8:
                return context.getResources().getString(R.string.signal_type_8);
            case 9:
                return context.getResources().getString(R.string.signal_type_9);
            case 10:
                return context.getResources().getString(R.string.signal_type_10);
            case 11:
                return context.getResources().getString(R.string.signal_type_11);
            case 12:
                return context.getResources().getString(R.string.signal_type_12);
            case 13:
                return context.getResources().getString(R.string.signal_type_13);
            case 14:
                return context.getResources().getString(R.string.signal_type_14);
            case 15:
                return context.getResources().getString(R.string.signal_type_15);
            case 16:
                return context.getResources().getString(R.string.signal_type_16);
            case 17:
                return context.getResources().getString(R.string.signal_type_17);
            case 18:
                return context.getResources().getString(R.string.signal_type_18);
            default:
                return "";
        }
    }
}
