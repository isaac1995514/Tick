package edu.umd.cs.expandedalarm.CustomReminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.umd.cs.expandedalarm.DependencyFactory;
import edu.umd.cs.expandedalarm.R;

public class CustomEvents extends AppCompatActivity {

    private Button addEventBtn;
    private ListView listView;
    private CustomEventAdapter eventAdapter;
    private SharedPreferences sharedPreferences;
    private List<Event> events;
    public static final int ADD_EVENT_REQUEST = 0;
    public static final String EDIT_EVENT = "EDIT_EVENT";
    private static final String EVENTS = "EVENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_events);

        //Initialize events
        events =  new ArrayList<Event>();

        //Initialize UI Elements
        addEventBtn = (Button) findViewById(R.id.add_event_btn);
        listView = (ListView) findViewById(R.id.calender_events);

        //Initalize Adapter and set adapter for ListView
        eventAdapter = new CustomEventAdapter(getApplicationContext(), R.layout.single_custom_event);
        listView.setAdapter(eventAdapter);


        //Initalize Shared Prefrences
        sharedPreferences = DependencyFactory.getUserPreference(CustomEvents.this);
        //Load all the events into the shared preferences
        Set<String> eventsString = sharedPreferences.getStringSet(EVENTS, new TreeSet<String>());
        Gson gson = new Gson();
        for (String s: eventsString) {
            events.add(gson.fromJson(s, Event.class));
        }



        //Link UI elements to actions in code
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAdder.class);
                startActivityForResult(intent, ADD_EVENT_REQUEST);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> eventsString = new TreeSet<>();
        Gson gson = new Gson();
        for (Event e: events) {
            String json = gson.toJson(e);
            eventsString.add(json);
         }
        editor.putStringSet(EVENTS, eventsString);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EVENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Event newEvent = (Event) data.getExtras().get(EventAdder.NEW_EVENT);
                eventAdapter.add(newEvent);

                //Create a new intent for the alarm
                Intent intent = new Intent()
                        .setAction("ADD_CUSTOM_EVENT")
                        .putExtra("Event", newEvent);

                sendBroadcast(intent);

            }
        }
    }

    public class CustomEventAdapter extends ArrayAdapter {

        public class EventHolder {
            public TextView name;
            public TextView date;
            public ImageView delete_icon;
        }

        public CustomEventAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void add(Object object) {
            super.add(object);
            Event eventToAdd = (Event) object;
            boolean eventFound = false;
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getEvent_ID() == eventToAdd.getEvent_ID()) {
                    events.remove(i);
                    events.add(eventToAdd);
                    eventFound = true;
                    break;
                }
            }
            if (eventFound == false)
                events.add(eventToAdd);
            Collections.sort(events);
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return events.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            EventHolder eventHolder = new EventHolder();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_custom_event, parent, false);
                eventHolder.name = (TextView) row.findViewById(R.id.single_event_name);
                eventHolder.date = (TextView) row.findViewById(R.id.single_event_date);
                eventHolder.delete_icon = (ImageView) row.findViewById(R.id.delete_icon);
                row.setTag(eventHolder);
            }
            else {
                eventHolder = (EventHolder) row.getTag();
            }

            final Event event = (Event) getItem(position);
            eventHolder.name.setText(event.getEvent_name());
            eventHolder.date.setText(event.getEvent_date());
            eventHolder.delete_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).getEvent_ID() == event.getEvent_ID())
                            events.remove(i);
                    }
                    notifyDataSetChanged();
                }
            });
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EventAdder.class);
                    intent.putExtra(EDIT_EVENT, event);
                    startActivityForResult(intent, ADD_EVENT_REQUEST);
                }
            });

            return row;
        }
    }
}
