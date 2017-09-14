package com.android.eximius.alarmondataoff.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.eximius.alarmondataoff.SettingsActivity;
import com.android.eximius.alarmondataoff.fragments.AlarmListItemFragment;
import com.android.eximius.alarmondataoff.fragments.TimePickerFragment;
import com.android.eximius.alarmondataoff.R;
import com.android.eximius.alarmondataoff.utils.AlarmOnDataOffUtils;
import com.android.eximius.alarmondataoff.utils.DBUtils;



public class HomeActivity extends ActionBarActivity implements TimePickerFragment.OnTimeEnteredListener{
    public AlarmListItemFragment mListFragment;
    public static final String MyPREFERENCES = "MyPrefs";

    public void HomeActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Getting fragment's reference so that via it, loader can be invoked again after insert/delete/update
        mListFragment = (AlarmListItemFragment) getSupportFragmentManager().findFragmentById(R.id.lstAlarms);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.addAlarm) {
            showTimePickerDialog(this.getResources().getString(R.string.turn_off_internet));
            return true;
        }
        else {
            if (id == R.id.action_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;
            } else if (id == R.id.action_share_app) {
                shareAppBtnClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTimePickerDialog(String aTitle){
        DialogFragment timeDialog = new TimePickerFragment();

        //To pass the customized title to the dialog, using Bundle class
        Bundle bundleObj = new Bundle();
        bundleObj.putString(getResources().getString(R.string.title), aTitle);
        timeDialog.setArguments(bundleObj);

        timeDialog.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeEntered(boolean isTimeSet, boolean isStartTime, int hour, int minute) {
        Log.e("TimeEntered:StartTime:", String.valueOf(isTimeSet));
        //In case the Start time was set then only show the 2nd (End time) time picker. Else do nothing
        if(isStartTime) {
            showTimePickerDialog(this.getResources().getString(R.string.turn_on_internet));
            saveSelectedTime(true, AlarmOnDataOffUtils.fetchTwoDigitTimeValue(String.valueOf(hour)),
                    AlarmOnDataOffUtils.fetchTwoDigitTimeValue(String.valueOf(minute)));
        }
        else {
            saveSelectedTime(false, AlarmOnDataOffUtils.fetchTwoDigitTimeValue(String.valueOf(hour)),
                    AlarmOnDataOffUtils.fetchTwoDigitTimeValue(String.valueOf(minute)));

            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            String sH = sharedpreferences.getString("StartHour", "");
            String sM = sharedpreferences.getString("StartMinute", "");
            String eH = sharedpreferences.getString("EndHour", "");
            String eM = sharedpreferences.getString("EndMinute", "");

            //Fetching am/pm values based on "hour" value
            String sStatus = AlarmOnDataOffUtils.fetchDayNightStatus(this, sH);
            String eStatus = AlarmOnDataOffUtils.fetchDayNightStatus(this, eH);

            //To set multiple alarms, we need to pass unique id
            final int _id1 = AlarmOnDataOffUtils.getUniquePendingId();
            AlarmOnDataOffUtils.setAlarm(this, _id1, false, Integer.valueOf(sH), Integer.valueOf(sM));
            Log.e("TimeEntered() called.", "Alarm 1 is set");
            Log.e("Alarm 1 ID:", String.valueOf(_id1));

            final int _id2 = AlarmOnDataOffUtils.getUniquePendingId();
            AlarmOnDataOffUtils.setAlarm(this, _id2, true, Integer.valueOf(eH), Integer.valueOf(eM));
            Log.e("TimeEntered() called.", "Alarm 2 is set");
            Log.e("Alarm 2 ID:", String.valueOf(_id2));

            //Saving alarm detail in table
            DBUtils.addRecordInAlarmsTable(this, _id1, _id2, sH, sM, eH, eM, sStatus, eStatus);
            if(mListFragment != null)
                mListFragment.refreshAlarmsListExplicitly();
        }
    }

    private void saveSelectedTime(boolean isStartTime, String hour, String minute)
    {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(isStartTime) {
            editor.putString("StartHour", hour);
            editor.putString("StartMinute", minute);
        }
        else{
            editor.putString("EndHour", hour);
            editor.putString("EndMinute", minute);
        }
        editor.commit();
    }

    private void shareAppBtnClicked(){
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra
                    (android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.share_text));
            sharingIntent.putExtra
                    (android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
            startActivity(Intent.createChooser
                    (sharingIntent, getResources().getString(R.string.share_chooser_heading)));
        }catch(Exception e){}
    }
}
