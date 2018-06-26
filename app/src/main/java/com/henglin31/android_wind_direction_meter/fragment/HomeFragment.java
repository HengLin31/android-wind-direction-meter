package com.henglin31.android_wind_direction_meter.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.henglin31.android_wind_direction_meter.R;
import com.henglin31.android_wind_direction_meter.api.GetWindDirectionMeterData;
import com.henglin31.android_wind_direction_meter.api.ThreadCallback;
import com.henglin31.android_wind_direction_meter.bean.LocationData;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private final ExecutorService service = Executors.newSingleThreadExecutor();

    private TextView textMessage;
    private Button sendBtn;
    private Button updateImgBtn;
    private ImageView windDirectionImg;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(null == view){
            return;
        }
        textMessage = view.findViewById(R.id.message);
        windDirectionImg = view.findViewById(R.id.wind_direction_img);
        sendBtn = view.findViewById(R.id.test_api);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.submit(new GetWindDirectionMeterData(new ThreadCallback<LocationData>() {
                    @Override
                    public void callback(final LocationData locationData) {
                        if(null != getActivity()){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textMessage.setText(new Gson().toJson(locationData.getWeatherElement()));
                                }
                            });
                        }else{
                            Log.w(TAG, "no find getActivity()");
                        }
                    }
                }));
            }
        });
        updateImgBtn = view.findViewById(R.id.update_img);
        updateImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });
    }

    public void updateImage(){
        windDirectionImg.setImageBitmap(rotation(new Random().nextInt(359)));
    }

    private Bitmap rotation(int degree){
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture,picture.getWidth(),picture.getHeight(),true);
        return Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
    }
}
