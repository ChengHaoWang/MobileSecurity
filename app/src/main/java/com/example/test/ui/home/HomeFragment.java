package com.example.test.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.test.ApkTool;
import com.example.test.AppItem;
import com.example.test.BottomNavigation;
import com.example.test.PhoneFlowItem;
import com.example.test.PhoneSpeedItem;
import com.example.test.R;
import com.example.test.ui.dashboard.DashboardFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

import static android.content.Context.NETWORK_STATS_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static com.example.test.ApkTool.scanLocalInstallAppInfoList;
import static com.example.test.BottomNavigation.homePopWindow;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<AppItem> appList=new ArrayList<AppItem>();
    private AppAdapter appAdapter;
    private long startTime;
    private List<AppItem> dayAppList=new ArrayList<AppItem>();
    private List<AppItem> weekAppList=new ArrayList<AppItem>();
    private List<AppItem> monthAppList=new ArrayList<AppItem>();
    private int timeFlag=0;//0=month;1=week;2=day
    final long firstdayOfmonth =getTimesMonthMorning();
    final long firstdayOfweek=getTimesWeekmorning();
    final long firsttimeOfday=getTimesmorning();
    private ImageView menu;
    private WaveSwipeRefreshLayout refreshLayout;
    private ListView applistview;
    private ProgressDialog progressDialog;
    private Handler mHandler = new Handler();//初始化很重要哦
    private final int flowcount = 60;
    private final int speedcount = 5;
    private PhoneFlowItem phoneFlowItem=new PhoneFlowItem();
    private PhoneSpeedItem phoneSpeedItem=new PhoneSpeedItem();
    private TextView phoneSpeed;
    private TextView monthFlow;

    private Runnable flowrunnable=new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {
            long phoneDayTotalFlowLong=getPhoneAllBytes(firsttimeOfday);//每日
            long phoneWeekTotalFlowLong=getPhoneAllBytes(firstdayOfweek);//每周
            long phoneMonthTotalFlowLong=getPhoneAllBytes(firstdayOfmonth);//每月
            phoneFlowItem.setPhoneDayTotalFlowLong(phoneDayTotalFlowLong);
            phoneFlowItem.setPhoneWeekTotalFlowLong(phoneWeekTotalFlowLong);
            phoneFlowItem.setPhoneMonthTotalFlowLong(phoneMonthTotalFlowLong);
            if (phoneDayTotalFlowLong>0){
                phoneFlowItem.setPhoneDayTotalFlowString(ApkTool.getFlowFromByte(phoneDayTotalFlowLong));
            }
            if (phoneWeekTotalFlowLong>0){
                phoneFlowItem.setPhoneWeekTotalFlowString(ApkTool.getFlowFromByte(phoneWeekTotalFlowLong));
            }
            if (phoneMonthTotalFlowLong>0){
                phoneFlowItem.setPhoneMonthTotalFlowString(ApkTool.getFlowFromByte(phoneMonthTotalFlowLong));
            }
            //更新月流量
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    monthFlow.setText(phoneFlowItem.getPhoneMonthTotalFlowString());
                }
            });
            mHandler.postDelayed(flowrunnable, flowcount * 1000);
        }
    };
    private Runnable speedrunnable=new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {
            long phoneMonthTotalFlowLong=getPhoneAllBytes(firsttimeOfday);//每日
            if (phoneSpeedItem.getEndTime()==0||phoneSpeedItem.getPhoneMonthTotalFlowLong()==0){
                phoneSpeedItem.setEndTime(System.currentTimeMillis());
                phoneSpeedItem.setPhoneMonthTotalFlowLong(phoneMonthTotalFlowLong);
            }else {
                long intervalFlow=phoneMonthTotalFlowLong-phoneSpeedItem.getPhoneMonthTotalFlowLong();
                int timeDifference = new Long(System.currentTimeMillis() - phoneSpeedItem.getEndTime()).intValue() / 1000;
                Log.e("时间间隔"+String.valueOf(timeDifference),"流量差"+String.valueOf(intervalFlow)+"目前："+phoneMonthTotalFlowLong+"原始："+phoneSpeedItem.getPhoneMonthTotalFlowLong());
                //更新信息
                phoneSpeedItem.setEndTime(System.currentTimeMillis());
                phoneSpeedItem.setPhoneMonthTotalFlowLong(phoneMonthTotalFlowLong);
                if (intervalFlow > 0 && timeDifference > 0) {
                    if (intervalFlow > 0 && intervalFlow < (1024 * timeDifference)) {
                        double speed = intervalFlow / timeDifference;
                        phoneSpeedItem.setPhoneSpeed(speed);
                        phoneSpeedItem.setSpeedUnit("B/s");
                    } else if (intervalFlow >= (1024 * timeDifference) && intervalFlow < (1024 * 1024 * timeDifference)) {
                        double speed = intervalFlow / 1024 / timeDifference;
                        phoneSpeedItem.setPhoneSpeed(speed);
                        phoneSpeedItem.setSpeedUnit("K/s");
                    } else if (intervalFlow >= (1024 * 1024 * timeDifference) && intervalFlow < (1024 * 1024 * 1024 * timeDifference)) {
                        double speed = intervalFlow / 1024 / 1024 / timeDifference;
                        phoneSpeedItem.setPhoneSpeed(speed);
                        phoneSpeedItem.setSpeedUnit("M/s");
                    } else {
                        double speed = intervalFlow / 1024 / 1024 / 1024 / timeDifference;
                        phoneSpeedItem.setPhoneSpeed(speed);
                        phoneSpeedItem.setSpeedUnit("G/s");
                    }
                }
                if (intervalFlow < 0 || intervalFlow == 0) {
                    phoneSpeedItem.setPhoneSpeed(0.0);
                    phoneSpeedItem.setSpeedUnit("B/s");
                }
            }
            Log.e("计算后",Double.toString(phoneSpeedItem.getPhoneSpeed())+phoneSpeedItem.getSpeedUnit());
            //更新网速
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    phoneSpeed.setText("");
                    Log.e("网速：",Double.toString(phoneSpeedItem.getPhoneSpeed())+phoneSpeedItem.getSpeedUnit());
                    phoneSpeed.setText(Double.toString(phoneSpeedItem.getPhoneSpeed())+phoneSpeedItem.getSpeedUnit());
                }
            });
            mHandler.postDelayed(speedrunnable, speedcount * 1000);
        }
    };

    private void showDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("信息加载中，请等待...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //窗口不可点击
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    //获取本月第一天0点的时间
    public static long getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }
    //获得本周周日0点时间，周日是一周的开始
    @SuppressLint("WrongConstant")
    public static long getTimesWeekmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTimeInMillis();
    }
    //获得当天0点时间
    public static long getTimesmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public long getPhoneAllBytes(long startTime) {
        NetworkStats.Bucket wifibucket;
        NetworkStats.Bucket mobilebucket;
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
        try {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getActivity().getSystemService(NETWORK_STATS_SERVICE);
            wifibucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    startTime,
                    System.currentTimeMillis());

            if (getActivity().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            }
            mobilebucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    tm.getSubscriberId(),
                    startTime,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        long phoneTotalFlow=wifibucket.getRxBytes()+wifibucket.getTxBytes()+mobilebucket.getRxBytes()+mobilebucket.getTxBytes();
        //这里可以区分发送和接收
        return phoneTotalFlow;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        //启动计时器
        phoneSpeed=root.findViewById(R.id.phonespeed);
        monthFlow=root.findViewById(R.id.monthflow);
        mHandler.postDelayed(flowrunnable, 0);
        mHandler.postDelayed(speedrunnable, 0);
        //获取流量信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                appList.clear();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showDialog();
                    }
                });
                appList=scanLocalInstallAppInfoList(getContext().getPackageManager(),getContext(),startTime);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                    //更新UI，回归主线程
                    appAdapter=new AppAdapter(getContext(),R.layout.app_item,appList);
                    applistview=root.findViewById(R.id.applistview);
                    applistview.setAdapter(appAdapter);
                    //进度框消失
                    progressDialog.dismiss();
                    //窗口恢复点击
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    //ListView的Item点击事件
                        applistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                    }
                });
            }
        }).start();
        startTime=firstdayOfmonth;
        menu=getActivity().findViewById(R.id.menu);

        homePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDismiss() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                homePopWindow.backgroundAlpha(getActivity(), 1f);
                            }
                        });
                        final CheckBox positiveOrderItem = homePopWindow.view.findViewById(R.id.positiveOrder);
                        final CheckBox reverseOrderItem = homePopWindow.view.findViewById(R.id.reverseOrder);
                        final CheckBox dayOrderItem = homePopWindow.view.findViewById(R.id.dayOrder);
                        final CheckBox weekOrderItem = homePopWindow.view.findViewById(R.id.weekOrder);
                        final CheckBox monthOrderItem=homePopWindow.view.findViewById(R.id.monothOrder);
                        //按天升序、降序排序
                        List<AppItem> tempAppList=new ArrayList<AppItem>();
                        if (positiveOrderItem.isChecked()&&dayOrderItem.isChecked()){
                            if (dayAppList.size()==0){
                                if (timeFlag==2&&appList.size()!=0){
                                    tempAppList.addAll(appList);
                                }
                                else {
                                    startTime=firsttimeOfday;
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDialog();
                                        }
                                    });
                                    tempAppList=scanLocalInstallAppInfoList(getContext().getPackageManager(),getContext(),startTime);
                                    //进度框消失
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            //窗口恢复点击
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    });
                                    timeFlag=2;
                                }
                                Collections.sort(tempAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o1.getTotalFlowLong()).compareTo(Long.valueOf(o2.getTotalFlowLong()));
                                    }
                                });

                                dayAppList.addAll(tempAppList);
                            }
                            else {
                                Collections.sort(dayAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o1.getTotalFlowLong()).compareTo(Long.valueOf(o2.getTotalFlowLong()));
                                    }
                                });

                                dayAppList.addAll(tempAppList);
                            }
                            appList.clear();
                            appList.addAll(dayAppList);
                            timeFlag=2;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    appAdapter.notifyDataSetChanged();
                                }
                            });

                        }
                        else if(reverseOrderItem.isChecked()&&dayOrderItem.isChecked()){
                            if (dayAppList.size()==0){
                                if (timeFlag==2&&appList.size()!=0){
                                    tempAppList.addAll(appList);
                                }
                                else {
                                    startTime=firsttimeOfday;
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDialog();
                                        }
                                    });
                                    tempAppList=scanLocalInstallAppInfoList(getContext().getPackageManager(),getContext(),startTime);
                                    //进度框消失
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            //窗口恢复点击
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    });
                                    timeFlag=2;
                                }
                                Collections.sort(tempAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o2.getTotalFlowLong()).compareTo(Long.valueOf(o1.getTotalFlowLong()));
                                    }
                                });
                                dayAppList.addAll(tempAppList);
                            }
                            else {
                                Collections.sort(dayAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o2.getTotalFlowLong()).compareTo(Long.valueOf(o1.getTotalFlowLong()));
                                    }
                                });
                            }
                            appList.clear();
                            appList.addAll(dayAppList);
                            timeFlag=2;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    appAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        //按周升序、降序排序
                        else if (positiveOrderItem.isChecked()&&weekOrderItem.isChecked()){
                            if (weekAppList.size()==0){
                                if (timeFlag==1&&appList.size()!=0){
                                    tempAppList.addAll(appList);
                                }
                                else {
                                    startTime=firstdayOfweek;
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDialog();
                                        }
                                    });
                                    tempAppList=scanLocalInstallAppInfoList(getContext().getPackageManager(),getContext(),startTime);
                                    //进度框消失
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            //窗口恢复点击
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    });
                                    timeFlag=1;
                                }
                                Collections.sort(tempAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o1.getTotalFlowLong()).compareTo(Long.valueOf(o2.getTotalFlowLong()));
                                    }
                                });
                                weekAppList.addAll(tempAppList);
                            }
                            else {
                                Collections.sort(weekAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o1.getTotalFlowLong()).compareTo(Long.valueOf(o2.getTotalFlowLong()));
                                    }
                                });
                            }
                            appList.clear();
                            appList.addAll(weekAppList);
                            timeFlag=1;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    appAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        else if (reverseOrderItem.isChecked()&&weekOrderItem.isChecked()){
                            if (weekAppList.size()==0){
                                if (timeFlag==1&&appList.size()!=0){
                                    tempAppList.addAll(appList);
                                }
                                else {
                                    startTime=firstdayOfweek;
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDialog();
                                        }
                                    });
                                    tempAppList=scanLocalInstallAppInfoList(getContext().getPackageManager(),getContext(),startTime);
                                    //进度框消失
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            //窗口恢复点击
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    });
                                    timeFlag=1;
                                }
                                Collections.sort(tempAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o2.getTotalFlowLong()).compareTo(Long.valueOf(o1.getTotalFlowLong()));
                                    }
                                });
                                //List<AppItem> tempAppList=new ArrayList<AppItem>();
                                weekAppList.addAll(tempAppList);
                            }
                            else {
                                Collections.sort(weekAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o2.getTotalFlowLong()).compareTo(Long.valueOf(o1.getTotalFlowLong()));
                                    }
                                });
                            }
                            appList.clear();
                            appList.addAll(weekAppList);
                            timeFlag=1;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    appAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        //按月升序、降序排序
                        else if(positiveOrderItem.isChecked()&&monthOrderItem.isChecked()){
                            if (monthAppList.size()==0){
                                if (timeFlag==0&&appList.size()!=0){
                                    tempAppList.addAll(appList);
                                }
                                else {
                                    startTime=firstdayOfmonth;
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDialog();
                                        }
                                    });
                                    tempAppList=scanLocalInstallAppInfoList(getContext().getPackageManager(),getContext(),startTime);
                                    //进度框消失
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            //窗口恢复点击
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    });
                                    timeFlag=0;
                                }
                                Collections.sort(tempAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o1.getTotalFlowLong()).compareTo(Long.valueOf(o2.getTotalFlowLong()));
                                    }
                                });
                                monthAppList.addAll(tempAppList);
                            }
                            else {
                                Collections.sort(monthAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o1.getTotalFlowLong()).compareTo(Long.valueOf(o2.getTotalFlowLong()));
                                    }
                                });
                            }
                            appList.clear();
                            appList.addAll(monthAppList);
                            timeFlag=0;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    appAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        else if(reverseOrderItem.isChecked()&&monthOrderItem.isChecked()){
                            if (monthAppList.size()==0){
                                if (timeFlag==0&&appList.size()!=0){
                                    tempAppList.addAll(appList);
                                }
                                else {
                                    startTime=firstdayOfmonth;
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDialog();
                                        }
                                    });
                                    tempAppList=scanLocalInstallAppInfoList(getContext().getPackageManager(),getContext(),startTime);
                                    //进度框消失
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            //窗口恢复点击
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    });
                                    timeFlag=0;
                                }
                                Collections.sort(tempAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o2.getTotalFlowLong()).compareTo(Long.valueOf(o1.getTotalFlowLong()));
                                    }
                                });
                                //List<AppItem> tempAppList=new ArrayList<AppItem>();
                                monthAppList.addAll(tempAppList);
                            }
                            else {
                                Collections.sort(monthAppList, new Comparator<AppItem>() {
                                    @Override
                                    public int compare(AppItem o1, AppItem o2) {
                                        return Long.valueOf(o2.getTotalFlowLong()).compareTo(Long.valueOf(o1.getTotalFlowLong()));
                                    }
                                });
                            }
                            appList.clear();
                            appList.addAll(monthAppList);
                            timeFlag=0;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    appAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }).start();


            }
        });
        refreshLayout = (WaveSwipeRefreshLayout)root.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.white));
        refreshLayout.setWaveColor(getResources().getColor(R.color.colorAccent));

        refreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {

                new Task().execute();
            }
        });
        ScrollView scrollView=root.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (refreshLayout!=null){
                    refreshLayout.setEnabled(i1==0);
                }
            }
        });

        return root;
    }

    private class Task extends AsyncTask<Void, Void, String[]> {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String[] doInBackground(Void... voids) {
            String[] test=new String[1];
            try {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //窗口不可点击
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });

                timeFlag=0;
                appList.clear();
                dayAppList.clear();
                weekAppList.clear();
                monthAppList.clear();
                appList=scanLocalInstallAppInfoList(getContext().getPackageManager(),getContext(),startTime);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        appAdapter.notifyDataSetChanged();
                    }
                });
                test[0]="true";

            }catch (Exception e){

            }
            return test;
        }
        @Override
        protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            Log.e("刷新","刷新已完成");
            refreshLayout.setRefreshing(false);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //窗口恢复点击
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
            super.onPostExecute(result);
        }
    }
}

