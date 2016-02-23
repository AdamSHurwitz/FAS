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
public class CartCursorAdapter extends CursorRecyclerViewAdapter<CartCursorAdapter.ViewHolder> {
    private final String LOG_TAG = CartCursorAdapter.class.getSimpleName();
    Context mContext;

    public CartCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView mImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.recycler_cart_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_cartitem_view, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        final MyListItem myListItem = MyListItem.fromCursor(cursor);

        final String imageUrl = myListItem.getImageUrl();

        Picasso.with(mContext).load(myListItem.getImageUrl()).noFade()
                .into(viewHolder.mImage);

        final String title = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                .COLUMN_NAME_TITLE));
        final String price = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                .COLUMN_NAME_PRICE));
        final String releaseDate = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                .COLUMN_NAME_RELEASEDATE));
        final String description = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                .COLUMN_NAME_DESCRIPTION));
        final String favorite = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                .COLUMN_NAME_FAVORITE));
        final String cart = cursor.getString(cursor.getColumnIndex(Contract.ProductData
                .COLUMN_NAME_CART));

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] detailArray = {imageUrl, title, price, releaseDate,
                        description, favorite, cart};
                mContext.startActivity(new Intent(mContext, DetailActivity.class)
                        .putExtra("recylerAdapterExtra", detailArray));
                Log.v(LOG_TAG, "onClick() - title " + title);
            }
        });
    }
}