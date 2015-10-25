package com.example.adamhurwitz.fas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * An adapter for the recycler view to efficiently switch between tabs in the application.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int mResourceId;
    private List<String> mValues;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);
        view.setBackgroundResource(mResourceId);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mValues.get(position));
    }

    public RecyclerViewAdapter(Context context, List<String> items) {
        TypedValue resourceValues = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, resourceValues, true);
        mResourceId = resourceValues.resourceId;
        mValues = items;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * A class to hold the different views for each item in the list of each tab.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = (TextView) view.findViewById(R.id.list_item_text);
        }

    }
}
