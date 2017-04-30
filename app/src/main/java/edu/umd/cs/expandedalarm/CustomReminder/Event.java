package edu.umd.cs.expandedalarm.CustomReminder;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Event implements Serializable, Comparable<Event> {
    private String event_name, event_description, event_date, event_time, initial_reminder_date,
            initial_reminder_time, reminder_freq, event_ID;

    public Event(String event_name, String event_description, String event_date, String event_time,
                 String initial_reminder_date, String initial_reminder_time, String reminder_freq,
                 String event_ID) {
        this.event_name = event_name;
        this.event_description = event_description;
        this.event_date = event_date;
        this.event_time = event_time;
        this.initial_reminder_date = initial_reminder_date;
        this.initial_reminder_time = initial_reminder_time;
        this.reminder_freq = reminder_freq;
        this.event_ID = event_ID;
    }

    public Event() {

    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getInitial_reminder_date() {
        return initial_reminder_date;
    }

    public void setInitial_reminder_date(String initial_reminder_date) {
        this.initial_reminder_date = initial_reminder_date;
    }

    public String getInitial_reminder_time() {
        return initial_reminder_time;
    }

    public void setInitial_reminder_time(String initial_reminder_time) {
        this.initial_reminder_time = initial_reminder_time;
    }

    public String getReminder_freq() {
        return reminder_freq;
    }

    public void setReminder_freq(String reminder_freq) {
        this.reminder_freq = reminder_freq;
    }

    public String getEvent_ID() {
        return event_ID;
    }

    public void setEvent_ID(String event_ID) {
        this.event_ID = event_ID;
    }

    private GregorianCalendar transformTextToDate(String strDate, String strTime) {
        int day, month, year, hours, minutes;
        month = Integer.parseInt(strDate.substring(0, strDate.indexOf("/")));
        day = Integer.parseInt(strDate.substring(strDate.indexOf("/") + 1, strDate.lastIndexOf("/")));
        year = Integer.parseInt(strDate.substring(strDate.lastIndexOf("/") + 1, strDate.length() -1));
        hours = Integer.parseInt(strTime.substring(0, strTime.indexOf(":")));
        minutes = Integer.parseInt(strTime.substring(strTime.indexOf(":") + 2, strTime.indexOf(":") + 4));
        if (strTime.contains("pm"))
            hours += 12;
        GregorianCalendar calendar = new GregorianCalendar(year, month, day, hours, minutes);
        return calendar;
    }


    @Override
    public int compareTo(Event e2) {
        GregorianCalendar c1 = transformTextToDate(event_date, event_time);
        GregorianCalendar c2 = transformTextToDate(e2.getEvent_date(), e2.getEvent_time());
        return c1.compareTo(c2);
    }

    public boolean isReminderBeforeEvent() {
        GregorianCalendar c1 = transformTextToDate(event_date, event_time);
        GregorianCalendar c2 = transformTextToDate(initial_reminder_date, initial_reminder_time);
        if (c1.compareTo(c2) > 0)
            return true;
        return false;
    }
}
