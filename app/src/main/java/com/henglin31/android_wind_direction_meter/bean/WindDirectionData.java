package com.henglin31.android_wind_direction_meter.bean;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WindDirectionData {
    private final static String LOCATION_KEY = "location";

    private String success;
    private Map result;
    private Map records;

    public String getSuccess() {
        return success;
    }

    public Map getResult() {
        return result;
    }

    public Map getRecords() {
        return records;
    }

    public List<LocationData> getLocations(){
        if(this.records.containsKey(LOCATION_KEY)){
            return (List<LocationData>) this.records.get(LOCATION_KEY);
        }
        return Collections.EMPTY_LIST;
    }

    public LocationData getLocationByName(String name){
        Gson gson = new Gson();
        final String str = gson.toJson(this.getLocations());
        List<LocationData> locationDatas = gson.fromJson(str, new TypeToken<List<LocationData>>(){}.getType());
        for(LocationData locationData:locationDatas){
            if(locationData.getLocationName().equals(name)){
                return locationData;
            }
        }
        return new LocationData();
    }
}
