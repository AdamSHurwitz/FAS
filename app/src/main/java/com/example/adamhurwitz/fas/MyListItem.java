package com.example.adamhurwitz.fas;

import android.database.Cursor;

import com.example.adamhurwitz.fas.data.Contract;

public class MyListItem {
    static String imageUrl;
    static String title;
    static String price;
    static String releaseDate;
    static String description;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPrice(String price) {

        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setReleaseDate(String releaseDate) {

        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public MyListItem() {
    }

    public static MyListItem fromCursor(Cursor cursor) {
        //cursor = (Cursor) parent.getItemAtPosition(position);
        String imageUrl = cursor.getString(cursor.getColumnIndex(
                Contract.ProductData.COLUMN_NAME_IMAGEURL));
        String title = cursor.getString(cursor.getColumnIndex(
                Contract.ProductData.COLUMN_NAME_TITLE));
        String price = cursor.getString(cursor.getColumnIndex(
                Contract.ProductData.COLUMN_NAME_PRICE));
        String releaseDate = cursor.getString(cursor.getColumnIndex(
                Contract.ProductData.COLUMN_NAME_RELEASEDATE));
        String description = cursor.getString(cursor.getColumnIndex(
                Contract.ProductData.COLUMN_NAME_DESCRIPTION));
        MyListItem myListItem = new MyListItem();
        myListItem.setImageUrl(imageUrl);
        myListItem.setTitle(title);
        myListItem.setPrice(price);
        myListItem.setReleaseDate(releaseDate);
        myListItem.setDescription(description);
        return myListItem;
    }
}