package com.example.test.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.test.GetPhoneInfo;
import com.example.test.PhoneInfoItem;
import com.example.test.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private PhoneInfoAdapter phoneInfoAdapter;
    private List<PhoneInfoItem> phoneInfoItems=new ArrayList<PhoneInfoItem>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);

        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //获取toolbar高度
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        int toolbarheight=toolbar.getHeight();
        LinearLayout background=root.findViewById(R.id.notification_background);
        //background.set

        //获取本机信息
        GetPhoneInfo getPhoneInfo=new GetPhoneInfo();
        //当前网速

        //手机型号
        String phoneModel=getPhoneInfo.getPhoneModel();
        if (phoneModel!=null){
            PhoneInfoItem phoneInfoItem=new PhoneInfoItem();
            phoneInfoItem.setPhoneinfoname("手机型号");
            phoneInfoItem.setPhoneinfocontext(phoneModel);
            phoneInfoItems.add(phoneInfoItem);
        }
        //操作系统
        String phoneOS=getPhoneInfo.getOS();
        if (phoneOS!=null){
            PhoneInfoItem phoneInfoItem=new PhoneInfoItem();
            phoneInfoItem.setPhoneinfoname("操作系统");
            phoneInfoItem.setPhoneinfocontext(phoneOS);
            phoneInfoItems.add(phoneInfoItem);
        }
        //手机分辨率
        String resolution=getPhoneInfo.getResolution(getContext());
        if (resolution!=null){
            PhoneInfoItem phoneInfoItem=new PhoneInfoItem();
            phoneInfoItem.setPhoneinfoname("分辨率");
            phoneInfoItem.setPhoneinfocontext(resolution);
            phoneInfoItems.add(phoneInfoItem);
        }
        //唯一设备号
        String deviceNo=getPhoneInfo.getDeviceNo(getContext());
        if (deviceNo!=null){
            PhoneInfoItem phoneInfoItem=new PhoneInfoItem();
            phoneInfoItem.setPhoneinfoname("唯一设备号");
            phoneInfoItem.setPhoneinfocontext(deviceNo);
            phoneInfoItems.add(phoneInfoItem);
        }
        //运营商
        String netOperator=getPhoneInfo.getNetOperator(getContext());
        if (netOperator!=null){
            PhoneInfoItem phoneInfoItem=new PhoneInfoItem();
            phoneInfoItem.setPhoneinfoname("运营商");
            phoneInfoItem.setPhoneinfocontext(netOperator);
            phoneInfoItems.add(phoneInfoItem);
        }
        //联网方式
        String netMode=getPhoneInfo.getNetMode(getContext());
        if (netMode!=null){
            PhoneInfoItem phoneInfoItem=new PhoneInfoItem();
            phoneInfoItem.setPhoneinfoname("联网方式");
            phoneInfoItem.setPhoneinfocontext(netMode);
            phoneInfoItems.add(phoneInfoItem);
        }
        //mac地址
        String mac=getPhoneInfo.getNewMac();
        if (mac!=null){
            PhoneInfoItem phoneInfoItem=new PhoneInfoItem();
            phoneInfoItem.setPhoneinfoname("MAC");
            phoneInfoItem.setPhoneinfocontext(mac);
            phoneInfoItems.add(phoneInfoItem);
        }

        //更新list
        phoneInfoAdapter=new PhoneInfoAdapter(getContext(),R.layout.phoneinfo_item,phoneInfoItems);
        ListView phoneinfolistview=root.findViewById(R.id.phoneinfolistview);
        phoneinfolistview.setAdapter(phoneInfoAdapter);

        return root;
    }
}