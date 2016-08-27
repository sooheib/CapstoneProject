package com.nanodegree.android.watchthemall.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.nanodegree.android.watchthemall.R;
import com.nanodegree.android.watchthemall.data.WtaContract;
import com.nanodegree.android.watchthemall.data.WtaProvider;

/**
 * RemoteViewsService controlling the data being shown in the scrollable popular series widget
 */
public class PopularShowsWidgetRemoteViewsService extends RemoteViewsService {

    private final String LOG_TAG = PopularShowsWidgetRemoteViewsService.class.getSimpleName();

    private static final String[] SHOW_COLUMNS = {
            WtaContract.ShowEntry.TABLE_NAME + "."
                    + WtaContract.ShowEntry._ID,
            WtaContract.ShowEntry.TABLE_NAME + "."
                    + WtaContract.ShowEntry.COLUMN_TITLE,
            WtaContract.ShowEntry.TABLE_NAME + "."
                    + WtaContract.ShowEntry.COLUMN_YEAR,
            WtaContract.ShowEntry.TABLE_NAME + "."
                    + WtaContract.ShowEntry.COLUMN_THUMB_PATH
    };
    // These indices are tied to SCORE_COLUMNS. If SCORE_COLUMNS changes, these must change too.
    private static final int COL_ID = 0;
    private static final int COL_TITLE = 1;
    private static final int COL_YEAR = 2;
    private static final int COL_THUMB_PATH = 3;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new PopularSeriesWidgetFactory();
    }

    private class PopularSeriesWidgetFactory implements RemoteViewsFactory {

        private Cursor data = null;

        public PopularSeriesWidgetFactory() {
        }

        @Override
        public void onCreate() {
            // Nothing to do
        }

        @Override
        public void onDataSetChanged() {
            if (data != null) {
                data.close();
            }
            // This method is called by the app hosting the widget (e.g., the launcher)
            // However, our ContentProvider is not exported so it doesn't have access to the
            // data. Therefore we need to clear (and finally restore) the calling identity so
            // that calls use our process and permission
            final long identityToken = Binder.clearCallingIdentity();

            data = null;
            Uri showsUri = WtaContract.ShowEntry.CONTENT_URI;
            data = getContentResolver().query(showsUri,
                    SHOW_COLUMNS,
                    WtaProvider.sPopularityShowSelection,
                    new String[]{"0"},
                    WtaContract.ShowEntry.COLUMN_POPULARITY);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION ||
                    data == null || !data.moveToPosition(position)) {
                return null;
            }

            RemoteViews views = new RemoteViews(getPackageName(),
                    R.layout.widget_popular_series_item);

            String thumbPath = data.getString(COL_THUMB_PATH);
            Bitmap thumbBitmap = null;
            try {
                thumbBitmap = Glide.with(PopularShowsWidgetRemoteViewsService.this)
                        .load(thumbPath)
                        .asBitmap()
                        .override(Target.SIZE_ORIGINAL, 90)
                        .fitCenter()
                        .into(Target.SIZE_ORIGINAL, 90)
                        .get();
            } catch (Exception e) {
                try {
                    thumbBitmap = Glide.with(PopularShowsWidgetRemoteViewsService.this)
                            .load(R.drawable.no_show_poster)
                            .asBitmap()
                            .override(Target.SIZE_ORIGINAL, 90)
                            .centerCrop()
                            .into(Target.SIZE_ORIGINAL, 90)
                            .get();
                } catch (Exception ex) {
                    Log.d(LOG_TAG, "Error getting bitmap", ex);
                }
            }

            if (thumbBitmap!=null) {
                views.setImageViewBitmap(R.id.widget_show_thumb, thumbBitmap);
            }

            AppWidgetManager.getInstance(getApplicationContext())
                    .updateAppWidget(new ComponentName(getApplicationContext(), PopularShowsWidgetRemoteViewsService.class),
                            views);

            views.setTextViewText(R.id.widget_show_title, data.getString(COL_TITLE));
            views.setTextViewText(R.id.widget_show_year, "(" + data.getString(COL_YEAR) + ")");

            setRemoteContentDescription(views, R.id.widget_list_item, data.getString(COL_TITLE));

            // Create an Intent to launch detail activity
            Uri uri = WtaContract.ShowEntry.buildShowUri(data.getInt(COL_ID));
            Intent fillInIntent = new Intent();
            fillInIntent.setData(uri);
            views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

            return views;
        }

        private void setRemoteContentDescription(RemoteViews views, int viewId,
                                                 String description) {
            views.setContentDescription(viewId, description);
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(getPackageName(), R.layout.widget_popular_series_item);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if (data.moveToPosition(position))
                return data.getLong(COL_TITLE);
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}