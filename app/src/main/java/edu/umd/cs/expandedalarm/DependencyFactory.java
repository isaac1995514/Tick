package edu.umd.cs.expandedalarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import edu.umd.cs.expandedalarm.model.ScheduleService;
import edu.umd.cs.expandedalarm.model.WeekDayService;

/**
 * Created by Isaac on 4/9/2017.
 */

public class DependencyFactory {
    private static WeekDayService weekDayService;
    private static ScheduleService scheduleService;
    private static AlarmManager alarmManager;
    private static SharedPreferences user_preference;
    private static NotificationManager notificationManager;

    public static WeekDayService getWeekDayService(Context context){
        if (weekDayService == null){
            weekDayService = new WeekDayService(context);
        }

        return weekDayService;
    }

    public static ScheduleService getScheduleService(Context context){
        if (scheduleService == null){
            scheduleService = new ScheduleService(context);
        }

        return scheduleService;
    }

    public static AlarmManager getAlarmManager(Context context){
        if (alarmManager == null){
            alarmManager = ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
        }

        return alarmManager;

    }

    public static SharedPreferences getUserPreference(Context context){

        if (user_preference == null){
            user_preference = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return user_preference;
    }

    public static NotificationManager getNotificationManager(Context context){

        if (notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        }

        return notificationManager;

    }


}
