package com.example.test.ui.call_log;

import android.content.Context;
import android.graphics.Color;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.provider.CallLog.Calls;
import com.example.test.CallLogInfo;
import com.example.test.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class CallLogAdapter extends ArrayAdapter {
    private final int resourceId;
    public CallLogAdapter(@NonNull Context context, int resource, @NonNull List<CallLogInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CallLogInfo call_log_item=(CallLogInfo) getItem(position);
        View view;//实例化一个对象
        view= LayoutInflater.from(getContext()).inflate(resourceId,null);

        TextView call_log_number=view.findViewById(R.id.call_log_number);
        TextView call_log_time=view.findViewById(R.id.call_log_time);
        TextView call_log_type=view.findViewById(R.id.cal_log_type);

        if (call_log_item!=null){
//            号码
            call_log_number.setText(call_log_item.getNumber());
//            日期
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            String dateStr = format.format(call_log_item.getDate());
            call_log_time.setText(dateStr);
//            来电类型
            String typeStr = null;
            int color = 0;
            switch (call_log_item.getType()) {
                case Calls.INCOMING_TYPE:
                    typeStr = "来电";
                    color = Color.BLUE;

                    break;
                case Calls.OUTGOING_TYPE:
                    typeStr = "去电";
                    color = Color.GREEN;

                    break;
                case Calls.MISSED_TYPE:
                    typeStr = "未接";
                    color = Color.RED;

                    break;

                default:
                    break;
            }//switch
            call_log_type.setText(typeStr);
            call_log_type.setTextColor(color);

        }
        return view;
    }
}
