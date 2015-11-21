package sii.letscode.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hubert on 21.11.2015.
 */
public class AddBookControler {
    private String SERVER_ADDRESS = "http://10.0.2.2:8080";
    private String ADD_BOOK = "/addBook";
    private MainWindowActivity mwa;


    AddBookControler(MainWindowActivity mwa){
        this.mwa = mwa;
    }

    public void addBook(String returnTitle, EditText author, String returnPublicationDate){
        Log.d("inside add book", "ok");


        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonObject = generateJSONObject(returnTitle, author.getText().toString(), returnPublicationDate);
        if(jsonObject == null)
            return;
        StringEntity entity = generateStringEntity(jsonObject);

        client.post(mwa.getApplicationContext(), SERVER_ADDRESS + ADD_BOOK, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Context context = mwa.getApplicationContext();
                CharSequence text = "Dodano książkę";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Log.d("POST", "OK");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Context context = mwa.getApplicationContext();
                CharSequence text = "Wystąpił błąd";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                String info = new String(bytes);
                Log.e("ADDBOOKERROR", "no i dupa " + i + " " + info);
            }
        });
    }

    /*public void addBookAuthor(){
        if(authorList.get(authorList.size()-1).getText().toString().isEmpty()){
            return;
        } else{
            LinearLayout lastEl = (LinearLayout) mwa.findViewById(R.id.lastAuthor);
            LinearLayout parent = (LinearLayout) mwa.findViewById(R.id.addBookMainLayout);
            EditText newField = new EditText(parent.getContext());
            newField.setText(authorList.get(authorList.size()-1).getText().toString());
            authorList.get(authorList.size()-1).setText("");
            authorList.add(authorList.size()-1, newField);
            parent.addView(newField, parent.getChildCount()-1);
        }
    }*/

    private String getToken(){
        String[] projection = {"value"};
        Cursor query = mwa.getContentResolver().query(Uri.parse("content://sii.letscode.contentProviders/properties")
                , projection, "", new String[0], "");
        String token = null;
        if (query.moveToFirst()){
            token = query.getString(query.getColumnIndex("value"));
        }
        return token;
    }

    private JSONObject generateJSONObject(String title, String author, String publicationDate){
        JSONObject jsonObject = new JSONObject();

        String token = getToken();
        try {
            jsonObject.put("token", token);
            jsonObject.put("authors", author);
            jsonObject.put("title", title);
            jsonObject.put("year", publicationDate);
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "JSON error: " + e.getMessage());
            return null;
        }
        Log.d("JSON", jsonObject.toString());
        return jsonObject;
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
