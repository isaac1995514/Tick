package edu.umd.cs.expandedalarm;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import edu.umd.cs.expandedalarm.model.RelationshipService;
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
    private static RelationshipService relationshipService;


    /**
     * Contain weekDay objects the initialize the Adapter
     */
    public static WeekDayService getWeekDayService(Context context){
        if (weekDayService == null){
            weekDayService = new WeekDayService(context);
        }

        return weekDayService;
    }

    /**
     * Contain user default schedule
     */
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

    /**
     * Contain user preference selected in the PreferenceActivity
     */
    public static SharedPreferences getUserPreference(Context context){

        if (user_preference == null){
            user_preference = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return user_preference;
    }

    /**
     * Contain date foe relationship
     */
    public static RelationshipService getRelationshipService(Context context){
        if (relationshipService == null){
            relationshipService = new RelationshipService(context);
        }

        relationshipService.loadDefaultDate();

        return relationshipService;
    }

}
