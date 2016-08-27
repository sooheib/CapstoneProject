package com.nanodegree.android.watchthemall.network;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.nanodegree.android.watchthemall.R;
import com.nanodegree.android.watchthemall.SearchResultsActivity;
import com.nanodegree.android.watchthemall.ShowsFragment;
import com.nanodegree.android.watchthemall.api.trakt.TraktService;
import com.nanodegree.android.watchthemall.data.WtaContract;
import com.nanodegree.android.watchthemall.util.Utility;

import java.util.List;

/**
 * An Async Task in order to retrieve Trakt shows data according to provided keywords search results
 */
public class SearchByKeywordsAsyncTask extends AsyncTask<Object, Void, List<Integer>> {

    private final String LOG_TAG = SearchByKeywordsAsyncTask.class.getSimpleName();

    private Context mContext;
    private Boolean mNewActivity;
    private String mSearchText;

    public SearchByKeywordsAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected List<Integer> doInBackground(Object... params) {

        Log.d(LOG_TAG, "AsyncTask started");

        List<Integer> result = null;
        mNewActivity = (Boolean) params[0];
        mSearchText = (String) params[1];
        Integer year = null;
        if (params.length>2) {
            year = (Integer) params[2];
        }
        if ((mSearchText==null) || (mSearchText.isEmpty())) {
            return null;
        }

        try {
            TraktService traktService = Utility.getTraktService();

            // First delete last search result marks and scores
            ContentValues updateValues = new ContentValues();
            updateValues.put(WtaContract.ShowEntry.COLUMN_LAST_SEARCH_RESULT, 0);
            updateValues.put(WtaContract.ShowEntry.COLUMN_SEARCH_SCORE, 0.0);
            mContext.getContentResolver().update(WtaContract.ShowEntry.CONTENT_URI, updateValues,
                    null, null);

            result = Utility.synchronizeShowsByKeywordsData(mContext, LOG_TAG, traktService, mSearchText, year);

            Log.d(LOG_TAG, "AsyncTask correctly ended");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage(), e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Integer> result) {
        if (mNewActivity) {
            Intent intent = new Intent(mContext, SearchResultsActivity.class);
            intent.putExtra(Utility.SEARCH_KEYWORDS_EXTRA_KEY, mSearchText);
            mContext.startActivity(intent);

            SynchronizeShowsDetailsAsyncTask detailsTask = new SynchronizeShowsDetailsAsyncTask(mContext);
            detailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, result);
        } else if (mContext instanceof SearchResultsActivity) {
            ShowsFragment showsFragment = (ShowsFragment)((SearchResultsActivity)mContext).getSupportFragmentManager()
                    .findFragmentById(R.id.shows_container);
            showsFragment.updateSwipeRefreshLayout(Boolean.FALSE);
        }
    }
}
