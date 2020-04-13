package com.example.test;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class SpeedItem  {
    private int appId=0;
    private String appName=null;
    private double appSpeed=0.0;
    private String speedUnit=null;
    private Drawable appIcon=null;
    private long totalFlow=0;
    private String appDescription=null;

    public SpeedItem() {
    }

    public SpeedItem(int appId, String appName, double appSpeed, Drawable appIcon, long totalFlow) {
        this.appId = appId;
        this.appName = appName;
        this.appSpeed = appSpeed;
        this.appIcon = appIcon;
        this.totalFlow = totalFlow;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public double getAppSpeed() {
        return appSpeed;
    }

    public void setAppSpeed(double appSpeed) {
        this.appSpeed = appSpeed;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public long getTotalFlow() {
        return totalFlow;
    }

    public void setTotalFlow(long totalFlow) {
        this.totalFlow = totalFlow;
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
}
