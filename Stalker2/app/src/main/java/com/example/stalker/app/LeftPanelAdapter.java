package com.example.stalker.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nimloth on 25.02.17.
 */
public class LeftPanelAdapter extends ArrayAdapter<LeftPanelItem> {
    private ArrayList<LeftPanelItem> items;
    private Context context;

    public LeftPanelAdapter(Context context, int textViewResourceId, ArrayList<LeftPanelItem> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if (rootView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = vi.inflate(R.layout.left_panel_item, null);
        }
        LeftPanelItem leftPanelItem = items.get(position);
        if (leftPanelItem != null) {
            TextView leftPanelItemText = (TextView) rootView.findViewById(R.id.left_panel_item_text);
            ImageView leftPanelItemImage = (ImageView) rootView.findViewById(R.id.left_panel_item_image);
            if (leftPanelItemText != null) {
                leftPanelItemText.setText(leftPanelItem.leftPanelItemText);
            }
            if (leftPanelItemImage != null) {
                leftPanelItemImage.setImageResource(leftPanelItem.leftPanelItemImage);
            }
        }
        return rootView;
    }
}
