package reminderpackage.reminder.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isaac on 4/6/2017.
 */

public class WeekDayService {
    private Context context;
    private List<WeekDay> weekDayList;

    public WeekDayService(Context context) {
        this.context = context;
        this.weekDayList = new ArrayList<WeekDay>();
        initList();

    }

    public WeekDay getWeekDay(int position) {
        return weekDayList.get(position);
    }

    public void updateTime(WeekDay.Day day, int hour, int minute){
        weekDayList.get(day.getValue()).setTime(hour,minute);
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
    }

    public List<WeekDay> getAllWeekDays() {
        return weekDayList;
    }

}
