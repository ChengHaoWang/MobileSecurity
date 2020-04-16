package com.example.test;

public class CallLogInfo {
    public String number;
    public long date;
    public int type;
    public CallLogInfo(String number, long date, int type) {
        super();
        this.number = number;
        this.date = date;
        this.type = type;
    }
    public CallLogInfo() {
        super();
    }

    public String getNumber() {
        return number;
    }

    public long getDate() {
        return date;
    }

    public int getType() {
        return type;
    }
}
