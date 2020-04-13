package com.example.test;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class AppItem {
    private String appName=null;
    private String appPackageName=null;
    private int appId=0;
    private String totalFlowString="0";
    private String totalTime="0";
    private Drawable appIcon=null;
    private double appSpeed=0.0;
    private String speedUnit="B/s";
    private String appDescription="0";
    private long totalFlowLong=0;
    private long firstInstallTime=0;
    private long startTime=0;
    private long endTime=0;
    private int unconvertedSpeed=0;

    public AppItem() {

    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }


    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public double getAppSpeed() {
        return appSpeed;
    }

    public void setAppSpeed(double appSpeed) {
        this.appSpeed = appSpeed;
    }

    public String getSpeedUnit() {
        return speedUnit;
    }

    public void setSpeedUnit(String speedUnit) {
        this.speedUnit = speedUnit;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getTotalFlowString() {
        return totalFlowString;
    }

    public void setTotalFlowString(String totalFlowString) {
        this.totalFlowString = totalFlowString;
    }

    public long getTotalFlowLong() {
        return totalFlowLong;
    }

    public void setTotalFlowLong(long totalFlowLong) {
        this.totalFlowLong = totalFlowLong;
    }

    public long getFirstInstallTime() {
        return firstInstallTime;
    }

    public void setFirstInstallTime(long firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getUnconvertedSpeed() {
        return unconvertedSpeed;
    }

    public void setUnconvertedSpeed(int unconvertedSpeed) {
        this.unconvertedSpeed = unconvertedSpeed;
    }
}
