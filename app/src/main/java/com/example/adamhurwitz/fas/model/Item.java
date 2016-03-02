package com.example.adamhurwitz.fas.model;

/**
 * Created by adamhurwitz on 3/1/16.
 */
public class Item {
    String item_id;
   /* String title;
    String description;
    String search_strings;
    String image_url;
    String release_date;
    String price;
    String popularity;
    String recent;
    String vintage;
    String favorite;
    String cart;*/

    public Item() {
    }

    public Item(String item_id
           /* , String title, String description, String search_strings,
                String image_url, String release_date, String price, String popularity,
                String recent, String vintage, String favorite, String cart*/
    ) {
        this.item_id = item_id;
        /*this.title = title;
        this.description = description;
        this.search_strings = search_strings;
        this.image_url = image_url;
        this.release_date = release_date;
        this.price = price;
        this.popularity = popularity;
        this.recent = recent;
        this.vintage = vintage;
        this.favorite = favorite;
        this.cart = cart;*/
    }

    public String getItem_id() {
        return item_id;
    }

   /* public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSearch_strings() {
        return search_strings;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPrice() {
        return price;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getRecent() {
        return recent;
    }

    public String getVintage() {
        return vintage;
    }

    public String getFavorite() {
        return favorite;
    }

    public String getCart() {
        return cart;
    }*/

}
