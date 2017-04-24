package edu.umd.cs.expandedalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Isaac on 4/20/2017.
 */

public class NotificationSender extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("WEATHER_NOTIFICATION")){

            Toast.makeText(context, "XXXXXXXXXXXXXXXXX", Toast.LENGTH_LONG).show();
            SharedPreferences pref = DependencyFactory.getUserPreference(context);
            String zip_code = pref.getString("zip_code","0");
            JSONObject json = RemoteFetch.getJSON(context, zip_code);
            Log.d("SimpleWeather", json.toString());
            String context_text = "";
            try {
                JSONObject main = json.getJSONObject("main");
                context_text = context_text.concat(String.valueOf(main.getDouble("temp")) + " \u2109");
            } catch (Exception e) {
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
                context_text = "cannot find data";
            }
            Log.d("CONTEXT_TEXT", context_text);
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setContentTitle("Weather Notification")
                    .setTicker(context_text)
                    .setContentText(context_text)
                    .build();
            manager.notify(1,notification);
        }

    }
}
