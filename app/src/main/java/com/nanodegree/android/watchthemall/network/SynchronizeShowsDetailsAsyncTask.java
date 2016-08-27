package com.nanodegree.android.watchthemall.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nanodegree.android.watchthemall.api.trakt.TraktService;
import com.nanodegree.android.watchthemall.util.Utility;

import java.util.List;

/**
 * An Async Task in order to retrieve Trakt shows additional data
 */
public class SynchronizeShowsDetailsAsyncTask extends AsyncTask<List<Integer>, Void, Void> {

    private final String LOG_TAG = SynchronizeShowsDetailsAsyncTask.class.getSimpleName();

    private Context mContext;

    public SynchronizeShowsDetailsAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(List<Integer>... params) {

        Log.d(LOG_TAG, "AsyncTask started");

        List<Integer> showIds = params[0];

        if (showIds==null) {
            return null;
        }

        try {
            TraktService traktService = Utility.getTraktService();

            for (Integer id : showIds) {
                Utility.synchronizeShowPeople(mContext, LOG_TAG, traktService, id);
                Utility.synchronizeShowSeasons(mContext, LOG_TAG, traktService, id);
                Utility.synchronizeShowComments(mContext, LOG_TAG, traktService, id);
            }

            Log.d(LOG_TAG, "AsyncTask correctly ended");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Do nothing
    }
}
