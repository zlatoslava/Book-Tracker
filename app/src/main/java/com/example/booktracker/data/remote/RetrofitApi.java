package com.example.booktracker.data.remote;

import com.example.booktracker.models.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {
    @GET("volumes?maxResults=20")
    Call<Results> getBooks(@Query("q") String search);
}
