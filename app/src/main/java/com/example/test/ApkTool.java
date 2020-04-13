package com.example.test;

import android.Manifest;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.test.ui.home.HomeFragment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NETWORK_STATS_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.Context.USAGE_STATS_SERVICE;
import static com.example.test.TimeTool.getTimesmorning;

public class ApkTool {
    public static List<AppItem> mLocalInstallApps = null;
    public final static int REQUEST_READ_PHONE_STATE = 1;
    public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
    public static List<AppItem> myAppInfos = new ArrayList<AppItem>();
    public static List<UsageStats> stats=null;
    public static PackageInfo packageInfo=null;
    public static ApplicationInfo applicationInfo=null;
    public static PackageManager packageManager;
    public static Context context;
    private static CountDownLatch cdl ;//数值是计数器初始值
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(30, Integer.MAX_VALUE,1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    private static ThreadLocal<AppItem> threadLocalst=new ThreadLocal<AppItem>();

    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<AppItem> scanLocalInstallAppInfoList(PackageManager packageManager1, Context context1, final long starttime) {

        packageManager=packageManager1;
        context=context1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a=timeStamp2Date(starttime,null);
        String b=timeStamp2Date(System.currentTimeMillis(),null);
        try {
            myAppInfos.clear();
            UsageStatsManager manager=(UsageStatsManager)context.getSystemService(USAGE_STATS_SERVICE);
            stats=manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, starttime,System.currentTimeMillis());
            StringBuilder sb=new StringBuilder();

            //获取应用相关信息
            final List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            cdl=new CountDownLatch(packageInfos.size());
            for (int i = 0; i < packageInfos.size(); i++) {
                final int index=i;
                packageInfo = packageInfos.get(index);
                applicationInfo = packageInfo.applicationInfo;
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    cdl.countDown();
                    continue;
                }

                Runnable threadsRunnable=new Runnable() {
                    @Override
                    public void run() {
                        //Log.e("序号", String.valueOf(index));
                        packageInfo = packageInfos.get(index);
                        applicationInfo = packageInfo.applicationInfo;
                        long firstInstallTime = packageInfo.firstInstallTime;// 应用第一次安装的时间
                        //int versionCode = packageInfo.versionCode;// 应用现在的版本号
                        //String versionName = packageInfo.versionName;// 应用现在的版本名称
                        //long lastUpdateTime = packageInfo.lastUpdateTime;// 最后一次更新时间

                        //String name = applicationInfo.name;// Application类名
                        String packageName = applicationInfo.packageName;// 包名
                        int uid = applicationInfo.uid;
                        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);//应用名称

                        //获取总流量
                        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            Log.e("Steven","没有权限");
                        }
                        String subId = tm.getSubscriberId();//网络接口ID
                        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NETWORK_STATS_SERVICE);

                        //获取方法二，md这个APP好麻烦
                        NetworkStats mobileFlowSummary= null;
                        try {
                            mobileFlowSummary = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, subId,starttime , System.currentTimeMillis());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        NetworkStats.Bucket mobileFlowBucket = new NetworkStats.Bucket();
                        long mobileTrafficeByte=0;//发送字节总数
                        long mobileReceiveByte=0;//接收字节总数
                        long mobileTotalByte=0;//总字节数
                        if (mobileFlowSummary!=null){
                            do{
                                mobileFlowSummary.getNextBucket(mobileFlowBucket);
                                if (uid==mobileFlowBucket.getUid()){
                                    mobileTrafficeByte= mobileTrafficeByte + mobileFlowBucket.getTxBytes();//接收的字节数
                                    mobileReceiveByte= mobileReceiveByte + mobileFlowBucket.getRxBytes();//传输的字节数
                                    mobileTotalByte = mobileTotalByte+mobileTrafficeByte+mobileReceiveByte;
                                }
                            }while (mobileFlowSummary.hasNextBucket());
                        }
                        //Log.e("应用名称", applicationName);
                        //Log.e("mobile流量", String.valueOf(mobileTotalByte));
                        NetworkStats wifiFlowSummary= null;
                        try {
                            wifiFlowSummary = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, subId,starttime , System.currentTimeMillis());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        NetworkStats.Bucket wifiFlowBucket = new NetworkStats.Bucket();
                        long wifiTrafficeByte=0;//发送字节总数
                        long wifiReceiveByte=0;//接收字节总数
                        long wifiTotalByte=0;//总字节数
                        if (wifiFlowSummary!=null){
                            do {
                                wifiFlowSummary.getNextBucket(wifiFlowBucket);
                                if (uid==wifiFlowBucket.getUid()){
                                    wifiTrafficeByte= wifiTrafficeByte + wifiFlowBucket.getTxBytes();//接收的字节数
                                    wifiReceiveByte= wifiReceiveByte + wifiFlowBucket.getRxBytes();//传输的字节数
                                    wifiTotalByte = wifiTotalByte+ wifiTrafficeByte+ wifiReceiveByte;
                                }
                            }while (wifiFlowSummary.hasNextBucket());
                        }
                        long totalByte=mobileTotalByte+wifiTotalByte;
                        Log.e("应用名称", applicationName);
                        //Log.e("wifi流量", String.valueOf(wifiTotalByte));
                        //获取运行时间
                        Long runLongTime= Long.valueOf(0);//应用的总运行时长
                        String runTime=null;
                        for(UsageStats us:stats){
                            if (us.getPackageName().equals(packageName)){
                                runLongTime+=us.getTotalTimeInForeground();
                            }
                        }
                        runTime=getTimeFromInt(Integer.parseInt(String.valueOf(runLongTime)));

                        //数据隔离
                        AppItem tappItem=new AppItem();
                        threadLocalst.set(tappItem);
                        AppItem mappItem=threadLocalst.get();
                        synchronized (this){
                            //为item赋值
                            mappItem.setAppName(applicationName);
                            mappItem.setAppPackageName(packageName);
                            mappItem.setAppId(uid);
                            mappItem.setTotalFlowString(getFlowFromByte(totalByte));
                            mappItem.setTotalTime(runTime);
                            packageInfo=packageInfos.get(index);
                            Drawable icon=packageInfo.applicationInfo.loadIcon(packageManager);
                            if (icon == null) {
                                mappItem.setAppIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
                            }
                            //Log.e("图片", String.valueOf(icon));
                            mappItem.setAppIcon(icon);
                            mappItem.setTotalFlowLong(totalByte);
                            mappItem.setAppSpeed(0.0);
                            mappItem.setSpeedUnit("B/s");
                            mappItem.setFirstInstallTime(firstInstallTime);
                            //mappItem.setTimeflag(timeflag);
                            mappItem.setStartTime(starttime);
                            long endtime=System.currentTimeMillis();
                            mappItem.setEndTime(endtime);
                            myAppInfos.add(mappItem);
                            cdl.countDown();
                        }
                        //SystemClock.sleep(1000);
                            }
                        //});

