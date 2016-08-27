package com.nanodegree.android.watchthemall.api.trakt;

/**
 * Class representing Trakt air info
 */
public class AirInfo {

    private String day;
    private String time;
    private String timezone;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "AirInfo{" +
                "day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
