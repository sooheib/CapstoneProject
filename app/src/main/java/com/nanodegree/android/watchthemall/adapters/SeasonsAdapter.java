package com.nanodegree.android.watchthemall.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.android.watchthemall.R;
import com.nanodegree.android.watchthemall.ShowSeasonsFragment;
import com.nanodegree.android.watchthemall.data.WtaContract;
/**
 * Created by Sooheib on 8/27/16.
 */
/**
 * {@link SeasonsAdapter} exposes a list of seasons (and their episodes)
 * from a {@link Cursor} to a {@link android.widget.ExpandableListView}.
 */
public class SeasonsAdapter extends CursorTreeAdapter {

    private static final String LOG_TAG = SeasonsAdapter.class.getSimpleName();

    private static final String[] EPISODE_COLUMNS = {
        WtaContract.EpisodeEntry._ID,
        WtaContract.EpisodeEntry.COLUMN_NUMBER,
        WtaContract.EpisodeEntry.COLUMN_TITLE,
        WtaContract.EpisodeEntry.COLUMN_WATCHED,
        WtaContract.EpisodeEntry.COLUMN_WATCHLIST
    };
    // These indices are tied to EPISODE_COLUMNS. If EPISODE_COLUMNS changes, these must change too.
    public static final int COL_EPISODE_ID = 0;
    public static final int COL_EPISODE_NUMBER = 1;
    public static final int COL_EPISODE_TITLE = 2;
    public static final int COL_WATCHED = 3;
    public static final int COL_WATCHLIST = 4;

    private Context mContext;

    public SeasonsAdapter(Cursor cursor, Context context) {
        super(cursor, context);

        mContext = context;
    }

    // These views are reused as needed
    @Override
    public View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_season, parent, false);
        GroupViewHolder viewHolder = new GroupViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_episode, parent, false);
        ChildViewHolder viewHolder = new ChildViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    // Fill-in the views with the contents of the cursor
    @Override
    public void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        GroupViewHolder viewHolder = (GroupViewHolder) view.getTag();

        Cursor itemCursor = getGroup(cursor.getPosition());

        String seasonNumber = itemCursor.getString(ShowSeasonsFragment.COL_SEASON_NUMBER);
        viewHolder.seasonNumber.setText(seasonNumber);

        String airedEpisodes = itemCursor.getString(ShowSeasonsFragment.COL_SEASON_AIRED_EPISODES);
        String episodeCount = itemCursor.getString(ShowSeasonsFragment.COL_SEASON_EPISODE_COUNT);
        viewHolder.episodesInfo.setText("(" + airedEpisodes + "/" + episodeCount + " " +
                context.getString(R.string.episodes) + ")");
    }

    @Override
    public void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        ChildViewHolder viewHolder = (ChildViewHolder) view.getTag();

        String episodeNumber = cursor.getString(COL_EPISODE_NUMBER);
        viewHolder.episodeNumber.setText(episodeNumber);

        String episodeTitle = cursor.getString(COL_EPISODE_TITLE);
        viewHolder.episodeTitle.setText(episodeTitle);

        int watched = cursor.getInt(COL_WATCHED);
        int watchlist = cursor.getInt(COL_WATCHLIST);
        int resourceId = -1;
        int contentDescription = R.string.episode_status;
        if (watched==1) {
            resourceId = R.drawable.ic_watched;
            contentDescription = R.string.watched_item;
        } else if (watchlist==1) {
            resourceId = R.drawable.ic_watchlist;
            contentDescription = R.string.watching_item;
        }
        if (resourceId==-1) {
            viewHolder.episodeStatusIcon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.episodeStatusIcon.setVisibility(View.VISIBLE);
            viewHolder.episodeStatusIcon.setImageResource(resourceId);
        }
        viewHolder.episodeStatusIcon.setContentDescription(context.getString(contentDescription));
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {

        Cursor itemCursor = getGroup(groupCursor.getPosition());
        int seasonId = itemCursor.getInt(ShowSeasonsFragment.COL_ID);
        CursorLoader cursorLoader = new CursorLoader(mContext,
                WtaContract.EpisodeEntry.buildSeasonEpisodesUri(seasonId), EPISODE_COLUMNS,
                            null, null, WtaContract.EpisodeEntry.COLUMN_NUMBER);

        Cursor childCursor = null;
        try {
            childCursor = cursorLoader.loadInBackground();
            childCursor.moveToFirst();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return childCursor;
    }

    /**
     * Cache of the group views
     */
    private static class GroupViewHolder {
        public final TextView seasonNumber;
        public final TextView episodesInfo;

        public GroupViewHolder(View view) {
            seasonNumber = (TextView) view.findViewById(R.id.list_item_season_number);
            episodesInfo = (TextView) view.findViewById(R.id.list_item_episodes_info);
        }
    }

    /**
     * Cache of the children views
     */
    private static class ChildViewHolder {
        public final TextView episodeNumber;
        public final TextView episodeTitle;
        public final ImageView episodeStatusIcon;

        public ChildViewHolder(View view) {
            episodeNumber = (TextView) view.findViewById(R.id.list_item_episode_number);
            episodeTitle = (TextView) view.findViewById(R.id.list_item_episode_title);
            episodeStatusIcon = (ImageView) view.findViewById(R.id.list_item_episode_status_icon);
        }
    }
}