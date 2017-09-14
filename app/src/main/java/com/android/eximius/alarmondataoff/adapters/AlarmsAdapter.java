package com.android.eximius.alarmondataoff.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.eximius.alarmondataoff.R;
import com.android.eximius.alarmondataoff.activities.HomeActivity;
import com.android.eximius.alarmondataoff.db.AlarmsTableHandler;
import com.android.eximius.alarmondataoff.utils.AlarmOnDataOffUtils;
import com.android.eximius.alarmondataoff.utils.DBUtils;

/**
 * Created by Richa on 06/29/2017.
 */
public class AlarmsAdapter extends CursorAdapter{

    Context mContext;

    public AlarmsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        return mInflater.inflate(R.layout.list_item_alarms, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = new ViewHolder();

        //initializing controls
        holder.startTime = (TextView) view.findViewById(R.id.txtStartTime);
        holder.endTime = (TextView) view.findViewById(R.id.txtEndTime);
        holder.firstDayNightHeading = (TextView) view.findViewById(R.id.txtDayNightHeadingFirst);
        holder.toHeading = (TextView) view.findViewById(R.id.txtToHeading);
        holder.secondDayNightHeading = (TextView) view.findViewById(R.id.txtDayNightHeadingSecond);
        holder.btnDelete = (Button) view.findViewById(R.id.btnDelete);

        int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 14) {
            holder.switchIsActive = (CompoundButton) view.findViewById(R.id.switchBtnIsActive);
        }
        else {
            holder.toggleIsActive = (CompoundButton) view.findViewById(R.id.toggleBtnIsActive);
        }

