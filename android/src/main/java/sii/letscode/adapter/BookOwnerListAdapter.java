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
import android.widget.ImageButton;
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
import sii.letscode.activities.MainWindowActivity;
import sii.letscode.activities.R;
import sii.letscode.model.BookListViewModel;
import sii.letscode.model.BookOwnerListViewModel;

/**
 * Created by dominik on 21.11.15.
 */
public class BookOwnerListAdapter extends ArrayAdapter<BookOwnerListViewModel> {
    List<BookOwnerListViewModel> data;
    Context context;
    int resource;
    MainWindowActivity mwa;

    public BookOwnerListAdapter(Context context, int resource, List<BookOwnerListViewModel> objects, MainWindowActivity mwa) {
        super(context, resource, objects);
        this.data = objects;
        this.context = context;
        this.resource = resource;
        this.mwa = mwa;
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
            holder.lvOwnerTitle = (TextView)row.findViewById(R.id.lvOwnerTitle);
            holder.lvOwnerAuthor = (TextView)row.findViewById(R.id.lvOwnerAuthor);
            holder.lvOwnerYear = (TextView)row.findViewById(R.id.lvOwnerYear);
            holder.bOwnerRemove = (ImageButton)row.findViewById(R.id.bOwnerRemove);

            row.setTag(holder);
        }
        else
        {
            holder = (BookHolder)row.getTag();
        }

        final BookOwnerListViewModel bookListViewModel = data.get(position);
        holder.lvOwnerTitle.setText(bookListViewModel.getTitle());
        holder.lvOwnerAuthor.setText(bookListViewModel.getAuthor());
        holder.lvOwnerYear.setText(bookListViewModel.getYear());

        holder.bOwnerRemove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getOwnBooks(bookListViewModel.getId());
            }
        });

        return row;
    }

    private static class BookHolder {
        TextView lvOwnerTitle;
        TextView lvOwnerAuthor;
        TextView lvOwnerYear;
        ImageButton bOwnerRemove;
    }

    public void getOwnBooks(String bookId) {
        String SERVER_ADDRESS = "http://10.0.2.2:8080";
        String BOOKS = "/remove";
        String TOKEN = "token";
        String BOOK_ID = "bookId";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(TOKEN, getToken());
        params.add(BOOK_ID, bookId);

        Log.e(this.getClass().getName(), "LECI");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TOKEN, getToken());
            jsonObject.put(BOOK_ID, bookId);
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
                CharSequence text = "Usunięto książkę";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                mwa.goToProfile();

                Log.e(this.getClass().getName(), "JSON OK");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Context context = getContext();
                CharSequence text = "Pojawił się błąd podczas usuwania książki";
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
