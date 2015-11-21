package sii.letscode.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import sii.letscode.adapter.BookListAdapter;
import sii.letscode.model.BookListViewModel;

/**
 * Created by Hubert on 20.11.2015.
 */
public class MainWindowActivity extends Activity {
    ImageButton findButton;
    ImageButton addButton;
    ImageButton profileButton;
    ImageButton settingsButton;
    EditText searchTF;
    Button searchButton;
    ListView bookListView;
    BookListAdapter bookListAdapter;
    ArrayList<BookListViewModel> bookList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window_find);
        findButton = (ImageButton) findViewById(R.id.findButton);
        addButton = (ImageButton) findViewById(R.id.addButton);
        searchTF = (EditText) findViewById(R.id.searchTF);
        searchButton = (Button) findViewById(R.id.searchButton);
        bookListView = (ListView) findViewById(R.id.bookListView);
        bookList = new ArrayList<BookListViewModel>();
        bookListAdapter = new BookListAdapter(this, R.layout.booklistview_item_row, bookList);
        bookListView.setAdapter(bookListAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.main_window_find);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getBooks(searchTF.getText().toString());
            }
        });

    }

    public void getBooks(String text) {
        String SERVER_ADDRESS = "http://10.0.2.2:8080";
        String BOOKS = "/books";
        String BOOKS_QUERY = "text";
        String TOKEN = "token";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(BOOKS_QUERY, text);
        params.add(TOKEN, getToken());

        client.get(SERVER_ADDRESS + BOOKS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                    //tytuł autor nick ulica miasto
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BookListViewModel bookListViewModel = new BookListViewModel();
                        bookListViewModel.setAuthor(jsonObject.getString("author"));
                        bookListViewModel.setTitle(jsonObject.getString("title"));
                        bookListViewModel.setNick(jsonObject.getString("nick"));
                        bookListViewModel.setStreet(jsonObject.getString("city") + jsonObject.getString("street"));
                        bookListViewModel.setId(jsonObject.getInt("bookId"));
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