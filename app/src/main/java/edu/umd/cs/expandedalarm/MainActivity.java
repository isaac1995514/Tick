package edu.umd.cs.expandedalarm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getResources().getText(R.string.notification));
        final SwitchCompat weather = (SwitchCompat) findViewById(R.id.weather);
        final SwitchCompat rain = (SwitchCompat) findViewById(R.id.snow);
        final SwitchCompat snow = (SwitchCompat) findViewById(R.id.rain);
        final SwitchCompat wind = (SwitchCompat) findViewById(R.id.wind);
        final SwitchCompat temp = (SwitchCompat) findViewById(R.id.temp);
        final EditText minTemp = (EditText) findViewById(R.id.minTemp);
        final EditText maxTemp = (EditText) findViewById(R.id.maxTemp);
        final TextView curr_temp = (TextView) findViewById(R.id.current_temp);
        final TextView tempText = (TextView) findViewById(R.id.tempText);
        final TextView main_weather = (TextView) findViewById(R.id.main_weather);

        if (weather.isChecked()) {
            rain.setChecked(true);
            snow.setChecked(true);
            wind.setChecked(true);
        }

        if (temp.isChecked()) {
            minTemp.setEnabled(true);
            maxTemp.setEnabled(true);
            tempText.setTextColor(Color.BLACK);
        } else {
            minTemp.setEnabled(false);
            maxTemp.setEnabled(false);
            tempText.setTextColor(Color.GRAY);
        }

        weather.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if (weather.isChecked()) {
                            rain.setChecked(true);
                            snow.setChecked(true);
                            wind.setChecked(true);
                            temp.setChecked(true);
                            minTemp.setEnabled(true);
                            maxTemp.setEnabled(true);
                            tempText.setTextColor(Color.BLACK);
                        } else {
                            rain.setChecked(false);
                            snow.setChecked(false);
                            wind.setChecked(false);
                            temp.setChecked(false);
                            minTemp.setEnabled(false);
                            maxTemp.setEnabled(false);
                            tempText.setTextColor(Color.GRAY);
                        }
                    }
                }
        );
        temp.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if (temp.isChecked()) {
                            minTemp.setEnabled(true);
                            maxTemp.setEnabled(true);
                            tempText.setTextColor(Color.BLACK);
                        } else {
                            minTemp.setEnabled(false);
                            maxTemp.setEnabled(false);
                            tempText.setTextColor(Color.GRAY);
                        }
                    }
                }
        );

        String city = new CityPreference(MainActivity.this).getCity();
        JSONObject json = RemoteFetch.getJSON(MainActivity.this, city);
        Log.d("SimpleWeather", json.toString());
        try {
            JSONObject main = json.getJSONObject("main");
            curr_temp.setText(String.valueOf(main.getDouble("temp")) + " \u2109");
            main_weather.setText(String.valueOf(json));
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
}
