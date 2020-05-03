package com.example.booktracker.view.ui;

import androidx.appcompat.app.AppCompatActivity;

public class ChangedBookActivity  extends AppCompatActivity {

//    private BookViewModel mBookViewModel;
//    private SingleBookViewModel mSingleBookViewModel;
//    private ActivityBookBinding binding;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_book);
//
//        mSingleBookViewModel = new SingleBookViewModel(getBookFromIntent(),getApplication());
//        initializeViews();
//        binding.setBook(mSingleBookViewModel);
//    }
//
//    private Book getBookFromIntent(){
//        Intent incomingIntent = getIntent();
//
//        if (incomingIntent.hasExtra("selected_book")) {
//            if (incomingIntent.getBooleanExtra("isFromWeb", false)) {
//                mSingleBookViewModel.setMode(SingleBookViewModel.WEB_MODE);
//            }
//            mSingleBookViewModel.setMode(SingleBookViewModel.INFO_MODE);
//            return incomingIntent.getParcelableExtra("selected_book");
//        }
//        return new Book();
//    }
//
//    private void initializeViews() {
//        binding.toolbar.setTitle(getResources().getString(R.string.app_name));
//        setSupportActionBar(binding.toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.spinner_choices, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinner.setAdapter(adapter);
//    }
//
//    private void disableInteractions() {
//        binding.editText.setInputType(InputType.TYPE_NULL);
//        binding.editText2.setInputType(InputType.TYPE_NULL);
//        binding.imageViewBook.setEnabled(false);
//        binding.ratingBar.setIsIndicator(true);
//        binding.spinner.setEnabled(false);
//    }
//
//    private void enableInteractions() {
//        binding.editText.setInputType(InputType.TYPE_CLASS_TEXT);
//        binding.editText2.setInputType(InputType.TYPE_CLASS_TEXT);
//        binding.imageViewBook.setEnabled(true);
//        binding.ratingBar.setIsIndicator(false);
//        binding.spinner.setEnabled(true);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_book_menu, menu);
//        MenuItem editItem = menu.findItem(R.id.edit);
//        MenuItem deleteItem = menu.findItem(R.id.delete);
//        MenuItem saveItem = menu.findItem(R.id.save);
//
//        mSingleBookViewModel.getMode().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer mode) {
//                switch (mode){
//                    case SingleBookViewModel.WEB_MODE:
//                    case SingleBookViewModel.NEW_BOOK_MODE:
//                        editItem.setVisible(false);
//                        deleteItem.setVisible(false);
//                        break;
//                    case SingleBookViewModel.INFO_MODE:
//                        saveItem.setVisible(false);
//                        disableInteractions();
//                        break;
//                    case SingleBookViewModel.EDIT_MODE:
//                        deleteItem.setVisible(false);
//                        editItem.setVisible(false);
//                        break;
//                }
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.save:
//                saveBook();  //TODO: change to databinding, calling method from xml
//                return true;
//            case R.id.edit:
//                editBook(); //TODO: change to databinding, calling method from xml
//                return true;
//            case R.id.delete:
//                deleteBook(); //TODO: change to databinding, calling method from xml
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void editBook() {
//        binding.toolbar.setTitle("Edit book");
//        enableInteractions();
//        mSingleBookViewModel.editBook();
//        invalidateOptionsMenu(); //maybe it is not needed
//    }
//
//    private void deleteBook(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setMessage(R.string.dialog_delete_book)
//                .setPositiveButton(R.string.dialog_positive_option, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //mBookViewModel.delete(mBook);    //maybe change to singleBookViewModel
//                        mSingleBookViewModel.deleteBook();
//                        finish();
//                    }
//                })
//                .setNegativeButton(R.string.dialog_negative_option, null);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//
//    private void saveBook(){
//        mSingleBookViewModel.saveBook();
//        finish();
//    }
}
