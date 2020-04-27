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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booktracker.models.Book;
import com.example.booktracker.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<Book> mBooks;
    private List<Book> mBookListFull;
    private StringBuilder authorsString;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_item, parent, false);
        return new MyViewHolder(view, mOnBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = mBooks.get(position);

        Uri imageUri = Uri.parse(book.getImageUrl());
//        Uri imageUri = Uri.parse("book.getImageUrl()"); //TODO: change

        Glide.with(mContext)
                .load(imageUri)
                .centerCrop()
                .error(R.drawable.book_cover_placeholder)
                .into(holder.bookImage);

        holder.bookName.setText(book.getName());

        authorsString = new StringBuilder();//TODO: maybe change
        ArrayList<String> authors = (ArrayList<String>) book.getAuthors();
        if (!authors.isEmpty()) {
            for (String author : authors) {
                authorsString.append(author + ", ");
            }
        }
        holder.bookAuthor.setText(authorsString.toString());
        holder.ratingBar.setNumStars(book.getRating());
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
        ImageView bookImage;
        TextView bookName;
        TextView bookAuthor;
        RatingBar ratingBar;

        OnBookListener onBookListener;


        public MyViewHolder(@NonNull View itemView, OnBookListener onBookListener) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image);
            bookName = itemView.findViewById(R.id.book_name);
            bookAuthor = itemView.findViewById(R.id.book_author);
            ratingBar = itemView.findViewById(R.id.book_rating);
            this.onBookListener = onBookListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBookListener.onBookClicked(getAdapterPosition());
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
                    if (authorsString.toString().toLowerCase().contains(filterPattern) ||
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
