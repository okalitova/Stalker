package com.example.stalker.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONException;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    ArrayList<LeftPanelItem> leftPanelMenu = new ArrayList<LeftPanelItem>();
    DrawerLayout drawerLayout;
    ListView leftPanelLayout;

    public MainActivityFragment() {
    }

    private void getLeftPanelMenu() {
        leftPanelMenu.add(new LeftPanelItem("Feed", R.drawable.feed));
        leftPanelMenu.add(new LeftPanelItem("Statistics", R.drawable.statistics));
        leftPanelMenu.add(new LeftPanelItem("Settings", R.drawable.settings));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        leftPanelLayout = (ListView) rootView.findViewById(R.id.left_drawer);

        getLeftPanelMenu();
        // Set the adapter for the list view
        leftPanelLayout.setAdapter(new LeftPanelAdapter(getActivity().getApplicationContext(),
                R.id.left_panel_item, leftPanelMenu));

        leftPanelLayout.setOnItemClickListener(new DrawerItemClickListener(getActivity(), leftPanelLayout, drawerLayout));

        InstagramAuth auth = new InstagramAuth(getActivity());

        auth.execute();

        return rootView;
    }

    public class InstagramAuth extends AsyncTask<String, Void, Void> {

        private final Context mContext;

        public InstagramAuth(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            String csrfmiddlewaretoken = "";
            URL addr = null;
            try {
                addr = new URL("https://api.instagram.com/oauth/authorize/?client_id=657c66cc12f44dbf85552acedb37e19b&redirect_uri=https://vk.com/olga.kalitova&response_type=code");
            } catch (MalformedURLException e) {
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) addr.openConnection();
            } catch (IOException e) {
            }
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setDoInput(true);
            set_cookie(conn);

            BufferedReader reader;
            try {
                InputStream inputStream = conn.getInputStream();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("csrfmiddlewaretoken")) {
                        int from = line.indexOf("csrfmiddlewaretoken");
                        csrfmiddlewaretoken = line.substring(from + 28, from + 28 + 32);
                    }
                }

                Log.v("csrfmiddlewaretoken", csrfmiddlewaretoken);
                inputStream.close();
                reader.close();
            } catch (IOException e) {
            }

            int status;
            try {
                status = conn.getResponseCode();
                Log.v("responseCode", String.valueOf(status));
                get_cookie(conn);
            } catch (IOException e2) {
            }

            try {
                conn = (HttpURLConnection) addr.openConnection();
            } catch (IOException e) {
            }
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("X-CSRFToken", csrfmiddlewaretoken);
            try {
                conn.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setDoOutput(true);
//            conn.setInstanceFollowRedirects(true);
            set_cookie(conn);

            try {
                conn.setRequestMethod("POST");
                conn.connect();
            } catch (Exception e) {
                Log.v("exception", e.getMessage());
            }

            String data = "username=username&password=password";

            //POST data:
            DataOutputStream dataOS = null;
            try {
                dataOS = new DataOutputStream(conn.getOutputStream());
            } catch (IOException e2) {
                Log.v("exception", "no data output stream");
            }
            try {
                ((DataOutputStream) dataOS).writeBytes(data);
                dataOS.close();
            } catch (IOException e) {
            }

            try {
                status = conn.getResponseCode();
                Log.v("responseCode", String.valueOf(status) + conn.getResponseMessage());
                get_cookie(conn);
            } catch (IOException e2) {
            }

            return null;
        }

        public void get_cookie(HttpURLConnection conn) {
            SharedPreferences sh_pref_cookie = mContext.getSharedPreferences("cookies", Context.MODE_PRIVATE);
            String cook_new;
            String COOKIES_HEADER;
            if (conn.getHeaderField("Set-Cookie") != null) {
                COOKIES_HEADER = "Set-Cookie";
            } else {
                COOKIES_HEADER = "Cookie";
            }
            cook_new = conn.getHeaderField(COOKIES_HEADER);
            Log.v("getting cookies", cook_new);
            SharedPreferences.Editor editor = sh_pref_cookie.edit();
            editor.putString("Cookie", cook_new);
            editor.commit();
            Log.v("got cookies", cook_new);
        }

        public void set_cookie(HttpURLConnection conn) {
            SharedPreferences sh_pref_cookie = mContext.getSharedPreferences("cookies", Context.MODE_PRIVATE);
            String COOKIES_HEADER = "Cookie";
            String cook = sh_pref_cookie.getString(COOKIES_HEADER, "no_cookie");
            if (!cook.equals("no_cookie")) {
                conn.setRequestProperty(COOKIES_HEADER, cook);
                Log.v("set cookies", cook);
            }
        }
    }
}
