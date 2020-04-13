package com.example.test;

public class PhoneSpeedItem {
    private long phoneMonthTotalFlowLong=0;
    private long endTime=0;
    private double phoneSpeed=0.0;
    private String speedUnit="B/s";

    public PhoneSpeedItem() {
    }

    public long getPhoneMonthTotalFlowLong() {
        return phoneMonthTotalFlowLong;
    }

    public void setPhoneMonthTotalFlowLong(long phoneMonthTotalFlowLong) {
        this.phoneMonthTotalFlowLong = phoneMonthTotalFlowLong;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getPhoneSpeed() {
        return phoneSpeed;
    }

    public void setPhoneSpeed(double phoneSpeed) {
        this.phoneSpeed = phoneSpeed;
    }

    public String getSpeedUnit() {
        return speedUnit;
    }

    public void setSpeedUnit(String speedUnit) {
        this.speedUnit = speedUnit;
    }
}