        final int alarmId = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmsTableHandler._ID));
        final int alarmIdStartTime = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmsTableHandler.ALARM_ID_START_TIME));
        final int alarmIdEndTime = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmsTableHandler.ALARM_ID_END_TIME));
        final String startHourValue = cursor.getString(cursor.getColumnIndexOrThrow(AlarmsTableHandler.START_HOUR));
        final String startMinuteValue = cursor.getString(cursor.getColumnIndexOrThrow(AlarmsTableHandler.START_MINUTE));
        final String endHourValue = cursor.getString(cursor.getColumnIndexOrThrow(AlarmsTableHandler.END_HOUR));
        final String endMinuteValue = cursor.getString(cursor.getColumnIndexOrThrow(AlarmsTableHandler.END_MINUTE));
        final String startTimeStatus = cursor.getString(cursor.getColumnIndexOrThrow(AlarmsTableHandler.START_TIME_STATUS));
        final String endTimeStatus = cursor.getString(cursor.getColumnIndexOrThrow(AlarmsTableHandler.END_TIME_STATUS));
        final boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmsTableHandler.IS_ACTIVE)) > 0;

        if(isActive){
            holder.startTime.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.endTime.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.firstDayNightHeading.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.toHeading.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.secondDayNightHeading.setTextColor(context.getResources().getColor(android.R.color.white));
        }
        else{
            holder.startTime.setTextColor(context.getResources().getColor(R.color.txt_color_alarm_not_active));
            holder.endTime.setTextColor(context.getResources().getColor(R.color.txt_color_alarm_not_active));
            holder.firstDayNightHeading.setTextColor(context.getResources().getColor(R.color.txt_color_alarm_not_active));
            holder.toHeading.setTextColor(context.getResources().getColor(R.color.txt_color_alarm_not_active));
            holder.secondDayNightHeading.setTextColor(context.getResources().getColor(R.color.txt_color_alarm_not_active));
        }

        //Setting values in text boxes & switch
        holder.startTime.setText(startHourValue + ":" + startMinuteValue);
        holder.endTime.setText(endHourValue + ":" + endMinuteValue);
        holder.firstDayNightHeading.setText(startTimeStatus);
        holder.secondDayNightHeading.setText(endTimeStatus);

        //NOTE:Both code blocks are identical absolutely other than control name. Manage any change in both
        //of them.
        if (version >= 14) {
            //First set a null change listener, then the programmatic initialization based on DB results,
            //before adding the actual listener.
            //Else setOnCheckedChangeListener will be called when setting values based on DB results

            if(holder.switchIsActive != null) {
                holder.switchIsActive.setOnCheckedChangeListener(null);
                holder.switchIsActive.setChecked(isActive);
                holder.switchIsActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.e("onCheckedChanged()", "Switch setOnCheckedChangeListener() called");
                        Log.e("Check_val_Switch:", String.valueOf(isChecked));
                        //If the current "checked" value is different than original, then only change the status.
                        //Without this check, code will be executed each time as we are above setting value in SetChecked()
                        // if(isActive != isChecked) {
                        activeStatusChanged(alarmId, isChecked, alarmIdStartTime, alarmIdEndTime,
                                startHourValue, startMinuteValue, endHourValue, endMinuteValue);
                    }
                });
            }
        }else{
            if(holder.toggleIsActive != null) {
                holder.toggleIsActive.setOnCheckedChangeListener(null);
                holder.toggleIsActive.setChecked(isActive);

                holder.toggleIsActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.e("onCheckedChanged()", "Switch setOnCheckedChangeListener() called");
                        Log.e("Check_val_Switch:", String.valueOf(isChecked));
                        //If the current "checked" value is different than original, then only change the status.
                        //Without this check, code will be executed each time as we are above setting value in SetChecked()
                        // if(isActive != isChecked) {
                        activeStatusChanged(alarmId, isChecked, alarmIdStartTime, alarmIdEndTime,
                                startHourValue, startMinuteValue, endHourValue, endMinuteValue);
                    }
                });
            }
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AlarmsAdapter", "Delete clicked");
                deleteBtnClicked(alarmId, alarmIdStartTime, alarmIdEndTime);
            }
        });
    }

    static class ViewHolder{
        TextView startTime;
        TextView endTime;
        TextView firstDayNightHeading;
        TextView toHeading;
        TextView secondDayNightHeading;
        Button btnDelete;
        CompoundButton switchIsActive;
        CompoundButton toggleIsActive;
    }

    //aAlarmId = Auto generated Primary key's value of the alarm record/row in table
    //alarmIDStartTime = Unique ID of Start Time alarm
    //alarmIDEndTime = Unique ID of End Time alarm
    private void deleteBtnClicked(final int aAlarmId, final int alarmIDStartTime, final int alarmIDEndTime) {
        new AlertDialog.Builder(mContext)
                .setMessage(R.string.are_you_sure_to_delete)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Deleting alarms from the Alarm Manager first
                                AlarmOnDataOffUtils.deleteAlarmInSystemAlarmManager(mContext, alarmIDStartTime);
                                AlarmOnDataOffUtils.deleteAlarmInSystemAlarmManager(mContext, alarmIDEndTime);

                                //Deleting record from DB
                                int numRowsDeleted = DBUtils.deleteAlarmRecordFromAlarmsTable(mContext, aAlarmId);
                                Log.e(mContext.getResources().getString(R.string.rowsDelHeadingForLogs), String.valueOf(numRowsDeleted));
                                //Refreshing the listview by executing loader again
                                HomeActivity homeActivity = (HomeActivity) mContext;
                                if(homeActivity != null){
                                    if (homeActivity.mListFragment != null)
                                        homeActivity.mListFragment.refreshAlarmsListExplicitly();
                                }
                            }
                        })
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }


    //aAlarmId = Auto generated Primary key's value of the alarm record/row in table
    //isActive = Status of the Alarm (Alarm is active or not)
    //alarmIDStartTime = Unique ID of Start Time alarm
    //alarmIDEndTime = Unique ID of End Time alarm
    private void activeStatusChanged(int alarmID, boolean isActive, int alarmIDStartTime, int alarmIDEndTime,
                                String startHour, String startMinute, String endHour, String endMinute){
        try {
            //Updating the status of "isActive" in DB
            int status = DBUtils.updateStatusFieldInAlarmsTable(mContext, alarmID, isActive);
            Log.e(mContext.getResources().getString(R.string.rowsUpdatedHeadingForLogs), String.valueOf(status));

            //If 'isActive' = true, both alarms for the current record should be set. Else, they should be deleted
            //from the system (alarm manager)
            if (isActive) {
                //To set multiple alarms, we need to pass unique id
                final int _id1 = AlarmOnDataOffUtils.getUniquePendingId();
                AlarmOnDataOffUtils.setAlarm(mContext, _id1, false, Integer.valueOf(startHour), Integer.valueOf(startMinute));
                Log.e("Adapter:StatusChanged:", "Alarm 1 set");
                Log.e("Alarm 1 ID:", String.valueOf(_id1));

                final int _id2 = AlarmOnDataOffUtils.getUniquePendingId();
                AlarmOnDataOffUtils.setAlarm(mContext, _id2, true, Integer.valueOf(endHour), Integer.valueOf(endMinute));
                Log.e("Adapter:StatusChanged:", "Alarm 2 set");
                Log.e("Alarm 2 ID:", String.valueOf(_id2));
            } else { //If isActive is currently = false, it means both alarms in the record needs to be deleted from system
                AlarmOnDataOffUtils.deleteAlarmInSystemAlarmManager(mContext, alarmIDStartTime);
                AlarmOnDataOffUtils.deleteAlarmInSystemAlarmManager(mContext, alarmIDEndTime);
            }
            //Refreshing the listview by executing loader again
            HomeActivity homeActivity = (HomeActivity) mContext;
            if(homeActivity != null) {
                if (homeActivity.mListFragment != null)
                    homeActivity.mListFragment.refreshAlarmsListExplicitly();
            }
        }catch (Exception e){}
    }

}
