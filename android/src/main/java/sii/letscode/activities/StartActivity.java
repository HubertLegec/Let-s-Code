package sii.letscode.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import sii.letscode.contentProviders.DatabaseContentProvider;

public class StartActivity extends Activity {
    TextView registerTextView;
    Button loggRegButton;
    EditText emailTf;
    EditText passwordTf;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isLoggedIn()) {
            setContentView(R.layout.start_activity);
            registerTextView = (TextView) findViewById(R.id.changeToRegister);
            registerTextView.setClickable(true);
            loggRegButton = (Button) findViewById(R.id.logButton);
            emailTf = (EditText) findViewById(R.id.emailTf);
            passwordTf = (EditText) findViewById(R.id.passwordTF);

            loggRegButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(loggRegButton.getText().toString().equals("Zaloguj")) {

                    } else{
                        registerUser(emailTf.getText().toString(), passwordTf.getText().toString());
                    }
                }
            });

            registerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(loggRegButton.getText().toString().equals("Zaloguj")){
                        loggRegButton.setText("ZAREJESTRUJ");
                    }
                }
            });
        } else {

        }
    }


    private boolean isLoggedIn(){
        return false;
    }

    private String SERVER_ADDRESS = "http://10.0.2.2:8080";
    private String ADD_USER = "/addUser";
    private String ADD_USER_EMAIL = "email";
    private String ADD_USER_PASSWORD = "password";

    public void registerUser(String email, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ADD_USER_EMAIL, email);
            jsonObject.put(ADD_USER_PASSWORD, password);
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
                setContentView(R.layout.main_window_activity);
                Log.e(this.getClass().getName(), "JSON OK");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
                Log.e(this.getClass().getName(), "JSON error: " + statusCode + " " + new String(responseBody));
            }
        });
    }
}
