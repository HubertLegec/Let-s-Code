package sii.letscode.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Hubert on 20.11.2015.
 */
public class MainWindowActivity extends Activity {
    ImageButton findButton;
    ImageButton addButton;
    ImageButton profileButton;
    ImageButton settingsButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_window_find);
        findButton = (ImageButton) findViewById(R.id.findButton);
        addButton = (ImageButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.main_window_find);
            }
        });

    }
}