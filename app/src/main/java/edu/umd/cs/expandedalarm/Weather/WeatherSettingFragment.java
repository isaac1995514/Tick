package edu.umd.cs.expandedalarm.Weather;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

import edu.umd.cs.expandedalarm.DependencyFactory;
import edu.umd.cs.expandedalarm.R;
import edu.umd.cs.expandedalarm.model.WeekDay;
import edu.umd.cs.expandedalarm.model.WeekDayService;


/**
 * Enable user to initialize and update default schedule
 */
public class WeatherSettingFragment extends Fragment {
    private RecyclerView recyclerView;
    private WeekDayAdapter weekDayAdapter;
    private WeekDayService weekDayService;

    /**
     *
     * @return an instance of WeatherSettingFragment
     */
    public static WeatherSettingFragment newInstance() {
        return new WeatherSettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weekDayService = DependencyFactory.getWeekDayService(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time_setting, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    /**
     * Update the Adapter(User interface) after any change to the default schedule
     */
    private void updateUI() {
        List<WeekDay> currentInfo = weekDayService.getAllWeekDays();

        if (weekDayAdapter == null) {
            weekDayAdapter = new WeekDayAdapter(currentInfo);
            recyclerView.setAdapter(weekDayAdapter);
        } else {
            weekDayAdapter.setWeekDays(currentInfo);
            weekDayAdapter.notifyDataSetChanged();
        }
    }

    private class WeekDayHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private WeekDay weekDay;
        private int hour, minute;
        private TextView day, time;

        public WeekDayHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Caviar_Dreams_Bold.ttf");

            day = (TextView) itemView.findViewById(R.id.day_of_week);
            time = (TextView) itemView.findViewById(R.id.time_of_week);
            day.setTypeface(font);
            time.setTypeface(font);
        }

        public void bindWeekDay(WeekDay weekDay) {
            this.weekDay = weekDay;

            day.setText(weekDay.getDay().toString());
            time.setText(weekDay.getTime());
        }

        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();

            //Getting Current Time
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    weekDayService.updateTime(weekDay.getDay(), Integer.toString(hourOfDay), Integer.toString(minute));
                    updateUI();

                    //reset Alarm by sending intent to the AlarmReceiver
                    Intent intent = new Intent();
                    intent.setAction("UPDATE_WEATHER");
                    intent.putExtra("DayOfWeek", weekDay);

                    getActivity().sendBroadcast(intent);
                }
            };

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, listener, hour, minute, false);

            timePickerDialog.show();
        }
    }

    private class WeekDayAdapter extends RecyclerView.Adapter<WeekDayHolder> {
        private List<WeekDay> weekDays;

        public WeekDayAdapter(List<WeekDay> weekDays) {
            this.weekDays = weekDays;
        }

        public void setWeekDays(List<WeekDay> weekDays) {
            this.weekDays = weekDays;
        }

        @Override
        public WeekDayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View weekDay_view = layoutInflater.inflate(R.layout.list_item_weekday, parent, false);
            return new WeekDayHolder(weekDay_view);
        }

        @Override
        public void onBindViewHolder(WeekDayHolder weekDayHolder, int position) {
            WeekDay weekDay = weekDays.get(position);
            weekDayHolder.bindWeekDay(weekDay);
        }

        @Override
        public int getItemCount() {
            return 7;
        }
    }
}
