package sii.letscode.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.myapp.R;

public class StartActivity extends Activity {
    TextView registerTextView;
    Button loggRegButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isLoggedIn()) {
            setContentView(R.layout.start_activity);
            registerTextView = (TextView) findViewById(R.id.changeToRegister);
            registerTextView.setClickable(true);
            loggRegButton = (Button) findViewById(R.id.logButton);

            loggRegButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(loggRegButton.getText().toString().equals("Zaloguj")) {

                    } else{
                        
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
        }else{

        }
    }


    private boolean isLoggedIn(){
        return false;
    }
}
