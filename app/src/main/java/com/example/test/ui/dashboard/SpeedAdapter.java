package com.example.test.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test.AppItem;
import com.example.test.R;
import com.example.test.SpeedItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class SpeedAdapter extends ArrayAdapter {
    private final int resourceId;
    public SpeedAdapter(@NonNull Context context, int resource, @NonNull List<AppItem> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        AppItem speedItem= (AppItem) getItem(position);
        View view;//实例化一个对象
        view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        RoundedImageView appImage=view.findViewById(R.id.app_speed_head);
        TextView appname=view.findViewById(R.id.app_speed_name);
        TextView appspeed=view.findViewById(R.id.app_speed);
        TextView speedunit=view.findViewById(R.id.speed_unit);

        appImage.setImageDrawable(speedItem.getAppIcon());//为图片视图设置图片资源
        appname.setText(speedItem.getAppName());
        appspeed.setText(Double.toString(speedItem.getAppSpeed()));
        speedunit.setText(speedItem.getSpeedUnit());
        return view;
    }
}
