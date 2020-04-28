package com.example.booktracker.view.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.example.booktracker.view.adapter.MyRecyclerAdapter;
import com.example.booktracker.models.Book;
import com.example.booktracker.viewModels.BookViewModel;
import com.example.booktracker.R;

import java.util.ArrayList;
import java.util.List;

public class BooksListFragment extends Fragment implements MyRecyclerAdapter.OnBookListener {

    private BookViewModel mBookViewModel;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;
    private String bookType;

    public BooksListFragment() {
    }


    public void setTypeOfBooks(String bookType){
        this.bookType = bookType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_books, container, false);

        initializeRecyclerView(rootView);

        loadDataToRecyclerView();

        return rootView;
    }

    private void initializeRecyclerView(View rootView){
        mRecyclerAdapter = new MyRecyclerAdapter(new ArrayList<Book>(), getActivity(), this);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private void loadDataToRecyclerView(){
        mBookViewModel = new ViewModelProvider(getActivity()).get(BookViewModel.class);
        mBookViewModel.getBooksByStatus(bookType).observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                mRecyclerAdapter.setData(books);
                mRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onBookClicked(int position) {
        Intent intent = new Intent(getContext(), BookActivity.class);
        Book selectedBook = mRecyclerAdapter.getBookAt(position);
        intent.putExtra("selected_book", selectedBook);
        startActivity(intent);
    }
}
