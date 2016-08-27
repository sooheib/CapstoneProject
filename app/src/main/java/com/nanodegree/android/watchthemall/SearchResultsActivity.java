package com.nanodegree.android.watchthemall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nanodegree.android.watchthemall.util.Utility;

public class SearchResultsActivity extends AppCompatActivity
        implements ShowsFragment.ShowCallback, ShowSeasonsFragment.EpisodeCallback {

    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;
    private String mSearchKeywords = "";
    private String mSelectedCollection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);

        if (getIntent().getExtras()!=null) {
            mSearchKeywords = getIntent().getExtras().getString(Utility.SEARCH_KEYWORDS_EXTRA_KEY);
            mSelectedCollection = getIntent().getExtras().getString(Utility.COLLECTION_EXTRA_KEY);
        }

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = Boolean.TRUE;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                ShowDetailFragment detailFragment = new ShowDetailFragment();
                Bundle arguments = new Bundle();
                detailFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, detailFragment,
                                DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = Boolean.FALSE;
        }

        if (getSupportActionBar()!=null) {
            String title = getString(R.string.title_activity_search_results) + ". ";
            if ((mSelectedCollection!=null)&&(!mSelectedCollection.isEmpty())) {
                if (mSelectedCollection.equals(getString(R.string.navigation_drawer_watching_series))) {
                    title = title + getString(R.string.title_watching_series);
                } else if (mSelectedCollection.equals(getString(R.string.navigation_drawer_watched_series))) {
                    title = title + getString(R.string.title_watched_series);
                } else if (mSelectedCollection.equals(getString(R.string.navigation_drawer_watchlist_series))) {
                    title = title + getString(R.string.title_watchlist_series);
                }
            } else {
                title = title + getString(R.string.title_search_results);
            }
            getSupportActionBar().setTitle(title);
        }

        ShowsFragment showsFragment = ((ShowsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.shows_container));
        showsFragment.setUseTwoPaneLayout(mTwoPane);
        showsFragment.setSearchKeywords(mSearchKeywords);
        showsFragment.setSelectedCollection(mSelectedCollection);
    }

    @Override
    public void onShowSelected(Uri uri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (uri==null) {
                WtaDetailFragment detailFragment =
                        ((WtaDetailFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.detail_container));
                detailFragment.hideDetailLayout();
            }
            Bundle args = new Bundle();
            args.putParcelable(Utility.DETAIL_URI_EXTRA_KEY, uri);
            ShowDetailFragment fragment = new ShowDetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, ShowDetailActivity.class)
                    .setData(uri);
            startActivity(intent);
        }
    }

    @Override
    public void onEpisodeSelected(Uri uri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (uri==null) {
                WtaDetailFragment detailFragment =
                        ((WtaDetailFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.detail_container));
                detailFragment.hideDetailLayout();
            }
            Bundle args = new Bundle();
            args.putParcelable(Utility.DETAIL_URI_EXTRA_KEY, uri);
            EpisodeDetailFragment fragment = new EpisodeDetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent =
                    new Intent(this, EpisodeDetailActivity.class)
                            .setData(uri);
            startActivity(intent);
        }
    }
}
