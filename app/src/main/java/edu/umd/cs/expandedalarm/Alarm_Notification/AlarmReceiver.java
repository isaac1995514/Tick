package edu.umd.cs.expandedalarm.Alarm_Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import edu.umd.cs.expandedalarm.CustomReminder.Event;
import edu.umd.cs.expandedalarm.DependencyFactory;
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

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){



        } else if(intent.getAction() == "UPDATE_WEATHER"){
            WeekDay day = (WeekDay) intent.getSerializableExtra("DayOfWeek");
            UpdateAlarmTask task = new UpdateAlarmTask(context);
            Log.d("RECEIVER", "ABOUT TO EXECUTE ASYNC");
            task.execute(day);
        } else if (intent.getAction() == "ADD_CUSTOM_EVENT") {
            AlarmManager alarmManager = DependencyFactory.getAlarmManager(context);

            Event event = (Event) intent.getExtras().get("Event");

            Intent alarmIntent = new Intent().setAction("ADD_CUSTOM_EVENT");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    event.getEvent_ID(),
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar currentTime = Calendar.getInstance();
            Calendar reminderStart = transformTextToDate(event.getInitial_reminder_date(), event.getInitial_reminder_time());
            long currentTimeMillis = currentTime.getTimeInMillis();
            long reminderStartMillis = reminderStart.getTimeInMillis();
            long timeDifference = TimeUnit.MILLISECONDS.toMillis(Math.abs(reminderStartMillis - currentTimeMillis));

            if (event.getReminder_freq().equals("Just once"))
                alarmManager.set(AlarmManager.RTC, timeDifference, pendingIntent);
            else if (event.getReminder_freq().equals("Every hour")) {
                alarmManager.setRepeating(AlarmManager.RTC, timeDifference, AlarmManager.INTERVAL_HOUR, pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every 3 hours")) {
                alarmManager.setRepeating(AlarmManager.RTC, timeDifference, AlarmManager.INTERVAL_HOUR * 3, pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every 6 hours")) {
                alarmManager.setRepeating(AlarmManager.RTC, timeDifference, AlarmManager.INTERVAL_HOUR * 6, pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every 12 hours")) {
                alarmManager.setRepeating(AlarmManager.RTC, timeDifference, AlarmManager.INTERVAL_HOUR * 12, pendingIntent);
            }
            else if (event.getReminder_freq().equals("Every 24 hours")) {
                alarmManager.setRepeating(AlarmManager.RTC, timeDifference, AlarmManager.INTERVAL_HOUR * 24, pendingIntent);
            }

        }
        else{
            Log.e("Error", "Should not happen");
        }
    }

    /**
     * AsyncTask that update weather alarm
     */
    public class UpdateAlarmTask extends AsyncTask<WeekDay, Void, Calendar>{

        AlarmManager alarmManager;
        ScheduleService scheduleService;
        WeekDayService weekDayService;
        WeekDay day;

        Context context;

        public UpdateAlarmTask(Context context){
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

            Log.d("Before TIME SET", calendar.getTime().toString());

            // If calendar's time is before current time, add time to next alarm
            if(calendar.getTime().before(new Date(System.currentTimeMillis()))){
                calendar.add(Calendar.DAY_OF_MONTH, 7);
            }

            Log.d("After TIME SET", calendar.getTime().toString());

            Intent notification = new Intent().setAction("WEATHER_NOTIFICATION");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, day.getDay().getValue(), notification, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();

            notification = new Intent().setAction("WEATHER_NOTIFICATION");
            pendingIntent = PendingIntent.getBroadcast(context, day.getDay().getValue(), notification, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

            return calendar;

        }

        @Override
        public void onPostExecute(Calendar calendar){

        }
    }

    private GregorianCalendar transformTextToDate(String strDate, String strTime) {
        int day, month, year, hours, minutes;
        month = Integer.parseInt(strDate.substring(0, strDate.indexOf("/")));
        day = Integer.parseInt(strDate.substring(strDate.indexOf("/") + 1, strDate.lastIndexOf("/")));
        year = Integer.parseInt(strDate.substring(strDate.lastIndexOf("/") + 1, strDate.length() -1));
        hours = Integer.parseInt(strTime.substring(0, strTime.indexOf(":")));
        minutes = Integer.parseInt(strTime.substring(strTime.indexOf(":") + 2, strTime.indexOf(":") + 4));
        if (strTime.contains("pm"))
            hours += 12;
        GregorianCalendar calendar = new GregorianCalendar(year, month, day, hours, minutes);
        return calendar;
    }



}
