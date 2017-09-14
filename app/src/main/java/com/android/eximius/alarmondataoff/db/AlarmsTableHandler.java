package com.android.eximius.alarmondataoff.db;

import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Richa on 06/29/2017.
 */
public class AlarmsTableHandler {

    public static final String TABLE_NAME = "alarms";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AlarmOnDataOffContentProvider.AUTHORITY + "/" + TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.eximius.alarms";

    public static final String _ID = "_id";
    public static final String ALARM_ID_START_TIME = "alarm_id_start_time";
    public static final String ALARM_ID_END_TIME = "alarm_id_end_time";
    public static final String START_HOUR = "start_hour"; //in the format: <hh>
    public static final String START_MINUTE = "start_minute"; //in the format: <mm>
    public static final String END_HOUR = "end_hour"; //in the format: <hh>
    public static final String END_MINUTE = "end_minute"; //in the format: <mm>
    public static final String START_TIME_STATUS = "start_time_status"; //either a.m./p.m.
    public static final String END_TIME_STATUS = "end_time_status"; //either a.m./p.m.
    public static final String IS_ACTIVE = "is_active";
    public static final String IS_FOR_ALL_DAYS = "is_for_all_days";
    public static final String IS_FOR_MONDAY = "is_for_monday";
    public static final String IS_FOR_TUESDAY = "is_for_tuesday";
    public static final String IS_FOR_WEDNESDAY = "is_for_wednesday";
    public static final String IS_FOR_THURSDAY = "is_for_thursday";
    public static final String IS_FOR_FRIDAY = "is_for_friday";
    public static final String IS_FOR_SATURDAY = "is_for_saturday";
    public static final String IS_FOR_SUNDAY = "is_for_sunday";

}
