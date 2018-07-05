package com.henglin31.android_wind_direction_meter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.henglin31.android_wind_direction_meter.api.GetWindDirectionMeterData;
import com.henglin31.android_wind_direction_meter.api.ThreadCallback;
import com.henglin31.android_wind_direction_meter.bean.LocationData;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RequestJobService extends JobService {
    private final static String TAG = "RequestJobService";
    private final static String WIND_NAME_KEY = "WIND_NAME";
    private boolean jobCancelled = false;

    private static final CopyOnWriteArraySet<String> CURRENT_SELECT_WIND_NAME_SET = new CopyOnWriteArraySet<>();

//    public static void updateWindNames(List<String> windNames){
//        CURRENT_SELECT_WIND_NAME_SET.clear();
//        CURRENT_SELECT_WIND_NAME_SET.addAll(windNames);
//    }

    public static void addWindName(String windName){
        CURRENT_SELECT_WIND_NAME_SET.add(windName);
    }

    public static void removeWindName(String windName){
        if(CURRENT_SELECT_WIND_NAME_SET.contains(windName)){
            CURRENT_SELECT_WIND_NAME_SET.remove(windName);
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "job started");
        doBackgroundWork(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "job cancelled before complete");
        jobCancelled = true;
        return true;
    }

    private void doBackgroundWork(final JobParameters params){
        if(jobCancelled){
            return;
        }
        new Thread(new GetWindDirectionMeterData(new ThreadCallback<LocationData>() {
            @Override
            public void callback(LocationData locationData) {
                Map<String, String> weatherMap = locationData.getWeatherElement();
                Log.d(TAG, new Gson().toJson(locationData.getWeatherElement()));
                if(weatherMap.containsKey(WIND_NAME_KEY) &&
                        CURRENT_SELECT_WIND_NAME_SET.contains(weatherMap.get(WIND_NAME_KEY))){
                    notification(weatherMap.get(WIND_NAME_KEY));
                }
                Log.d(TAG, "job finished");
                jobFinished(params, false);
            }
        })).start();
    }

    private void notification(String data){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("目前風向: " + data)
                .setContentText(data)
                .setTicker(data)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(data.hashCode(), notification);
        Log.d(TAG, "notification finish.");
    }
}
