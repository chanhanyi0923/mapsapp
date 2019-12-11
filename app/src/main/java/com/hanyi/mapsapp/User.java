package com.hanyi.mapsapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    static private User loggedInUser = null;
    static User getLoggedInUser() {
        return loggedInUser;
    }
    static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    @Expose
    public String token;

    @SerializedName("account_id")
    public int id;

    @SerializedName("user_name")
    public String name;

    @SerializedName("password")
    public String password;

    @SerializedName("resident_id")
    public String residentId;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("phone_number")
    public String phoneNumber;
}
