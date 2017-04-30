package edu.umd.cs.expandedalarm.Weather;

import android.os.Bundle;

import edu.umd.cs.expandedalarm.SingleFragmentActivity;


/**
 * WeatherActivity - No describe function
 *      execute WeatherFragment
 */
public class WeatherActivity extends SingleFragmentActivity {
    @Override
    protected WeatherFragment createFragment() {
        return WeatherFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

}
