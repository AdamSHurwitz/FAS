package com.example.adamhurwitz.fas.model;

/**
 * Created by adamhurwitz on 3/1/16.
 */
public class Item {
    String item_id;
    String title;
    String description;
    String search_strings;
    static String imageUrl;
    String release_date;
    int price;
    double popularity;
    boolean recent;
    boolean vintage;
    boolean favorite;
    boolean cart;

    public Item() {
    }

    public Item(String item_id, String title, String description, String search_strings,
                String image_url, String release_date, int price, double popularity,
                boolean recent, boolean vintage, boolean favorite, boolean cart) {
        this.item_id = item_id;
        this.title = title;
        this.description = description;
        this.search_strings = search_strings;
        this.imageUrl = image_url;
        this.release_date = release_date;
        this.price = price;
        this.popularity = popularity;
        this.recent = recent;
        this.vintage = vintage;
        this.favorite = favorite;
        this.cart = cart;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSearch_strings() {
        return search_strings;
    }

    public static String getImageUrl() {
        return imageUrl;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getPrice() {
        return price;
    }

    public double getPopularity() {
        return popularity;
    }

    public boolean getRecent() {
        return recent;
    }

    public boolean getVintage() {
        return vintage;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public boolean getCart() {
        return cart;
    }
}
