package edu.umd.cs.expandedalarm.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.expandedalarm.DependencyFactory;

/**
 * Created by Isaac on 4/6/2017.
 */

public class WeekDayService {
    private Context context;
    private List<WeekDay> weekDayList;
    private ScheduleService scheduleService;

    public WeekDayService(Context context) {
        this.context = context;
        this.weekDayList = new ArrayList<WeekDay>();
        this.scheduleService = DependencyFactory.getScheduleService(context);

        initList();

    }

    public WeekDay getWeekDay(int position) {
        return weekDayList.get(position);
    }

    public void updateTime(WeekDay.Day day, String hour, String minute){
        weekDayList.get(day.getValue()).setTime(hour,minute);
        scheduleService.setSchedule(day.toString(), hour + ":" + minute );

    }

    public void initList() {


        for (int i = 0; i < 7; i++) {
            weekDayList.add(new WeekDay());
        }

        weekDayList.get(0).setSunday();
        weekDayList.get(1).setMonday();
        weekDayList.get(2).setTuesday();
        weekDayList.get(3).setWednesday();
        weekDayList.get(4).setThursday();
        weekDayList.get(5).setFriday();
        weekDayList.get(6).setSaturday();


        for(WeekDay.Day day: WeekDay.Day.values()){
            String time = scheduleService.getSchedule(day.toString());
            if (time != null){
                    String[] timeSplit = time.split(":");
                    Log.d("TimeSplit",timeSplit[0]);
                weekDayList.get(day.getValue()).setTime(timeSplit[0], timeSplit[1]);
            }

        }


    }

    public List<WeekDay> getAllWeekDays() {
        return weekDayList;
    }

}
