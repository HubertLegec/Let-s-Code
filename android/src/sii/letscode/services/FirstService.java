package sii.letscode.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Hubert on 20.11.2015.
 */
public class FirstService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
