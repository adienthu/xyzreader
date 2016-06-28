package com.example.xyzreader.ui;

import android.animation.ObjectAnimator;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;

public class FeedDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ArticleDetailFragment.OnScrollListener {

    private static final String LOG_TAG = FeedDetailActivity.class.getSimpleName();

    private Cursor mCursor;
    private long mStartId;

    private long mSelectedItemId;
    private int mCurrentPos;

    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private Toolbar mToolbar;
    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        getLoaderManager().initLoader(0, null, this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mCursor != null) {
                    mCursor.moveToPosition(position);
                    mCurrentPos = position;
                    Log.d(LOG_TAG, "Current position " + mCurrentPos);
                }
                mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Feeds");

        mFAB = (FloatingActionButton)findViewById(R.id.fab_share);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getData() != null) {
                mStartId = ItemsContract.Items.getItemId(getIntent().getData());
                mSelectedItemId = mStartId;
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
        mPagerAdapter.notifyDataSetChanged();

        // Select the start ID
        if (mStartId > 0) {
            mCursor.moveToFirst();
            // TODO: optimize
            while (!mCursor.isAfterLast()) {
                if (mCursor.getLong(ArticleLoader.Query._ID) == mStartId) {
                    mCurrentPos = mCursor.getPosition();
                    mPager.setCurrentItem(mCurrentPos, false);
                    Log.d(LOG_TAG, "Current position " + mCurrentPos);
                    break;
                }
                mCursor.moveToNext();
            }
            mStartId = 0;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void contentScrolled(int scrollY, int dy) {
//        Log.d(LOG_TAG, "Content scrolled");
        if (dy > 0) {
            if (mToolbar.getTranslationY() == 0)
            {
//                Slide slide = new Slide(Gravity.TOP);
//                TransitionManager.beginDelayedTransition(mToolbar, slide);
//                mToolbar.setVisibility(View.INVISIBLE);

                ObjectAnimator animator = ObjectAnimator.ofFloat(mToolbar, "translationY", 0, -mToolbar.getMeasuredHeight());
                animator.setDuration(250);
                animator.start();
            }

            if (mFAB.getTranslationY() == 0)
            {
                ObjectAnimator animator = ObjectAnimator.ofFloat(mFAB, "translationY", 0, mFAB.getMeasuredHeight()+mFAB.getPaddingBottom());
                animator.setDuration(250);
                animator.start();
            }
        }else if (dy < -10){
            if (mToolbar.getTranslationY() == -mToolbar.getMeasuredHeight())
            {
//                Slide slide = new Slide(Gravity.TOP);
//                TransitionManager.beginDelayedTransition(mToolbar, slide);
//                mToolbar.setVisibility(View.VISIBLE);
                ObjectAnimator animator = ObjectAnimator.ofFloat(mToolbar, "translationY", -mToolbar.getMeasuredHeight(), 0);
                animator.setDuration(250);
                animator.start();
            }

            if (mFAB.getTranslationY() == mFAB.getMeasuredHeight()+mFAB.getPaddingBottom())
            {
                ObjectAnimator animator = ObjectAnimator.ofFloat(mFAB, "translationY", mFAB.getMeasuredHeight()+mFAB.getPaddingBottom(), 0);
                animator.setDuration(250);
                animator.start();
            }
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            mCursor.moveToPosition(position);
            return ArticleDetailFragment.newInstance(
                    mCursor.getString(ArticleLoader.Query.TITLE),
                    mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                    mCursor.getString(ArticleLoader.Query.AUTHOR),
                    mCursor.getString(ArticleLoader.Query.THUMB_URL),
                    mCursor.getString(ArticleLoader.Query.BODY)
            );
//            fragment.setOnScrollListener(FeedDetailActivity.this);
//            return fragment;
        }

        @Override
        public int getCount() {
            return (mCursor != null) ? mCursor.getCount() : 0;
        }
    }

    public void onFABClick(View button) {
        if (mCursor != null) {
            if (mCursor.getPosition() != mCurrentPos)
                mCursor.moveToPosition(mCurrentPos);
            final String title = mCursor.getString(ArticleLoader.Query.TITLE);
            final String author = mCursor.getString(ArticleLoader.Query.AUTHOR);
            final String shareMsg = getString(R.string.share_action_msg, title, author);
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareMsg)
                    .getIntent(), getString(R.string.action_share)));
        }
    }
}
