package com.example.maxi.swipetabs;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Fragment_c extends Fragment {

    ListView listView;

    public Fragment_c() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_c, container, false);

        listView = (ListView) view.findViewById(R.id.listMonth);

        List<String> lista2 = new ArrayList<>();
        lista2.add("60");
        lista2.add("61");
        List<String> lista2_1 = new ArrayList<>();
        lista2_1.add("62");
        lista2_1.add("70");
        List<String> lista2_3 = new ArrayList<>();
        lista2_3.add(" / / ");
        lista2_3.add(" / / ");

        MyViewDayListAdapter adapter2 = new MyViewDayListAdapter(getActivity(), lista2, lista2_1, lista2_3);
        listView.setAdapter(adapter2);

        return view;
    }
}
