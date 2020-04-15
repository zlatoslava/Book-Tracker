package com.example.booktracker.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booktracker.Models.Book;
import com.example.booktracker.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<Book> mBooks;
    private List<Book> mBookListFull;
    private Context mContext;

    public MyRecyclerAdapter(List<Book> books, Context context) {
        mBooks = books;
        mContext = context;
        mBookListFull = new ArrayList<>(mBooks);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = mBooks.get(position);

//        Uri imageUri = Uri.parse(book.getImageUrl());
        Uri imageUri = Uri.parse("book.getImageUrl()");

        Glide.with(mContext)
                .load(imageUri)
                .centerCrop()
                .error(R.drawable.book_cover_placeholder)
                .into(holder.bookImage);

        holder.bookName.setText(book.getName());
        holder.bookAuthor.setText(book.getAuthor());
        holder.ratingBar.setNumStars(book.getRating());
    }


    @Override
    public int getItemCount() {
        return mBooks.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookName;
        TextView bookAuthor;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image);
            bookName = itemView.findViewById(R.id.book_name);
            bookAuthor = itemView.findViewById(R.id.book_author);
            ratingBar = itemView.findViewById(R.id.book_rating);
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
                    if (book.getAuthor().toLowerCase().contains(filterPattern) ||
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
}
