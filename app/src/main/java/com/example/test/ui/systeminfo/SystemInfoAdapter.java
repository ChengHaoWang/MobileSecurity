package com.example.test.ui.systeminfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test.AppItem;
import com.example.test.PhoneInfoItem;
import com.example.test.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SystemInfoAdapter extends ArrayAdapter {
    private final int resourceId;
    public SystemInfoAdapter(@NonNull Context context, int resource, List<PhoneInfoItem> systemInfolist) {
        super(context, resource,systemInfolist);
        resourceId = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PhoneInfoItem systemInfoItem = (PhoneInfoItem) getItem(position);
        View view;//实例化一个对象
        view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView systeminfoname = view.findViewById(R.id.infoname);
        TextView systeminfocontext = view.findViewById(R.id.infocontext);
        if (systeminfoname != null&&systeminfocontext!=null) {
            systeminfoname.setText(systemInfoItem.getPhoneinfoname());
            systeminfocontext.setText(systemInfoItem.getPhoneinfocontext());
        }
        return view;
    }
}
