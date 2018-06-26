package com.henglin31.android_wind_direction_meter;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.henglin31.android_wind_direction_meter.api.GetWindDirectionMeterData;
import com.henglin31.android_wind_direction_meter.api.ThreadCallback;
import com.henglin31.android_wind_direction_meter.bean.LocationData;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int JOB_ID = 123;

    private final ExecutorService service = Executors.newSingleThreadExecutor();

    private TextView mTextMessage;
    private Button sendBtn;
    private ImageView windDirectionImg;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //windDirectionImg = findViewById(R.id.wind_direction_img);
        //windDirectionImg.setImageBitmap(rotation(100));

        sendBtn = findViewById(R.id.test_api);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.submit(new GetWindDirectionMeterData(new ThreadCallback<LocationData>() {
                    @Override
                    public void callback(final LocationData locationData) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextMessage.setText(new Gson().toJson(locationData.getWeatherElement()));
                            }
                        });
                    }
                }));
            }
        });


    }

    public void updateImage(View v){
        windDirectionImg = findViewById(R.id.wind_direction_img);
        windDirectionImg.setImageBitmap(rotation(new Random().nextInt(359)));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob(View v){
        ComponentName componentName = new ComponentName(this, RequestJobService.class);
        JobInfo info = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(30 * 1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "job scheduled");
        }else{
            Log.d(TAG, "JOB scheduling failed");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(JOB_ID);
        Log.d(TAG, "job cancel");
    }

    private Bitmap rotation(int degree){
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture,picture.getWidth(),picture.getHeight(),true);
        return Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
    }
}
