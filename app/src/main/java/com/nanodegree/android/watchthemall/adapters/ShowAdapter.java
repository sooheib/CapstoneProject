package com.nanodegree.android.watchthemall.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nanodegree.android.watchthemall.R;
import com.nanodegree.android.watchthemall.ShowsFragment;
/**
 * Created by Sooheib on 8/27/16.
 */
/**
 * {@link ShowAdapter} exposes a list of shows
 * from a {@link android.database.Cursor} to a {@link android.widget.GridView}.
 */
public class ShowAdapter extends CursorAdapter {

    public ShowAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    // These views are reused as needed
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_show, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    // Fill-in the views with the contents of the cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String posterPath = cursor.getString(ShowsFragment.COL_POSTER_PATH);
        if (posterPath!=null) {
            Glide.with(mContext).load(posterPath)
                    .error(R.drawable.no_show_poster)
                    .crossFade().into(viewHolder.posterView);
        } else {
            Glide.with(mContext).load(R.drawable.no_show_poster)
                    .crossFade().into(viewHolder.posterView);
        }
        viewHolder.titleView.setText(cursor.getString(ShowsFragment.COL_TITLE));
    }

    /**
     * Cache of the children views for a shows grid view.
     */
    private static class ViewHolder {
        public final ImageView posterView;
        public final TextView titleView;

        public ViewHolder(View view) {
            posterView = (ImageView) view.findViewById(R.id.grid_item_show_poster);
            titleView = (TextView) view.findViewById(R.id.grid_item_show_title);
        }
    }
}