package com.example.adventurelibertarian.utils;

import android.os.CountDownTimer;

import com.example.adventurelibertarian.models.Factory;

import java.util.ArrayList;
import java.util.List;

public class MyCountDownTimer {
    private static long previousMillisUntilFinished = 100000;

    private static List<Factory> factories = new ArrayList<>();

    public static void addFactory(Factory factory){
        factories.add(factory);
    }

    public static void removeFactory(Factory factory){
        factories.remove(factory);
    }

    public static void doWork(){
        new CountDownTimer(100000, 10){

            @Override
            public void onTick(long millisUntilFinished) {
                long timePassed = previousMillisUntilFinished - millisUntilFinished;
                if(timePassed < 0){
                    timePassed = 100000 - timePassed;
                }
                previousMillisUntilFinished = millisUntilFinished;
                for(int i = 0; i < factories.size(); i++){
                    Factory factory = factories.get(i);
                    factory.onTimePassed(timePassed);
                }
            }

            @Override
            public void onFinish() {
                doWork();
            }
        }.start();
    }



}
