package com.example.booktracker.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.booktracker.databinding.ActivityBookSearchBinding;
import com.example.booktracker.view.adapter.MyRecyclerAdapter;
import com.example.booktracker.models.Book;
import com.example.booktracker.models.Result;
import com.example.booktracker.models.Results;
import com.example.booktracker.R;
import com.example.booktracker.data.remote.RetrofitApi;
import com.example.booktracker.data.remote.RetrofitInstance;
import com.example.booktracker.viewModels.BookSearchViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSearchActivity extends AppCompatActivity implements MyRecyclerAdapter.OnBookListener, SearchView.OnQueryTextListener {

    private static final String TAG = "mtag";

    ActivityBookSearchBinding binding;
    private MyRecyclerAdapter mAdapter;
    private BookSearchViewModel mBookSearchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBookSearchViewModel = new BookSearchViewModel();

        initializeViews();
    }

    private void initializeViews(){
        setSupportActionBar(binding.toolbarSearchActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.recyclerViewSearchActivity.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyRecyclerAdapter(new ArrayList<>(), this, this);
        binding.recyclerViewSearchActivity.setAdapter(mAdapter);

        binding.searchViewSearchActivity.onActionViewExpanded();
        binding.searchViewSearchActivity.setOnQueryTextListener(this);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        mBookSearchViewModel.getBooksFromWeb(query).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                mAdapter.setData(books);
                mAdapter.notifyDataSetChanged();
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
