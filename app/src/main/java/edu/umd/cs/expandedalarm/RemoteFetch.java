package edu.umd.cs.expandedalarm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteFetch {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?APPID=%s&zip=%s&units=imperial";

    private static JSONObject weather_data;

    public static JSONObject getJSON(Context context, String city) {
        RetrieveWeatherJSONTask task = new RetrieveWeatherJSONTask();
        task.execute(context.getString(R.string.open_weather_maps_app_id), city);
        try {
            return task.get();
        } catch (Exception e) {
            Log.e("remotefetch", e.toString());
            return null;
        }
        //return weather_data;
    }

    public static class RetrieveWeatherJSONTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String[] strs) {
            if (strs.length != 2)
                return null;
            try {
                URL url = new URL(String.format(OPEN_WEATHER_MAP_API, strs[0], strs[1]));
                Log.d("remotefetch", url.toString());
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject data = new JSONObject(json.toString());
                Log.d("remotefetch", data.toString());

                // This value will be 404 if the request was not
                // successful
                if (data.getInt("cod") != 200) {
                    return null;
                }
                return data;
            } catch (Exception e) {
                Log.e("remotefetch", e.toString());
                return null;
            }
        }
    }
}
