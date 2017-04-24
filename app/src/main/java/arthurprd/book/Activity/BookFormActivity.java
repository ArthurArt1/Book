package arthurprd.book.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import arthurprd.book.Model.Book;
import arthurprd.book.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BookFormActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.editBookTitle)
    EditText editBookTitle;
    @BindView(R.id.editBookAuthor)
    EditText editBookAuthor;
    @BindView(R.id.editGenre)
    EditText editGenre;
    @BindView(R.id.editIsbn)
    EditText editISBN;
    @BindView(R.id.editPublishedYear)
    EditText editPublishYear;
    @BindView(R.id.editSynopsis)
    EditText editSynopsis;
    @BindView(R.id.btnSave)
    Button btnSave;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_form);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //tombol untuk back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        //untuk view detail dari buku yang sudah ada
        if (bundle != null) {
            book = (Book) bundle.getSerializable("bookEdit");
            editISBN.setText(book.getISBN());
            editPublishYear.setText(book.getPublished_year() + "");
            editBookAuthor.setText(book.getBook_author());
            editBookTitle.setText(book.getBook_title());
            editGenre.setText(book.getBook_genre());
            editSynopsis.setText(book.getBook_synopsis());
            btnSave.setEnabled(false);
            getSupportActionBar().setTitle(book.getBook_title());
        } else {
            //buku baru
            book = new Book();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    book.setISBN(editISBN.getText().toString());
                    book.setBook_title(editBookTitle.getText().toString());
                    book.setBook_author(editBookAuthor.getText().toString());
                    book.setPublished_year(Integer.parseInt(editPublishYear.getText().toString()));
                    book.setBook_genre(editGenre.getText().toString());
                    book.setBook_synopsis(editSynopsis.getText().toString().equals("") ? "-" :
                            editSynopsis.getText().toString());

                    Intent i = new Intent();
                    i.putExtra("book", book);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate() {
        boolean valid = true;

        String isbn = editISBN.getText().toString();
        String booktitle = editBookTitle.getText().toString();
        String bookauthor = editBookAuthor.getText().toString();
        String publishedYear = editPublishYear.getText().toString();

        if (isbn.isEmpty()) {
            editISBN.setError("Enter ISBN");
            valid = false;
        } else {
            editISBN.setError(null);
        }

        if (booktitle.isEmpty()) {
            editBookTitle.setError("Enter Book Title");
            valid = false;
        } else {
            editBookTitle.setError(null);
        }

        if (bookauthor.isEmpty()) {
            editBookAuthor.setError("Enter Book Author");
            valid = false;
        } else {
            editBookAuthor.setError(null);
        }

        if (publishedYear.isEmpty() || publishedYear.length() < 4) {
            editPublishYear.setError("Publish Year empty or must in yyyy format");
            valid = false;
        } else {
            editPublishYear.setError(null);
        }

        return valid;
    }

}