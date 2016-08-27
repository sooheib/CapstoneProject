package com.nanodegree.android.watchthemall;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.nanodegree.android.watchthemall.adapters.ViewPagerAdapter;
import com.nanodegree.android.watchthemall.data.WtaContract;
import com.nanodegree.android.watchthemall.data.WtaProvider;
import com.nanodegree.android.watchthemall.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShowDetailFragment extends Fragment
        implements WtaDetailFragment, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_SHOW_LOADER_ID = 6;

    private static final String[] SHOW_COLUMNS = {
            WtaContract.ShowEntry._ID,
            WtaContract.ShowEntry.COLUMN_TITLE,
            WtaContract.ShowEntry.COLUMN_BANNER_PATH,
            WtaContract.ShowEntry.COLUMN_WATCHING,
            WtaContract.ShowEntry.COLUMN_WATCHED,
            WtaContract.ShowEntry.COLUMN_WATCHLIST
    };
    // These indices are tied to SHOWS_COLUMNS. If SHOWS_COLUMNS changes, these must change too.
    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_BANNER_PATH = 2;
    public static final int COL_WATCHING = 3;
    public static final int COL_WATCHED = 4;
    public static final int COL_WATCHLIST = 5;

    private Uri mUri;
    private String mShowId;
    private String mShowTitle = "";
    private Unbinder mButterKnifeUnbinder;
    private boolean mIsWatching;
    private boolean mIsWatched;
    private boolean mIsWatchlist;

    @BindView(R.id.show_banner)
    ImageView mShowBanner;
    @BindView(R.id.show_detail_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.show_detail_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.show_detail_appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.show_detail_collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.show_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.show_detail_menu_watching_item)
    FloatingActionButton mFabWatching;
    @BindView(R.id.show_detail_menu_watched_item)
    FloatingActionButton mFabWatched;
    @BindView(R.id.show_detail_menu_watchlist_item)
    FloatingActionButton mFabWatchlist;
    @BindView(R.id.show_detail_root)
    CoordinatorLayout mRoot;

    public ShowDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(Utility.DETAIL_URI_EXTRA_KEY);
            mShowId = null;
            if (mUri!=null) {
                mShowId = WtaContract.ShowEntry.getShowIdFromUri(mUri);
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_show_detail, container, false);
        mButterKnifeUnbinder = ButterKnife.bind(this, rootView);

        getLoaderManager().initLoader(DETAIL_SHOW_LOADER_ID, null, this);

        ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
        mToolbar.setTitleMarginTop(params.height * -1);
        params.height = params.height * 2;
        mToolbar.setLayoutParams(params);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mCollapsingToolbarLayout != null) {
                        mCollapsingToolbarLayout.setTitle(mShowTitle);
                    }
                    isShow = true;
                } else if (isShow) {
                    if (mCollapsingToolbarLayout != null) {
                        mCollapsingToolbarLayout.setTitle("");
                    }
                    isShow = false;
                }
            }
        });

        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);

        mFabWatching
                .setOnClickListener(new WtaShowFabOnClickListener(WtaContract.ShowEntry.COLUMN_WATCHING));
        mFabWatched
                .setOnClickListener(new WtaShowFabOnClickListener(WtaContract.ShowEntry.COLUMN_WATCHED));
        mFabWatchlist
                .setOnClickListener(new WtaShowFabOnClickListener(WtaContract.ShowEntry.COLUMN_WATCHLIST));

        mRoot.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mButterKnifeUnbinder!=null) {
            mButterKnifeUnbinder.unbind();
        }
    }

    public void hideDetailLayout() {
        mRoot.setVisibility(View.INVISIBLE);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle arguments = new Bundle();
        arguments.putParcelable(Utility.DETAIL_URI_EXTRA_KEY, mUri);
        Fragment tabFragment;
        tabFragment = new ShowInfoFragment();
        tabFragment.setArguments(arguments);
        adapter.addFragment(tabFragment, getString(R.string.show_info_tab));
        tabFragment = new ShowSeasonsFragment();
        tabFragment.setArguments(arguments);
        adapter.addFragment(tabFragment, getString(R.string.show_seasons_tab));
        tabFragment = new ShowCommentsFragment();
        tabFragment.setArguments(arguments);
        adapter.addFragment(tabFragment, getString(R.string.show_comments_tab));

        mViewPager.setAdapter(adapter);
    }

    private void setWatchingFabStatus(boolean active) {
        int colorId = active ? R.color.colorAccent : R.color.wtaGray;
        mFabWatching.setColorNormal(ContextCompat.getColor(getActivity(), colorId));
        mIsWatching = active;
    }

    private void setWatchedFabStatus(boolean active) {
        int colorId = active ? R.color.colorAccent : R.color.wtaGray;
        mFabWatched.setColorNormal(ContextCompat.getColor(getActivity(), colorId));
        mIsWatched = active;
    }

    private void setWatchlistFabStatus(boolean active) {
        int colorId = active ? R.color.colorAccent : R.color.wtaGray;
        mFabWatchlist.setColorNormal(ContextCompat.getColor(getActivity(), colorId));
        mIsWatchlist = active;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            return new CursorLoader(getActivity(),
                    mUri, SHOW_COLUMNS, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        onDetailShowLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Do nothing
    }

    private void onDetailShowLoadFinished(Cursor data) {

        mShowTitle = data.getString(COL_TITLE);
        String bannerPath = data.getString(COL_BANNER_PATH);
        if (bannerPath!=null) {
            Glide.with(getActivity()).load(bannerPath)
                    .crossFade().into(mShowBanner);
        } else {
            mShowBanner.setImageBitmap(null);
        }
        int watching = data.getInt(COL_WATCHING);
        setWatchingFabStatus(watching == 1);
        int watched = data.getInt(COL_WATCHED);
        setWatchedFabStatus(watched == 1);
        int watchlist = data.getInt(COL_WATCHLIST);
        setWatchlistFabStatus(watchlist == 1);

        mRoot.setVisibility(View.VISIBLE);
    }

    private class WtaShowFabOnClickListener implements View.OnClickListener {

        private String mColumnName;

        public WtaShowFabOnClickListener(String column) {
            mColumnName = column;
        }

        @Override
        public void onClick(View view) {
            int value = 0;
            if (mColumnName.equals(WtaContract.ShowEntry.COLUMN_WATCHING)) {
                mIsWatching = !mIsWatching;
                value = mIsWatching ? 1 : 0;
            } else if (mColumnName.equals(WtaContract.ShowEntry.COLUMN_WATCHED)) {
                mIsWatched = !mIsWatched;
                value = mIsWatched ? 1 : 0;
            } else if (mColumnName.equals(WtaContract.ShowEntry.COLUMN_WATCHLIST)) {
                mIsWatchlist = !mIsWatchlist;
                value = mIsWatchlist ? 1 : 0;
            }

            ContentValues show = new ContentValues();
            show.put(WtaContract.ShowEntry._ID, mShowId);
            show.put(mColumnName, value);
            getActivity().getContentResolver()
                    .update(WtaContract.ShowEntry.CONTENT_URI,
                            show,
                            WtaProvider.sShowSelection,
                            new String[]{mShowId});
        }
    }
}
