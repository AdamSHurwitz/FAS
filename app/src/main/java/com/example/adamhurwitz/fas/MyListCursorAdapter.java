package com.example.adamhurwitz.fas;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder>{
Context mContext;

    public MyListCursorAdapter(Context context,Cursor cursor){
        super(context,cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.recycler_item_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        MyListItem myListItem = MyListItem.fromCursor(cursor);
        // viewHolder.mImageView.setText(myListItem.getUrl());

        //ViewHolder holder = (ViewHolder) view.getTag();
        Picasso.with(mContext).load(myListItem.getUrl()).noFade()
                .into(viewHolder.mImageView);
    }
}