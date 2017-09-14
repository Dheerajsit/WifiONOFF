package com.android.eximius.alarmondataoff.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.android.eximius.alarmondataoff.R;

import java.util.Calendar;

/**
 * TimePickerFragment class
 */
public class TimePickerFragment extends DialogFragment
                         implements TimePickerDialog.OnTimeSetListener{

    OnTimeEnteredListener mCallback;
    String mTitle;

    // Container Activity must implement this interface.
    // After the time is set in the first dialog, we need to show 2nd time picker dialog. Hence,
    // via onTimeEntered(), parent activity will be informed that the time has been set.
    public interface OnTimeEnteredListener {
        // isTimeSet to indicate time has been set
        // isStartTime to indicate which of the two (start/end) time was set
        // hour to indicate hour of the day selected
        // minute to indicate minute value selected
        public void onTimeEntered(boolean isTimeSet, boolean isStartTime, int hour, int minute);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnTimeEnteredListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTimeEnteredListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Getting the current time of system
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Setting the current hour and minute value in Time Picker
        // Last argument is False to show am/pm values also
        TimePickerDialog tDialog = new TimePickerDialog(getActivity(), this, hour, minute, false);
        mTitle = getArguments().getString(getResources().getString(R.string.title));
        tDialog.setTitle(mTitle);
        return tDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Use the following if statement, else the block gets executed twice
        if (view.isShown()) {
            // Send the event to the host activity
            if(mTitle.equals(getResources().getString(R.string.turn_off_internet)))
                mCallback.onTimeEntered(true, true, hourOfDay, minute);
            else
                mCallback.onTimeEntered(true, false, hourOfDay, minute);
        }
    }
}
