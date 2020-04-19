package com.example.booktracker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String imageUrl;
    private String name;
    private String author;
    private int rating;
    private String status;  //"READ", "TOREAD", "UNFINISHED"

    public Book(String imageUrl, String name, String author, int rating, String status) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.author = author;
        this.rating = rating;
        this.status = status;
    }

    @Ignore
    public Book() {
    }

    protected Book(Parcel in){
        id = in.readInt();
        imageUrl = in.readString();
        name = in.readString();
        author = in.readString();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeInt(rating);
        dest.writeString(status);
    }
}
