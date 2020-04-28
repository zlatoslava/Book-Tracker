package com.example.booktracker.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.DBUtil;

import com.bumptech.glide.Glide;
import com.example.booktracker.databinding.BookItemBinding;
import com.example.booktracker.models.Book;
import com.example.booktracker.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<Book> mBooks;
    private List<Book> mBookListFull;
    private Context mContext;
    private OnBookListener mOnBookListener;

    public MyRecyclerAdapter(List<Book> books, Context context, OnBookListener onBookListener) {
        mBooks = books;
        mContext = context;
        mBookListFull = new ArrayList<>(mBooks);
        mOnBookListener = onBookListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.book_item, parent, false);
        return new MyViewHolder(binding, mOnBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = mBooks.get(position);
        holder.bind(book);
    }


    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public Book getBookAt(int position) {
        return mBooks.get(position);
    }

    public void setData(List<Book> books) {
        mBooks = books;
        mBookListFull.addAll(books);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final BookItemBinding binding;
        OnBookListener onBookListener;

        public MyViewHolder(@NonNull BookItemBinding binding, OnBookListener onBookListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onBookListener = onBookListener;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBookListener.onBookClicked(getAdapterPosition());
        }

        public void bind(Book book){
            binding.setBook(book);
            binding.executePendingBindings();
        }
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mBookListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Book book : mBookListFull) {
                    if (book.getAuthors().toString().toLowerCase().contains(filterPattern) ||   //TODO: check if right
                            book.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mBooks.clear();
            mBooks.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnBookListener {
        void onBookClicked(int position);
    }
}
