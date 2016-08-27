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

public class EpisodeCommentsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_EPISODE_COMMENTS_LOADER_ID = 8;

    // These columns must be the same that those defined in ShowCommentsFragment, in order to get
    // this working properly with the CommentsAdapter
    private static final String[] COMMENT_COLUMNS = {
            WtaContract.CommentEntry._ID,
            WtaContract.CommentEntry.COLUMN_CREATED_AT,
            WtaContract.CommentEntry.COLUMN_CONTENT,
            WtaContract.CommentEntry.COLUMN_USER
    };

    @BindView(R.id.commentList)
    NonScrollListView mEpisodeCommentsListView;
    @BindView(R.id.emptyCommentListLabel)
    TextView mEmptyCommentListLabel;

    private Unbinder mButterKnifeUnbinder;
    private Uri mUri;
    private CommentsAdapter mCommentsAdapter;

    public EpisodeCommentsFragment() {
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

        getLoaderManager().initLoader(DETAIL_EPISODE_COMMENTS_LOADER_ID, null, this);

        mCommentsAdapter = new CommentsAdapter(getActivity(), null, 0);
        mEpisodeCommentsListView.setAdapter(mCommentsAdapter);

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
            return new CursorLoader(getActivity(),
                    WtaContract.CommentEntry
                            .buildEpisodeCommentsUri(Long.valueOf(WtaContract.CommentEntry
                                    .getEpisodeIdFromUri(mUri))), COMMENT_COLUMNS,
                    null, null, WtaContract.CommentEntry.COLUMN_CREATED_AT + " DESC");
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            //Comments data cannot be retrieved from DB
            mEpisodeCommentsListView.setVisibility(View.INVISIBLE);
            mEmptyCommentListLabel.setVisibility(View.VISIBLE);
            return;
        }

        mEpisodeCommentsListView.setVisibility(View.VISIBLE);
        mEmptyCommentListLabel.setVisibility(View.INVISIBLE);
        onDetailEpisodeCommentsLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCommentsAdapter.swapCursor(null);
    }

    private void onDetailEpisodeCommentsLoadFinished(Cursor data) {
        mCommentsAdapter.swapCursor(data);
    }
}
