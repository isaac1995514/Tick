package reminderpackage.reminder;

import android.content.Context;

import reminderpackage.reminder.model.WeekDayService;

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
