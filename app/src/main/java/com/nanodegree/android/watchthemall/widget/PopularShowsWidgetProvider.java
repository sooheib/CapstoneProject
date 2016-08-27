package com.nanodegree.android.watchthemall.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.nanodegree.android.watchthemall.MainActivity;
import com.nanodegree.android.watchthemall.R;
import com.nanodegree.android.watchthemall.ShowDetailActivity;
import com.nanodegree.android.watchthemall.sync.WtaSyncAdapter;

/**
 * Provider for a scrollable popular series list widget
 */
public class PopularShowsWidgetProvider extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Perform this loop procedure for each App Widget that belongs to this provider 
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.widget_popular_series);

            // Create an Intent to main activity and other to detail activity
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, mainPendingIntent);

            Intent detailIntent = new Intent(context, ShowDetailActivity.class);
            PendingIntent detailPendingIntent = PendingIntent.getActivity(context, 0, detailIntent, 0);
            views.setPendingIntentTemplate(R.id.widget_list, detailPendingIntent);

            // Set up the collection 
            setRemoteAdapter(context, views, appWidgetId);

            views.setEmptyView(R.id.widget_list, R.id.widget_empty);

            views.setTextViewText(R.id.widget_title, context.getString(R.string.title_widget_popular_shows));

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);

        if (WtaSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }


    /**
     * Sets the remote adapter used to fill in the list items 
     *
     * @param views RemoteViews to set the RemoteAdapter 
     */
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views,
                                  int appWidgetId) {
        Intent intent = new Intent(context, PopularShowsWidgetRemoteViewsService.class);
        intent.setData(Uri.fromParts("content", String.valueOf(appWidgetId), null));
        views.setRemoteAdapter(R.id.widget_list, intent);
    }
}