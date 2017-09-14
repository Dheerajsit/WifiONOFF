package com.android.eximius.alarmondataoff.model;

/**
 * Created by Richa on 06/29/2017.
 */
public class AlarmDetail {
    private int id; //Auto-generated field set in DB. Will only be read (and not write) for deletion/updation
    private int alarmIdStartTime; //Unique id of alarm so that alarm can be cancelled when the whole entry is deleted from table
    private int alarmIdEndTime; //Unique id of alarm so that alarm can be cancelled when the whole entry is deleted from table
    private String startHour;
    private String startMinute;
    private String endHour;
    private String endMinute;
    private String startTimeStatus;
    private String endTimeStatus;
    private boolean isActive;
    private boolean isForAllDays;
    private boolean isForMonday;
    private boolean isForTuesday;
    private boolean isForWednesday;
    private boolean isForThursday;
    private boolean isForFriday;
    private boolean isForSaturday;
    private boolean isForSunday;

    public AlarmDetail(int _id, int aAlarmIdStartTime, int aAlarmIdEndTime,
                       String aStartHour, String aStartMinute,
                       String aEndHour, String aEndMinute,
                       String aStartTimeStatus, String aEndTimeStatus,
                       boolean isActive, boolean isForAllDays, boolean isForMonday,
                       boolean isForTuesday, boolean isForWednesday, boolean isForThursday,
                       boolean isForFriday, boolean isForSaturday, boolean isForSunday){
        this.id = _id;
        this.alarmIdStartTime = aAlarmIdStartTime;
        this.alarmIdEndTime = aAlarmIdEndTime;
        this.startHour = aStartHour;
        this.startMinute = aStartMinute;
        this.endHour = aEndHour;
        this.endMinute = aEndMinute;
        this.startTimeStatus = aStartTimeStatus;
        this.endTimeStatus = aEndTimeStatus;
        this.isActive = isActive;
        this.isForAllDays = isForAllDays;
        this.isForMonday = isForMonday;
        this.isForTuesday = isForTuesday;
        this.isForWednesday = isForWednesday;
        this.isForThursday = isForThursday;
        this.isForFriday = isForFriday;
        this.isForSaturday = isForSaturday;
        this.isForSunday = isForSunday;
    }

    public int getId() {
        return id;
    }

    public int getAlarmIdStartTime() {
        return alarmIdStartTime;
    }

    public void setAlarmIdStartTime(int alarmIdStartTime) {
        this.alarmIdStartTime = alarmIdStartTime;
    }

    public int getAlarmIdEndTime() {
        return alarmIdEndTime;
    }

    public void setAlarmIdEndTime(int alarmIdEndTime) {
        this.alarmIdEndTime = alarmIdEndTime;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    public String getStartTimeStatus() {
        return startTimeStatus;
    }

    public void setStartTimeStatus(String startTimeStatus) {
        this.startTimeStatus = startTimeStatus;
    }

    public String getEndTimeStatus() {
        return endTimeStatus;
    }

    public void setEndTimeStatus(String endTimeStatus) {
        this.endTimeStatus = endTimeStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isForAllDays() {
        return isForAllDays;
    }

    public void setForAllDays(boolean isForAllDays) {
        this.isForAllDays = isForAllDays;
    }

    public boolean isForMonday() {
        return isForMonday;
    }

    public void setForMonday(boolean isForMonday) {
        this.isForMonday = isForMonday;
    }

    public boolean isForTuesday() {
        return isForTuesday;
    }

    public void setForTuesday(boolean isForTuesday) {
        this.isForTuesday = isForTuesday;
    }

    public boolean isForWednesday() {
        return isForWednesday;
    }

    public void setForWednesday(boolean isForWednesday) {
        this.isForWednesday = isForWednesday;
    }

    public boolean isForThursday() {
        return isForThursday;
    }

    public void setForThursday(boolean isForThursday) {
        this.isForThursday = isForThursday;
    }

    public boolean isForFriday() {
        return isForFriday;
    }

    public void setForFriday(boolean isForFriday) {
        this.isForFriday = isForFriday;
    }

    public boolean isForSaturday() {
        return isForSaturday;
    }

    public void setForSaturday(boolean isForSaturday) {
        this.isForSaturday = isForSaturday;
    }

    public boolean isForSunday() {
        return isForSunday;
    }

    public void setForSunday(boolean isForSunday) {
        this.isForSunday = isForSunday;
    }
}
