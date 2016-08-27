package com.nanodegree.android.watchthemall;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nanodegree.android.watchthemall.adapters.ShowAdapter;
import com.nanodegree.android.watchthemall.data.WtaContract;
import com.nanodegree.android.watchthemall.data.WtaProvider;
import com.nanodegree.android.watchthemall.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShowsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String SELECTED_POSITION_KEY = "SELECTED_POSITION";
    private static final int SHOW_LOADER_ID = 0;

    private static final String[] SHOW_COLUMNS = {
            WtaContract.ShowEntry._ID,
            WtaContract.ShowEntry.COLUMN_POSTER_PATH,
            WtaContract.ShowEntry.COLUMN_TITLE
    };
    // These indices are tied to SHOW_COLUMNS. If SHOW_COLUMNS changes, these must change too.
    public static final int COL_ID = 0;
    public static final int COL_POSTER_PATH = 1;
    public static final int COL_TITLE = 2;

    private ShowAdapter mShowAdapter;
    private boolean mUseTwoPaneLayout;
    private String mSearchKeywords;
    private String mSelectedCollection;
    private int mSelectedPosition;
    private Unbinder mButterKnifeUnbinder;

    @BindView(R.id.gridview_shows)
    GridView mShowPostersGridView;
    @BindView(R.id.no_show_data_imageview)
    ImageView mNoDataRetrieved;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    public ShowsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSelectedPosition = ListView.INVALID_POSITION;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SELECTED_POSITION_KEY)) {
                mSelectedPosition = savedInstanceState.getInt(SELECTED_POSITION_KEY);
            }
        }

        getLoaderManager().initLoader(SHOW_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        mButterKnifeUnbinder = ButterKnife.bind(this, rootView);

        mShowAdapter = new ShowAdapter(getActivity(), null, 0);
        mShowPostersGridView.setAdapter(mShowAdapter);
        mShowPostersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                if (cursor != null) {
                    Uri uri = null;
                    if (cursor.getCount()>0) {
                        uri = WtaContract.ShowEntry
                                .buildShowUri(cursor.getInt(COL_ID));
                    }
                    ((ShowCallback) getActivity())
                            .onShowSelected(uri);
                }
                mSelectedPosition = i;
            }
        });

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent,
                R.color.colorPrimaryLight, R.color.colorPrimaryLighter, R.color.colorPrimaryDark);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSelectedPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_POSITION_KEY, mSelectedPosition);
        }
    }

    @Override
    public void onRefresh() {
        Utility.updateShowsSearch(getActivity(), mSearchKeywords, this, new int[]{SHOW_LOADER_ID}, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mButterKnifeUnbinder!=null) {
            mButterKnifeUnbinder.unbind();
        }
    }

    public void setUseTwoPaneLayout(boolean useTwoPaneLayout) {
        mUseTwoPaneLayout = useTwoPaneLayout;
    }

    public void setSearchKeywords(String keywords) {
        mSearchKeywords = keywords;
    }

    public void setSelectedCollection(String selectedCollection) {
        mSelectedCollection = selectedCollection;
        getLoaderManager().restartLoader(SHOW_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri showsUri = WtaContract.ShowEntry.CONTENT_URI;
        String selection = WtaProvider.sLastSearchedShowSelection;
        String[] selectionArgs = new String[]{"1"};
        String sortOrder = WtaContract.ShowEntry.COLUMN_SEARCH_SCORE + " DESC";
        if ((mSelectedCollection!=null)&&(!mSelectedCollection.isEmpty())) {
            String currentQueryPreference = Utility.getCurrentQueryPreference(getActivity());
            if (currentQueryPreference.equals(getString(R.string.pref_sort_order_rating))) {
                sortOrder = WtaContract.ShowEntry.COLUMN_RATING + " DESC";
            } else {
                // Title sort order is the default user preference
                sortOrder = WtaContract.ShowEntry.COLUMN_TITLE;
            }
            if (mSelectedCollection.equals(getString(R.string.navigation_drawer_watching_series))) {
                selection = WtaProvider.sWatchingShowSelection;
            } else if (mSelectedCollection.equals(getString(R.string.navigation_drawer_watched_series))) {
                selection = WtaProvider.sWatchedShowSelection;
            } else if (mSelectedCollection.equals(getString(R.string.navigation_drawer_watchlist_series))) {
                selection = WtaProvider.sWatchlistShowSelection;
            }
        }

        return new CursorLoader(getActivity(), showsUri, SHOW_COLUMNS,
                selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mShowAdapter.swapCursor(data);
        if (data.getCount()>0) {
            mShowPostersGridView.setVisibility(View.VISIBLE);
            mNoDataRetrieved.setVisibility(View.INVISIBLE);
            int position = mSelectedPosition;
            if (position== ListView.INVALID_POSITION) {
                position = 0;
            }
            if (mUseTwoPaneLayout) {
                CustomRunnable customRunnable = new CustomRunnable(position);
                mShowPostersGridView.postDelayed(customRunnable, 0);
            }
            mShowPostersGridView.smoothScrollToPosition(position);
        } else {
            //Shows data cannot be retrieved from DB
            mShowPostersGridView.setVisibility(View.INVISIBLE);
            mNoDataRetrieved.setVisibility(View.VISIBLE);
            if (mUseTwoPaneLayout) {
                CustomRunnable customRunnable = new CustomRunnable(0);
                mShowPostersGridView.postDelayed(customRunnable, 0);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mShowAdapter.swapCursor(null);
    }

    public void updateSwipeRefreshLayout(boolean isRefreshing) {
        mSwipeLayout.setRefreshing(isRefreshing);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface ShowCallback {

        void onShowSelected(Uri uri);
    }

    private class CustomRunnable implements Runnable {

        private int position;

        public CustomRunnable(int position) {
            this.position = position;
        }

        @Override
        public void run() {
            mShowPostersGridView.setSoundEffectsEnabled(Boolean.FALSE);
            mShowPostersGridView.performItemClick(mShowPostersGridView.getChildAt(position),
                    position, mShowPostersGridView.getItemIdAtPosition(position));
            mShowPostersGridView.setSoundEffectsEnabled(Boolean.TRUE);
        }
    }
}
