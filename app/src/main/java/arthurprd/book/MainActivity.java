package arthurprd.book;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import arthurprd.book.Adapter.BooksAdapter;
import arthurprd.book.Adapter.DividerDecoration;
import arthurprd.book.Helper.HelperFunction;
import arthurprd.book.Model.Book;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public int TO_FORM = 1;
    @BindView(R.id.recyclerBook)
    RecyclerView recyclerBook;
    @BindView(R.id.fab)
    FloatingActionButton btnAdd;
    private List<Book> bookList = new ArrayList<Book>();
    private BooksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Books Catalog");

        mAdapter = new BooksAdapter(this, bookList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerBook.setLayoutManager(mLayoutManager);
        recyclerBook.setItemAnimator(new DefaultItemAnimator());
        recyclerBook.addItemDecoration(new DividerDecoration(this));

        recyclerBook.setAdapter(mAdapter);
        recyclerBook.addOnItemTouchListener(new HelperFunction.
                RecyclerTouchListener(this, recyclerBook,
                new HelperFunction.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(MainActivity.this, BookFormActivity.class);
                        i.putExtra("bookEdit", bookList.get(position));
                        startActivity(i);
                    }

                    @Override
                    public void onLongClick(View view, final int position) {
                        final Book book = bookList.get(position);
                        AlertDialog dialog = new AlertDialog.
                                Builder(MainActivity.this).setTitle("Delete")
                                .setMessage("Are you sure to delete " + book.getBook_title() + " ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        bookList.remove(book);
                                        mAdapter.notifyItemRemoved(position);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .create();
                        dialog.show();
                    }
                }));

        prepareBookData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, BookFormActivity.class);
                startActivityForResult(i, TO_FORM);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_search:
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) item.getActionView();

                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setOnQueryTextListener(MainActivity.this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TO_FORM) {
            Book bookForm = (Book) data.getExtras().getSerializable("book");
            bookList.add(bookForm);
            Toast.makeText(this, "Book " + bookForm.getBook_title() + " successfully added", Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void prepareBookData() {
        Book book = new Book("9780439064873", "Harry Potter And The Chamber Of Secrets", "J.K. Rowling", 2000, "Fantasy, Thriller, Mystery", "This is some synopsis", R.drawable.images);
        bookList.add(book);

        book = new Book("9780316015844", "Twilight (The Twilight Saga, Book 1)", "Stephanie Meyer", 2006, "Fantasy, Drama, Romance", "This is some synopsis", R.drawable.images);
        bookList.add(book);

        book = new Book("9781484724989", "Journey to Star Wars: The Force Awakens Lost Stars", "Claudia Gray", 2015, "Action, Thriller, Science Fiction", "This is some synopsis", R.drawable.images);
        bookList.add(book);

        book = new Book("9780439136365", "Harry Potter and the Prisoner of Azkaban", "J.K. Rowling", 2001, "Fantasy, Thriller, Mystery", "This is some synopsis", R.drawable.images);
        bookList.add(book);

        mAdapter.notifyDataSetChanged();
    }
}
