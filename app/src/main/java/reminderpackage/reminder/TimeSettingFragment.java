package reminderpackage.reminder;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

import reminderpackage.reminder.model.WeekDay;
import reminderpackage.reminder.model.WeekDayService;

public class TimeSettingFragment extends Fragment {

    private FragmentManager fm;
    private RecyclerView recyclerView;
    private WeekDayAdapter weekDayAdapter;
    private WeekDayService weekDayService;
    private Button user;


    public static TimeSettingFragment newInstance(){
        return new TimeSettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weekDayService = DependencyFactory.getWeekDayService(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time_setting, container, false);
        user = (Button) view.findViewById(R.id.user_name_button);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), PerferenceActivity.class));
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.day_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;

    }

    private void updateUI(){

        List<WeekDay> currentInfo = weekDayService.getAllWeekDays();

        if (weekDayAdapter == null){
            weekDayAdapter = new WeekDayAdapter(currentInfo);
            recyclerView.setAdapter(weekDayAdapter);
        } else {
            weekDayAdapter.setWeekDays(currentInfo);
            weekDayAdapter.notifyDataSetChanged();
        }

    }

    private class WeekDayHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private WeekDay weekDay;
        private int hour, minute;
        private TextView day, time;

        public WeekDayHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            day = (TextView) itemView.findViewById(R.id.day_of_week);
            time = (TextView) itemView.findViewById(R.id.time_of_week);

        }

        public void bindWeekDay(WeekDay weekDay){
            this.weekDay = weekDay;

            day.setText(weekDay.getDay().toString());
            time.setText(weekDay.getTime());
        }

        @Override
        public void onClick(View view){
            Calendar calendar = Calendar.getInstance();

            //Getting Current Time
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener(){


                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    weekDayService.updateTime(weekDay.getDay(), hourOfDay, minute);
                    updateUI();
                }
            };

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),listener,hour, minute, false);
            timePickerDialog.show();

        }

    }

    private class WeekDayAdapter extends RecyclerView.Adapter<WeekDayHolder>{

        private List<WeekDay> weekDays;

        public WeekDayAdapter(List<WeekDay> weekDays){
            this.weekDays = weekDays;
        }

        public void setWeekDays(List<WeekDay> weekDays){
            this.weekDays = weekDays;
        }

        @Override
        public WeekDayHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View weekDay_view = layoutInflater.inflate(R.layout.list_item_weekday, parent, false);
            return new WeekDayHolder(weekDay_view);
        }

        @Override
        public void onBindViewHolder(WeekDayHolder weekDayHolder, int position){
            WeekDay weekDay = weekDays.get(position);
            weekDayHolder.bindWeekDay(weekDay);
        }

        @Override
        public int getItemCount(){
            return 7;
        }

    }






}
