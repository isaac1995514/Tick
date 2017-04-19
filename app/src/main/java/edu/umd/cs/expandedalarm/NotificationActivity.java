package edu.umd.cs.expandedalarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;


/**
 * Created by Typhaze on 4/11/17.
 */

public class NotificationActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.umd.cs.expandedalarm.R.layout.activity_preference);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new UserPreference()).commit();
    }

    public static class UserPreference extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(edu.umd.cs.expandedalarm.R.xml.notification_preference);
            setPreferencesFromResource(edu.umd.cs.expandedalarm.R.xml.notification_preference, rootKey);

            CheckBoxPreference weather =
                    (CheckBoxPreference)getPreferenceManager().findPreference("weather");

            weather.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference pref) {
                    CheckBoxPreference p = (CheckBoxPreference)pref;
                    CheckBoxPreference rain =
                            (CheckBoxPreference)getPreferenceManager().findPreference("rain");
                    CheckBoxPreference snow =
                            (CheckBoxPreference)getPreferenceManager().findPreference("snow");
                    CheckBoxPreference wind =
                            (CheckBoxPreference)getPreferenceManager().findPreference("wind");
                    CheckBoxPreference temp =
                            (CheckBoxPreference)getPreferenceManager().findPreference("temp");
                    rain.setChecked(p.isChecked());
                    snow.setChecked(p.isChecked());
                    wind.setChecked(p.isChecked());
                    temp.setChecked(p.isChecked());
                    return false;
                }
            });
        }

    }
}
