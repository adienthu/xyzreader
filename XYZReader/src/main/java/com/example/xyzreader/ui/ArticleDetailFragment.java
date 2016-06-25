package com.example.xyzreader.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link FeedDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "ArticleDetailFragment";

    // TODO: remove
    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_TITLE = "title";
    public static final String ARG_PUBLISH_DATE = "publish_date";
    public static final String ARG_AUTHOR = "author";
    public static final String ARG_IMG_URL = "img_url";
    public static final String ARG_BODY = "body";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    // TODO: remove
    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
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

        final NestedScrollView imageContainer = (NestedScrollView)rootView.findViewById(R.id.image_container);
        ObservableNestedScrollView textContainer = (ObservableNestedScrollView)rootView.findViewById(R.id.text_container);
        textContainer.setOnScrollListener(new ObservableNestedScrollView.OnScrollListener() {
            @Override
            public void onScroll(int dx, int dy) {
                imageContainer.smoothScrollBy((int)Math.floor(dx*0.75), (int)Math.floor(dy*0.75));
            }
        });

        return rootView;
    }

    private void bindViews(View rootView) {
        if (rootView == null) {
            return;
        }

//        rootView.setVisibility(View.VISIBLE);
//        TextView titleLabel = (TextView)rootView.findViewById(R.id.text_view_title);
//        titleLabel.setText(getArguments().getString(ARG_TITLE));
//
//        TextView subtitleLabel = (TextView)rootView.findViewById(R.id.text_view_subtitle);
//        String publishDate = DateUtils.getRelativeTimeSpanString(
//                getArguments().getLong(ARG_PUBLISH_DATE),
//                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
//                DateUtils.FORMAT_ABBREV_ALL).toString();
//        String author = getArguments().getString(ARG_AUTHOR);
//        subtitleLabel.setText(publishDate + " by " + author);

//        DynamicHeightNetworkImageView feedImage = (DynamicHeightNetworkImageView)rootView.findViewById(R.id.image_view_feed_image);
//        feedImage.setImageUrl(
//                getArguments().getString(ARG_IMG_URL),
//                ImageLoaderHelper.getInstance(getActivity()).getImageLoader());

        TextView bodyLabel = (TextView)rootView.findViewById(R.id.text_view_feed_body);
        bodyLabel.setText(Html.fromHtml(getArguments().getString(ARG_BODY)));
    }

    // TODO: remove
    public int getUpButtonFloor() {
        return 0;
    }
}
