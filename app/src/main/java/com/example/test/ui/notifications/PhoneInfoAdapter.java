package com.example.test.ui.notifications;

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

public class PhoneInfoAdapter extends ArrayAdapter {
    private final int resourceId;
    public PhoneInfoAdapter(@NonNull Context context, int resource, List<PhoneInfoItem> phoneInfolist) {
        super(context, resource,phoneInfolist);
        resourceId = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PhoneInfoItem phoneInfoItem = (PhoneInfoItem) getItem(position);
        View view;//实例化一个对象
        view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView phoneinfoname = view.findViewById(R.id.infoname);
        TextView phoneinfocontext = view.findViewById(R.id.infocontext);
        if (phoneinfoname != null&&phoneinfocontext!=null) {
            phoneinfoname.setText(phoneInfoItem.getPhoneinfoname());
            phoneinfocontext.setText(phoneInfoItem.getPhoneinfocontext());
        }
        return view;
    }
}
