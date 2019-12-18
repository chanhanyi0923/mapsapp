package com.hanyi.mapsapp;

import android.content.Context;
import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

public class AlarmInfo {
    @SerializedName("content")
    public String content;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("signal_type")
    public int signalType;

    @SerializedName("signal_level")
    public int signalLevel;

    @Override
    public int hashCode() {
        String hashString = latitude + "$" + longitude + "$" + signalType + "$" + signalLevel;
        return hashString.hashCode();
    }

    public String getSignalTypeName(Context context) {
        String[] signalTypeArray = context.getResources().getStringArray(R.array.signal_type_array);
        return signalTypeArray[this.signalType];
    }

    public int getSignalLevelColor() {
        switch (this.signalLevel) {
            case 1:
                return Color.argb(128, 0, 0, 255);
            case 2:
                return Color.argb(128, 255, 255, 0);
            case 3:
                return Color.argb(128, 255, 140, 0);
            case 4:
                return Color.argb(128, 255, 0, 0);
            default:
                return Color.argb(128, 255, 255, 255);
        }
    }
}
