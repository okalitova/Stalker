package com.example.stalker.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

class DrawerItemClickListener implements ListView.OnItemClickListener {
    private Context context;
    private ListView drawerList;
    private DrawerLayout drawerLayout;

    DrawerItemClickListener(Context context, ListView drawerList, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerList = drawerList;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        if (position == 0) {
            Fragment fragment = new NewsFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            drawerList.setItemChecked(position, true);
            ((AppCompatActivity) context).setTitle("News");
            drawerLayout.closeDrawer(drawerList);
        } else {
            if (position == 1) {
                String[] names = {"Anna", "Petr", "Andrew"};
                int[] likesAmount = {20, 30, 56};

                Fragment fragment = new StatsFragment();
                Bundle args = new Bundle();
                args.putStringArray("names", names);
                args.putIntArray("likesAmount", likesAmount);
                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();

                // Highlight the selected item, update the title, and close the drawer
                drawerList.setItemChecked(position, true);
                ((AppCompatActivity) context).setTitle("Stats");
                drawerLayout.closeDrawer(drawerList);
            }
        }
    }
}