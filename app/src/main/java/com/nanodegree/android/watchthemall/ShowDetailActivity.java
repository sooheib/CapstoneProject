package com.nanodegree.android.watchthemall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nanodegree.android.watchthemall.util.Utility;

public class ShowDetailActivity extends AppCompatActivity
        implements ShowSeasonsFragment.EpisodeCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_detail);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(Utility.DETAIL_URI_EXTRA_KEY, getIntent().getData());

            ShowDetailFragment fragment = new ShowDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onEpisodeSelected(Uri uri) {
        // When using this activity, NEVER would be in TwoPane mode
        Intent intent =
                new Intent(this, EpisodeDetailActivity.class)
                        .setData(uri);
        startActivity(intent);
    }
}
