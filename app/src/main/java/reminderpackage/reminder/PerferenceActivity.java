package reminderpackage.reminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class PerferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perference);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new UserPreference()).commit();
    }

    public static class UserPreference extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.user_preference);

            setPreferencesFromResource(R.xml.user_preference, rootKey);
        }

    }
}
