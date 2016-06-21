package com.example.xyzreader.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.xyzreader.R;

public class FeedListAdapter extends ArrayAdapter<String>{

    public FeedListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public FeedListAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_feed, null, false);//View.inflate(getContext(), R.layout.list_item_feed, parent);
        }
        return convertView;
    }
}
