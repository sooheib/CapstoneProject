package com.nanodegree.android.watchthemall;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.watchthemall.adapters.CommentsAdapter;
import com.nanodegree.android.watchthemall.components.NonScrollListView;
import com.nanodegree.android.watchthemall.data.WtaContract;
import com.nanodegree.android.watchthemall.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShowCommentsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_SHOW_COMMENTS_LOADER_ID = 5;

    private static final String[] COMMENT_COLUMNS = {
            WtaContract.CommentEntry._ID,
            WtaContract.CommentEntry.COLUMN_CREATED_AT,
            WtaContract.CommentEntry.COLUMN_CONTENT,
            WtaContract.CommentEntry.COLUMN_USER
    };
    // These indices are tied to COMMENT_COLUMNS. If COMMENT_COLUMNS changes, these must change too.
    public static final int COL_ID = 0;
    public static final int COL_CREATED_AT = 1;
    public static final int COL_CONTENT = 2;
    public static final int COL_USER = 3;

    @BindView(R.id.commentList)
    NonScrollListView mShowCommentsListView;
    @BindView(R.id.emptyCommentListLabel)
    TextView mEmptyCommentListLabel;

    private Unbinder mButterKnifeUnbinder;
    private Uri mUri;
    private CommentsAdapter mCommentsAdapter;

    public ShowCommentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(Utility.DETAIL_URI_EXTRA_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_tab_comments, container, false);
        mButterKnifeUnbinder = ButterKnife.bind(this, rootView);

        getLoaderManager().initLoader(DETAIL_SHOW_COMMENTS_LOADER_ID, null, this);

        mCommentsAdapter = new CommentsAdapter(getActivity(), null, 0);
        mShowCommentsListView.setAdapter(mCommentsAdapter);

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
                case DETAIL_SHOW_COMMENTS_LOADER_ID: {
                    return new CursorLoader(getActivity(),
                            WtaContract.CommentEntry
                                    .buildShowCommentsUri(Long.valueOf(WtaContract.CommentEntry
                                            .getShowIdFromUri(mUri))), COMMENT_COLUMNS,
                            null, null, WtaContract.CommentEntry.COLUMN_CREATED_AT + " DESC");
                }
            }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            //Comments data cannot be retrieved from DB
            mShowCommentsListView.setVisibility(View.INVISIBLE);
            mEmptyCommentListLabel.setVisibility(View.VISIBLE);
            return;
        }

        mShowCommentsListView.setVisibility(View.VISIBLE);
        mEmptyCommentListLabel.setVisibility(View.INVISIBLE);
        long loaderId = loader.getId();
        if (loaderId == DETAIL_SHOW_COMMENTS_LOADER_ID) {
            onDetailShowCommentsLoadFinished(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        long loaderId = loader.getId();
        if (loaderId == DETAIL_SHOW_COMMENTS_LOADER_ID) {
            mCommentsAdapter.swapCursor(null);
        }
    }

    private void onDetailShowCommentsLoadFinished(Cursor data) {
        mCommentsAdapter.swapCursor(data);
    }
}