                 //   }
                };
                poolExecutor.execute(threadsRunnable);
            }
        } catch (Exception e) {
            Log.e("Steven", "===============获取应用包信息失败");
            Log.e("Steven", e.toString());
            e.printStackTrace();
        }
        try{
            cdl.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return myAppInfos;
    }

    //毫秒转换
    public static String getTimeFromInt(int time) {
        if (time <= 0) {
            return "0小时0分";
        }
        int totalseconds=time/1000;
        int totalhours=totalseconds/3600;
        int totalminutes=(totalseconds%3600)/60;
        return totalhours + "小时" + totalminutes+"分";
    }
    //字节转换
    public static String getFlowFromByte(long totalByte){

        BigDecimal totalB = new BigDecimal(Double.toString(totalByte));
        BigDecimal kb = new BigDecimal(Double.toString(1024));
        BigDecimal mb = new BigDecimal(Double.toString((1024*1024)));
        BigDecimal gb = new BigDecimal(Double.toString((1024*1024*1024)));

        double totalKb1 = totalB.divide(kb, 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        double totalMb1=totalB.divide(mb, 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        double totalGb2=totalB.divide(gb, 2, BigDecimal.ROUND_HALF_UP).doubleValue();

        if(totalByte<0){
            return "0B";
        }
        if(totalByte>0&&totalByte<1024){
            return totalByte+"B";
        }
        //if (totalTb<1){
            if(totalGb2<1){
                if (totalMb1<1){
                    return totalKb1+"KB";
                }
                else {
                    return totalMb1+"MB";
                }
            }
            else {
                return totalGb2+"GB";
            }
       // }else {
        //    return totalTb+"TB";
       // }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<SpeedItem> getInitiSpeedList(PackageManager packageManager, Context context){

        List<SpeedItem> speedItemList= new ArrayList<SpeedItem>();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo packageInfo = packageInfos.get(i);

            //过滤掉系统app
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                continue;
            }

            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            int uid = applicationInfo.uid;//应用id
            String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);//应用名称
            if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                continue;
            }
            Drawable icon=packageInfo.applicationInfo.loadIcon(packageManager);//应用图标

            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("Steven","没有权限");
            }
            String subId = tm.getSubscriberId();//网络接口ID
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NETWORK_STATS_SERVICE);
            //移动流量
            NetworkStats mobileFlowDetails= networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, subId,getTimesmorning().getTime() , System.currentTimeMillis(),uid);
            NetworkStats.Bucket mobileFlowBucket = new NetworkStats.Bucket();
            long mobileTrafficeByte=0;//发送字节总数
            long mobileReceiveByte=0;//接收字节总数
            long mobileTotalByte=0;//总字节数
            do {
                mobileFlowDetails.getNextBucket(mobileFlowBucket);
                mobileTrafficeByte= mobileTrafficeByte + mobileFlowBucket.getTxBytes();//接收的字节数
                mobileReceiveByte= mobileReceiveByte + mobileFlowBucket.getRxBytes();//传输的字节数
                mobileTotalByte = mobileTotalByte+mobileTrafficeByte+mobileReceiveByte;
            } while (mobileFlowDetails.hasNextBucket());

            //网络流量
            NetworkStats wifiFlowDetails= networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, subId,getTimesmorning().getTime() , System.currentTimeMillis(),uid);
            NetworkStats.Bucket wifiFlowBucket = new NetworkStats.Bucket();
            long wifiTrafficeByte=0;//发送字节总数
            long wifiReceiveByte=0;//接收字节总数
            long wifiTotalByte=0;//总字节数
            do {
                wifiFlowDetails.getNextBucket(wifiFlowBucket);
                wifiTrafficeByte= wifiTrafficeByte + wifiFlowBucket.getTxBytes();//接收的字节数
                wifiReceiveByte= wifiReceiveByte + wifiFlowBucket.getRxBytes();//传输的字节数
                wifiTotalByte = wifiTotalByte+ wifiTrafficeByte+ wifiReceiveByte;
            } while (wifiFlowDetails.hasNextBucket());

            long totalByte=mobileTotalByte+wifiTotalByte;
            //设置对象内容
            SpeedItem speedItem=new SpeedItem();
            speedItem.setAppId(uid);
            speedItem.setAppName(applicationName);
            speedItem.setAppIcon(icon);
            speedItem.setTotalFlow(totalByte);
            speedItem.setAppSpeed(0.0);
            speedItem.setSpeedUnit("K/s");

            speedItemList.add(speedItem);
        }
        return speedItemList;
    }

}