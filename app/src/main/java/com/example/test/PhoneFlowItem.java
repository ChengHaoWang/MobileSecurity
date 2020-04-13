package com.example.test;

public class PhoneFlowItem {
    private long phoneDayTotalFlowLong=0;
    private String phoneDayTotalFlowString="";
    private long phoneWeekTotalFlowLong=0;
    private String phoneWeekTotalFlowString="";
    private long phoneMonthTotalFlowLong=0;
    private String phoneMonthTotalFlowString="";

    public PhoneFlowItem(long phoneDayTotalFlowLong, String phoneDayTotalFlowString, long phoneWeekTotalFlowLong, String phoneWeekTotalFlowString, long phoneMonthTotalFlowLong, String phoneMonthTotalFlowString) {
        this.phoneDayTotalFlowLong = phoneDayTotalFlowLong;
        this.phoneDayTotalFlowString = phoneDayTotalFlowString;
        this.phoneWeekTotalFlowLong = phoneWeekTotalFlowLong;
        this.phoneWeekTotalFlowString = phoneWeekTotalFlowString;
        this.phoneMonthTotalFlowLong = phoneMonthTotalFlowLong;
        this.phoneMonthTotalFlowString = phoneMonthTotalFlowString;
    }

    public PhoneFlowItem() {
    }

    public long getPhoneDayTotalFlowLong() {
        return phoneDayTotalFlowLong;
    }

    public void setPhoneDayTotalFlowLong(long phoneDayTotalFlowLong) {
        this.phoneDayTotalFlowLong = phoneDayTotalFlowLong;
    }

    public String getPhoneDayTotalFlowString() {
        return phoneDayTotalFlowString;
    }

    public void setPhoneDayTotalFlowString(String phoneDayTotalFlowString) {
        this.phoneDayTotalFlowString = phoneDayTotalFlowString;
    }

    public long getPhoneWeekTotalFlowLong() {
        return phoneWeekTotalFlowLong;
    }

    public void setPhoneWeekTotalFlowLong(long phoneWeekTotalFlowLong) {
        this.phoneWeekTotalFlowLong = phoneWeekTotalFlowLong;
    }

    public String getPhoneWeekTotalFlowString() {
        return phoneWeekTotalFlowString;
    }

    public void setPhoneWeekTotalFlowString(String phoneWeekTotalFlowString) {
        this.phoneWeekTotalFlowString = phoneWeekTotalFlowString;
    }

    public long getPhoneMonthTotalFlowLong() {
        return phoneMonthTotalFlowLong;
    }

    public void setPhoneMonthTotalFlowLong(long phoneMonthTotalFlowLong) {
        this.phoneMonthTotalFlowLong = phoneMonthTotalFlowLong;
    }

    public String getPhoneMonthTotalFlowString() {
        return phoneMonthTotalFlowString;
    }

    public void setPhoneMonthTotalFlowString(String phoneMonthTotalFlowString) {
        this.phoneMonthTotalFlowString = phoneMonthTotalFlowString;
    }

}
