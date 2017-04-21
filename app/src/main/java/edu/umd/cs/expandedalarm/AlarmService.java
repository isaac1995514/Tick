package edu.umd.cs.expandedalarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.umd.cs.expandedalarm.model.ScheduleService;
import edu.umd.cs.expandedalarm.model.WeekDay;
import edu.umd.cs.expandedalarm.model.WeekDayService;


public class AlarmService extends IntentService {

    public AlarmService(){
        super("Alarm Service");

    }

    public static Intent newIntent(Context context){
        return new Intent(context, AlarmService.class);
    }


    @Override
    protected void onHandleIntent(Intent intent) {


    }

    public static void scheduleAlarm(Context context, String action, WeekDay day){

        AlarmManager alarmManager = DependencyFactory.getAlarmManager(context);
        ScheduleService scheduleService = DependencyFactory.getScheduleService(context);
        WeekDayService weekDayService = DependencyFactory.getWeekDayService(context);

        if(action == "UPDATE"){

        }else if(action == "REBOOT"){

            List<WeekDay> weekDays = weekDayService.getAllWeekDays();

            for(WeekDay weekDay: weekDays){
                if(!weekDay.isDefault()){
                    Intent rebootAlarm = AlarmService.newIntent(context);
                    PendingIntent pendingIntent = PendingIntent.getService(context, weekDay.getDay().getValue(), rebootAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

                    Calendar calendar = Calendar.getInstance();
                    int hour = scheduleService.getHour(weekDay.getDay().toString());
                    int minute = scheduleService.getMinute(weekDay.getDay().toString());

                    calendar.set(Calendar.DAY_OF_WEEK, weekDay.getDay().getValue());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    if(calendar.getTime().before(new Date(System.currentTimeMillis()))){
                        calendar.add(Calendar.WEEK_OF_MONTH,1);
                    }

                    Log.d("TIME SET", calendar.getTime().toString());

                    alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), Calendar.WEEK_OF_MONTH, pendingIntent);

                }
            }



        }

    }






}
