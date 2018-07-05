package com.henglin31.android_wind_direction_meter.bean;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.henglin31.android_wind_direction_meter.R;

import java.util.List;

public class CheckboxListAdapter extends BaseAdapter {
    //private Activity activity;
    private List<String> checkboxs;

    private static LayoutInflater inflater = null;

    public CheckboxListAdapter(Activity activity, List<String> checkboxs){
        //this.activity = activity;
        this.checkboxs = checkboxs;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.checkboxs.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(null == convertView){
            view = inflater.inflate(R.layout.list_item, null);
        }
        CheckedTextView checkedTextView = view.findViewById(R.id.wind_checked_text_view);
        checkedTextView.setText(this.checkboxs.get(position));
        return view;
    }
}
