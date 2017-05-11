package edu.umd.cs.expandedalarm.main_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import at.markushi.ui.CircleButton;
import edu.umd.cs.expandedalarm.DependencyFactory;
import edu.umd.cs.expandedalarm.R;
import edu.umd.cs.expandedalarm.custom_reminder.CustomEvents;
import edu.umd.cs.expandedalarm.onboarding.OnboardingActivity;
import edu.umd.cs.expandedalarm.relationship.RelationshipActivity;
import edu.umd.cs.expandedalarm.user_setting.PreferenceActivity;
import edu.umd.cs.expandedalarm.weather.WeatherActivity;

public class MainScreen extends AppCompatActivity {
    SharedPreferences userPreference;
    TextView weather_text;
    TextView relationship_text;
    TextView customize_text;
    TextView user_setting_text;
    CircleButton weather_button;
    CircleButton relationship_button;
    CircleButton customize_button;
    CircleButton user_setting_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userPreference = DependencyFactory.getUserPreference(getApplicationContext());

        if (userPreference.getBoolean("first_run", true)) {
            Intent intent = new Intent(this, OnboardingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main_screen);
        getSupportActionBar().hide();

        Typeface font = Typeface.createFromAsset(getAssets(), "JLSDataGothicC_NC.otf");

        weather_text = (TextView) findViewById(R.id.weather_text);
        relationship_text = (TextView) findViewById(R.id.relationship_text);
        customize_text = (TextView) findViewById(R.id.customize_text);
        user_setting_text = (TextView) findViewById(R.id.user_setting_text);

        weather_text.setTypeface(font);
        relationship_text.setTypeface(font);
        customize_text.setTypeface(font);
        user_setting_text.setTypeface(font);

        weather_button = (CircleButton) findViewById(R.id.weather_button);

        weather_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));

            }
        });

        relationship_button = (CircleButton) findViewById(R.id.relationship_button);

        relationship_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), RelationshipActivity.class));

            }
        });

        customize_button = (CircleButton) findViewById(R.id.customize_button);

        customize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), CustomEvents.class));

            }
        });

        user_setting_button = (CircleButton) findViewById(R.id.user_setting_button);

        user_setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), PreferenceActivity.class));

            }
        });

        setUpRelationAlarm();
        userPreferenceTesting(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!userPreference.getBoolean("relationship", false)){
            relationship_button.setEnabled(false);
            relationship_button.setImageResource(R.drawable.gray_heart_icon);
        }else{
            relationship_button.setEnabled(true);
            relationship_button.setImageResource(R.drawable.heart_icon);
        }
    }

    private void setUpRelationAlarm(){

        Intent valentine = new Intent();
        valentine.setAction("UPDATE_RELATIONSHIP");
        valentine.putExtra("DATE", "VALENTINE");
        valentine.putExtra("ID", R.id.valentine);
        sendBroadcast(valentine);

        Intent christmas = new Intent();
        christmas.setAction("UPDATE_RELATIONSHIP");
        christmas.putExtra("DATE", "CHRISTMAS");
        christmas.putExtra("ID", R.id.christmas);
        sendBroadcast(christmas);

    }

    private void userPreferenceTesting(Context context){
        SharedPreferences s = DependencyFactory.getUserPreference(context);

        Log.d("USER_NAME", s.getString("user_name", "null"));
        Log.d("USER_ZIP", s.getString("zip_code", "null"));
        Log.d("USER_RELATIONSHIP", Boolean.toString(s.getBoolean("relationship", false)));
        Log.d("USER_RAIN", Boolean.toString(s.getBoolean("rain", false)));
        Log.d("USER_SNOW", Boolean.toString(s.getBoolean("snow", false)));
        Log.d("USER_WIND", Boolean.toString(s.getBoolean("wind", false)));
        Log.d("USER_TEMP", Boolean.toString(s.getBoolean("temp", false)));

    }
}
