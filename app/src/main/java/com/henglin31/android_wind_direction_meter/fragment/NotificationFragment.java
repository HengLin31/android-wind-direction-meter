package com.henglin31.android_wind_direction_meter.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.henglin31.android_wind_direction_meter.MainActivity;
import com.henglin31.android_wind_direction_meter.R;
import com.henglin31.android_wind_direction_meter.RequestJobService;
import com.henglin31.android_wind_direction_meter.bean.CheckboxListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private static final String TAG = "NotificationFragment";
    private static final String SWITCH_ON_TEXT = "推播已開啟";
    private static final String SWITCH_OFF_TEXT = "推播已關閉";

    private Switch notificationSwitch;

    private List<String> list;
    private ListView listView;
    private List<Boolean> listShow;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(null == view){
            return;
        }
        notificationSwitch = view.findViewById(R.id.notification_switch);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if(null != mainActivity){
                    if(isChecked){
                        Log.d(TAG, SWITCH_ON_TEXT);
                        notificationSwitch.setText(SWITCH_ON_TEXT);
                        mainActivity.startScheduleJob();
                    }else{
                        Log.d(TAG, SWITCH_OFF_TEXT);
                        notificationSwitch.setText(SWITCH_OFF_TEXT);
                        mainActivity.cancelScheduleJob();
                    }
                }else{
                    Log.w(TAG, "mainActivity is null, can't start schedule...");
                }

            }
        });

        if(null != getActivity()){
            listView = getActivity().findViewById(R.id.wind_checkbox_list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CheckedTextView chkItem = view.findViewById(R.id.wind_checked_text_view);
                    chkItem.setChecked(!chkItem.isChecked());
                    //String msg;
                    if(chkItem.isChecked()){
                        //msg = "勾選了 " + list.get(position);
                        RequestJobService.addWindName(list.get(position));
                    }else{
                        //msg = "取消勾選 " + list.get(position);
                        RequestJobService.removeWindName(list.get(position));
                    }
                    //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    listShow.set(position, chkItem.isChecked());
                }
            });

            initWindCheckboxNames();
            CheckboxListAdapter adapter = new CheckboxListAdapter(getActivity(), list);
            listView.setAdapter(adapter);
        }
    }

    private void initWindCheckboxNames(){
        this.list = new ArrayList<>();
        this.list.add("靜風");
        this.list.add("北北東");
        this.list.add("東北");
        this.list.add("東北東");
        this.list.add("東");
        this.list.add("東南東");
        this.list.add("東南");
        this.list.add("南南東");
        this.list.add("南");
        this.list.add("南南西");
        this.list.add("西南");
        this.list.add("西南西");
        this.list.add("西");
        this.list.add("西北西");
        this.list.add("西北");
        this.list.add("北北西");
        this.list.add("北");

        this.listShow = new ArrayList<>();
        for(int size=1; size<=this.list.size(); size++){
            this.listShow.add(false);
        }
    }
}
