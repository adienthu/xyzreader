package com.example.xyzreader.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.xyzreader.R;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * contained in a {@link FeedDetailActivity}.
 */
public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "ArticleDetailFragment";

    public static final String ARG_TITLE = "title";
    public static final String ARG_PUBLISH_DATE = "publish_date";
    public static final String ARG_AUTHOR = "author";
    public static final String ARG_IMG_URL = "img_url";
    public static final String ARG_BODY = "body";

    private DetailFragmentEventListener mDetailFragmentEventListener;
    private GestureDetectorCompat mDetector;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(String title, long publishDate, String author, String imgURL, String body) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putLong(ARG_PUBLISH_DATE, publishDate);
        arguments.putString(ARG_AUTHOR, author);
        arguments.putString(ARG_IMG_URL, imgURL);
        arguments.putString(ARG_BODY, body);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed_detail, container, false);
        bindViews(rootView);

        final NetworkImageView imageView = (NetworkImageView)rootView.findViewById(R.id.image_view_feed_image);
        final ObservableScrollView textContainer = (ObservableScrollView)rootView.findViewById(R.id.text_container);
        textContainer.setCallbacks(new ObservableScrollView.Callbacks() {
            @Override
            public void onScrollChanged(int oldt, int newt) {
                final boolean isCard = getResources().getBoolean(R.bool.detail_is_card);
                if (!isCard)
                    imageView.setTranslationY(-newt * 0.75f);
                if (mDetailFragmentEventListener != null)
                        mDetailFragmentEventListener.onScroll(textContainer.getScrollY(), (newt - oldt));
            }
        });

        mDetector = new GestureDetectorCompat(getActivity(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mDetailFragmentEventListener!=null)
                    mDetailFragmentEventListener.onSingleTap();
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        textContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return view.onTouchEvent(motionEvent);
            }
        });
        return rootView;
    }

    private void bindViews(View rootView) {
        if (rootView == null) {
            return;
        }

        TextView titleLabel = (TextView)rootView.findViewById(R.id.text_view_title);
        titleLabel.setText(getArguments().getString(ARG_TITLE));

        TextView subtitleLabel = (TextView)rootView.findViewById(R.id.text_view_subtitle);
        String publishDate = DateUtils.getRelativeTimeSpanString(
                getArguments().getLong(ARG_PUBLISH_DATE),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString();
        String author = getArguments().getString(ARG_AUTHOR);
        subtitleLabel.setText(publishDate + " by " + author);

        NetworkImageView feedImage = (NetworkImageView)rootView.findViewById(R.id.image_view_feed_image);
        feedImage.setImageUrl(
                getArguments().getString(ARG_IMG_URL),
                ImageLoaderHelper.getInstance(getActivity()).getImageLoader());

        TextView bodyLabel = (TextView)rootView.findViewById(R.id.text_view_feed_body);
        bodyLabel.setText(Html.fromHtml(getArguments().getString(ARG_BODY)));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDetailFragmentEventListener = (DetailFragmentEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnScrollListener");
        }
    }

    interface DetailFragmentEventListener {
        void onScroll(int scrollY, int dy);
        void onSingleTap();
    }
}
