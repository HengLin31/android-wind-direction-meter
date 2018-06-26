package com.henglin31.android_wind_direction_meter.api;

import android.util.Log;

import com.google.gson.Gson;
import com.henglin31.android_wind_direction_meter.bean.LocationData;
import com.henglin31.android_wind_direction_meter.bean.WindDirectionData;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetWindDirectionMeterData implements Runnable {
    private final static String URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/O-A0001-001?Authorization=rdec-key-123-45678-011121314";
    private final static String LOCATION_NAME = "臺西";

    private ThreadCallback<LocationData> threadCallback;

    public GetWindDirectionMeterData(ThreadCallback<LocationData> threadCallback){
        this.threadCallback = threadCallback;
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        try {
            final Response response = client.newCall(request).execute();
            final String res = response.body().string();
            final WindDirectionData windDirectionData = new Gson().fromJson(res, WindDirectionData.class);
            final LocationData locationData = windDirectionData.getLocationByName(LOCATION_NAME);
            this.threadCallback.callback(locationData);
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("sendBtn", e);
        }
    }
}
