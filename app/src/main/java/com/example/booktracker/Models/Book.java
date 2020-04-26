package com.example.booktracker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.booktracker.Persistance.Converters;
import com.example.booktracker.Retrofit.ImageLinks;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "books")
public class Book implements Parcelable {

    @Expose(serialize = false, deserialize = false)
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("imageLinks")
    @Embedded
    private ImageLinks imageLinks;
//    private String imageUrl;
    @SerializedName("title")
    private String name;
    @SerializedName("authors")
    @TypeConverters(Converters.class)
    private List<String> authors = new ArrayList<>();
//    private String author;
    @Expose(serialize = false, deserialize = false)
    private int rating;
    @Expose(serialize = false, deserialize = false)
    private String status;  //"READ", "TOREAD", "UNFINISHED"

    public Book(ImageLinks imageLinks, String name, List<String> authors, int rating, String status) {
        this.imageLinks = imageLinks;
        this.name = name;
        this.authors = authors;
        this.rating = rating;
        this.status = status;
    }

    @Ignore
    public Book() {
    }

    protected Book(Parcel in){
        id = in.readInt();
        imageLinks = in.readParcelable(ImageLinks.class.getClassLoader());
        name = in.readString();
        in.readStringList(authors);
        rating = in.readInt();
        status = in.readString();
    }


    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
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

    public String getImageUrl(){
        return imageLinks.getImageUrl();
    }

    public void setImageUrl(String url){
        imageLinks.setImageUrl(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(imageLinks, flags);
        dest.writeString(name);
        dest.writeStringList(authors);
        dest.writeInt(rating);
        dest.writeString(status);
    }
}
