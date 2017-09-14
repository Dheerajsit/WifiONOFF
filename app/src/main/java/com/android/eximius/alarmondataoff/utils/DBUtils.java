package com.android.eximius.alarmondataoff.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.android.eximius.alarmondataoff.db.AlarmsTableHandler;
import com.android.eximius.alarmondataoff.model.AlarmDetail;

import java.util.ArrayList;

/**
 * Created by Richa on 06/29/2017.
 */
public class DBUtils {

    public static ArrayList<AlarmDetail> fetchAlarmsRecordsFromAlarmsTable(Context aContext) {
        ArrayList<AlarmDetail> alarmDetailArrayList = new ArrayList<AlarmDetail>();
        Cursor cursor = null;

        try {
            cursor = aContext.getContentResolver().query(
                    AlarmsTableHandler.CONTENT_URI, null, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    alarmDetailArrayList.add(new AlarmDetail(
                            cursor.getInt(cursor.getColumnIndex("_id")),
                            cursor.getInt(cursor.getColumnIndex("alarm_id_start_time")),
                            cursor.getInt(cursor.getColumnIndex("alarm_id_end_time")),
                            cursor.getString(cursor.getColumnIndex("start_hour")),
                            cursor.getString(cursor.getColumnIndex("start_minute")),
                            cursor.getString(cursor.getColumnIndex("end_hour")),
                            cursor.getString(cursor.getColumnIndex("end_minute")),
                            cursor.getString(cursor.getColumnIndex("start_time_status")),
                            cursor.getString(cursor.getColumnIndex("end_time_status")),
                            cursor.getInt(cursor.getColumnIndex("is_active")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_all_days")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_monday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_tuesday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_wednesday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_thursday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_friday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_saturday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_sunday")) > 0));
                } while (cursor.moveToNext());
            }
            return alarmDetailArrayList;
        } catch (Exception e) {
            return alarmDetailArrayList;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public static void addRecordInAlarmsTable(Context aContext,
                                              int alarmIdStartTime, int alarmIdEndTime,
                                              String startHour, String startMinute,
                                              String endHour, String endMinute,
                                              String startTimeStatus, String endTimeStatus) {

        try {
            ContentValues values = new ContentValues();
            values.put(AlarmsTableHandler.ALARM_ID_START_TIME, alarmIdStartTime);
            values.put(AlarmsTableHandler.ALARM_ID_END_TIME, alarmIdEndTime);
            values.put(AlarmsTableHandler.START_HOUR, startHour);
            values.put(AlarmsTableHandler.START_MINUTE, startMinute);
            values.put(AlarmsTableHandler.END_HOUR, endHour);
            values.put(AlarmsTableHandler.END_MINUTE, endMinute);
            values.put(AlarmsTableHandler.START_TIME_STATUS, startTimeStatus);
            values.put(AlarmsTableHandler.END_TIME_STATUS, endTimeStatus);
            values.put(AlarmsTableHandler.IS_ACTIVE, true);
            values.put(AlarmsTableHandler.IS_FOR_ALL_DAYS, true);
            values.put(AlarmsTableHandler.IS_FOR_MONDAY, false);
            values.put(AlarmsTableHandler.IS_FOR_TUESDAY, false);
            values.put(AlarmsTableHandler.IS_FOR_WEDNESDAY, false);
            values.put(AlarmsTableHandler.IS_FOR_THURSDAY, false);
            values.put(AlarmsTableHandler.IS_FOR_FRIDAY, false);
            values.put(AlarmsTableHandler.IS_FOR_SATURDAY, false);
            values.put(AlarmsTableHandler.IS_FOR_SUNDAY, false);

            Uri insertedUri = aContext.getContentResolver().insert(
                    AlarmsTableHandler.CONTENT_URI, values);
            aContext.getContentResolver().notifyChange(insertedUri, null, false);
            Log.e("Inserted row's url is:",
                    insertedUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //To delete an alarm's entry from database.
    //Parameter 'aAlarmId' is the auto-generated primary key of the table
    public static int deleteAlarmRecordFromAlarmsTable(Context aContext, int aAlarmId) {
        try {
            return (aContext.getContentResolver().delete(AlarmsTableHandler.CONTENT_URI,
                    AlarmsTableHandler._ID + "= " + aAlarmId, null));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int updateStatusFieldInAlarmsTable(Context aContext, int aAlarmId, boolean isActive) {
        try {
            ContentValues values = new ContentValues();
            values.put(AlarmsTableHandler.IS_ACTIVE, isActive);

            String selectionClause = AlarmsTableHandler._ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(aAlarmId) };

            return (aContext.getContentResolver().update(AlarmsTableHandler.CONTENT_URI, values,
                    selectionClause, selectionArgs));
        } catch (Exception e) {
            return 0;
        }
    }

    public static ArrayList<AlarmDetail> fetchAllActiveAlarmsFromAlarmsTable(Context aContext) {
        ArrayList<AlarmDetail> alarmDetailArrayList = new ArrayList<AlarmDetail>();
        String selectionClause = AlarmsTableHandler.IS_ACTIVE;

        Cursor cursor = null;

        try {
            //Only pass selectionClause to get all "active" records
            cursor = aContext.getContentResolver().query(
                    AlarmsTableHandler.CONTENT_URI, null, selectionClause, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    alarmDetailArrayList.add(new AlarmDetail(
                            cursor.getInt(cursor.getColumnIndex("_id")),
                            cursor.getInt(cursor.getColumnIndex("alarm_id_start_time")),
                            cursor.getInt(cursor.getColumnIndex("alarm_id_end_time")),
                            cursor.getString(cursor.getColumnIndex("start_hour")),
                            cursor.getString(cursor.getColumnIndex("start_minute")),
                            cursor.getString(cursor.getColumnIndex("end_hour")),
                            cursor.getString(cursor.getColumnIndex("end_minute")),
                            cursor.getString(cursor.getColumnIndex("start_time_status")),
                            cursor.getString(cursor.getColumnIndex("end_time_status")),
                            cursor.getInt(cursor.getColumnIndex("is_active")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_all_days")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_monday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_tuesday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_wednesday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_thursday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_friday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_saturday")) > 0,
                            cursor.getInt(cursor.getColumnIndex("is_for_sunday")) > 0));
                } while (cursor.moveToNext());
            }
            return alarmDetailArrayList;
        } catch (Exception e) {
            return alarmDetailArrayList;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

}
