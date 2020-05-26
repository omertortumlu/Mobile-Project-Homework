package com.omer.proje;

import android.view.View;

public class reminder {
    private int time;
    private boolean notification;
    private boolean vibrator;
    private boolean alarm;


    public reminder(int time, boolean notification, boolean vibrator, boolean alarm) {
        this.time = time;
        this.notification = notification;
        this.vibrator = vibrator;
        this.alarm = alarm;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean isVibrator() {
        return vibrator;
    }

    public void setVibrator(boolean vibrator) {
        this.vibrator = vibrator;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

}
