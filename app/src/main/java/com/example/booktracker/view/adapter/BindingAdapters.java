package com.example.booktracker.view.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.bumptech.glide.Glide;
import com.example.booktracker.R;

import java.util.ArrayList;
import java.util.List;

public class BindingAdapters {

    @BindingConversion
    public static String convertAuthorsToString(List<String> authors) {
        StringBuilder sb = new StringBuilder();
        for (String author : authors) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(author);
        }
        return sb.toString();
    }

    @BindingAdapter("android:src")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Log.d("vm", "loadImage: " + imageUrl);
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.book_cover_placeholder)
                .into(imageView);
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static List<String> convertStringToAuthors(EditText editText) {
        ArrayList<String> authorsList = new ArrayList<>();
        authorsList.add(editText.getText().toString());
        return authorsList;
    }

    @BindingAdapter(value = {"selected_value", "selected_valueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(Spinner spinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != null) {;
            switch (newSelectedValue) {
                case "READ":
                    spinner.setSelection(0, true);
                    break;
                case "TOREAD":
                    spinner.setSelection(2);
                    break;
                case "UNFINISHED":
                    spinner.setSelection(1);
                    break;
                default:
                    spinner.setSelection(0);
            }
        }
    }

    @InverseBindingAdapter(attribute = "selected_value", event = "selected_valueAttrChanged")
    public static String captureSelectedValue(Spinner spinner) {
        switch (spinner.getSelectedItem().toString()) {
            case "Finished book":
                return "READ";
            case "Unfinished book":
                return "UNFINISHED";
            case "Book to read":
                return "TOREAD";
        }
        return "READ";
    }
}
