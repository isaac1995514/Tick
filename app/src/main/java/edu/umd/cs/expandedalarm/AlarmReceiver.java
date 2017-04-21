package edu.umd.cs.expandedalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

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
            //Currently Not Working

        }else if(intent.getAction() == "UPDATE_WEATHER"){
            WeekDay day = (WeekDay) intent.getSerializableExtra("DayOfWeek");
            UpdateAlarmTask task = new UpdateAlarmTask(context);
            Log.d("RECEIVER", "ABOUT TO EXECUTE ASYNC");
            task.execute(day);
        }else{
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
                calendar.add(Calendar.WEEK_OF_MONTH, 1);
            }

            Log.d("After TIME SET", calendar.getTime().toString());

            Intent notification = new Intent().setAction("WEATHER_NOTIFICATION");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, day.getDay().getValue(), notification, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), Calendar.WEEK_OF_MONTH, pendingIntent);

            return calendar;

        }

        @Override
        public void onPostExecute(Calendar calendar){

        }
    }




}
