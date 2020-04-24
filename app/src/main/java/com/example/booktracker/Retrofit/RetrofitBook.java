package com.example.booktracker.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetrofitBook {

    // TODO: to make this class suitable for Adapter.       Made it suitable for my Retrofit Adapter
    // TODO: make progress bar
    // TODO: turn RetrofitBook to Book. Use MyRecyclerAdapter
    // TODO: to take care of List<String> authors
    // TODO: receive imageUrl from BookApi... HOW?


    @SerializedName("title")
    private String name;
    private List<String> authors;

    @SerializedName("imageLinks")
    private ImageLinks imageLinks;

    @Expose(serialize = false, deserialize = false)
    private int id;
//    @Expose(serialize = false, deserialize = false)
//    private String imageUrl;                                  //TODO: imageLinks
    @Expose(serialize = false, deserialize = false)
    private String author;
    @Expose(serialize = false, deserialize = false)
    private int rating;
    @Expose(serialize = false, deserialize = false)
    private String status;  //"READ", "TOREAD", "UNFINISHED"

    public String getTitle() {
        return name;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getImageUrl(){
        return imageLinks.getImageUrl();
    }

    public void setImageUrl(String url){
        imageLinks.setImageUrl(url);
    }
}
