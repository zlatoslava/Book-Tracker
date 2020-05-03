package com.example.booktracker.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.booktracker.R;
import com.example.booktracker.data.DataBaseRepository;
import com.example.booktracker.models.Book;

public class SingleBookViewModel extends AndroidViewModel {

    public static final int EDIT_MODE = 0;
    public static final int INFO_MODE = 1;
    public static final int NEW_BOOK_MODE = 2;
    public static final int WEB_MODE = 3;

    private MutableLiveData<Integer> mMode = new MutableLiveData<>();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private boolean mIsNewBook = false;

    private DataBaseRepository mRepository;

    private Book mBook;

    public SingleBookViewModel(Application application) {
        super(application);
        mRepository = new DataBaseRepository(application);
    }

    public void setNewBook(Book book){
        if(book.getName().length() == 0){
            mIsNewBook = true;
        }
        mBook = book;
    }

    public LiveData<Integer> getMode(){
        return mMode;
    }

    public void setMode(Integer mode){
        mMode.setValue(mode);
    }

    public LiveData<String> getToastMessage() { return toastMessage; }

    public void deleteBook(){
        mRepository.deleteBooksTask(mBook);
        toastMessage.setValue(getApplication().getString(R.string.toast_delete_book));
    }

    public void editBook(){
        mMode.setValue(EDIT_MODE);
    }

    public void saveBook(){
        if(validateUserInput()) {
            if (mIsNewBook || mMode.getValue() == WEB_MODE) {
                mRepository.insertBookTask(mBook);
                mIsNewBook = false;
            } else {
                mRepository.updateBooksTask(mBook);
            }
            mMode.setValue(INFO_MODE);
        }
    }

    private boolean validateUserInput(){
        toastMessage.setValue(getApplication().getString(R.string.toast_saved));
        if (mBook.getName().trim().isEmpty() || mBook.getAuthors().get(0).trim().isEmpty()) {
            toastMessage.setValue(getApplication().getString(R.string.toast_input_data));
            return false;
        }

        if (mBook.getRating() == 0) {
            toastMessage.setValue(getApplication().getString(R.string.toast_input_rating));
            return false;
        }
        return true;
    }

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book book) {
        mBook = book;
    }

    public void setImageUri(String imageUri){
        mBook.setImageUrl(imageUri);
    }

}
