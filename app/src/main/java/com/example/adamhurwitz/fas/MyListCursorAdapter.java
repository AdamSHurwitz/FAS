package com.example.adamhurwitz.fas;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adamhurwitz.fas.data.Contract;
import com.squareup.picasso.Picasso;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {
    private final String LOG_TAG = MyListCursorAdapter.class.getSimpleName();
    Context mContext;

    public MyListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView mImage;
        //public TextView mTitle;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.recycler_item_image);
            //mTitle = (TextView) view.findViewById(R.id.detail_title);
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
        //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        final MyListItem myListItem = MyListItem.fromCursor(cursor);
        // viewHolder.mImageView.setText(myListItem.getUrl());

        final String imageUrl = myListItem.getImageUrl();

        //ViewHolder holder = (ViewHolder) view.getTag();
        Picasso.with(mContext).load(myListItem.getImageUrl()).noFade()
                .into(viewHolder.mImage);

        final String title = cursor.getString(cursor.getColumnIndex(Contract.ProductData.COLUMN_NAME_TITLE));
        final String price = cursor.getString(cursor.getColumnIndex(Contract.ProductData.COLUMN_NAME_TITLE));
        final String releaseDate = cursor.getString(cursor.getColumnIndex(Contract.ProductData.COLUMN_NAME_TITLE));
        final String description = cursor.getString(cursor.getColumnIndex(Contract.ProductData.COLUMN_NAME_TITLE));
        final String favorite = cursor.getString(cursor.getColumnIndex(Contract.ProductData.COLUMN_NAME_FAVORITE));

        /*String title = myListItem.getTitle();
        Log.v(LOG_TAG, "onBindViewHolder() - title " + title);*/


        /*final String image = myListItem.getImageUrl();
        final String title = myListItem.getTitle();
        final String price = myListItem.getPrice();
        final String releaseDate = myListItem.getReleaseDate();
        final String description = myListItem.getDescription();*/

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] detailArray = {imageUrl, title, price, releaseDate,
                        description, favorite};
                mContext.startActivity(new Intent(mContext, DetailActivity.class)
                        .putExtra("recylerAdapterExtra", detailArray));
                Log.v(LOG_TAG, "onClick() - title " + title);
            }
        });
    }
}