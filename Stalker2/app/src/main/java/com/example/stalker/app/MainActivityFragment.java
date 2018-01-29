package com.example.stalker.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
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

        String clientId = "657c66cc12f44dbf85552acedb37e19b";
        String callbackUri = "https://vk.com/olga.kalitova";
        String clientSecret = "3cd79c1658d443f4a3acb5979b2fa448";
        InstagramApp instagramApp = new InstagramApp(getActivity(),
                clientId,
                clientSecret,
                callbackUri);
        instagramApp.authorize();

        Log.v("auth", "ok");

        return rootView;
    }
}
