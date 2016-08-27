package com.nanodegree.android.watchthemall;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nanodegree.android.watchthemall.data.WtaContract;
import com.nanodegree.android.watchthemall.util.Utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EpisodeInfoFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_EPISODE_LOADER_ID = 7;

    private static final String[] EPISODE_COLUMNS = {
            WtaContract.EpisodeEntry._ID,
            WtaContract.EpisodeEntry.COLUMN_OVERVIEW,
            WtaContract.EpisodeEntry.COLUMN_FIRST_AIRED,
            WtaContract.EpisodeEntry.COLUMN_RATING,
            WtaContract.EpisodeEntry.COLUMN_VOTE_COUNT
    };
    // These indices are tied to EPISODE_COLUMNS. If EPISODE_COLUMNS changes, these must change too.
    public static final int COL_ID = 0;
    public static final int COL_OVERVIEW = 1;
    public static final int COL_FIRST_AIRED = 2;
    public static final int COL_RATING = 3;
    public static final int COL_VOTE_COUNT = 4;

    @BindView(R.id.episodeOverview)
    WebView mEpisodeOverview;
    @BindView(R.id.episodeRating)
    TextView mEpisodeRating;
    @BindView(R.id.episodeVoteCount)
    TextView mEpisodeVoteCount;
    @BindView(R.id.episodeFirstAired)
    TextView mEpisodeFirstAired;

    private Unbinder mButterKnifeUnbinder;
    private Uri mUri;

    public EpisodeInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(Utility.DETAIL_URI_EXTRA_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_episode_info, container, false);
        mButterKnifeUnbinder = ButterKnife.bind(this, rootView);

        getLoaderManager().initLoader(DETAIL_EPISODE_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mButterKnifeUnbinder!=null) {
            mButterKnifeUnbinder.unbind();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            return new CursorLoader(getActivity(),
                    mUri, EPISODE_COLUMNS, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        onDetailEpisodeLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Do nothing
    }

    private void onDetailEpisodeLoadFinished(Cursor data) {

        Double rating = data.getDouble(COL_RATING);
        mEpisodeRating.setText(new DecimalFormat("0.##").format(rating));
        Integer voteCount = data.getInt(COL_VOTE_COUNT);
        mEpisodeVoteCount.setText("(" + voteCount + " " +
                getActivity().getString(R.string.votes_label) + ")");
        if (data.isNull(COL_FIRST_AIRED)) {
            mEpisodeFirstAired.setText(getActivity().getString(R.string.unknown_date));
        } else {
            Long firstAiredDate = data.getLong(COL_FIRST_AIRED);
            SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_format));
            mEpisodeFirstAired.setText(sdf.format(new Date(firstAiredDate)));
        }
        mEpisodeOverview.loadData(String.format(Utility.HTML_TEXT_FORMAT,
                data.getString(COL_OVERVIEW)), Utility.HTML_TEXT_MIME_TYPE,
                Utility.HTML_TEXT_ENCODING);
        mEpisodeOverview.setBackgroundColor(Color.TRANSPARENT);
    }
}
