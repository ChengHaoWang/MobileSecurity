package com.example.test.ui.systeminfo;

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

import com.example.test.GetSystemInfo;
import com.example.test.PhoneInfoItem;
import com.example.test.R;

import java.util.ArrayList;
import java.util.List;

public class SystemInfoFragment extends Fragment {

    private SystemInfoViewModel systeminfoViewModel;
    private SystemInfoAdapter systemInfoAdapter;
    private List<PhoneInfoItem> systemInfoItems=new ArrayList<PhoneInfoItem>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        systeminfoViewModel =
                ViewModelProviders.of(this).get(SystemInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);

        systeminfoViewModel.getText().observe(this, new Observer<String>() {
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

        //获取系统信息
        GetSystemInfo getSystemInfo=new GetSystemInfo();


        //SDK version
        String phoneSDK=getSystemInfo.getPhoneSDK()+ "";
        if (phoneSDK!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("Android SDK 版本号");
            systemInfoItem.setPhoneinfocontext(phoneSDK);
            systemInfoItems.add(systemInfoItem);
        }

        //bootloader version
        String bootLoader=getSystemInfo.getBootLoader();
        if (bootLoader!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("Bootloader 版本号");
            systemInfoItem.setPhoneinfocontext(bootLoader);
            systemInfoItems.add(systemInfoItem);
        }

        //CPU_ABI
        String CPU_ABI=getSystemInfo.getCPU_ABI();
        if (CPU_ABI!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("CPU 指令集");
            systemInfoItem.setPhoneinfocontext(CPU_ABI);
            systemInfoItems.add(systemInfoItem);
        }

        //产品
        String product=getSystemInfo.getPRODUCT();
        if (product!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("产品");
            systemInfoItem.setPhoneinfocontext(product);
            systemInfoItems.add(systemInfoItem);
        }

        //标签
        String tag=getSystemInfo.getTags();
        if (tag!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("标签");
            systemInfoItem.setPhoneinfocontext(tag);
            systemInfoItems.add(systemInfoItem);
        }

        //驱动
        String device=getSystemInfo.getDEVICE();
        if (device!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("驱动");
            systemInfoItem.setPhoneinfocontext(device);
            systemInfoItems.add(systemInfoItem);
        }

        //底层板名称
        String board=getSystemInfo.getBOARD();
        if (board!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("基板");
            systemInfoItem.setPhoneinfocontext(board);
            systemInfoItems.add(systemInfoItem);
        }

        //设备标识
        String fingerprint=getSystemInfo.getFINGERPRINT();
        if (fingerprint!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("设备标识");
            systemInfoItem.setPhoneinfocontext(fingerprint);
            systemInfoItems.add(systemInfoItem);
        }

        //制造商
        String manu=getSystemInfo.getMANUFACTURER();
        if (manu!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("制造商");
            systemInfoItem.setPhoneinfocontext(manu);
            systemInfoItems.add(systemInfoItem);
        }

        //用户
        String user=getSystemInfo.getUSER();
        if (user!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("用户");
            systemInfoItem.setPhoneinfocontext(user);
            systemInfoItems.add(systemInfoItem);
        }

        //硬件名称
        String hardware=getSystemInfo.getHARDWARE();
        if (hardware!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("硬件名称");
            systemInfoItem.setPhoneinfocontext(hardware);
            systemInfoItems.add(systemInfoItem);
        }

        //主机地址
        String host=getSystemInfo.getHOST();
        if (host!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("主机地址");
            systemInfoItem.setPhoneinfocontext(host);
            systemInfoItems.add(systemInfoItem);
        }

        //出厂时间
        String time=getSystemInfo.getTIME();
        if (time!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("出厂时间");
            systemInfoItem.setPhoneinfocontext(time);
            systemInfoItems.add(systemInfoItem);
        }

        //硬件序列号
        String serial=getSystemInfo.getSERIAL();
        if (serial!=null){
            PhoneInfoItem systemInfoItem=new PhoneInfoItem();
            systemInfoItem.setPhoneinfoname("硬件序列号");
            systemInfoItem.setPhoneinfocontext(serial);
            systemInfoItems.add(systemInfoItem);
        }




        //更新list
        systemInfoAdapter=new SystemInfoAdapter(getContext(),R.layout.phoneinfo_item,systemInfoItems);
        ListView systeminfolistview=root.findViewById(R.id.phoneinfolistview);
        systeminfolistview.setAdapter(systemInfoAdapter);

        return root;
    }
}