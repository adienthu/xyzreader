package com.example.xyzreader.ui;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventCallback} and {@link DataSource} interfaces.
 */
public class FeedListFragment extends Fragment {

    private EventCallback mListener;
    private DataSource mDataSource;

    private RecyclerView mFeedsRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public FeedListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed_list, container, false);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        Typeface robotoBlack = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Black.ttf");
        collapsingToolbarLayout.setExpandedTitleTypeface(robotoBlack);
        collapsingToolbarLayout.setCollapsedTitleTypeface(robotoBlack);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        mFeedsRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_feeds);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mFeedsRecyclerView.setLayoutManager(sglm);
        mFeedsRecyclerView.setAdapter(new Adapter());
        return rootView;
    }

    public void refreshFeeds() {
        mFeedsRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public long getItemId(int position) {
            return mDataSource.getFeedItemId(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.list_item_feed, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onFeedSelected(vh.getAdapterPosition());
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.titleText.setText(mDataSource.getFeedTitle(position));
            holder.subtitleText.setText(mDataSource.getFeedByline(position));
            holder.feedImage.setImageUrl(
                    mDataSource.getFeedImageURL(position),
                    ImageLoaderHelper.getInstance(getActivity()).getImageLoader());
            holder.feedImage.setAspectRatio(mDataSource.getFeedImageAspectRatio(position));
        }

        @Override
        public int getItemCount() {
            return mDataSource.getFeedCount();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView subtitleText;
        DynamicHeightNetworkImageView feedImage;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.text_view_title);
            subtitleText = (TextView) itemView.findViewById(R.id.text_view_subtitle);
            feedImage = (DynamicHeightNetworkImageView) itemView.findViewById(R.id.image_view_feed_image);
        }
    }

    public void startRefreshUI() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void stopRefreshUI() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (EventCallback) activity;
            mDataSource = (DataSource) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mDataSource = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface EventCallback {
        void onFeedSelected(int position);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment for rendering the feed list
     */
    public interface DataSource {
        int getFeedCount();
        long getFeedItemId(int position);
        String getFeedTitle(int position);
        String getFeedByline(int position);
        String getFeedImageURL(int position);
        float getFeedImageAspectRatio(int position);
    }

}
