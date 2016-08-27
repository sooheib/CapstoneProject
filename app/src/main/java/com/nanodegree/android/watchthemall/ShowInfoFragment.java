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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nanodegree.android.watchthemall.adapters.PeopleAdapter;
import com.nanodegree.android.watchthemall.components.NonScrollListView;
import com.nanodegree.android.watchthemall.data.WtaContract;
import com.nanodegree.android.watchthemall.util.Utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShowInfoFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_SHOW_LOADER_ID = 1;
    private static final int DETAIL_SHOW_GENRES_LOADER_ID = 2;
    private static final int DETAIL_SHOW_PEOPLE_LOADER_ID = 3;

    private static final String[] SHOW_COLUMNS = {
            WtaContract.ShowEntry._ID,
            WtaContract.ShowEntry.COLUMN_OVERVIEW,
            WtaContract.ShowEntry.COLUMN_POSTER_PATH,
            WtaContract.ShowEntry.COLUMN_STATUS,
            WtaContract.ShowEntry.COLUMN_FIRST_AIRED,
            WtaContract.ShowEntry.COLUMN_AIR_DAY,
            WtaContract.ShowEntry.COLUMN_RUNTIME,
            WtaContract.ShowEntry.COLUMN_NETWORK,
            WtaContract.ShowEntry.COLUMN_RATING,
            WtaContract.ShowEntry.COLUMN_VOTE_COUNT
    };
    // These indices are tied to SHOWS_COLUMNS. If SHOWS_COLUMNS changes, these must change too.
    public static final int COL_ID = 0;
    public static final int COL_OVERVIEW = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_STATUS = 3;
    public static final int COL_FIRST_AIRED = 4;
    public static final int COL_AIR_DAY = 5;
    public static final int COL_RUNTIME = 6;
    public static final int COL_NETWORK = 7;
    public static final int COL_RATING = 8;
    public static final int COL_VOTE_COUNT = 9;

    private static final String[] GENRES_COLUMNS = {
            WtaContract.ShowEntry.GENRE_RELATION_TABLE_NAME + "." + WtaContract.ShowEntry.COLUMN_SHOW_ID,
            WtaContract.ShowEntry.GENRE_RELATION_TABLE_NAME + "." + WtaContract.ShowEntry.COLUMN_GENRE_ID,
            WtaContract.GenreEntry.TABLE_NAME + "." + WtaContract.GenreEntry.COLUMN_NAME
    };
    // These indices are tied to GENRES_COLUMNS. If GENRES_COLUMNS changes, these must change too.
    public static final int COL_SHOW_ID = 0;
    public static final int COL_GENRE_ID = 1;
    public static final int COL_GENRE_NAME = 2;

    private static final String[] PEOPLE_COLUMNS = {
            WtaContract.ShowEntry.PERSON_RELATION_TABLE_NAME + "." + WtaContract.ShowEntry.COLUMN_SHOW_ID,
            WtaContract.ShowEntry.PERSON_RELATION_TABLE_NAME + "." + WtaContract.ShowEntry.COLUMN_PERSON_ID,
            WtaContract.ShowEntry.PERSON_RELATION_TABLE_NAME + "." + WtaContract.ShowEntry.COLUMN_CHARACTER,
            WtaContract.PersonEntry.TABLE_NAME + "." + WtaContract.PersonEntry.COLUMN_NAME,
            WtaContract.PersonEntry._ID
    };
    // These indices are tied to GENRES_COLUMNS. If GENRES_COLUMNS changes, these must change too.
    // public static final int COL_SHOW_ID = 0; // Column index already defined. Using the same
    public static final int COL_PERSON_ID = 1;
    public static final int COL_CHARACTER = 2;
    public static final int COL_PERSON_NAME = 3;
    public static final int COL_PERSON_ENTRY_ID = 4;

    @BindView(R.id.showPoster)
    ImageView mShowPoster;
    @BindView(R.id.showOverview)
    WebView mShowOverview;
    @BindView(R.id.showRating)
    TextView mShowRating;
    @BindView(R.id.showVoteCount)
    TextView mShowVoteCount;
    @BindView(R.id.showGenres)
    TextView mShowGenres;
    @BindView(R.id.showCastList)
    NonScrollListView mShowCastListView;
    @BindView(R.id.showFirstAired)
    TextView mShowFirstAired;
    @BindView(R.id.showStatus)
    TextView mShowStatus;
    @BindView(R.id.showAirsDay)
    TextView mShowAirsDay;
    @BindView(R.id.showNetworkCountry)
    TextView mShowNetworkCountry;
    @BindView(R.id.showRuntime)
    TextView mShowRuntime;

    private Unbinder mButterKnifeUnbinder;
    private Uri mUri;
    private PeopleAdapter mPeopleAdapter;

    public ShowInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(Utility.DETAIL_URI_EXTRA_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_show_info, container, false);
        mButterKnifeUnbinder = ButterKnife.bind(this, rootView);

        getLoaderManager().initLoader(DETAIL_SHOW_LOADER_ID, null, this);
        getLoaderManager().initLoader(DETAIL_SHOW_GENRES_LOADER_ID, null, this);
        getLoaderManager().initLoader(DETAIL_SHOW_PEOPLE_LOADER_ID, null, this);

        mPeopleAdapter = new PeopleAdapter(getActivity(), null, 0);
        mShowCastListView.setAdapter(mPeopleAdapter);

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
            switch (id) {
                case DETAIL_SHOW_LOADER_ID: {
                    return new CursorLoader(getActivity(),
                            mUri, SHOW_COLUMNS, null, null, null);
                }
                case DETAIL_SHOW_GENRES_LOADER_ID: {
                    return new CursorLoader(getActivity(),
                            WtaContract.ShowEntry
                                    .buildShowGenreUri(Long.valueOf(WtaContract.ShowEntry
                                            .getShowIdFromUri(mUri))), GENRES_COLUMNS,
                            null, null, WtaContract.ShowEntry.COLUMN_GENRE_ID);
                }
                case DETAIL_SHOW_PEOPLE_LOADER_ID: {
                    return new CursorLoader(getActivity(),
                            WtaContract.ShowEntry
                                    .buildShowPersonUri(Long.valueOf(WtaContract.ShowEntry
                                            .getShowIdFromUri(mUri))), PEOPLE_COLUMNS,
                            null, null, WtaContract.ShowEntry.COLUMN_CHARACTER);
                }
            }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        long loaderId = loader.getId();
        if (loaderId == DETAIL_SHOW_LOADER_ID) {
            onDetailShowLoadFinished(data);
        } else if (loaderId == DETAIL_SHOW_GENRES_LOADER_ID) {
            onDetailShowGenresLoadFinished(data);
        } else if (loaderId == DETAIL_SHOW_PEOPLE_LOADER_ID) {
            onDetailShowPeopleLoadFinished(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        long loaderId = loader.getId();
        if (loaderId == DETAIL_SHOW_PEOPLE_LOADER_ID) {
            mPeopleAdapter.swapCursor(null);
        }
        //else: Do nothing
    }

    private void onDetailShowLoadFinished(Cursor data) {

        String posterPath = data.getString(COL_POSTER_PATH);
        if (posterPath!=null) {
            Glide.with(getActivity()).load(posterPath)
                    .error(getActivity().getDrawable(R.drawable.no_show_poster))
                    .crossFade().into(mShowPoster);
        } else {
            Glide.with(getActivity()).load(R.drawable.no_show_poster)
                    .crossFade().into(mShowPoster);
        }
        Double rating = data.getDouble(COL_RATING);
        mShowRating.setText(new DecimalFormat("0.##").format(rating));
        Integer voteCount = data.getInt(COL_VOTE_COUNT);
        mShowVoteCount.setText("(" + voteCount + " " +
                getActivity().getString(R.string.votes_label) + ")");
        if (data.isNull(COL_FIRST_AIRED)) {
            mShowFirstAired.setText(getActivity().getString(R.string.unknown_date));
        } else {
            Long firstAiredDate = data.getLong(COL_FIRST_AIRED);
            SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_format));
            mShowFirstAired.setText(sdf.format(new Date(firstAiredDate)));
        }
        mShowStatus.setText(data.getString(COL_STATUS));
        mShowAirsDay.setText(data.getString(COL_AIR_DAY));
        String showNetworkCountry = data.getString(COL_NETWORK);
        mShowNetworkCountry.setText(showNetworkCountry);
        String showRuntime = data.getString(COL_RUNTIME);
        if ((showRuntime!=null) && (!showRuntime.isEmpty())) {
            mShowRuntime.setText(showRuntime + " " + getActivity().getString(R.string.minute_label));
        } else {
            mShowRuntime.setText(getActivity().getString(R.string.unknown_runtime));
        }
        mShowOverview.loadData(String.format(Utility.HTML_TEXT_FORMAT,
                data.getString(COL_OVERVIEW)), Utility.HTML_TEXT_MIME_TYPE,
                Utility.HTML_TEXT_ENCODING);
        mShowOverview.setBackgroundColor(Color.TRANSPARENT);
    }

    private void onDetailShowGenresLoadFinished(Cursor data) {

        if (data.getCount()==0) {
            mShowGenres.setText(getActivity().getString(R.string.empty_genre_list));
            return;
        }

        StringBuffer genres = new StringBuffer();
        String genre;
        do {
            genre = data.getString(COL_GENRE_NAME);
            if ((genre==null) || (genre.isEmpty())) {
                genre = Utility.capitalizeAndFormatDelimiters(data.getString(COL_GENRE_ID),
                        new char[]{' ', '-', '.'});
            }
            genres.append(genre).append(", ");
        } while (data.moveToNext());

        mShowGenres.setText(genres.substring(0, genres.length()-2));
    }

    private void onDetailShowPeopleLoadFinished(Cursor data) {
        mPeopleAdapter.swapCursor(data);
    }
}
