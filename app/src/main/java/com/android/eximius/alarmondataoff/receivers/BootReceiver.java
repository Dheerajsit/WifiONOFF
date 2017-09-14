package com.android.eximius.alarmondataoff.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.eximius.alarmondataoff.model.AlarmDetail;
import com.android.eximius.alarmondataoff.utils.AlarmOnDataOffUtils;
import com.android.eximius.alarmondataoff.utils.DBUtils;

import java.util.ArrayList;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    // In AlarmManager, all set alarms gets cancelled automatically when device is rebooted.
    // Hence setting all those alarms here again whose status is "Active" in DB
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            try {
                //Fetching all alarms details whose status is Active in DB
                ArrayList<AlarmDetail> alarmDetailArrayList = DBUtils.fetchAllActiveAlarmsFromAlarmsTable(context);
                if (alarmDetailArrayList != null) {
                    int arrSize = alarmDetailArrayList.size();
                    if (arrSize > 0) { //If there is atleast one alarm
                        for (int index = 0; index < arrSize; index++) {
                            //Keeping alarm detail in an object
                            AlarmDetail alarmDetail = alarmDetailArrayList.get(index);

                            //Setting alarm for "start time" (i.e. Internet has to be turned off)
                            AlarmOnDataOffUtils.setAlarm(context, alarmDetail.getAlarmIdStartTime(), false,
                                    Integer.valueOf(alarmDetail.getStartHour()),
                                    Integer.valueOf(alarmDetail.getStartMinute()));
                            Log.e("AlarmsAdapter:activeStatusChanged() called.", "Alarm 1 is set");
                            Log.e("Alarm 1 ID:", String.valueOf(alarmDetail.getAlarmIdStartTime()));

                            //Setting alarm for "end time" (i.e. Internet has to be turned on)
                            AlarmOnDataOffUtils.setAlarm(context, alarmDetail.getAlarmIdEndTime(), true,
                                    Integer.valueOf(alarmDetail.getEndHour()),
                                    Integer.valueOf(alarmDetail.getEndMinute()));
                            Log.e("AlarmsAdapter:activeStatusChanged() called.", "Alarm 2 is set");
                            Log.e("Alarm 2 ID:", String.valueOf(alarmDetail.getAlarmIdEndTime()));
                        }
                    }
                }
            }catch(Exception e){}
        }
    }

}
