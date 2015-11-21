package sii.letscode.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by Hubert on 21.11.2015.
 */
public class SmartPopup {
    String text;
    SmartPopup(String text){
        this.text = text;
    }

    public void show(Context context){
        Dialog dialog = new Dialog(context);
        TextView tv = new TextView(dialog.getContext());
        tv.setText(text);
        dialog.setContentView(tv);
        dialog.show();
    }
}
