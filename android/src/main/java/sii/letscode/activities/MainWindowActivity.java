package sii.letscode.activities;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import sii.letscode.adapter.BookListAdapter;
import sii.letscode.adapter.BookListNot1Adapter;
import sii.letscode.adapter.BookListNot2Adapter;
import sii.letscode.adapter.BookOwnerListAdapter;
import sii.letscode.model.BookListNot1ViewModel;
import sii.letscode.model.BookListNot2ViewModel;
import sii.letscode.model.BookListViewModel;
import sii.letscode.model.BookOwnerListViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hubert on 20.11.2015.
 */
public class MainWindowActivity extends Activity {
    private ImageButton findButton;
    private ImageButton addButton;
    private ImageButton profileButton;
    private ImageButton notificationsButton;
    private AddBookControler addBookControler;
    //add book

    private EditText titleTF;
    private EditText author;
    private EditText publicationDate;
    private Button addBookButton;
    private ImageButton addAuthorButton;
    private ToggleButton notiToggle;

    EditText searchTF;
    Button searchButton;
    ListView bookListView;
    BookListAdapter bookListAdapter;
    ArrayList<BookListViewModel> bookList;
    ViewFlipper vf;
    ViewFlipper notificationsVf;

    ListView bookOwnerListView;
    BookOwnerListAdapter bookOwnerListAdapter;
    ArrayList<BookOwnerListViewModel> bookOwnerList;

    ListView ownBookingListView;
    BookListNot1Adapter ownBookingListAdapter;
    ArrayList<BookListNot1ViewModel> ownBookingList;

