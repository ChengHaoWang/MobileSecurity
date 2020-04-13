package com.example.test.ui.home;

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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class AppAdapter extends ArrayAdapter {
    private final int resourceId;
    public AppAdapter(@NonNull Context context, int resource, @NonNull List<AppItem> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AppItem appItem= (AppItem) getItem(position);
        View view;//实例化一个对象
        view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        RoundedImageView appImage=view.findViewById(R.id.app_head);
        TextView appname=view.findViewById(R.id.app_name);
        TextView appflow=view.findViewById(R.id.app_flow);
        TextView apptime=view.findViewById(R.id.app_time);
        if (appItem!=null){
            if (appItem.getAppIcon()!=null){
                appImage.setImageDrawable(appItem.getAppIcon());//为图片视图设置图片资源
            }
            if (appItem.getAppName()!=null){
                appname.setText(appItem.getAppName());
            }
            appflow.setText(appItem.getTotalFlowString());
            apptime.setText(appItem.getTotalTime());
        }


        return view;
    }
}
