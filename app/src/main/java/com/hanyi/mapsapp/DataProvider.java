package com.hanyi.mapsapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataProvider {
    private static DataProvider instance;
    private static final String BASE_URL = "http://10.0.2.2:5000";
    private RequestQueue queue;

    private DataProvider(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static DataProvider getInstance(Context context) {
        if (instance == null) {
            instance = new DataProvider(context);
        }
        return instance;
    }

    public void getAlarmInfo(final double latitude, final double longitude, final IDataCallback<ArrayList<AlarmInfo>> callback) {
        String url = BASE_URL + "/get_alarm_info/?latitude=" + latitude + "&longitude=" + longitude;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<AlarmInfo>>(){}.getType();
                        ArrayList<AlarmInfo> alarmInfos = gson.fromJson(response, listType);
                        callback.onComplete(alarmInfos);
                    } catch (Exception e) {
                        Exception e2 = new Exception("bad response: ", e);
                        callback.onFailed(e2);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Exception e = new Exception("http failed");
                    callback.onFailed(e);
                }
            }
        );

        queue.add(stringRequest);
    }

    public void signIn(final String username, final String password, final IDataCallback<User> callback) {
        String url = BASE_URL + "/login_user_account/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    Map map = gson.fromJson(response, Map.class);
                    try {
                        if ("Success".equals(map.get("message"))) {
                            System.err.println(gson.toJson(map.get("account")));
                            User user = gson.fromJson(gson.toJson(map.get("account")), User.class);
                            user.token = map.get("token").toString();
                            callback.onComplete(user);
                        }
                    } catch (Exception e) {
                        Exception e2 = new Exception("bad response: ", e);
                        callback.onFailed(e2);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Exception e = new Exception("http failed");
                    callback.onFailed(e);
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("user_name", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void searchFriends(String name, final IDataCallback<ArrayList<User>> callback) {
        final String url = BASE_URL + "/find_accounts/?name=" + name;
        final String token = User.getLoggedInUser().token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
                            ArrayList<User> users = gson.fromJson(response, listType);
                            callback.onComplete(users);
                        } catch (Exception e) {
                            Exception e2 = new Exception("bad response: ", e);
                            callback.onFailed(e2);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Exception e = new Exception("http failed");
                        callback.onFailed(e);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
