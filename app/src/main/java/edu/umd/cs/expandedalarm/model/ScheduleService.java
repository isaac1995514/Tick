package edu.umd.cs.expandedalarm.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Isaac on 4/16/2017.
 */

/**
 * This Service contain information about the user default weather notification schedule
 * Stored using SharedPreference
 */
public class ScheduleService {

    private SharedPreferences schedule;
    private static final String SP_NAME = "SCHEDULE";

    public ScheduleService(Context context){
        schedule = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    /**
     *
     * @param key String {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY}
     * @param value Time format -> "xx:xx'
     */
    public void setSchedule(String key, String value){
        SharedPreferences.Editor editor = schedule.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     *
     * @param key String {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY}
     * @return The selected hour in integer format {From 0 to 23 hour}
     */
    public int getHour(String key){
        String schedule = getSchedule(key);
        if(schedule == null){
            Log.d("getHour()", "Should not happen");
            return -1;

        }else{
            String[] timeSplit = WeekDay.convert(schedule);
            return Integer.valueOf(timeSplit[0]);
        }
    }

    /**
     *
     * @param key String {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY}
     * @return The selected minute in integer format {From 0 to 59 minute}
     */
    public int getMinute(String key){
        String schedule = getSchedule(key);
        if(schedule == null){
            Log.d("getMinute()", "Should not happen");
            return -1;

        }else{
            String[] timeSplit = WeekDay.convert(schedule);
            return Integer.valueOf(timeSplit[1]);
        }
    }

    /**
     *
     * @param key String {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY}
     * @return the schedule in a specific day of Week
     */
    public String getSchedule(String key){

        if (schedule != null) {
            return schedule.getString(key,null);
        }
        return null;
    }






}