    ListView fBookingListView;
    BookListNot2Adapter fBookingListAdapter;
    ArrayList<BookListNot2ViewModel> fBookingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window);
        vf = (ViewFlipper)findViewById(R.id.vf);
        findButton = (ImageButton) findViewById(R.id.findButton);
        addButton = (ImageButton) findViewById(R.id.addButton);
        profileButton = (ImageButton) findViewById(R.id.profileButton);
        notificationsButton = (ImageButton) findViewById(R.id.notificationsButton);
        goToFind();

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToAddBook();
            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToFind();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToProfile();
            }
        });

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToNotifications();
            }
        });

    }

    private MainWindowActivity getThis(){
        return this;
    }

    public void goToNotifications(){
        vf.setDisplayedChild(3);
        notiToggle = (ToggleButton) findViewById(R.id.notiToggle);
        notificationsVf = (ViewFlipper)findViewById(R.id.notificationsVf);

        notiToggle.setText("Zamówione przeze mnie");
        notiToggle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(notiToggle.isChecked()){
                    notiToggle.setText("Zamówienia na moje książki");
                    notificationsVf.setDisplayedChild(0);

                    fBookingListView = (ListView) findViewById(R.id.fBookingListView);
                    fBookingList = new ArrayList<BookListNot2ViewModel>();
                    fBookingListAdapter = new BookListNot2Adapter(getApplicationContext(), R.layout.booklistview_item_not2, fBookingList, getThis());
                    fBookingListView.setAdapter(fBookingListAdapter);

                    getFBorrow();
                }else{
                    notiToggle.setText("Zamówione przeze mnie");
                    notificationsVf.setDisplayedChild(1);

                    ownBookingListView = (ListView) findViewById(R.id.ownBookingListView);
                    ownBookingList = new ArrayList<BookListNot1ViewModel>();
                    ownBookingListAdapter = new BookListNot1Adapter(getApplicationContext(), R.layout.booklistview_item_not1, ownBookingList, getThis());
                    ownBookingListView.setAdapter(ownBookingListAdapter);

                    getOwnBorrow();
                }
            }
        });
    }

    public void goToAddBook(){
        vf.setDisplayedChild(1);
        titleTF = (EditText) findViewById(R.id.bookTitleTF);
        author = (EditText) findViewById(R.id.bookAuthorTF);
        publicationDate = (EditText) findViewById(R.id.publicationDateTF);
        addBookButton = (Button) findViewById(R.id.addBookButton);
        addBookControler = new AddBookControler(this);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("ADD book", "click");
                addBookControler.addBook(titleTF.getText().toString(), author, publicationDate.getText().toString());
                titleTF.setText("");
                    author.setText("");
                publicationDate.setText("");
            }
        });

    }

    public void goToProfile(){
        vf.setDisplayedChild(2);

        bookOwnerListView = (ListView) findViewById(R.id.bookOwnerListView);
        bookOwnerList = new ArrayList<BookOwnerListViewModel>();
        bookOwnerListAdapter = new BookOwnerListAdapter(getApplicationContext(), R.layout.booklistview_item_row_owner, bookOwnerList, this);
        bookOwnerListView.setAdapter(bookOwnerListAdapter);

        getOwnBooks();
    }

    public void goToFind(){
        vf.setDisplayedChild(0);
        searchTF = (EditText) findViewById(R.id.searchTF);
        searchButton = (Button) findViewById(R.id.searchButton);
        bookListView = (ListView) findViewById(R.id.bookListView);
        bookList = new ArrayList<BookListViewModel>();
        bookListAdapter = new BookListAdapter(this, R.layout.booklistview_item_row, bookList);
        bookListView.setAdapter(bookListAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getBooks(searchTF.getText().toString());
            }
        });
    }

    public void getBooks(String text) {
        String SERVER_ADDRESS = "http://10.0.2.2:8080";
        String BOOKS = "/searchBooks";
        String BOOKS_QUERY = "query";
        String TOKEN = "token";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(BOOKS_QUERY, text);
        params.add(TOKEN, getToken());

        client.get(SERVER_ADDRESS + BOOKS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Log.e(this.getClass().getName(), new String(responseBody));
                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                    //tytuł autor nick ulica miasto
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BookListViewModel bookListViewModel = new BookListViewModel();
                        bookListViewModel.setAuthor(jsonObject.getString("author"));
                        bookListViewModel.setTitle(jsonObject.getString("title"));
                        bookListViewModel.setNick(jsonObject.getString("nick"));
                        bookListViewModel.setStreet(jsonObject.getString("city") + jsonObject.getString("street"));
                        bookListViewModel.setId(jsonObject.getString("bookId"));
                        bookList.add(bookListViewModel);
                    }
                    bookListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "JSON ERROR: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e(this.getClass().getName(), "JSON error: " + statusCode + " " + new String(responseBody));
            }
        });
    }

    public void getOwnBooks() {
        String SERVER_ADDRESS = "http://10.0.2.2:8080";
        String BOOKS = "/books";
        String TOKEN = "token";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(TOKEN, getToken());

        client.get(SERVER_ADDRESS + BOOKS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Log.e(this.getClass().getName(), new String(responseBody));
                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                    //tytuł autor nick ulica miasto
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BookOwnerListViewModel bookListViewModel = new BookOwnerListViewModel();
                        bookListViewModel.setAuthor(jsonObject.getString("author"));
                        bookListViewModel.setTitle(jsonObject.getString("title"));
                        bookListViewModel.setYear(jsonObject.getString("year"));
                        bookListViewModel.setId(jsonObject.getString("bookId"));
                        bookOwnerList.add(bookListViewModel);
                    }
                    bookOwnerListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "JSON ERROR: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e(this.getClass().getName(), "JSON error: " + statusCode + " " + new String(responseBody));
            }
        });
    }

    public void getOwnBorrow() {
        String SERVER_ADDRESS = "http://10.0.2.2:8080";
        String BOOKS = "/getSentRequests";
        String TOKEN = "token";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(TOKEN, getToken());

        client.get(SERVER_ADDRESS + BOOKS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                    //tytuł autor nick ulica miasto
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BookListNot1ViewModel bookListViewModel = new BookListNot1ViewModel();
                        bookListViewModel.setTitle(jsonObject.getString("book"));
                        bookListViewModel.setNick(jsonObject.getString("sender"));
                        bookListViewModel.setStatus(jsonObject.getString("status"));
                        bookListViewModel.setId(jsonObject.getString("id"));
                        ownBookingList.add(bookListViewModel);
                    }
                    ownBookingListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "JSON ERROR: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e(this.getClass().getName(), "JSON error: " + statusCode + " " + new String(responseBody));
            }
        });
    }

    public void getFBorrow() {
        String SERVER_ADDRESS = "http://10.0.2.2:8080";
        String BOOKS = "/getReceivedRequests";
        String TOKEN = "token";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(TOKEN, getToken());

        client.get(SERVER_ADDRESS + BOOKS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                    Log.d("JSON", new String(responseBody));
                    //tytuł autor nick ulica miasto
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BookListNot2ViewModel bookListViewModel = new BookListNot2ViewModel();
                        bookListViewModel.setTitle(jsonObject.getString("book"));
                        bookListViewModel.setNick(jsonObject.getString("sender"));
                        bookListViewModel.setStatus(jsonObject.getString("status"));
                        bookListViewModel.setId(jsonObject.getString("id"));
                        fBookingList.add(bookListViewModel);
                    }
                    fBookingListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "JSON ERROR: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e(this.getClass().getName(), "JSON error: " + statusCode + " " + new String(responseBody));
            }
        });
    }

    private String getToken(){
        String[] projection = {"value"};
        Cursor query = getContentResolver().query(Uri.parse("content://sii.letscode.contentProviders/properties")
                , projection, "", new String[0], "");
        String token = null;
        if (query.moveToFirst()){
            token = query.getString(query.getColumnIndex("value"));
        }
        return token;
    }
}