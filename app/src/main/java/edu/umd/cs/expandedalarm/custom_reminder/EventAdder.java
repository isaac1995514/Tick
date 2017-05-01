package edu.umd.cs.expandedalarm.custom_reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import edu.umd.cs.expandedalarm.R;

import static edu.umd.cs.expandedalarm.custom_reminder.CustomEvents.EDIT_EVENT;

public class EventAdder extends AppCompatActivity {

    EditText event_name, event_description;
    TextView event_date, event_time, initial_reminder_date, initial_reminder_time;
    Button cancel_button, save_button;
    Spinner reminder_spinner;
    ImageView event_date_calendar, event_time_clock, initial_reminder_date_calendar,
              initial_reminder_time_clock;
    Calendar calendar = Calendar.getInstance();
    public static final String NEW_EVENT = "NEW_EVENT";
    Event event;
//    public static int eventCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_adder);
        getSupportActionBar().hide();

        if (getIntent().getExtras() != null)
            event = (Event) getIntent().getExtras().getSerializable(EDIT_EVENT);

        //INITIALIZE UI ELEMENTS
        //----------------------
        //Initialize EditTexts
        event_name = (EditText) findViewById(R.id.event_name);
        event_description = (EditText) findViewById(R.id.event_description);
        event_date = (TextView) findViewById(R.id.event_date);
        event_time = (TextView) findViewById(R.id.event_time);
        initial_reminder_date = (TextView) findViewById(R.id.initial_reminder_date);
        initial_reminder_time = (TextView) findViewById(R.id.initial_reminder_time);
        //Initialize Buttons
        cancel_button = (Button) findViewById(R.id.cancel_button);
        save_button = (Button) findViewById(R.id.save_button);
        //Initalize Spinner
        reminder_spinner = (Spinner) findViewById(R.id.reminder_spinner);
        //Initialize ImageViews
        event_date_calendar = (ImageView) findViewById(R.id.event_date_calendar);
        event_time_clock = (ImageView) findViewById(R.id.event_time_clock);
        initial_reminder_date_calendar = (ImageView) findViewById(R.id.initial_reminder_date_calendar);
        initial_reminder_time_clock = (ImageView) findViewById(R.id.initial_reminder_time_clock);

        //Link UI elements to actions in code
        event_date_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventAdder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        event_date.setText(month + "/" + dayOfMonth + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        event_time_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EventAdder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setTimeOnEditText(event_time, hourOfDay, minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });
        initial_reminder_date_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventAdder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        initial_reminder_date.setText(month + "/" + dayOfMonth + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        initial_reminder_time_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EventAdder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setTimeOnEditText(initial_reminder_time, hourOfDay, minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validEntries = true;

                //Check for valid event name:
                if (event_name.getText().toString().equals("")) {
                    showErrorDialog("", getResources().getString(R.string.event_name_reminder),
                            getResources().getString(R.string.ok));
                    validEntries = false;
                }

                //Check for valid event description:
                else if (event_description.getText().toString().equals("")) {
                    showErrorDialog("", getResources().getString(R.string.event_description_reminder),
                            getResources().getString(R.string.ok));
                    validEntries = false;
                }

                //Check for valid event start date:
                else if (event_date.getText().toString().equals("")) {
                    showErrorDialog("", getResources().getString(R.string.date_reminder),
                            getResources().getString(R.string.ok));
                    validEntries = false;
                }

                //Check for valid event start time:
                else if (event_time.getText().toString().equals("")) {
                    showErrorDialog("", getResources().getString(R.string.time_reminder),
                            getResources().getString(R.string.ok));
                    validEntries = false;
                }

                //Check for valid event reminder date:
                else if (initial_reminder_date.getText().toString().equals("")) {
                    showErrorDialog("", getResources().getString(R.string.initial_date_reminder),
                            getResources().getString(R.string.ok));
                    validEntries = false;
                }

                //Check for valid event reminder time:
                else if (initial_reminder_time.getText().toString().equals("")) {
                    showErrorDialog("", getResources().getString(R.string.initial_time_reminder),
                            getResources().getString(R.string.ok));
                    validEntries = false;
                }

                if (validEntries) {

                    GregorianCalendar c1 = transformTextToDate(event_date.getText().toString(),
                            event_time.getText().toString());
                    GregorianCalendar c2 = transformTextToDate(
                            initial_reminder_date.getText().toString(),
                            initial_reminder_time.getText().toString());

                    if (c1.compareTo(c2) <= 0) {
                        //Reminder date occurs on or after event date -> Display error
                        showErrorDialog("", getResources().getString(R.string.reminder_date_error),
                                getResources().getString(R.string.ok));
                    }
                    else {
                        Intent intent = new Intent();
                        if (event == null) {
                            event = new Event();
                            event.setEvent_ID(String.valueOf(UUID.randomUUID()).hashCode());
                        }
                        event.setEvent_name(event_name.getText().toString());
                        event.setEvent_description(event_description.getText().toString());
                        event.setEvent_date(event_date.getText().toString());
                        event.setEvent_time(event_time.getText().toString());
                        event.setReminder_freq(reminder_spinner.getSelectedItem().toString());
                        event.setInitial_reminder_date(initial_reminder_date.getText().toString());
                        event.setInitial_reminder_time(initial_reminder_time.getText().toString());

                        intent.putExtra(NEW_EVENT, event);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        //Fill out event's original details if event is not null
        if (event != null) {
            event_name.setText(event.getEvent_name());
            event_description.setText(event.getEvent_description());
            event_date.setText(event.getEvent_date());
            event_time.setText(event.getEvent_time());
            initial_reminder_date.setText(event.getInitial_reminder_date());
            initial_reminder_time.setText(event.getInitial_reminder_time());
            String[] reminders = getResources().getStringArray(R.array.reminder_alarm);
            for (int i = 0; i < reminders.length; i++) {
                if (event.getReminder_freq().equals(reminders[i])) {
                    reminder_spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setTimeOnEditText(TextView view, int hourOfDay, int minute) {
        String minuteString = Integer.toString(minute);
        String amPm = "AM";
        if (minute < 10)
            minuteString = "0" + minuteString;
        if (hourOfDay > 11) {
            hourOfDay -= 12;
            amPm = "PM";
        }
        if (hourOfDay == 0)
            hourOfDay += 12;
        String hourString = Integer.toString(hourOfDay);
        if (hourOfDay == 0)
            hourString = "0" + hourString;
        view.setText(hourString + ": " + minuteString + " " + amPm);
    }

    private void showErrorDialog(String title, String message, String btnText) {
        AlertDialog alertDialog = new AlertDialog.Builder(EventAdder.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, btnText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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


}
