package com.example.stalker.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by nimloth on 15.10.17.
 */
public class SettingsFragment extends Fragment {
    private LinearLayout rootView;
    private EditText stalkedUsernameEditText;
    private Button okButton;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String INSTAGRAM_PREF = "Instagram_Preferences";
    public static final String SHARED = "Stalked_Preferences";
    public static final String STALKED_USERNAME = "stalked_username";
    public static final String STALKED_ID = "stalked_id";
    private static final String API_ACCESS_TOKEN = "access_token";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = (LinearLayout)inflater.inflate(R.layout.fragment_settings, container, false);
        this.stalkedUsernameEditText = (EditText) rootView.findViewById(R.id.stalked_username);
        this.okButton = (Button) rootView.findViewById(R.id.ok_button);
        setOnOkButtonClickListener();

        sharedPref = getActivity().getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        return rootView;
    }

    private void setOnOkButtonClickListener() {
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkButtonClicked();
            }
        });
    }

    private void onOkButtonClicked() {
        // TODO: clear username from spaces
        String stalkedUsername = stalkedUsernameEditText.getText().toString();
        SharedPreferences instagramPref = getActivity().getSharedPreferences(INSTAGRAM_PREF,
                Context.MODE_PRIVATE);
        String accessToken = instagramPref.getString(API_ACCESS_TOKEN, "");
        String getBasicInfoUrl = "https://api.instagram.com/v1/users/search?q="
                + stalkedUsername
                + "&access_token="
                + accessToken
                + "&scope=basic";

        try {
            CheckUsernameExistsTask checkUsernameExistsTask = new CheckUsernameExistsTask();
            String response = checkUsernameExistsTask.execute(getBasicInfoUrl).get();
            Log.d("settings", response);
            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray data = jsonObj.getJSONArray("data");
            if (data.length() != 0) {
                Log.d("settings", "searched result is not empty");
                JSONObject userObj = data.optJSONObject(0);
                String username = userObj.getString("username");
                Log.d("settings", "first username found: " + username);
                if (username.equals(stalkedUsername)) {
                    editor.putString(STALKED_USERNAME, stalkedUsername);
                    String stalkedId = userObj.getString("id");
                    editor.putString(STALKED_ID, stalkedId);
                    Log.d("settings", "staked username: " + stalkedUsername);
                    Log.d("settings", "staked id: " + stalkedId);
                }
            }
        } catch (Exception e) {
            Log.d("settings", "exception in sendGetRequest");
        }
        editor.putString(STALKED_USERNAME, stalkedUsername);
    }

    private class CheckUsernameExistsTask extends AsyncTask<String, Object, String> {
        public String doInBackground(String... urls) {
            String url = urls[0];
            StringBuffer response = new StringBuffer();
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");


                int responseCode = 0;
                responseCode = con.getResponseCode();
                Log.d("settings", "\nSending 'GET' request to URL : " + url);
                Log.d("settings", "Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch(IOException exc) {
                Log.v("settings", "exception in get request");
            }
            return response.toString();
        }
    }
}
