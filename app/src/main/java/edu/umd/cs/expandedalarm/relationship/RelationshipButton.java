package edu.umd.cs.expandedalarm.relationship;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import edu.umd.cs.expandedalarm.DependencyFactory;
import edu.umd.cs.expandedalarm.R;
import edu.umd.cs.expandedalarm.model.RelationshipService;


/**
 * Created by Isaac on 4/24/2017.
 */

public class RelationshipButton {

    Button button;
    String day;
    RelationshipService relationshipService;

    public RelationshipButton(final View view, final int buttonId, final String day, final boolean defaultDate){
        button = (Button) view.findViewById(buttonId);
        relationshipService = DependencyFactory.getRelationshipService(view.getContext());
        this.day = day;

        button.setOnClickListener(new View.OnClickListener() {

            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {

                if(defaultDate){

                    String [] date = relationshipService.getDate(day);

                    Calendar calendar = Calendar.getInstance();

                    calendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
                    calendar.set(Calendar.MONTH, Integer.valueOf(date[1]));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[2]));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    Log.d("TIME_CURR", c.getTime().toString());
                    Log.d("TIME_SET", calendar.getTime().toString());

                    if(calendar.before(c)){
                        calendar.add(Calendar.YEAR, 1);
                    }

                    long diff = (calendar.getTime().getTime() - c.getTime().getTime());
                    int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));

                    AlertDialog alert = new AlertDialog.Builder(view.getContext(), R.style.popup_theme).create();

                    alert.setTitle("Warning!");
                    alert.setMessage("You are " + numOfDays + " days away from the next " + day);
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "CONFIRM",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );

                    alert.show();

                }else{

                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){

                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            Log.d("TEST", Integer.toString(year) + " - " + Integer.toString(month) +" - " + Integer.toString(dayOfMonth));

                            relationshipService.setDate(day, year, month, dayOfMonth);

                            Intent intent = new Intent();
                            intent.setAction("UPDATE_RELATIONSHIP");
                            intent.putExtra("DATE", day);
                            intent.putExtra("ID", buttonId);

                            view.getContext().sendBroadcast(intent);
                        }
                    };

                    // Launch Date Picker Dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), R.style.DatePickerTheme, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();

                }
            }
        });

    }



}
