package com.henglin31.android_wind_direction_meter.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationData {
    private final static String ELEMENT_NAME = "elementName";
    private final static String ELEMENT_VALUE = "elementValue";
    private final static String WIND_ELEMENT_NAME = "WDIR";
    private final static String WIND_NAME_KEY = "WIND_NAME";

    private String lat;
    private String lon;
    private String locationName;
    private String stationId;
    private Map time;
    private List<Map> weatherElement;

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getStationId() {
        return stationId;
    }

    public Map getTime() {
        return time;
    }

    public Map<String, String> getWeatherElement() {
        Map<String, String> result = new HashMap<>();
        for(Map ele:weatherElement){
            result.put(ele.get(ELEMENT_NAME).toString(), ele.get(ELEMENT_VALUE).toString());
            if(ele.get(ELEMENT_NAME).toString().equals(WIND_ELEMENT_NAME)){
                float degree = Float.parseFloat(ele.get(ELEMENT_VALUE).toString());
                result.put(WIND_NAME_KEY, windDirection(degree));
            }
        }
        return result;
    }

    private String windDirection(float degree){
        if(degree == 0f) return "靜風";
        if(11.26 <= degree  && degree <= 33.75)  return "北北東";
        if(33.26 <= degree  && degree <= 56.75)  return "東北";
        if(56.26 <= degree  && degree <= 78.75)  return "東北東";
        if(78.26 <= degree  && degree <= 101.75) return "東";
        if(101.26 <= degree && degree <= 123.75) return "東南東";
        if(123.26 <= degree && degree <= 146.75) return "東南";
        if(146.26 <= degree && degree <= 168.75) return "南南東";
        if(168.26 <= degree && degree <= 191.75) return "南";
        if(191.26 <= degree && degree <= 213.75) return "南南西";
        if(213.26 <= degree && degree <= 236.75) return "西南";
        if(236.26 <= degree && degree <= 258.75) return "西南西";
        if(258.26 <= degree && degree <= 281.75) return "西";
        if(281.26 <= degree && degree <= 303.75) return "西北西";
        if(303.26 <= degree && degree <= 326.75) return "西北";
        if(326.26 <= degree && degree <= 348.75) return "北北西";
        return "北";
    }

}
