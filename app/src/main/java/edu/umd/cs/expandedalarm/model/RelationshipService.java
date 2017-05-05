package edu.umd.cs.expandedalarm.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by Isaac on 4/24/2017.
 */

public class RelationshipService {

    private SharedPreferences relationshipDate;
    private static final String SP_NAME = "RELATIONSHIP";


    public RelationshipService(Context context){
        relationshipDate = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    public void setDate(String day, int year, int month, int dayOfMonth){
        SharedPreferences.Editor editor = relationshipDate.edit();
        editor.putString(day, format(year, month, dayOfMonth));
        editor.commit();
    }

    public void setDate(String day, int month, int dayOfMonth){
        SharedPreferences.Editor editor = relationshipDate.edit();
        editor.putString(day, format(Calendar.getInstance().get(Calendar.YEAR), month, dayOfMonth));
        editor.commit();
    }

    public String[] getDate(String day){
        String date = relationshipDate.getString(day, "");
        String[] f_date = date.split("-");

        return f_date;
    }

    public String[] getPrintableDate(String day){
        String date = relationshipDate.getString(day, "");
        String[] f_date = date.split("-");
        int dayOfMonth = Integer.valueOf(f_date[2]);
        int month = Integer.valueOf(f_date[1]);
        String[] month_lt = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        f_date[1] = month_lt[month];

        if(dayOfMonth < 10){
            f_date[2] = "0" + f_date[2];
        }

        return f_date;
    }

    public String format(int year, int month, int dayOfMonth){
        return Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(dayOfMonth);

    }

    public void loadDefaultDate(){
        setDate("VALENTINE", Calendar.FEBRUARY, 14);
        setDate("CHRISTMAS", Calendar.DECEMBER, 25);

    }



}
