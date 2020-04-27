package com.example.booktracker.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.booktracker.models.Book;
import com.example.booktracker.data.local.BookDao;
import com.example.booktracker.data.local.BooksDatabase;

import java.util.List;

public class DataBaseRepository {

    private BookDao mBookDao;

    public DataBaseRepository(Application application) {
        BooksDatabase mBooksDatabase = BooksDatabase.getInstance(application);
        mBookDao = mBooksDatabase.getBookDao();
    }

    public LiveData<List<Book>> getAllBooks() {
        return mBookDao.getAllBooks();
    }

//    public LiveData<List<Book>> getBooksByAuthorTask(String author){
//        return mBookDao.getBooksByAuthor(author);
//    }

    public LiveData<List<Book>> getBooksByStatusTask(String status){
        return mBookDao.getBooksByStatus(status);
    }

    public void insertBookTask(Book book) {
        new InsertAsyncTask(mBookDao).execute(book);
    }

    public void deleteBooksTask(Book... books){
        new DeleteAsyncTask(mBookDao).execute(books);
    }

    public void updateBooksTask(Book... books){
        new UpdateAsyncTask(mBookDao).execute(books);
    }


    private static class InsertAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao mAsyncTaskDao;

        public InsertAsyncTask(BookDao asyncTaskDao) {
            mAsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            mAsyncTaskDao.insertBook(books[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Book, Void, Void>{

        private BookDao mAsyncTaskDao;

        public DeleteAsyncTask(BookDao asyncTaskDao) {
            mAsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            mAsyncTaskDao.deleteBooks(books);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Book, Void, Void>{

        private BookDao mAsyncTaskDao;

        public UpdateAsyncTask(BookDao asyncTaskDao) {
            mAsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            mAsyncTaskDao.updateBooks(books);
            return null;
        }
    }
}