package com.nanodegree.android.watchthemall;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nanodegree.android.watchthemall.sync.WtaSyncAdapter;
import com.nanodegree.android.watchthemall.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.search_keywords)
    EditText mSearchKeywords;
    @BindView(R.id.loading_spinner)
    ProgressBar mLoadingSpinner;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.start_drawer)
    ListView mDrawerList;
    @BindView(R.id.adView)
    AdView mAdView;

    private String[] mNavigationDrawerOptions;
    private CustomRunnable mCloseDrawerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNavigationDrawerOptions = getResources()
                .getStringArray(R.array.main_navigation_drawer_options);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.main_drawer_list_item, mNavigationDrawerOptions));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        // Make sure that Analytics tracking has started
        ((App) getApplication()).startTracking();

        WtaSyncAdapter.initializeSyncAdapter(this);

        if (savedInstanceState==null) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mCloseDrawerRunnable = new CustomRunnable(mDrawerLayout);
            mDrawerLayout.postDelayed(mCloseDrawerRunnable, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCloseDrawerRunnable!=null) {
            mDrawerLayout.removeCallbacks(mCloseDrawerRunnable);
            mCloseDrawerRunnable = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoadingSpinner.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.search_button)
    public void searchShowsByKeywords(View view) {

        String searchText = "";
        if (mSearchKeywords.getText()!=null) {
            searchText = mSearchKeywords.getText().toString();
        }

        if (!searchText.isEmpty()) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            mLoadingSpinner.setVisibility(View.VISIBLE);

            Utility.updateShowsSearch(this, searchText);
        } else {
            Toast.makeText(this, getString(R.string.empty_search_keywords_error), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            if ((position!=ListView.INVALID_POSITION)&&(position<mNavigationDrawerOptions.length)) {
                String selectedOption = mNavigationDrawerOptions[position];
                
                if ((selectedOption.equals(getString(R.string.navigation_drawer_watchlist_series))) ||
                        (selectedOption.equals(getString(R.string.navigation_drawer_watching_series))) ||
                        (selectedOption.equals(getString(R.string.navigation_drawer_watched_series)))) {
                    Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                    intent.putExtra(Utility.COLLECTION_EXTRA_KEY, selectedOption);
                    startActivity(intent);
                }  else if (selectedOption.equals(getString(R.string.action_settings))) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                } else if (selectedOption.equals(getString(R.string.action_about))) {
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                }
            }
            mDrawerList.setItemChecked(position, Boolean.FALSE);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    private class CustomRunnable implements Runnable {

        private DrawerLayout mDrawer;

        public CustomRunnable(DrawerLayout drawer) {
            this.mDrawer = drawer;
        }

        @Override
        public void run() {
            mDrawer.closeDrawer(GravityCompat.START);
            MainActivity.this.mCloseDrawerRunnable = null;
        }
    }
}
