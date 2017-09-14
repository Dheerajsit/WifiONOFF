package com.android.eximius.alarmondataoff.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Richa on 06/29/2017.
 */
public class AlarmOnDataOffContentProvider extends ContentProvider {

    private static String DATABASE = "alarmondataoff.db";
    public static String AUTHORITY = "com.android.eximius.alarmondataoff.db.AlarmOnDataOffContentProvider";

    private static int VERSION = 1;

    public static final int ALARMS = 1;

    static UriMatcher uriMatcher;

    public static String ALARMS_TABLE_NAME = "alarms";

    DBHelper databaseOpenHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ALARMS_TABLE_NAME, ALARMS);
    }

    private static HashMap<String, String> alarmsProjectionMap;

    static {
        alarmsProjectionMap = new HashMap<String, String>();

        alarmsProjectionMap.put(AlarmsTableHandler._ID, AlarmsTableHandler._ID);
        alarmsProjectionMap.put(AlarmsTableHandler.ALARM_ID_START_TIME,
                AlarmsTableHandler.ALARM_ID_START_TIME);
        alarmsProjectionMap.put(AlarmsTableHandler.ALARM_ID_END_TIME,
                AlarmsTableHandler.ALARM_ID_END_TIME);
        alarmsProjectionMap.put(AlarmsTableHandler.START_HOUR,
                AlarmsTableHandler.START_HOUR);
        alarmsProjectionMap.put(AlarmsTableHandler.START_MINUTE,
                AlarmsTableHandler.START_MINUTE);
        alarmsProjectionMap.put(AlarmsTableHandler.END_HOUR,
                AlarmsTableHandler.END_HOUR);
        alarmsProjectionMap.put(AlarmsTableHandler.END_MINUTE,
                AlarmsTableHandler.END_MINUTE);
        alarmsProjectionMap.put(AlarmsTableHandler.START_TIME_STATUS,
                AlarmsTableHandler.START_TIME_STATUS);
        alarmsProjectionMap.put(AlarmsTableHandler.END_TIME_STATUS,
                AlarmsTableHandler.END_TIME_STATUS);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_ACTIVE,
                AlarmsTableHandler.IS_ACTIVE);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_FOR_ALL_DAYS,
                AlarmsTableHandler.IS_FOR_ALL_DAYS);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_FOR_MONDAY,
                AlarmsTableHandler.IS_FOR_MONDAY);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_FOR_TUESDAY,
                AlarmsTableHandler.IS_FOR_TUESDAY);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_FOR_WEDNESDAY,
                AlarmsTableHandler.IS_FOR_WEDNESDAY);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_FOR_THURSDAY,
                AlarmsTableHandler.IS_FOR_THURSDAY);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_FOR_FRIDAY,
                AlarmsTableHandler.IS_FOR_FRIDAY);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_FOR_SATURDAY,
                AlarmsTableHandler.IS_FOR_SATURDAY);
        alarmsProjectionMap.put(AlarmsTableHandler.IS_FOR_SUNDAY,
                AlarmsTableHandler.IS_FOR_SUNDAY);
    }

    class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            //Creating database here passing name of the database and version number
            super(context, Environment.getExternalStorageDirectory()
                    + File.separator+ DATABASE, null, VERSION); //TODO: Uncomment below line as we dont to save data in sd card
            //super(context, DATABASE, null, VERSION); //to save db in internal memory (for play store, use this
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARMS_TABLE_NAME + " ("
                    + AlarmsTableHandler._ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + AlarmsTableHandler.ALARM_ID_START_TIME + " INTEGER,"
                    + AlarmsTableHandler.ALARM_ID_END_TIME + " INTEGER,"
                    + AlarmsTableHandler.START_HOUR + " VARCHAR(255),"
                    + AlarmsTableHandler.START_MINUTE + " VARCHAR(255),"
                    + AlarmsTableHandler.END_HOUR + " VARCHAR(255),"
                    + AlarmsTableHandler.END_MINUTE + " VARCHAR(255),"
                    + AlarmsTableHandler.START_TIME_STATUS + " VARCHAR(255),"
                    + AlarmsTableHandler.END_TIME_STATUS + " VARCHAR(255),"
                    + AlarmsTableHandler.IS_ACTIVE + " BOOLEAN,"
                    + AlarmsTableHandler.IS_FOR_ALL_DAYS + " BOOLEAN,"
                    + AlarmsTableHandler.IS_FOR_MONDAY + " BOOLEAN,"
                    + AlarmsTableHandler.IS_FOR_TUESDAY + " BOOLEAN,"
                    + AlarmsTableHandler.IS_FOR_WEDNESDAY + " BOOLEAN,"
                    + AlarmsTableHandler.IS_FOR_THURSDAY + " BOOLEAN,"
                    + AlarmsTableHandler.IS_FOR_FRIDAY + " BOOLEAN,"
                    + AlarmsTableHandler.IS_FOR_SATURDAY + " BOOLEAN,"
                    + AlarmsTableHandler.IS_FOR_SUNDAY + " BOOLEAN" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE_NAME);
            onCreate(db);
        }
    }

        @Override
    public boolean onCreate() {
        databaseOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String groupBy = null;
        String having = null;
        Cursor cursor;
        SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case ALARMS:
                sqLiteQueryBuilder.setTables(ALARMS_TABLE_NAME);
                if (projection == null)
                    sqLiteQueryBuilder.setProjectionMap(alarmsProjectionMap);
                break;
        }

        cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, groupBy,
                having, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri arg0) {
        String type;
        switch (uriMatcher.match(arg0)) {
            case ALARMS:
                type = AlarmsTableHandler.CONTENT_TYPE;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + arg0);
        }
        return type;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        Uri rowUri = null;
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        long rowID;

        switch (uriMatcher.match(arg0)) {
            case ALARMS:
                rowID = db.insert(ALARMS_TABLE_NAME, null, arg1);
                if (rowID > 0)
                    rowUri = ContentUris.withAppendedId(
                            AlarmsTableHandler.CONTENT_URI, rowID);
                break;
        }
        return rowUri;
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(arg0)) {
            case ALARMS:
                count = db.delete(ALARMS_TABLE_NAME, arg1, arg2);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI  " + arg0);
        }
        getContext().getContentResolver().notifyChange(arg0, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case ALARMS:
                count = db.update(ALARMS_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI  " + uri);
        }
        return count;
    }
}
