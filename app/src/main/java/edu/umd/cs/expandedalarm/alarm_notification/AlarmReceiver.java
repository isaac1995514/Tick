package edu.umd.cs.expandedalarm.alarm_notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import edu.umd.cs.expandedalarm.DependencyFactory;
import edu.umd.cs.expandedalarm.custom_reminder.Event;
import edu.umd.cs.expandedalarm.model.RelationshipService;
import edu.umd.cs.expandedalarm.model.ScheduleService;
import edu.umd.cs.expandedalarm.model.WeekDay;
import edu.umd.cs.expandedalarm.model.WeekDayService;

/**
 * Created by Isaac on 4/19/2017.
 */


/**
 * Receive intent that update or initialize alarm
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Log.d("RECEIVER", "BOOT_COMPLETE");

        }else if(intent.getAction().equals("UPDATE_WEATHER")) {
            Log.d("TEST", "UPDATE_WEATHER_RECEIVED");
            WeekDay day = (WeekDay) intent.getSerializableExtra("DayOfWeek");
            UpdateWeatherAlarmTask task = new UpdateWeatherAlarmTask(context);
            task.execute(day);
        }else if(intent.getAction().equals("UPDATE_RELATIONSHIP")){
            UpdateRelationshipAlarmTask task = new UpdateRelationshipAlarmTask(context);
            task.execute(intent);

        }else if (intent.getAction().equals("ADD_CUSTOM_EVENT")) {
            AlarmManager alarmManager = DependencyFactory.getAlarmManager(context);

            Event event = (Event) intent.getExtras().get("Event");
            Intent alarmIntent = new Intent().setAction("CUSTOM_EVENT_NOTIFICATION").putExtra("Event",event);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, event.getEvent_ID(),
                    alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.d("sad",event.getInitial_reminder_date());
            Log.d("sad",event.getInitial_reminder_time());
            Calendar reminderStart = Event.getCalenderDate(event.getInitial_reminder_date(), event.getInitial_reminder_time());
            Log.d("EVENT","Time Set: " + reminderStart.getTime().toString());
            Log.d("EVENT","Frequency: " + event.getReminder_freq());
            Log.d("EVENT","Match just once" + Boolean.toString(event.getReminder_freq().equals("Just once")));
            Log.d("EVENT","Match one hour" + Boolean.toString(event.getReminder_freq().equals("Every hour")));


            if (event.getReminder_freq().equals("Just once")) {
                Log.d("EVENT","Just once notification");
                alarmManager.set(AlarmManager.RTC, reminderStart.getTimeInMillis(), pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every hour")) {
                Log.d("EVENT", "One Hour notification");
                alarmManager.setRepeating(AlarmManager.RTC, reminderStart.getTimeInMillis(), 60 * 1000 * 60, pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every 3 hours")) {
                alarmManager.setRepeating(AlarmManager.RTC, reminderStart.getTimeInMillis(), 60 * 1000 * 60 * 3, pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every 6 hours")) {
                alarmManager.setRepeating(AlarmManager.RTC, reminderStart.getTimeInMillis(), 60 * 1000 * 60 * 6, pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every 12 hours")) {
                alarmManager.setRepeating(AlarmManager.RTC, reminderStart.getTimeInMillis(), 60 * 1000 * 60 * 12, pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every 24 hours")) {
                alarmManager.setRepeating(AlarmManager.RTC, reminderStart.getTimeInMillis(), 60 * 1000 * 60 * 24, pendingIntent);
            }
        }else if (intent.getAction().equals("REMOVE_CUSTOM_EVENT")) {
            AlarmManager alarmManager = DependencyFactory.getAlarmManager(context);

            Event event = (Event) intent.getExtras().get("Event");
            Intent alarmIntent = new Intent().setAction("CUSTOM_EVENT_NOTIFICATION");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, event.getEvent_ID(),
                    alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }else{
            Log.e("Error", "Should not happen");
        }
    }

    /**
     * AsyncTask that update weather alarm
     */
    public class UpdateWeatherAlarmTask extends AsyncTask<WeekDay, Void, Calendar>{

        AlarmManager alarmManager;
        ScheduleService scheduleService;
        WeekDayService weekDayService;
        WeekDay day;

        Context context;

        public UpdateWeatherAlarmTask(Context context){
            this.context = context;
            this.alarmManager = DependencyFactory.getAlarmManager(context);
            this.scheduleService = DependencyFactory.getScheduleService(context);
            this.weekDayService = DependencyFactory.getWeekDayService(context);
        }

        @Override
        protected Calendar doInBackground(WeekDay... params) {

            day = params[0];

            Calendar calendar = Calendar.getInstance();
            int hour = scheduleService.getHour(day.getDay().toString());
            int minute = scheduleService.getMinute(day.getDay().toString());

            calendar.set(Calendar.DAY_OF_WEEK, day.getDay().getValue());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // If calendar's time is before current time, add time to next alarm
            if(calendar.getTime().before(new Date(System.currentTimeMillis()))){
                calendar.add(Calendar.DAY_OF_MONTH, 7);
            }

            Log.d("TEST_WEATHER_TIME", calendar.getTime().toString());

            Intent notification = new Intent().setAction("WEATHER_NOTIFICATION");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, day.getDay().getValue(), notification, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();

            notification = new Intent().setAction("WEATHER_NOTIFICATION");
            pendingIntent = PendingIntent.getBroadcast(context, day.getDay().getValue(), notification, PendingIntent.FLAG_UPDATE_CURRENT);

            Log.d("TEST_WEATHER_SENT", "NOTIFICATION_SENT");
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

            return calendar;

        }

        @Override
        public void onPostExecute(Calendar calendar){

        }
    }

    public class UpdateRelationshipAlarmTask extends AsyncTask<Intent, Void, Calendar>{

        AlarmManager alarmManager;
        RelationshipService relationshipService;
        Calendar curr_time;
        Context context;

        public UpdateRelationshipAlarmTask(Context context){
            this.context = context;
            this.alarmManager = DependencyFactory.getAlarmManager(context);
            this.relationshipService = DependencyFactory.getRelationshipService(context);
            this.curr_time = Calendar.getInstance();

        }

        @Override
        protected Calendar doInBackground(Intent... params) {

            Intent intent = params[0];

            String date = (String) intent.getSerializableExtra("DATE");
            int id = (Integer) intent.getSerializableExtra("ID");

            Log.d("TEST_DATE", date);
            Log.d("TEST_ID", Integer.toString(id));

            String[] f_date = relationshipService.getDate(date);

            Calendar calendar = Calendar.getInstance();
            int month = Integer.valueOf(f_date[1]);
            int day = Integer.valueOf(f_date[2]);

            Log.d("TEST_MONTH", Integer.toString(month));
            Log.d("TEST_DAY", Integer.toString(day));

            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);

            Log.d("TEST_DATE-1", calendar.getTime().toString());

            // If calendar's time is before current time, add time to next alarm
            if(calendar.getTime().before(new Date(System.currentTimeMillis()))){
                calendar.add(Calendar.YEAR, 1);
            }

            long diff = (calendar.getTime().getTime() - curr_time.getTime().getTime());
            int dayRemainder = (int) (diff / (1000 * 60 * 60 * 24));

            Log.d("TEST_DIFF1", Integer.toString(dayRemainder));


            Intent notification = new Intent().setAction("RELATIONSHIP_NOTIFICATION");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notification, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();

            notification = new Intent().setAction("RELATIONSHIP_NOTIFICATION");
            notification.putExtra("DATE", date);
            notification.putExtra("ID", id);


            Log.d("TEST_DATE-2", calendar.getTime().toString());

            if (dayRemainder == 0){

                pendingIntent = PendingIntent.getBroadcast(context, id, notification, PendingIntent.FLAG_UPDATE_CURRENT);
                //alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 60*1000, pendingIntent);
                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 60 * 500, pendingIntent);

            }else if(dayRemainder < 5){
                    //calendar.add(Calendar.DAY_OF_YEAR, -dayRemainder);
                    //calendar.add(Calendar.MINUTE, 5);
                    //Log.d("TEST_END", calendar.getTime().toString());
                calendar.add(Calendar.MINUTE, 1); //In case delay
                notification.putExtra("STOP", (Calendar) calendar.clone());
                calendar.add(Calendar.MINUTE, -1); //In case delay
                pendingIntent = PendingIntent.getBroadcast(context, id, notification, PendingIntent.FLAG_UPDATE_CURRENT);
                calendar.add(Calendar.DAY_OF_YEAR, -dayRemainder);
                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 60 * 1000 * 60 * 24, pendingIntent);
                    //calendar.add(Calendar.MINUTE, -5);
                    //Log.d("TEST_START",calendar.getTime().toString());
                    //alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),60 * 1000, pendingIntent);

            }else{
                calendar.add(Calendar.MINUTE, 1); //In case delay
                notification.putExtra("STOP", (Calendar) calendar.clone());
                calendar.add(Calendar.MINUTE, -1); //In case delay
                pendingIntent = PendingIntent.getBroadcast(context, id, notification, PendingIntent.FLAG_UPDATE_CURRENT);
                calendar.add(Calendar.DAY_OF_YEAR, -5);
                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 60 * 1000 * 60 * 24, pendingIntent);
                    //alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),60 * 1000, pendingIntent);

            }


            Log.d("TEST_DATE-3", calendar.getTime().toString());

            return calendar;

        }

        @Override
        public void onPostExecute(Calendar calendar){

        }
    }
}