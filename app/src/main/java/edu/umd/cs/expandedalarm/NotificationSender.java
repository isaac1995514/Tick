package edu.umd.cs.expandedalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Isaac on 4/20/2017.
 */

public class NotificationSender extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("WEATHER_NOTIFICATION")){

            Toast.makeText(context, "XXXXXXXXXXXXXXXXX", Toast.LENGTH_LONG).show();


        }

    }
}
