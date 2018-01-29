package com.example.stalker.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Log.d("main", "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.settings:
                Log.d("main", "settings option item selected");
                showSettings();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettings() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new SettingsFragment();
        Log.d("main", "setting fragment selected");
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        Log.d("main", "setting fragment activated");
    }
}
