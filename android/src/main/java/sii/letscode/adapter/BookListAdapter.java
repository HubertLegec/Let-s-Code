package sii.letscode.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import sii.letscode.activities.R;
import sii.letscode.model.BookListViewModel;

/**
 * Created by dominik on 21.11.15.
 */
public class BookListAdapter extends ArrayAdapter<BookListViewModel> {
    List<BookListViewModel> data;
    Context context;
    int resource;

    public BookListAdapter(Context context, int resource, List<BookListViewModel> objects) {
        super(context, resource, objects);
        this.data = objects;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BookHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(resource, parent, false);

            holder = new BookHolder();
            holder.lvTitle = (TextView)row.findViewById(R.id.lvTitle);
            holder.lvAuthor = (TextView)row.findViewById(R.id.lvAuthor);
            holder.lvNick = (TextView)row.findViewById(R.id.lvNick);
            holder.lvStreet = (TextView)row.findViewById(R.id.lvStreet);
            holder.bLend = (Button)row.findViewById(R.id.bLend);

            row.setTag(holder);
        }
        else
        {
            holder = (BookHolder)row.getTag();
        }

        final BookListViewModel bookListViewModel = data.get(position);
        holder.lvTitle.setText(bookListViewModel.getTitle());
        holder.lvAuthor.setText(bookListViewModel.getAuthor());
        holder.lvNick.setText(bookListViewModel.getNick());
        holder.lvStreet.setText(bookListViewModel.getStreet());

        holder.bLend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getBooks(bookListViewModel.getId());
            }
        });


        return row;
    }

    private static class BookHolder {
        TextView lvTitle;
        TextView lvAuthor;
        TextView lvNick;
        TextView lvStreet;
        Button bLend;
    }

    public void getBooks(String text) {
        String SERVER_ADDRESS = "http://10.0.2.2:8080";
        String BOOKS = "/addRequest";
        String BOOKS_ID = "bookId";
        String TOKEN = "token";

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(BOOKS_ID, text);
            jsonObject.put(TOKEN, getToken());
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "JSON error: " + e.getMessage());
            return;
        }
        StringEntity entity = generateStringEntity(jsonObject);
        if(entity == null)
            return;
        client.post(context, SERVER_ADDRESS + BOOKS, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Context context = getContext();
                CharSequence text = "Wypożyczono książkę";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Context context = getContext();
                CharSequence text = "Pojawił się błąd podczas wypożyczania książki";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Log.e(this.getClass().getName(), "JSON error: " + statusCode + " " + new String(responseBody));
            }
        });
    }

    private String getToken(){
        String[] projection = {"value"};
        Cursor query = getContext().getContentResolver().query(Uri.parse("content://sii.letscode.contentProviders/properties")
                , projection, "", new String[0], "");
        String token = null;
        if (query.moveToFirst()){
            token = query.getString(query.getColumnIndex("value"));
        }
        return token;
    }

    private StringEntity generateStringEntity(JSONObject obj){
        StringEntity entity;
        try {
            entity = new StringEntity(obj.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            Log.e(this.getClass().getName(), "JSON error: " + e.getMessage());
            return null;
        }

        return entity;
    }
}
