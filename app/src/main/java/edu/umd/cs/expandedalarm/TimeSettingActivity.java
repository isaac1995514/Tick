package edu.umd.cs.expandedalarm;

public class TimeSettingActivity extends SingleFragmentActivity {
    @Override
    protected TimeSettingFragment createFragment() {
        return TimeSettingFragment.newInstance();
    }
}
