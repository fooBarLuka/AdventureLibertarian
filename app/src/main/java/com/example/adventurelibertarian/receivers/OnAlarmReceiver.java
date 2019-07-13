package com.example.adventurelibertarian.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.adventurelibertarian.utils.NotificationsUtil;

public class OnAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationsUtil.createNotification(context, "Money time", "Time to collect money");
    }
}
