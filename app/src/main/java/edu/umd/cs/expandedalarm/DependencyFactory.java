package edu.umd.cs.expandedalarm;

import android.content.Context;

import edu.umd.cs.expandedalarm.model.WeekDayService;

/**
 * Created by Isaac on 4/9/2017.
 */

public class DependencyFactory {
    private static WeekDayService weekDayService;

    public static WeekDayService getWeekDayService(Context context){
        if (weekDayService == null){
            weekDayService = new WeekDayService(context);
        }

        return weekDayService;
    }
}
