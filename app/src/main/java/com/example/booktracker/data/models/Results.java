package com.example.booktracker.data.models;

import java.util.List;

public class Results {
    int totalItems;
    List<Result> items;

    public int getTotalItems() {
        return totalItems;
    }

    public List<Result> getItems() {
        return items;
    }
}
