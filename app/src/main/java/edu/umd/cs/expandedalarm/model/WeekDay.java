package edu.umd.cs.expandedalarm.model;


import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Isaac on 4/1/2017.
 */

/**
 *
 * WeekDay is an object class that represent each day in days of week
 *
 */
public class WeekDay implements Serializable{

    private Day day;
    private String hour = "00";
    private String minute = "00";
    private boolean dafault_time = true;

    /**
     * enum value:
     *      toString -> {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY}
     *      getValue() -> {1, 2, 3, 4, 5, 6, 7}
     */
    public enum Day {
        SUNDAY(Calendar.SUNDAY), MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY),
        WEDNESDAY(Calendar.WEDNESDAY), THURSDAY(Calendar.THURSDAY), FRIDAY(Calendar.FRIDAY),
        SATURDAY(Calendar.SATURDAY);

        private int value;

        Day(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }
    }

    public boolean isDefault(){
        return dafault_time;
    }

    /**
     *
     * @param hour The hour selected in timePicker
     * @param minute The minute selected in timePicker
     */
    public void setTime(String hour, String minute){
        this.hour = format(hour);
        this.minute = format(minute);
        dafault_time = false;
    }

    /**
     *
     * @return String: get time in "xx:xx" format
     */
    public String getTime(){
        return hour + " : " + minute;
    }

    /**
     *
     * @param dayOfWeek Calendar.DAY_OF_WEEK {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY}
     */
    public void setDay(int dayOfWeek){

        switch (dayOfWeek){
            case Calendar.SUNDAY:
                day = Day.SUNDAY;
                break;
            case Calendar.MONDAY:
                day = Day.MONDAY;
                break;
            case Calendar.TUESDAY:
                day = Day.TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                day = Day.WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                day = Day.THURSDAY;
                break;
            case Calendar.FRIDAY:
                day = Day.FRIDAY;
                break;
            case Calendar.SATURDAY:
                day = Day.SATURDAY;
                break;
        }
    }

    public Day getDay(){
        return day;
    }

    /**
     *
     * @return an array of all Calendar.DAYS_OF_WEEK {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY}
     */
    public static Integer[] getAllDaysOfWeek(){
        return new Integer[] {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY,
                        Calendar.FRIDAY, Calendar.SATURDAY};
    }

    public static String[] convert(String hourMinute){

        return hourMinute.split(":");

    }

    public String format(String time){
        if (Integer.valueOf(time) < 10){
            return "0" + time;
        }else{
            return time;
        }
    }

}


