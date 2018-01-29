package com.example.stalker.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView rootView = (ListView)inflater.inflate(R.layout.fragment_news, container, false);

        String[] news = {"First news", "Second news"};

        rootView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.news_item, news));
        return rootView;
    }
}
