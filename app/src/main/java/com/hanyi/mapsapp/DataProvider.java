package com.hanyi.mapsapp;

import android.content.Context;
import android.provider.ContactsContract;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    public void getAlarmInfo(final IDataCallback<AlarmInfo> callback) {
        String url = BASE_URL + "/get_alarm_info";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    AlarmInfo alarmInfo = new AlarmInfo();
                    alarmInfo.content = response;
                    // TrafficResult[] results = new Gson().fromJson(sb.toString(),TrafficResult[].class);
                    callback.onComplete(alarmInfo);
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
}
