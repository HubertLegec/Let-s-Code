package sii.letscode.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

public class StartActivity extends Activity {
    TextView registerTextView;
    Button loggRegButton;
    EditText emailTf;
    EditText passwordTf;
    private EditText nick;
    private EditText city;
    private EditText street;
    private EditText houseNumber;
    private Button nextButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContentResolver().delete(Uri.parse("content://sii.letscode.contentProviders/properties"), null, null);
        if(!isLoggedIn()) {
            setContentView(R.layout.start_activity);
            registerTextView = (TextView) findViewById(R.id.changeToRegister);
            registerTextView.setClickable(true);
            loggRegButton = (Button) findViewById(R.id.logButton);
            emailTf = (EditText) findViewById(R.id.emailTf);
            passwordTf = (EditText) findViewById(R.id.passwordTF);

            loggRegButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(loggRegButton.getText().toString().equals("Zaloguj")) {

                    } else{
                        registerUser(emailTf.getText().toString(), passwordTf.getText().toString());
                    }
                }
            });

            registerTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(loggRegButton.getText().toString().equals("Zaloguj")){
                        loggRegButton.setText("ZAREJESTRUJ");
                    }
                }
            });
        } else {
            Intent myIntent = new Intent(getWindow().getContext(), MainWindowActivity.class);
            super.onResume();
            startActivityForResult(myIntent, 0);
        }
    }


    private boolean isLoggedIn(){
        return false;
    }

    private String SERVER_ADDRESS = "http://10.0.2.2:8080";
    private String ADD_USER = "/addUser";
    private String USER_EMAIL = "email";
    private String USER_PASSWORD = "password";

    private void registerUser(String email, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(USER_EMAIL, email);
            jsonObject.put(USER_PASSWORD, password);
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "JSON error: " + e.getMessage());
            return;
        }
        StringEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            Log.e(this.getClass().getName(), "JSON error: " + e.getMessage());
            return;
        }
        client.post(getApplicationContext(), SERVER_ADDRESS + ADD_USER, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(this.getClass().getName(), new String(responseBody));
                ContentValues mNewValues = new ContentValues();
                mNewValues.put("id", "token");
                mNewValues.put("value", new String(responseBody));
                getContentResolver().insert(Uri.parse("content://sii.letscode.contentProviders/properties"), mNewValues);
                goToNextScreen();
                Log.e(this.getClass().getName(), "JSON OK");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e(this.getClass().getName(), "JSON error: " + statusCode + " " + new String(responseBody));
            }
        });
    }

    private static final String UPDATE_USER = "/updateUser";
    private static final String USER_NICK = "nick";
    private static final String USER_TOKEN = "token";
    private static final String USER_CITY = "city";
    private static final String USER_STREET = "street";
    private static final String USER_HOUSE_NUMBER = "nr";

    private void completeUserData(String nick, String city, String street, String houseNumber){
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonObject = new JSONObject();
        String[] projection = {"value"};
        Cursor query = getContentResolver().query(Uri.parse("content://sii.letscode.contentProviders/properties")
                , projection, "", new String[0], "");
        String token = null;
        if (query.moveToFirst()){
                token = query.getString(query.getColumnIndex("value"));
        }
        Log.d("T:   ", token + " " + nick + " " + city + " " + street);
        try {
            jsonObject.put(USER_TOKEN, token);
            jsonObject.put(USER_PASSWORD, "");
            jsonObject.put(USER_NICK, nick);
            jsonObject.put(USER_CITY, city);
            jsonObject.put(USER_STREET, street);
            jsonObject.put(USER_HOUSE_NUMBER, houseNumber);
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "JSON error: " + e.getMessage());
            return;
        }
        StringEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            Log.e(this.getClass().getName(), "JSON error: " + e.getMessage());
            return;
        }

        client.post(getApplicationContext(), SERVER_ADDRESS + UPDATE_USER, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                goToMainWindow();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e("POSTERROR", "no i dupa " + i);
            }
        });
    }

    private void goToNextScreen(){
        setContentView(R.layout.complete_user_data);
        nick = (EditText) findViewById(R.id.nickTF);
        city = (EditText) findViewById(R.id.cityTF);
        street = (EditText) findViewById(R.id.streetTF);
        houseNumber = (EditText) findViewById(R.id.houseNumberTF);
        nextButton = (Button) findViewById(R.id.confirmExtraDataButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                completeUserData(nick.getText().toString(), city.getText().toString(), street.getText().toString(), houseNumber.getText().toString());
            }
        });
    }

    private void goToMainWindow(){
        Intent myIntent = new Intent(getWindow().getContext(), MainWindowActivity.class);
        super.onResume();
        startActivityForResult(myIntent, 0);
    }
}
