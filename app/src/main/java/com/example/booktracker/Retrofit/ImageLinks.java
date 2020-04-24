package com.example.booktracker.Retrofit;

import com.google.gson.annotations.SerializedName;

public class ImageLinks {
    @SerializedName("smallThumbnail")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
