package com.android.eximius.alarmondataoff.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.android.eximius.alarmondataoff.utils.AlarmOnDataOffUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmReceiver class:", "onReceive() called");
        Bundle bundle = intent.getExtras();
        boolean isNetToBeEnabled = bundle.getBoolean("Enabled");

        if(isNetToBeEnabled) { //Turning ON internet (2G/3G/WiFi)
            try {
                if(AlarmOnDataOffUtils.getLastWiFiStatus(context)) {
                    AlarmOnDataOffUtils.setWiFiState(context, true);
                }
                if(AlarmOnDataOffUtils.getLastMobileDataStatus(context)) {
                    AlarmOnDataOffUtils.setMobileDataEnabled(context, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else                 //Turning OFF internet (2G/3G/WiFi)
        {
            try {
                //Fetching current status of WiFi & Mobile data so that if either of them is "not connected"
                //at the time of "Start Time/Setting alarm", the WiFi or Mobile Data SHOULDN'T turned on
                //at the time of "End Time" of alarm
                boolean isWiFiConnected = AlarmOnDataOffUtils.isWiFiConnected(context);
                boolean isMobileDataConnected = AlarmOnDataOffUtils.isMobileDataConnected(context);

                //Saving both status in shared preferences
                AlarmOnDataOffUtils.saveCurrentWiFiStatus(context, isWiFiConnected);
                AlarmOnDataOffUtils.saveCurrentMobileDataStatus(context, isMobileDataConnected);

                //Turning off both WiFi and Mobile data (if they are in "connected" state) when alarm is raised
                if(isWiFiConnected)
                    AlarmOnDataOffUtils.setWiFiState(context, false);
                if(isMobileDataConnected)
                    AlarmOnDataOffUtils.setMobileDataEnabled(context, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
