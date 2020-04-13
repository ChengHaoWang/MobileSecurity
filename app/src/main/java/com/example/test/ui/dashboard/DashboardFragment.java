package com.example.test.ui.dashboard;

import android.Manifest;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.test.AppItem;
import com.example.test.R;
import com.example.test.TimeTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NETWORK_STATS_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static com.example.test.ApkTool.scanLocalInstallAppInfoList;
import static com.example.test.BottomNavigation.homePopWindow;
import static com.example.test.BottomNavigation.speedPopWindow;

public class DashboardFragment extends Fragment {

    public static Intent speedServiceIntent;
    private DashboardViewModel dashboardViewModel;
    private List<AppItem> appList=new ArrayList<AppItem>();
    private SpeedAdapter speedAdapter;
    private List<AppItem> sortAppList=new ArrayList<AppItem>();
    //public static int serviceStatus=1;
    private long startTime;
    private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(30, Integer.MAX_VALUE, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    private static ThreadLocal<AppItem> threadLocalst = new ThreadLocal<AppItem>();
    //定时器

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){//处理消息
            switch (msg.what){
                case 0:
                    Log.e("开始循环","sada");
                    for (int i = 0; i < appList.size(); i++) {
                        final int index = i;

                        Runnable threadsRunnable = new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                //数据隔离
                                AppItem tappItem = appList.get(index);
                                threadLocalst.set(tappItem);
                                AppItem appItem = threadLocalst.get();

                                int appId = appItem.getAppId();
                                //long startTime = appItem.getStartTime();

                                //计算现在的流量
                                TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
                                if (getActivity().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                    Log.e("Steven", "没有权限");
                                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                                }
                                String subId = tm.getSubscriberId();//网络接口ID
                                NetworkStatsManager networkStatsManager = (NetworkStatsManager) getActivity().getSystemService(NETWORK_STATS_SERVICE);
                                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateStr = dateformat.format(System.currentTimeMillis());
                                //获取方法二
                                NetworkStats mobileFlowSummary = null;
                                try {
                                    mobileFlowSummary = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, subId, startTime, System.currentTimeMillis());
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                                NetworkStats.Bucket mobileFlowBucket = new NetworkStats.Bucket();
                                long mobileTrafficeByte = 0;//发送字节总数
                                long mobileReceiveByte = 0;//接收字节总数
                                long mobileTotalByte = 0;//总字节数
                                if (mobileFlowSummary!=null){
                                    do {

                                        mobileFlowSummary.getNextBucket(mobileFlowBucket);
                                        if (appId == mobileFlowBucket.getUid()) {
                                            mobileTrafficeByte = mobileTrafficeByte + mobileFlowBucket.getTxBytes();//接收的字节数
                                            mobileReceiveByte = mobileReceiveByte + mobileFlowBucket.getRxBytes();//传输的字节数
                                            mobileTotalByte = mobileTotalByte + mobileTrafficeByte + mobileReceiveByte;
                                        }

                                    } while (mobileFlowSummary.hasNextBucket());
                                }

                                NetworkStats wifiFlowSummary = null;
                                try {
                                    wifiFlowSummary = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, subId, startTime, System.currentTimeMillis());
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                                NetworkStats.Bucket wifiFlowBucket = new NetworkStats.Bucket();
                                long wifiTrafficeByte = 0;//发送字节总数
                                long wifiReceiveByte = 0;//接收字节总数
                                long wifiTotalByte = 0;//总字节数
                                if (wifiFlowSummary!=null){
                                    do {

                                        wifiFlowSummary.getNextBucket(wifiFlowBucket);
                                        if (appId == wifiFlowBucket.getUid()) {
                                            wifiTrafficeByte = wifiTrafficeByte + wifiFlowBucket.getTxBytes();//接收的字节数
                                            wifiReceiveByte = wifiReceiveByte + wifiFlowBucket.getRxBytes();//传输的字节数
                                            wifiTotalByte = wifiTotalByte + wifiTrafficeByte + wifiReceiveByte;
                                        }

                                    } while (wifiFlowSummary.hasNextBucket());
                                }

                                long totalFlowNow = mobileTotalByte + wifiTotalByte;
                                //网速
                                //时间差转int
                                long endTime = appItem.getEndTime();
                                int timeDifference = new Long(System.currentTimeMillis() - endTime).intValue() / 1000;

                                long totalFlow = appItem.getTotalFlowLong();
                                long intervalFlow = totalFlowNow - totalFlow;

                                //Log.e("名称：", appItem.getAppName() + ",详情：" + String.valueOf(totalFlowNow) + "." + String.valueOf(totalFlow) + "差：" + String.valueOf(intervalFlow)+"时间差："+String.valueOf(timeDifference));
                                if (intervalFlow > 0 && timeDifference > 0) {
                                    int unconvertedSpeed = new Long(intervalFlow).intValue() / timeDifference;
                                    appItem.setUnconvertedSpeed(unconvertedSpeed);
                                    Log.e("名称：", appItem.getAppName() + ",详情：" + String.valueOf(totalFlowNow) + "." + String.valueOf(totalFlow) + "差：" + String.valueOf(intervalFlow)+"时间差："+String.valueOf(timeDifference));

                                    if (intervalFlow > 0 && intervalFlow < (1024 * timeDifference)) {
                                        double speed = intervalFlow / timeDifference;
                                        appItem.setAppSpeed(speed);
                                        appItem.setSpeedUnit("B/s");
                                        //Log.e("序号:", String.valueOf(index) + ",名称：" + speedItemArrayList.get(index).getAppName() + ",速度：" + String.valueOf(speed)+",时间差：" + String.valueOf(timeDifference));
                                    } else if (intervalFlow >= (1024 * timeDifference) && intervalFlow < (1024 * 1024 * timeDifference)) {
                                        double speed = intervalFlow / 1024 / timeDifference;
                                        appItem.setAppSpeed(speed);
                                        appItem.setSpeedUnit("K/s");
                                        //Log.e("序号:", String.valueOf(index) + ",名称：" + speedItemArrayList.get(index).getAppName() + ",速度：" + String.valueOf(speed)+",时间差：" + String.valueOf(timeDifference));
                                    } else if (intervalFlow >= (1024 * 1024 * timeDifference) && intervalFlow < (1024 * 1024 * 1024 * timeDifference)) {
                                        double speed = intervalFlow / 1024 / 1024 / timeDifference;
                                        appItem.setAppSpeed(speed);
                                        appItem.setSpeedUnit("M/s");
                                    } else if (intervalFlow >= (1024 * 1024 * 1024 * timeDifference)){
                                        double speed = intervalFlow / 1024 / 1024 / 1024 / timeDifference;
                                        appItem.setAppSpeed(speed);
                                        appItem.setSpeedUnit("G/s");
                                    }
                                }

                                if (intervalFlow < 0 || intervalFlow == 0) {
                                    appItem.setUnconvertedSpeed(0);
                                    appItem.setAppSpeed(0.0);
                                    appItem.setSpeedUnit("B/s");
                                    //Log.e("序号:", String.valueOf(index) + ",名称：" + speedItemArrayList.get(index).getAppName() +",时间差：" + String.valueOf(timeDifference));
                                }
                                appItem.setTotalFlowLong(totalFlowNow);//更新流量
                                appItem.setEndTime(System.currentTimeMillis());
                                //更新UI
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        speedAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

                        };
                        poolExecutor.execute(threadsRunnable);
                    }
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //重新获取流量等信息
        startTime= TimeTool.getTimesmorning().getTime();
        appList=scanLocalInstallAppInfoList(getActivity().getPackageManager(),getContext(),startTime);

        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);

        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        if(savedInstanceState!= null){
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        if (appList.size()!=0){
            //更新界面
            speedAdapter=new SpeedAdapter(getContext(),R.layout.speed_item,appList);
            ListView speedlistview=root.findViewById(R.id.speedlistview);
            speedlistview.setAdapter(speedAdapter);
            //跳转到详情
            speedlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent =  new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", appList.get(i).getAppPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        intent.putExtra("com.android.settings.ApplicationPkgName", appList.get(i).getAppPackageName());
                    }
                    startActivity(intent);
                }
            });
        }else {
            Toast.makeText(getActivity(),"请先加载APP流量列表",Toast.LENGTH_SHORT).show();
        }

        Timer timer = new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = 0;
                handler.sendMessage(msg);//定时器超时，发送消息
            }
        };
        timer.schedule(timerTask,0,10000);

        //右上角菜单
        speedPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        homePopWindow.backgroundAlpha(getActivity(), 1f);
                    }
                });

                final CheckBox positiveOrderItem = speedPopWindow.view.findViewById(R.id.positiveOrder);
                final CheckBox reverseOrderItem = speedPopWindow.view.findViewById(R.id.reverseOrder);
                final CheckBox nameOrderItem = speedPopWindow.view.findViewById(R.id.nameOrder);
                //判断是否存在排序需求
                //速度正序
                if (positiveOrderItem.isChecked()){
                    if (sortAppList.size()==0){
                        sortAppList.addAll(appList);
                    }
                    Collections.sort(sortAppList, new Comparator<AppItem>() {
                        @Override
                        public int compare(AppItem o1, AppItem o2) {
                            //int i = (int)(o1.getTotalFlowLong() - o2.getTotalFlowLong());
                            return Long.valueOf(o1.getUnconvertedSpeed()).compareTo(Long.valueOf(o2.getUnconvertedSpeed()));
                        }
                    });
                    List<AppItem> tempAppList=new ArrayList<AppItem>();
                    tempAppList.addAll(sortAppList);
                    appList.clear();
                    appList.addAll(tempAppList);

                    speedAdapter.notifyDataSetChanged();
                    //通知Service数组变更
                    final List<AppItem> tappList=new ArrayList<AppItem>();
                    tappList.removeAll(tappList);
                    tappList.addAll(appList);
                    EventBus.getDefault().postSticky(tappList);
                }
                //速度倒序
                if (reverseOrderItem.isChecked()){
                    if (sortAppList.size()==0){
                        sortAppList.addAll(appList);
                    }
                    Collections.sort(sortAppList, new Comparator<AppItem>() {
                        @Override
                        public int compare(AppItem o1, AppItem o2) {
                            //int i = (int)(o1.getTotalFlowLong() - o2.getTotalFlowLong());
                            return Long.valueOf(o2.getUnconvertedSpeed()).compareTo(Long.valueOf(o1.getUnconvertedSpeed()));
                        }
                    });
                    List<AppItem> tempAppList=new ArrayList<AppItem>();
                    tempAppList.addAll(sortAppList);
                    appList.clear();
                    appList.addAll(tempAppList);
                    for (int i=0;i<10;i++){
                        Log.e("排序：",appList.get(i).getAppName()+String.valueOf(appList.get(i).getUnconvertedSpeed()));
                    }
                    speedAdapter.notifyDataSetChanged();
                    //通知Service数组变更
                    final List<AppItem> tappList=new ArrayList<AppItem>();
                    tappList.removeAll(tappList);
                    tappList.addAll(appList);
                    EventBus.getDefault().postSticky(tappList);
                }
                //名称排序
                if (nameOrderItem.isChecked()){
                    if (sortAppList.size()==0){
                        sortAppList.addAll(appList);
                    }
                    Collections.sort(sortAppList, new Comparator<AppItem>() {
                        @Override
                        public int compare(AppItem o1, AppItem o2) {
                            Comparator<Object> compare = Collator.getInstance(java.util.Locale.CHINA);
                            return compare.compare(o1.getAppName(), o2.getAppName());
                        }
                    });
                    List<AppItem> tempAppList=new ArrayList<AppItem>();
                    tempAppList.addAll(sortAppList);
                    appList.clear();
                    appList.addAll(tempAppList);
                    speedAdapter.notifyDataSetChanged();
                }

            }
        });
        return root;
    }
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //解除注册
    @Override
    public void onPause() {
        super.onPause();
    }
}