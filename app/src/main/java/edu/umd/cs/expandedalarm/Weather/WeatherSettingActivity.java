package edu.umd.cs.expandedalarm.Weather;

import android.os.Bundle;

import edu.umd.cs.expandedalarm.SingleFragmentActivity;


/**
 * WeatherSettingActivity - No describe function
 *      execute WeatherSettingFragment
 */
public class WeatherSettingActivity extends SingleFragmentActivity {
    @Override
    protected WeatherSettingFragment createFragment() {
        return WeatherSettingFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

}
