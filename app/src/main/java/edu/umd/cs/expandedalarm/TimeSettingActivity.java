package edu.umd.cs.expandedalarm;

import android.os.Bundle;


/**
 * TimeSettingActivity - No describe function
 *      execute TimeSettingFragment
 */
public class TimeSettingActivity extends SingleFragmentActivity {
    @Override
    protected TimeSettingFragment createFragment() {
        return TimeSettingFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

}
