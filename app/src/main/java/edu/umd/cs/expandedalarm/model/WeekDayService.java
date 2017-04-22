package edu.umd.cs.expandedalarm.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.umd.cs.expandedalarm.DependencyFactory;

/**
 * Created by Isaac on 4/6/2017.
 */


/**
 * This Service contain the 7 days of week that will be load to the TimeSetting Fragment each time
 * the activity execute.
 *
 * Store using HashMap<Integer, WeekDay>
 *     key: Calendar.DAY_OF_WEEK {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY}
 *     value: WeekDay object {From Sunday(1) to Saturday(7)}
 */
public class WeekDayService {
    private Context context;
    private HashMap<Integer, WeekDay> weekDayHash;
    private ScheduleService scheduleService;

    public WeekDayService(Context context) {
        this.context = context;
        this.weekDayHash = new HashMap<Integer, WeekDay>();
        this.scheduleService = DependencyFactory.getScheduleService(context);

        initList();

    }

    /**
     *
     * @param dayOfWeek Calendar.DAY_OF_WEEK value
     * @return WeekDay object
     */
    public WeekDay getWeekDay(int dayOfWeek) {
        return weekDayHash.get(dayOfWeek);    //HashTable implementation

    }

    /**
     *
     * @param day Weekday object
     * @param hour The hour selected in timePicker
     * @param minute The minute selected in timePicker
     */
    public void updateTime(WeekDay.Day day, String hour, String minute){

        //Getting select weekDay object in HashMap and change its time
        weekDayHash.get(day.getValue()).setTime(hour, minute);

        //Update time in schedule Service
        scheduleService.setSchedule(day.toString(), hour + ":" + minute );

    }

    /**
     * Initializing the Adapter in WeatherSettingFragment each time it execute
     */
    public void initList() {

        // Fill the Adapter with weekDays objects and mark them from Sunday to Saturday
        for(Integer dayOfWeek: WeekDay.getAllDaysOfWeek()){
            WeekDay newDay = new WeekDay();
            newDay.setDay(dayOfWeek);

            weekDayHash.put(dayOfWeek, newDay);

        }

        // Load previous selected schedule from scheduleService
        for(WeekDay.Day day: WeekDay.Day.values()){
            String time = scheduleService.getSchedule(day.toString());
            if (time != null){
                String[] timeSplit = WeekDay.convert(time);
                weekDayHash.get(day.getValue()).setTime(timeSplit[0], timeSplit[1]);
            }
        }
    }

    /**
     *
     * @return a list of all Weekdays object {Sunday to Saturday with the selected time in each}
     */
    public List<WeekDay> getAllWeekDays() {

        List<WeekDay> weekDays = new ArrayList<WeekDay>();

        for(Integer dayOfWeek: WeekDay.getAllDaysOfWeek()){
            weekDays.add(weekDayHash.get(dayOfWeek));
        }

        return weekDays;

    }

}
