package com.example.adventurelibertarian.utils;

import android.util.Log;

public class CountDownUtil {
    public CountDownUtil(long fullTime){
        int seconds = (int) (fullTime / 1000);
        this.minutes = seconds / 60;
        this.seconds = seconds % 60;
        this.milliSeconds = (int) (fullTime % 1000);
        timeLeft = fullTime;

    }

    public void onTickHappened(long newTimeLeft){
        long timePassed = timeLeft - newTimeLeft;

        milliSeconds -= timePassed;

        while(milliSeconds < 0){
            milliSeconds += 1000;
            seconds--;
        }

        while(seconds < 0){
            seconds += 60;
            minutes--;
        }
        timeLeft = newTimeLeft;
    }

    public int getMilliSeconds() {
        return milliSeconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    private int minutes;
    private int seconds;
    private int milliSeconds;
    private long timeLeft;
}
