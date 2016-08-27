package com.nanodegree.android.watchthemall.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.watchthemall.R;
import com.nanodegree.android.watchthemall.ShowCommentsFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sooheib on 8/27/16.
 */
/**
 * {@link CommentsAdapter} exposes a list of comments
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class CommentsAdapter extends CursorAdapter {

    public CommentsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    // These views are reused as needed
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    // Fill-in the views with the contents of the cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String commentUser = cursor.getString(ShowCommentsFragment.COL_USER);
        viewHolder.commentUser.setText(commentUser);
        if (cursor.isNull(ShowCommentsFragment.COL_CREATED_AT)) {
            viewHolder.commentContent.setText(context.getString(R.string.unknown_date));
        } else {
            Long createdAtDate = cursor.getLong(ShowCommentsFragment.COL_CREATED_AT);
            SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.sdf_with_hour_format));
            String commentDate = sdf.format(new Date(createdAtDate));
            viewHolder.commentDate.setText(commentDate);
        }
        String commentContent = cursor.getString(ShowCommentsFragment.COL_CONTENT);
        viewHolder.commentContent.setText(commentContent);
    }

    /**
     * Cache of the children views
     */
    private static class ViewHolder {
        public final TextView commentUser;
        public final TextView commentDate;
        public final TextView commentContent;

        public ViewHolder(View view) {
            commentUser = (TextView) view.findViewById(R.id.list_item_comment_user);
            commentDate = (TextView) view.findViewById(R.id.list_item_comment_date);
            commentContent = (TextView) view.findViewById(R.id.list_item_comment_content);
        }
    }
}