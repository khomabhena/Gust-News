package com.mabinc.gust;

public class SetterMonth {

    public SetterMonth() {
    }

    private String month, year;
    private long timestamp;

    public SetterMonth(String month, String year, long timestamp) {
        this.month = month;
        this.year = year;
        this.timestamp = timestamp;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
