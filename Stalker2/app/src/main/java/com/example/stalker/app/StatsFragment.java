package com.example.stalker.app;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


public class StatsFragment extends Fragment {
    private LinearLayout rootView;

    // Pie Chart Section Names
    String[] names;
    int[] likesAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = (LinearLayout)inflater.inflate(R.layout.fragment_stats, container, false);

        Bundle args = getArguments();
        this.names = args.getStringArray("names");
        this.likesAmount = args.getIntArray("likesAmount");

        drawChart();
        return rootView;
    }

    public void drawChart(){

        // Color of each Pie Chart Sections
        int[] colors = {Color.BLUE, Color.MAGENTA, Color.GREEN};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries("Likes");
        for (int i = 0; i < likesAmount.length; i++) {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(names[i], likesAmount[i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < likesAmount.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);

        }

        defaultRenderer.setZoomButtonsVisible(false);
        defaultRenderer.setShowLegend(false);
        defaultRenderer.setBackgroundColor(Color.WHITE);
        defaultRenderer.setApplyBackgroundColor(true);
        defaultRenderer.setShowLabels(true);
        defaultRenderer.setLabelsTextSize(30f);
        defaultRenderer.setLabelsColor(Color.RED);
        defaultRenderer.setChartTitle("Likes statistics");
        defaultRenderer.setChartTitleTextSize(40f);
        defaultRenderer.setPanEnabled(false);

        // remove any views before u paint the chart
        rootView.removeAllViews();
        // drawing pie chart
        View mChart = ChartFactory.getPieChartView(getActivity(),
                distributionSeries, defaultRenderer);
        // adding the view to the linearlayout
        rootView.addView(mChart);

    }
}