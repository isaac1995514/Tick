package edu.umd.cs.expandedalarm.Alarm_Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.umd.cs.expandedalarm.DependencyFactory;
import edu.umd.cs.expandedalarm.R;
import edu.umd.cs.expandedalarm.RemoteFetch;

/**
 * Created by Isaac on 4/20/2017.
 */

public class NotificationSender extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("WEATHER_NOTIFICATION")){
            boolean hot = false, cold = false, raining = false, snowing = false, windy = false;
            double temp = 1000, speed = 1000;
            String rainDescription = "", snowDescription = "";

            SharedPreferences pref = DependencyFactory.getUserPreference(context);
            String zip_code = pref.getString("zip_code","0");
            JSONObject json = RemoteFetch.getJSON(context, zip_code+",us");
            Log.d("SimpleWeather", json.toString());
            String context_text = "", context_text2 = "Remember to bring ", context_text3 = "";
            try {
                JSONObject main = json.getJSONObject("main");
                temp = main.getDouble("temp");
                if(temp > 80)
                    hot = true;
                else if(temp < 50)
                    cold = true;
            } catch (Exception e) {
                Log.e("SimpleWeather", "Temp not found");
            }
            try {
                JSONArray weather = json.getJSONArray("weather");

                for (int i = 0;i<weather.length();i++) {
                    String main = weather.getJSONObject(i).getString("main");
                    Log.e("SimpleWeather", main);
                    if(main.equals("Rain")) {
                        raining = true;
                        rainDescription = weather.getJSONObject(i).getString("description");
                    }
                    if(main.equals("Snow")) {
                        snowing = true;
                        snowDescription = weather.getJSONObject(i).getString("description");
                    }
                }
            } catch (Exception e) {
                Log.e("SimpleWeather", "Weather not found\n"+e);
            }

            try {
                JSONObject wind = json.getJSONObject("wind");
                speed = wind.getDouble("speed");
                if(speed > 15)
                    windy = true;
            } catch (Exception e) {
                Log.e("SimpleWeather", "Wind not found");
            }

            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (!pref.getBoolean("rain",false))
                raining = false;
            if (!pref.getBoolean("snow",false))
                snowing = false;
            if (!pref.getBoolean("wind",false))
                windy = false;
            else context_text3 = context_text3.concat(String.valueOf(speed) + "mph ");
            if (!pref.getBoolean("temp",false)) {
                hot = false;
                cold = false;
            }
            else context_text3 = context_text3.concat(String.valueOf(temp) + "\u2109 ");

            String str = "";
            if (cold) {
                context_text = context_text.concat("Cold.");
                str = str.concat("cold/");
            }
            else if (hot) {
                context_text = context_text.concat("Hot.");
                str = str.concat("hot/");
            }
            if (raining) {
                context_text = context_text.concat("Rain.");
                str = str.concat("rain/");
                context_text3 = context_text3.concat(rainDescription + " ");
            }
            if (snowing) {
                context_text = context_text.concat("Snow.");
                str = str.concat("snow/");
                context_text3 = context_text3.concat(snowDescription + " ");
            }
            if (windy) {
                context_text = context_text.concat("Wind.");
                str = str.concat("wind/");
            }

            String[] strs = str.split("/");
            int length = strs.length;
            boolean started = false;
            if (length == 1)
                context_text2 = "It is nice outside. Have a nice day!";
            else
                for (int i = 0; i < length; i++) {
                    if (strs[i].equals("cold"))
                        context_text2 = "Very cold!\n" + context_text2;
                    else if (strs[i].equals("hot"))
                        context_text2 = "Very hot!\n" + context_text2;
                    else if (i == length - 1) {
                        if (started) {
                            if (strs[i].equals("rain"))
                                context_text2 = context_text2.concat("and an umbrella!");
                            else if (strs[i].equals("snow"))
                                context_text2 = context_text2.concat("and a snow jacket!");
                            else if (strs[i].equals("wind"))
                                context_text2 = context_text2.concat("and a windbreaker!");
                        }
                        else {
                            if (strs[i].equals("rain"))
                                context_text2 = context_text2.concat("an umbrella!");
                            else if (strs[i].equals("snow"))
                                context_text2 = context_text2.concat("a snow jacket!");
                            else if (strs[i].equals("wind"))
                                context_text2 = context_text2.concat("a windbreaker!");
                        }
                    } else if (strs[i].equals("rain")) {
                        context_text2 = context_text2.concat("an umbrella, ");
                        started = true;
                    } else if (strs[i].equals("snow")) {
                        context_text2 = context_text2.concat("a snow jacket, ");
                        started = true;
                    } else if (strs[i].equals("wind")) {
                        context_text2 = context_text2.concat("a windbreaker, ");
                        started = true;
                    }
                }
            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_cloud_queue_black_24dp)
                    .setContentTitle("Weather Notification")
                    .setTicker("New Weather Notification!")
                    .setStyle(new Notification.BigTextStyle().bigText(context_text2).setSummaryText(context_text3))
                    .setContentText(context_text2)
                    .build();
            manager.notify(1,notification);
        }

    }
}
