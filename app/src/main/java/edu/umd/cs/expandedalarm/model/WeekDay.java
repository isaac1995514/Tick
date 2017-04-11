package edu.umd.cs.expandedalarm.model;

import java.io.Serializable;

/**
 * Created by Isaac on 4/1/2017.
 */

public class WeekDay implements Serializable{

    private Day day;
    private int hour = 0;
    private int minute = 0;

    public void setTime(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public String getTime(){
        return Integer.toString(hour) + " : " + Integer.toString(minute);
    }

    public void setMonday(){
        day = Day.MON;
    }

    public void setTuesday(){
        day = Day.TUE;
    }

    public void setWednesday(){
        day = Day.WED;
    }

    public void setThursday(){
        day = Day.THURS;
    }

    public void setFriday(){
        day = Day.FRI;
    }

    public void setSaturday(){
        day = Day.SAT;
    }

    public void setSunday(){
        day = Day.SUN;
    }

    public Day getDay(){
        return day;
    }

    public enum Day {
        SUN(0), MON(1), TUE(2), WED(3), THURS(4), FRI(5), SAT(6);

        private int value;

        Day(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }
    }

}


