package com.hanyi.mapsapp;

import android.content.Context;
import android.util.Log;

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
                            Type listType = new TypeToken<ArrayList<AlarmInfo>>() {
                            }.getType();
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
                            Exception e2 = new Exception("Bad response: ", e);
                            callback.onFailed(e2);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Exception e = new Exception("Http failed");
                        callback.onFailed(e);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void signUp(final String username, final String password, final String residentId,
                       final String firstName, final String lastName, final String phoneNumber,
                       final IDataCallback<Boolean> callback) {
        String url = BASE_URL + "/create_user_account/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Map map = gson.fromJson(response, Map.class);
                        try {
                            if ("Success".equals(map.get("message"))) {
                                callback.onComplete(true);
                            }
                        } catch (Exception e) {
                            Exception e2 = new Exception("Bad response: ", e);
                            callback.onFailed(e2);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Exception e = new Exception("Http failed");
                        callback.onFailed(e);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", username);
                params.put("password", password);
                params.put("resident_id", residentId);
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("phone_number", phoneNumber);
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
                            Type listType = new TypeToken<ArrayList<User>>() {
                            }.getType();
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

    public void postEmergencyPost(final int id, final double latitude, final double longitude,
                                  final int signalType, final int signalLevel, final String message,
                                  final int peopleNum, final IDataCallback<Boolean> callback) {
        String url = BASE_URL + "/post_emergency_post/";
        final String token = User.getLoggedInUser().token;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Map map = gson.fromJson(response, Map.class);
                        try {
                            if ("Success".equals(map.get("message"))) {
                                callback.onComplete(true);
                            }
                        } catch (Exception e) {
                            Exception e2 = new Exception("Bad response: ", e);
                            callback.onFailed(e2);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Exception e = new Exception("Http failed");
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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("account_id", String.valueOf(id));
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("signal_type", String.valueOf(signalType));
                params.put("signal_level", String.valueOf(signalLevel));
                params.put("message", message);
                params.put("people_num", String.valueOf(peopleNum));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void getFriendPost(final IDataCallback<ArrayList<Post>> callback) {
        String url = BASE_URL + "/get_friend_post/?account_id=" + User.getLoggedInUser().id;
        final String token = User.getLoggedInUser().token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<Post>>() {
                            }.getType();
                            ArrayList<Post> postList = gson.fromJson(response, listType);
                            callback.onComplete(postList);
                        } catch (Exception e) {
                            Exception e2 = new Exception("Bad response: ", e);
                            callback.onFailed(e2);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Exception e = new Exception("Http failed");
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
