package edu.umd.cs.expandedalarm.alarm_notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

import edu.umd.cs.expandedalarm.DependencyFactory;
import edu.umd.cs.expandedalarm.R;
import edu.umd.cs.expandedalarm.RemoteFetch;
import edu.umd.cs.expandedalarm.custom_reminder.Event;
import edu.umd.cs.expandedalarm.model.RelationshipService;

/**
 * Created by Isaac on 4/20/2017.
 */

public class NotificationSender extends BroadcastReceiver {
    private static final String EVENTS = "EVENTS";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("TEST", "onReceive Reached");

        if (intent.getAction().equals("CUSTOM_EVENT_NOTIFICATION")) {
            Log.d("fasdasfada", "osafafsaadsad");
            Event event = (Event)intent.getSerializableExtra("Event");
            Calendar reminderStart = Event.transformTextToDate(event.getInitial_reminder_date(), event.getInitial_reminder_time());

            if (reminderStart.getTime().before( Calendar.getInstance().getTime())){
                Log.d("Cancel","cancelling");
                //Test if alarm needs to cancel
                AlarmManager alarmManager = DependencyFactory.getAlarmManager(context);

                Intent notification = new Intent();
                notification.setAction("RELATIONSHIP_NOTIFICATION");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, event.getEvent_ID(), notification, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();

            }
            else {
                NotificationManager manager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                String context_text = event.getEvent_name();
                String context_text2 = event.getEvent_description();

                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_calander)
                        .setContentTitle(context_text)
                        .setTicker(context_text)
                        .setStyle(new Notification.BigTextStyle().bigText(context_text2))
                        .setContentText(context_text2)
                        .build();
                manager.notify(1, notification);
            }
        }
        else if (intent.getAction().equals("RELATIONSHIP_NOTIFICATION")) {

            Calendar stop = (Calendar) intent.getSerializableExtra("STOP");
            Calendar calendar = Calendar.getInstance();
            //Not a one-time alarm
            if (stop != null && stop.getTime().before(calendar.getTime())){

                //Test if alarm needs to cancel
                AlarmManager alarmManager = DependencyFactory.getAlarmManager(context);

                int id = (Integer) intent.getSerializableExtra("ID");
                Intent notification = new Intent();
                notification.setAction("RELATIONSHIP_NOTIFICATION");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notification, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();

            } else {
                RelationshipService relationshipService = new RelationshipService(context);

                String date = (String) intent.getSerializableExtra("DATE");
                String[] f_date = relationshipService.getPrintableDate(date);

                int id = (int)intent.getSerializableExtra("ID");

                Log.d("TEST_NOTIFICATION", f_date[2] + " " + f_date[1] + " " + f_date[0]);

                NotificationManager manager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                int dayRemainder = 1;
                if (stop != null) {
                    long diff = (stop.getTime().getTime() - calendar.getTime().getTime());
                    dayRemainder = (int) (diff / (1000 * 60 * 60 * 24)) + 1;
                }

                String context_text2 = f_date[2] + " " + f_date[1] + " " + f_date[0]
                                + "\n" + dayRemainder + " MORE DAY(S)";

                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_relationship)
                        .setContentTitle(date)
                        .setTicker(date)
                        .setStyle(new Notification.BigTextStyle().bigText(context_text2))
                        .setContentText(context_text2)
                        .build();
                manager.notify(id,notification);
            }

        }
        else if (intent.getAction().equals("WEATHER_NOTIFICATION")){

            Log.d("TEST", "WEATHER_NOTIFICATION_RECEIVED");

            boolean hot = false, cold = false, raining = false, snowing = false, windy = false;
            double temp = 1000, mintemp = 1000, maxtemp = 1000, speed = 1000;
            String rainDescription = "", snowDescription = "";

            SharedPreferences pref = DependencyFactory.getUserPreference(context);
            String zip_code = pref.getString("zip_code","0");
            JSONObject json = RemoteFetch.getJSON(context, zip_code+",us");
            Log.d("SimpleWeather", json.toString());
            String context_text = "", context_text2 = "Remember to bring ", context_text3 = "";
            try {
                JSONObject main = json.getJSONObject("main");
                temp = main.getDouble("temp");
                mintemp = main.getDouble("temp_min");
                maxtemp = main.getDouble("temp_max");
                if(temp > 70)
                    hot = true;
                else if(temp < 40)
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
                        //rainDescription = weather.getJSONObject(i).getString("description");
                    }
                    if(main.equals("Snow")) {
                        snowing = true;
                        //snowDescription = weather.getJSONObject(i).getString("description");
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
            else context_text3 = context_text3.concat("Wind: " + String.valueOf(speed) + "mph ");
            if (!pref.getBoolean("temp",false)) {
                hot = false;
                cold = false;
            }
            else context_text3 = String.format("%sTemp: %s°--%s°--%s℉ ", context_text3,
                    String.valueOf(mintemp),String.valueOf(temp),String.valueOf(maxtemp));

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

            if (strs[0].equals(""))
                context_text2 = "It is nice outside. Have a nice day!";
            else
                for (int i = 0; i < length; i++) {
                    if (i == length - 1) {
                        if (strs[i].equals("cold"))
                            context_text2 = "Very cold!";
                        else if (strs[i].equals("hot"))
                            context_text2 = "Very hot!";
                        else if (started) {
                            if (strs[i].equals("rain"))
                                context_text2 = context_text2.concat("and an umbrella!");
                            else if (strs[i].equals("snow"))
                                context_text2 = context_text2.concat("and a snow jacket!");
                            else if (strs[i].equals("wind"))
                                context_text2 = context_text2.concat("and a windbreaker!");
                        } else {
                            if (strs[i].equals("rain"))
                                context_text2 = context_text2.concat("an umbrella!");
                            else if (strs[i].equals("snow"))
                                context_text2 = context_text2.concat("a snow jacket!");
                            else if (strs[i].equals("wind"))
                                context_text2 = context_text2.concat("a windbreaker!");
                        }
                    } else if (strs[i].equals("cold")) {
                        context_text2 = "Very cold!\n" + context_text2;
                        started = true;
                    } else if (strs[i].equals("hot")) {
                        context_text2 = "Very hot!\n" + context_text2;
                        started = true;
                    }else if (strs[i].equals("rain")) {
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
            manager.notify(3,notification);

        }else{
            Log.e("ERROR","incorrect action");
        }
    }
}