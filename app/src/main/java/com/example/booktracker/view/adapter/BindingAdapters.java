package com.example.booktracker.view.adapter;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;

import com.bumptech.glide.Glide;
import com.example.booktracker.R;

import java.util.List;

public class BindingAdapters {

    @BindingConversion
    public static String convertAuthorsToString(List<String> authors) {
        StringBuilder sb = new StringBuilder();
        for (String author: authors) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(author);
        }
        return sb.toString();
    }

    @BindingAdapter("android:src")
    public static void loadImage(ImageView imageView, String imageUrl){
        Log.d("mtag", "loadImage: " + imageUrl);
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.book_cover_placeholder)
                .into(imageView);
    }
}
