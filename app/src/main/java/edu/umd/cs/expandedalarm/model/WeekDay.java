package edu.umd.cs.expandedalarm.model;

import java.io.Serializable;

/**
 * Created by Isaac on 4/1/2017.
 */

public class WeekDay implements Serializable{

    private Day day;
    private String hour = "00";
    private String minute = "00";

    public void setTime(String hour, String minute){
        this.hour = hour;
        this.minute = minute;
    }

    public String getTime(){
        return hour + " : " + minute;
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


