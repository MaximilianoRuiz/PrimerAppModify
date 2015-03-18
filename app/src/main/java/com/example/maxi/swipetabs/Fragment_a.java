package com.example.maxi.swipetabs;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Fragment_a extends Fragment {

    ListView listView;
    Button add, remove;
    DataBaseHandler dataBaseHandler;

    public Fragment_a() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment_a, container, false);

        listView = (ListView) view.findViewById(R.id.listDay);
        add = (Button) view.findViewById(R.id.btnDayAdd);
        remove = (Button) view.findViewById(R.id.btnDayRemove);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDayDialog(view);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(view);
            }
        });

        dataBaseHandler = new DataBaseHandler(getActivity().getBaseContext());
        dataBaseHandler.open();

        refresh();

        return view;
    }

    protected void refresh() {
        List<WeightVO> weightVOs = dataBaseHandler.returnWeightVO();
        List<String> morningList = new ArrayList<>();
        List<String> nightList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();

        for (WeightVO weightVO : weightVOs){
            morningList.add(weightVO.getMorning_weight());
            nightList.add(weightVO.getNight_weight());
            dateList.add(weightVO.getDate());
        }

        MyViewDayListAdapter adapter = new MyViewDayListAdapter(getActivity(), morningList, nightList, dateList);
        listView.setAdapter(adapter);
    }

    public void addDayDialog(View v){
        AddDayDialog myDialog = new AddDayDialog(this);
        myDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    public void deleteData(View v) {
        String paramName = "";
        dataBaseHandler = new DataBaseHandler(getActivity().getBaseContext());
        dataBaseHandler.open();
        Cursor C = dataBaseHandler.returnData();
        if (C.moveToNext()){
            paramName = C.getString(0);
        }
        Toast.makeText(getActivity().getBaseContext(), "Deleted: "+paramName, Toast.LENGTH_LONG).show();
        dataBaseHandler.deleteData(paramName);

        refresh();
        dataBaseHandler.close();
    }

}
