package com.android.eximius.alarmondataoff.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.android.eximius.alarmondataoff.R;
import com.android.eximius.alarmondataoff.receivers.AlarmReceiver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by Richa on 06/29/2017.
 */
public class AlarmOnDataOffUtils {

    public static final String MyPREFERENCES = "MyPrefs";

    //To get a unique integer ID which can be used to set an alarm which doesn't overrides any other alarm
    //because of the same broadcast receiver class
    public static int getUniquePendingId(){
        final int id = (int) System.currentTimeMillis();
        return id;
    }

    //To set an alarm. Can be set via a "New Alarm (Home screen) or via BootReceiver class (if required, after phone is restarted)
    // or when the status is changed to "On" (Home screen's alarms list box)
    //aAlarmId = Unique alarm Id so that one alarm doesn't overrides other
    //isEnabled = To set whether internet has to switched on or off
    //hour = Hour of the time
    //minute = Minute of the time
    public static void setAlarm(Context aContext, int aAlarmId, boolean isEnabled, int hour, int minute)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        Intent myIntent = new Intent(aContext, AlarmReceiver.class);
        myIntent.putExtra("Enabled", isEnabled);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(aContext, aAlarmId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) aContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);//INTERVAL_DAY is used so that alarm is invoked DAILY (with an interval of a day)
        Log.e("Alarm is Set for:", hour + ":" + minute);
    }

    //To delete/cancel an "active" alarm based on alarmIDStartOrEndTime which is either alarmIDStartTime or alarmIDEndTime
    // from the system itself. To delete/cancel an alarm, pending intent has to be fetched
    public static void deleteAlarmInSystemAlarmManager(Context aContext, int alarmIDStartOrEndTime){
        try {
            AlarmManager alarmMgr = (AlarmManager) aContext.getSystemService(Context.ALARM_SERVICE);
            if (alarmMgr != null) {
                PendingIntent pIntent = getPendingIntent(aContext, alarmIDStartOrEndTime, false);
                if(pIntent != null) {
                    alarmMgr.cancel(pIntent);
                    Log.e("Alarm deleted from AlarmManager with alarmId:", String.valueOf(alarmIDStartOrEndTime));
                }
            }
        }catch (Exception e){}
    }

    // To fetch a pending intent for a given 'alarmIDStartOrEndTime' which is either alarmIDStartTime or alarmIDEndTime.
    // Setting "isNetToBeEnabled" in the intent to be used by
    // AlarmReceiver so that right action (either switching on/of Internet) can be taken out.
    public static PendingIntent getPendingIntent(Context aContext, int alarmIDStartOrEndTime, boolean isNetToBeEnabled){
        PendingIntent pendingIntent = null;
        try {
            Intent intentObj = new Intent(aContext, AlarmReceiver.class);
            intentObj.putExtra("Enabled", isNetToBeEnabled);//So that in the AlarmReceiver it can be decided whether net has to enable/disabled
            pendingIntent =
                    PendingIntent.getBroadcast(aContext, alarmIDStartOrEndTime, intentObj, PendingIntent.FLAG_UPDATE_CURRENT);
            return pendingIntent;
        }catch (Exception e){return pendingIntent;}
    }

    //To fetch whether an hour value belongs to 'am' or 'pm' as it has to be shown on screen.
    //We could have passed the converted values in the DB while saving the records itself, but
    //later on while setting an alarm, we would have required again to convert an Hour value to 24 hour format.
    public static String fetchDayNightStatus(Context aContext, String aHourValue){
        //Setting 'a.m.' to default value
        String aStatus = aContext.getResources().getString(R.string.am);
        try {
            int hourValue = Integer.valueOf(aHourValue);
            if (hourValue >= 0 && hourValue <= 11)
                aStatus = aContext.getResources().getString(R.string.am);
            else if (hourValue >= 12 && hourValue <= 23)
                aStatus = aContext.getResources().getString(R.string.pm);
            return aStatus;
        }catch (Exception e)
        {return aStatus;}
    }

    public static String fetchHourValueFromTime(String aTime){
        int colonPos = aTime.indexOf(":");
        String hh = aTime.substring(0, colonPos);
        return hh;
    }

    //To convert a single digit hour or minute value into 2-digits value.
    //If input is > 9 then do nothing else process.
    public static String fetchTwoDigitTimeValue(String aHourOrMinuteValue){
        try {
            if (Integer.valueOf(aHourOrMinuteValue) >= 0 && Integer.valueOf(aHourOrMinuteValue) <= 9) {
                if (aHourOrMinuteValue.equals("0"))
                    return "00";
                else if (aHourOrMinuteValue.equals("1"))
                    return "01";
                else if (aHourOrMinuteValue.equals("2"))
                    return "02";
                else if (aHourOrMinuteValue.equals("3"))
                    return "03";
                else if (aHourOrMinuteValue.equals("4"))
                    return "04";
                else if (aHourOrMinuteValue.equals("5"))
                    return "05";
                else if (aHourOrMinuteValue.equals("6"))
                    return "06";
                else if (aHourOrMinuteValue.equals("7"))
                    return "07";
                else if (aHourOrMinuteValue.equals("8"))
                    return "08";
                else if (aHourOrMinuteValue.equals("9"))
                    return "09";
            }

            return aHourOrMinuteValue; //return original input as default value
        }catch(Exception e)
        {return aHourOrMinuteValue;} //return original input
    }

    //This method should work for Android 2.3 and above
    public static void setMobileDataEnabled(Context context, boolean isEnabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InvocationTargetException {
        try {
            Log.e("AlarmOnDataOffUtils: Inside setMobileDataEnabled(). Invoked to set status as:", String.valueOf(isEnabled));
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
            connectivityManagerField.setAccessible(true);
            final Object connectivityManager = connectivityManagerField.get(conman);
            final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(connectivityManager, isEnabled);
            Log.e("Mobile Data status changed to:", String.valueOf(isEnabled));
        }catch(Exception e)
        {}
    }

    public static void setWiFiState(Context context, boolean isEnabled) {
        try {
            Log.e("AlarmOnDataOffUtils: Inside setWiFiState(). Invoked to set status as:", String.valueOf(isEnabled));
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(isEnabled);
            Log.e("Wifi status changed to:", String.valueOf(isEnabled));
        }catch (Exception e)
        {}
    }

    public static boolean isMobileDataConnected(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (mMobile.isConnected()) {
                Log.e("isMobileDataConnected() result:", String.valueOf(true));
                return true;
            }
            else{
                Log.e("isMobileDataConnected() result:", String.valueOf(false));
                return false;
            }
        }catch(Exception e){return false;}
    }

    public static boolean isWiFiConnected(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
                Log.e("isWiFiConnected() result:", String.valueOf(true));
                return true;
            }
            else {
                Log.e("isWiFiConnected() result:", String.valueOf(false));
                return false;
            }
        }catch(Exception e){return false;}
    }

    //Current mobile data status is being saved in the shared preferences while at the time of "Start Time"
    //so that at the time of "End Time", either of WiFi/Mobile data is not turned on if it was not in the
    //connecting state (at "start time").
    public static void saveCurrentMobileDataStatus(Context context, boolean isCurrentlyMobileDataConnected)
    {
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putBoolean(context.getResources().getString(R.string.isCurrentlyMobileDataConnected),
                    isCurrentlyMobileDataConnected);
            editor.commit();
        }catch(Exception e){}
    }

    //Current mobile data status is being saved in the shared preferences while at the time of "Start Time"
    //so that at the time of "End Time", either of WiFi/Mobile data is not turned on if it was not in the
    //connecting state (at "start time").
    public static void saveCurrentWiFiStatus(Context context, boolean isWiFiConnected)
    {
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putBoolean(context.getResources().getString(R.string.isCurrentlyWiFiConnected), isWiFiConnected);
            editor.commit();
        }catch (Exception e){}
    }

    public static boolean getLastMobileDataStatus(Context context)
    {
        boolean lastStatusMobileData = true; //Keeping the default value as TRUE. Will be used while picking from shared pref
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
            lastStatusMobileData = sharedpreferences.getBoolean
                    (context.getResources().getString(R.string.isCurrentlyMobileDataConnected), lastStatusMobileData);
            return lastStatusMobileData;
        }catch (Exception e){return lastStatusMobileData;}
    }

    public static boolean getLastWiFiStatus(Context context)
    {
        boolean lastStatusWiFi = true; //Keeping the default value as TRUE. Will be used while picking from shared pref
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
            lastStatusWiFi = sharedpreferences.getBoolean
                    (context.getResources().getString(R.string.isCurrentlyWiFiConnected), lastStatusWiFi);
            return lastStatusWiFi;
        }catch (Exception e){return lastStatusWiFi;}
    }
}
