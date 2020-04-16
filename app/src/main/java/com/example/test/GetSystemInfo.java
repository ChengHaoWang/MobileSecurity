package com.example.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.TELEPHONY_SERVICE;

public class GetSystemInfo {
    private static final String TAG = "DeviceInfoModel";
    private static GetSystemInfo instance;

    public GetSystemInfo() {
    }


    public static GetSystemInfo getInstance() {

        if (instance == null) {
            instance = new GetSystemInfo();
        }
        return instance;
    }

    /**
     * 获取SDK版本号
     */
    public String getPhoneSDK() {
        int sdkversion = android.os.Build.VERSION.SDK_INT;
        Log.w(TAG, "Android SDK版本号：" + sdkversion);
        return sdkversion+"";
    }

    /**
     * 获取bootloader版本号,该属性用于获取Android手机系统的bootloader版本号。bootloader为系统开机时的引导系统，其值为String字符串类型
     */
    public String getBootLoader() {
        String bootloaderversion = android.os.Build.BOOTLOADER;
        Log.w(TAG, "bootloader 版本号：" + bootloaderversion);
        return bootloaderversion;
    }

    /**
     * CPU_ABI属性：指令集
     * 该属性用于获取Android手机的CPU指令集名称，其值为String字符串类型，这里的指令集名称为CPU类型+ABI公约，如armeabl，该属性需要硬件设备的支持。
     */
    public String getCPU_ABI() {
        String CPUabi = android.os.Build.CPU_ABI;
        Log.w(TAG, "CPU 指令集：" + CPUabi);
        return CPUabi;
    }

    /**
     * PRODUCT属性：产品名称
     * 该属性用于获取Android手机的整体产品名称，其值为String字符串类型。
     */
    public String getPRODUCT() {
        String product = android.os.Build.PRODUCT;
        Log.w(TAG, "产品：" + product);
        return product;
    }

    /**
     * TAGS属性：标签（可以用来判断有无root权限）
     * 该属性用于获取Android手机的关于构建的标签，其值为String字符串类型。
     */
    public String getTags() {
        String tag = android.os.Build.TAGS;
        Log.w(TAG, "标签：" + tag);
        return tag;
    }

    /**
     * DEVICE属性：驱动
     */
    public String getDEVICE() {
        String device = android.os.Build.DEVICE;
        Log.w(TAG, "驱动：" + device);
        return device;
    }

    /**
     * BOARD属性：底层板名称
     * 该属性用于获取Android手机底层板的名称，其值为String字符串类型。
     */
    public String getBOARD() {
        String board = android.os.Build.BOARD;
        Log.w(TAG, "基板：" + board);
        return board;
    }

    /**
     * FINGERPRINT属性：设备标识
     */
    public String getFINGERPRINT() {
        String fingerprint = android.os.Build.FINGERPRINT;
        Log.w(TAG, "设备标识：" + fingerprint);
        return fingerprint;
    }

    /**
     * MANUFACTURER属性：制造商
     */
    public String getMANUFACTURER() {
        String manufacture = android.os.Build.MANUFACTURER;
        Log.w(TAG, "制造商：" + manufacture);
        return manufacture;
    }


    /**
     * USER：用户
     */
    public String getUSER() {
        String user = android.os.Build.USER;
        Log.w(TAG, "用户：" + user);
        return user;
    }


    /**
     * HARDWARE：硬件名称
     */
    public String getHARDWARE() {
        String hardware = android.os.Build.HARDWARE;
        Log.w(TAG, "硬件：" + hardware);
        return hardware;
    }


    /**
     * HOST：主机地址
     */
    public String getHOST() {
        String host = android.os.Build.HOST;
        Log.w(TAG, "主机地址：" + host);
        return host;
    }


    /**
     * TIME：出厂时间
     */
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public String getTIME() {
        long time = android.os.Build.TIME;
        SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT);
        Date date = new Date(time);
        Log.w(TAG, "出厂时间：" + date);
        return sf.format(date);
    }

    /**
     * SERIAL属性：硬件序列号
     * 该属性用于获取Android手机的硬件序列号，其值为String字符串类型。
     */
    public String getSERIAL() {
        String serial = android.os.Build.SERIAL;
        Log.w(TAG, "序列号：" + serial);
        return serial;
    }




}