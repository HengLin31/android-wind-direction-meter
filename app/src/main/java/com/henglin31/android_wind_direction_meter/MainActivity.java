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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.henglin31.android_wind_direction_meter.fragment.DashboardFragment;
import com.henglin31.android_wind_direction_meter.fragment.HomeFragment;
import com.henglin31.android_wind_direction_meter.fragment.NotificationFragment;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";
    private final int JOB_ID = 123456789;

    private BottomNavigationView navigation;
    private ViewPager viewPager;

    private HomeFragment homeFragment = new HomeFragment();
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private NotificationFragment notificationFragment = new NotificationFragment();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return homeFragment;
                    case 1:
                        return dashboardFragment;
                    case 2:
                        return notificationFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        scheduleJob();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob(){
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
    public void cancelJob(){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(JOB_ID);
        Log.d(TAG, "job cancel");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            viewPager.setCurrentItem(item.getOrder());
            return true;
        }

    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        navigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
