package com.example.adamhurwitz.fas;

import android.database.Cursor;

import com.example.adamhurwitz.fas.data.Contract;

public class MyListItem {
    static String imageUrl;

    public void setUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return imageUrl;
    }

    public MyListItem() {
    }

    public static MyListItem fromCursor(Cursor cursor) {
        String imageUrl = cursor.getString(cursor.getColumnIndex(
                Contract.ProductData.COLUMN_NAME_IMAGEURL));
        MyListItem myListItem = new MyListItem();
        myListItem.setUrl(imageUrl);
        return myListItem;
    }
}