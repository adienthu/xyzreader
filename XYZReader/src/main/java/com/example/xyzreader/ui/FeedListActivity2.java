package com.example.xyzreader.ui;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.UpdaterService;

public class FeedListActivity2 extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, FeedListFragment.EventCallback, FeedListFragment.DataSource {

    private final static String DETAIL_FRAGMENT_TAG = "DFTAG";

    private Cursor mCursor;
    private FeedListFragment mListFragment;
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list2);

        getLoaderManager().initLoader(0, null, this);

        mListFragment = (FeedListFragment)getFragmentManager().findFragmentByTag(getString(R.string.list_fragment_tag));
        View detailFragmentContainer = findViewById(R.id.feed_detail_container);
        if (detailFragmentContainer != null ) {
            // Two-pane layout
            mTwoPane = true;

//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.feed_detail_container, new ArticleDetailFragment(), DETAIL_FRAGMENT_TAG)
//                        .commit();
//            }
        }else {
            mTwoPane = false;
        }

        if (savedInstanceState == null) {
            refresh();
        }
    }

    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                boolean isRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                if (isRefreshing)
                    mListFragment.startRefreshUI();
                else
                    mListFragment.stopRefreshUI();
            }
        }
    };

    @Override
    public int getFeedCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getFeedItemId(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(ArticleLoader.Query._ID);
        }
        return 0;
    }

    @Override
    public String getFeedTitle(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor.getString(ArticleLoader.Query.TITLE);
        }
        return null;
    }

    @Override
    public String getFeedByline(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return DateUtils.getRelativeTimeSpanString(
                     mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                     System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                     DateUtils.FORMAT_ABBREV_ALL).toString()
                     + " by "
                     + mCursor.getString(ArticleLoader.Query.AUTHOR);
        }
        return null;
    }

    @Override
    public String getFeedImageURL(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor.getString(ArticleLoader.Query.THUMB_URL);
        }
        return null;
    }

    @Override
    public void onFeedSelected(int position) {
        if (!mTwoPane) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    ItemsContract.Items.buildItemUri(getFeedItemId(position))));
        }else {
            if (mCursor != null) {
                mCursor.moveToPosition(position);
                ArticleDetailFragment detailFragment = ArticleDetailFragment.newInstance(
                        mCursor.getString(ArticleLoader.Query.TITLE),
                        mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                        mCursor.getString(ArticleLoader.Query.AUTHOR),
                        mCursor.getString(ArticleLoader.Query.THUMB_URL),
                        mCursor.getString(ArticleLoader.Query.BODY)
                );
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.feed_detail_container, detailFragment, DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        mListFragment.refreshFeeds();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        mListFragment.refreshFeeds();
    }
}
