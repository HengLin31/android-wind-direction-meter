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

import com.henglin31.android_wind_direction_meter.api.GetWindDirectionMeterData;
import com.henglin31.android_wind_direction_meter.api.ThreadCallback;
import com.henglin31.android_wind_direction_meter.bean.LocationData;

import java.util.Random;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RequestJobService extends JobService {
    private static final String TAG = "RequestJobService";
    private boolean jobCancelled = false;
    //private static final long[] VIBRATE_PATTERN = {0, 300, 200, 300, 200, 300};

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
                locationData.getWeatherElement();
                notification(String.valueOf(new Random().nextInt(100)));
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
