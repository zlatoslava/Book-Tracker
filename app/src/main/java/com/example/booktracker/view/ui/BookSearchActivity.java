package com.example.booktracker.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.booktracker.view.adapter.MyRecyclerAdapter;
import com.example.booktracker.models.Book;
import com.example.booktracker.models.Result;
import com.example.booktracker.models.Results;
import com.example.booktracker.R;
import com.example.booktracker.data.remote.RetrofitApi;
import com.example.booktracker.data.remote.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSearchActivity extends AppCompatActivity implements MyRecyclerAdapter.OnBookListener {

    private static final String TAG = "mtag";

    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private ArrayList<Book> mBooks;
    private MyRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        mBooks = new ArrayList<>();

        initializeViews();

    }

    private void initializeViews(){
        Toolbar toolbar = findViewById(R.id.toolbar_search_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recycler_view_search_activity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyRecyclerAdapter(mBooks, this, this);
        mRecyclerView.setAdapter(mAdapter);

        mSearchView = findViewById(R.id.searchView_search_activity);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doRetrofitCall(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void doRetrofitCall(String query) {

        final RetrofitApi retrofitApi = RetrofitInstance.getInstance().create(RetrofitApi.class);
        Call<Results> call = retrofitApi.getBooks(query);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if(response.body() == null || response.body().getTotalItems() == 0){
                    Toast.makeText(BookSearchActivity.this, "Results is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Result> results = response.body().getItems();

                if(results == null || results.size() < 1){
                    Toast.makeText(BookSearchActivity.this, "Result is empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                for(Result result: results) {
                    Log.d(TAG, "onResponse: " + result.getBook().getName());
                    mBooks.add(result.getBook());
                    Log.d(TAG, "onResponse: " + mBooks.size());

                }

                mAdapter.setData(mBooks);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Toast.makeText(BookSearchActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBookClicked(int position) {
            Intent intent = new Intent(this, BookActivity.class);
            Book selectedBook = mAdapter.getBookAt(position);
            selectedBook.setStatus("READ");
            intent.putExtra("selected_book", selectedBook);
            intent.putExtra("isFromWeb", true);
            startActivity(intent);
            finish();
    }
}
