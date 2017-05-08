package edu.umd.cs.expandedalarm.onboarding;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.expandedalarm.DependencyFactory;
import edu.umd.cs.expandedalarm.R;
import edu.umd.cs.expandedalarm.main_screen.MainScreen;

public class OnboardingActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 5;

    private ViewPager mViewPager;
    private OnboardingPagerAdapter mPagerAdapter;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private Button mFinishButton;

    private Drawable enabledIndicator;
    private Drawable disabledIndicator;

    private static String name;
    private static String zipcode;
    private static boolean relationshipReminder;
    private static boolean rainReminder;
    private static boolean snowReminder;
    private static boolean windReminder;
    private static boolean temperatureReminder;

    private static TextView nameTextView;
    private static TextView zipTextView;
    private static TextView relationshipTextView;
    private static TextView rainTextView;
    private static TextView snowTextView;
    private static TextView windTextView;
    private static TextView temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        enabledIndicator = ContextCompat.getDrawable(this, R.drawable.ic_brightness_1_white_24dp);
        disabledIndicator = ContextCompat.getDrawable(this, R.drawable.ic_panorama_fish_eye_white_24dp);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mPagerAdapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mPreviousButton = (ImageButton) findViewById(R.id.onboard_prev);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mViewPager.getCurrentItem();

                if (currentPosition > 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                }
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.onboard_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mViewPager.getCurrentItem();

                if (currentPosition < NUM_PAGES - 1) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                }
            }
        });
        mFinishButton = (Button) findViewById(R.id.onboard_finish);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = DependencyFactory.getUserPreference(OnboardingActivity.this).edit();
                editor.putString("user_name", name);
                editor.putString("zip_code", zipcode);
                editor.putBoolean("relationship", relationshipReminder);
                editor.putBoolean("rain", rainReminder);
                editor.putBoolean("snow", snowReminder);
                editor.putBoolean("wind", windReminder);
                editor.putBoolean("temp", temperatureReminder);
                editor.putBoolean("first_run", false);
                editor.commit();

                Intent intent = new Intent(OnboardingActivity.this, MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        ImageView welcomeIndicator = (ImageView) findViewById(R.id.onboard_indicator_welcome);
        welcomeIndicator.setImageDrawable(enabledIndicator);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            List<ImageView> indicators;
            ArgbEvaluator evaluator;
            int[] colorList;

            {
                indicators = new ArrayList<>();
                evaluator = new ArgbEvaluator();
                colorList = new int[] {
                        ContextCompat.getColor(getApplicationContext(), R.color.background_welcome),
                        ContextCompat.getColor(getApplicationContext(), R.color.background_personal),
                        ContextCompat.getColor(getApplicationContext(), R.color.background_relationship),
                        ContextCompat.getColor(getApplicationContext(), R.color.background_weather),
                        ContextCompat.getColor(getApplicationContext(), R.color.background_summary),
                };

                indicators.add((ImageView) findViewById(R.id.onboard_indicator_welcome));
                indicators.add((ImageView) findViewById(R.id.onboard_indicator_personal));
                indicators.add((ImageView) findViewById(R.id.onboard_indicator_relationship));
                indicators.add((ImageView) findViewById(R.id.onboard_indicator_weather));
                indicators.add((ImageView) findViewById(R.id.onboard_indicator_summary));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int colorUpdate = (int) evaluator.evaluate(positionOffset, colorList[position], colorList[position == (NUM_PAGES - 1) ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                int color;

                updateIndicators(position);

                switch (position) {
                    case 0:
                        color = ContextCompat.getColor(getApplicationContext(), R.color.background_welcome);
                        break;
                    case 1:
                        color = ContextCompat.getColor(getApplicationContext(), R.color.background_personal);
                        break;
                    case 2:
                        color = ContextCompat.getColor(getApplicationContext(), R.color.background_relationship);
                        break;
                    case 3:
                        color = ContextCompat.getColor(getApplicationContext(), R.color.background_weather);
                        break;
                    case 4:
                        color = ContextCompat.getColor(getApplicationContext(), R.color.background_summary);
                        break;
                    default:
                        return;
                }

                mViewPager.setBackgroundColor(color);
                mPreviousButton.setVisibility(position != 0 ? View.VISIBLE : View.INVISIBLE);
                mNextButton.setVisibility(position != (NUM_PAGES - 1) ? View.VISIBLE : View.INVISIBLE);
                mFinishButton.setVisibility(position == (NUM_PAGES - 1) ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            private void updateIndicators(int position) {
                for (int i = 0; i < indicators.size(); i++) {
                    Drawable drawable;

                    if (i == position) {
                        drawable = enabledIndicator;
                    } else {
                        drawable = disabledIndicator;
                    }

                    indicators.get(i).setImageDrawable(drawable);
                }
            }
        });
    }

    public static class OnboardingPageFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        private final String TAG = getClass().getSimpleName();

        public OnboardingPageFragment() {
        }

        public static OnboardingPageFragment newInstance(int sectionNumber) {
            OnboardingPageFragment fragment = new OnboardingPageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private static void updateTextView(TextView textView, boolean enabled, int enabledStringResId, int disabledStringResId, Context context) {
            if (enabled) {
                textView.setText(context.getString(enabledStringResId));
            } else {
                textView.setText(context.getString(disabledStringResId));
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            View rootView;
            switch (sectionNumber) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_onboarding_welcome, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_onboarding_personal, container, false);

                    EditText nameEditText = (EditText) rootView.findViewById(R.id.name);
                    EditText zipEditText = (EditText) rootView.findViewById(R.id.zip);

                    nameEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            name = s.toString();

                            if (nameTextView != null) {
                                nameTextView.setText(name);
                            }
                        }
                    });

                    zipEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            zipcode = s.toString();

                            if (zipTextView != null) {
                                zipTextView.setText(zipcode);
                            }
                        }
                    });

                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_onboarding_relationship, container, false);

                    Switch reminderSwitch = (Switch) rootView.findViewById(R.id.reminder_switch);

                    reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            relationshipReminder = isChecked;

                            if (relationshipTextView != null) {
                                updateTextView(
                                        relationshipTextView,
                                        isChecked,
                                        R.string.onboard_summary_relationship_enabled,
                                        R.string.onboard_summary_relationship_disabled,
                                        getContext());
                            }
                        }
                    });

                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_onboarding_weather, container, false);

                    Switch rainSwitch = (Switch) rootView.findViewById(R.id.rain_switch);
                    Switch snowSwitch = (Switch) rootView.findViewById(R.id.snow_switch);
                    Switch windSwitch = (Switch) rootView.findViewById(R.id.wind_switch);
                    Switch temperatureSwitch = (Switch) rootView.findViewById(R.id.temperature_switch);

                    rainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            rainReminder = isChecked;

                            if (rainTextView != null) {
                                updateTextView(
                                        rainTextView,
                                        isChecked,
                                        R.string.onboard_summary_rain_enabled,
                                        R.string.onboard_summary_rain_disabled,
                                        getContext());
                            }
                        }
                    });
                    snowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            snowReminder = isChecked;

                            if (snowTextView != null) {
                                updateTextView(
                                        snowTextView,
                                        isChecked,
                                        R.string.onboard_summary_snow_enabled,
                                        R.string.onboard_summary_snow_disabled,
                                        getContext());
                            }
                        }
                    });
                    windSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            windReminder = isChecked;

                            if (windTextView != null) {
                                updateTextView(
                                        windTextView,
                                        isChecked,
                                        R.string.onboard_summary_wind_enabled,
                                        R.string.onboard_summary_wind_disabled,
                                        getContext());
                            }
                        }
                    });
                    temperatureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            temperatureReminder = isChecked;

                            if (temperatureTextView!= null) {
                                updateTextView(
                                        temperatureTextView,
                                        isChecked,
                                        R.string.onboard_summary_temperature_enabled,
                                        R.string.onboard_summary_temperature_disabled,
                                        getContext());
                            }
                        }
                    });

                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_onboarding_summary, container, false);

                    nameTextView = (TextView) rootView.findViewById(R.id.name);
                    zipTextView = (TextView) rootView.findViewById(R.id.zip);
                    relationshipTextView = (TextView) rootView.findViewById(R.id.relationship);
                    rainTextView = (TextView) rootView.findViewById(R.id.rain);
                    snowTextView = (TextView) rootView.findViewById(R.id.snow);
                    windTextView = (TextView) rootView.findViewById(R.id.wind);
                    temperatureTextView = (TextView) rootView.findViewById(R.id.temperature);

                    nameTextView.setText(name);
                    zipTextView.setText(zipcode);

                    if (relationshipReminder) {
                        relationshipTextView.setText(getString(R.string.onboard_summary_relationship_enabled));
                    } else {
                        relationshipTextView.setText(getString(R.string.onboard_summary_relationship_disabled));
                    }

                    if (rainReminder) {
                        rainTextView.setText(getString(R.string.onboard_summary_rain_enabled));
                    } else {
                        rainTextView.setText(getString(R.string.onboard_summary_rain_disabled));
                    }

                    if (snowReminder) {
                        snowTextView.setText(getString(R.string.onboard_summary_snow_enabled));
                    } else {
                        snowTextView.setText(getString(R.string.onboard_summary_snow_disabled));
                    }

                    if (windReminder) {
                        windTextView.setText(getString(R.string.onboard_summary_wind_enabled));
                    } else {
                        windTextView.setText(getString(R.string.onboard_summary_wind_disabled));
                    }

                    if (temperatureReminder) {
                        temperatureTextView.setText(getString(R.string.onboard_summary_temperature_enabled));
                    } else {
                        temperatureTextView.setText(getString(R.string.onboard_summary_temperature_disabled));
                    }

                    break;
                default:
                    Log.e(TAG, "Invalid section number: " + sectionNumber);
                    return null;
            }

            return rootView;
        }
    }

    public class OnboardingPagerAdapter extends FragmentPagerAdapter {

        public OnboardingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return OnboardingPageFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Welcome";
                case 1:
                    return "Personal Information";
                case 2:
                    return "Relationships";
                case 3:
                    return "Weather";
                case 4:
                    return "Summary";
            }
            return null;
        }
    }
}
