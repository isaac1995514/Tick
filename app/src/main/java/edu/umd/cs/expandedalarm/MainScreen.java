package edu.umd.cs.expandedalarm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import at.markushi.ui.CircleButton;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        TextView weather_text = (TextView) findViewById(R.id.weather_text);
        Typeface weather_font = Typeface.createFromAsset(getAssets(), "JLSDataGothicC_NC.otf");
        weather_text.setTypeface(weather_font);

        TextView relationship_text = (TextView) findViewById(R.id.relationship_text);
        Typeface relationship_font = Typeface.createFromAsset(getAssets(), "JLSDataGothicC_NC.otf");
        relationship_text.setTypeface(relationship_font);

        TextView customize_text = (TextView) findViewById(R.id.customize_text);
        Typeface customize_font = Typeface.createFromAsset(getAssets(), "JLSDataGothicC_NC.otf");
        customize_text.setTypeface(customize_font);

        TextView user_setting_text = (TextView) findViewById(R.id.user_setting_text);
        Typeface user_setting_font = Typeface.createFromAsset(getAssets(), "JLSDataGothicC_NC.otf");
        user_setting_text.setTypeface(user_setting_font);

        CircleButton weather_button = (CircleButton) findViewById(R.id.weather_button);

        weather_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), TimeSettingActivity.class));

            }
        });

        CircleButton relationship_button = (CircleButton) findViewById(R.id.relationship_button);

        relationship_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        CircleButton customize_button = (CircleButton) findViewById(R.id.customize_button);

        customize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        CircleButton user_setting_button = (CircleButton) findViewById(R.id.user_setting_button);

        user_setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), PreferenceActivity.class));

            }
        });


    }
}
