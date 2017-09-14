package com.android.eximius.alarmondataoff.fragments;

import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.eximius.alarmondataoff.R;
import com.android.eximius.alarmondataoff.adapters.AlarmsAdapter;
import com.android.eximius.alarmondataoff.db.AlarmsTableHandler;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 *
 */
public class AlarmListItemFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // Identifies a particular Loader being used in this component
    private static final int LOADER_ID = 0;
    AlarmsAdapter alarmsAdapter;

    public AlarmListItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.fragment_list_item_alarm, container, false));
    }

    public void refreshAlarmsListExplicitly(){
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void setEmptyText(CharSequence text) {
        super.setEmptyText(text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Starting loader to fetch data from DB
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        return new CursorLoader(getActivity(), AlarmsTableHandler.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        //Setting the cursor to the list
        if(cursor != null) {
            alarmsAdapter = new AlarmsAdapter(getActivity(), cursor);
            setListAdapter(alarmsAdapter);
            alarmsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        if(alarmsAdapter != null)
            alarmsAdapter.changeCursor(null);
    }

}
