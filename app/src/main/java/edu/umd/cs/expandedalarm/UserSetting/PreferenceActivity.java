package edu.umd.cs.expandedalarm.UserSetting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import edu.umd.cs.expandedalarm.R;

public class PreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new UserPreference()).commit();
    }

    public static class UserPreference extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.user_preference);

            setPreferencesFromResource(R.xml.user_preference, rootKey);

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
