package com.example.maxi.swipetabs;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Fragment_a extends Fragment {

    ListView listView;
    Button add;
    DataBaseHandler dataBaseHandler;

    List<String> morningList;
    List<String> nightList;
    List<String> dateList;

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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AddDayDialog.currentDate().equals(lastDateAdd())){
                    addDayDialog(view);
                }else{
                    Toast.makeText(getActivity(),"Sorry, you can only add one record per day",Toast.LENGTH_LONG).show();
                }
            }
        });

        dataBaseHandler = new DataBaseHandler(getActivity().getBaseContext());
        dataBaseHandler.open();

        refresh();

        return view;
    }

    protected void refresh() {
        List<WeightVO> weightVOs = dataBaseHandler.returnWeightVO();
        morningList = new ArrayList<>();
        nightList = new ArrayList<>();
        dateList = new ArrayList<>();

        for (WeightVO weightVO : weightVOs){
            morningList.add(weightVO.getMorning_weight());
            nightList.add(weightVO.getNight_weight());
            dateList.add(weightVO.getDate());
        }

        MyViewDayListAdapter adapter = new MyViewDayListAdapter(getActivity(), morningList, nightList, dateList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtDate = (TextView) view.findViewById(R.id.txtDayDate);
                TextView txtMorning = (TextView) view.findViewById(R.id.txtDayMorning);
                TextView txtNight = (TextView) view.findViewById(R.id.txtDayNight);

                String date = txtDate.getText().toString();
                String morning = txtMorning.getText().toString().replace(" Kg", "");
                String night = txtNight.getText().toString().replace(" Kg", "");
                Toast.makeText(getActivity(),"Position: "+position+"    ID: "+id+"     Date: "+date, Toast.LENGTH_LONG).show();

                modifyDayDialog(view, morning, night, date);
            }
        });
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
            paramName = C.getString(2);
        }
        Toast.makeText(getActivity().getBaseContext(), "Deleted: "+paramName, Toast.LENGTH_LONG).show();
        dataBaseHandler.deleteData(paramName);

        refresh();
        dataBaseHandler.close();
    }

    private void modifyDayDialog(View v, String morning, String night, String date){
        ModifyDayDialog modifyDayDialog = new ModifyDayDialog(this, morning, night, date);
        modifyDayDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private String lastDateAdd(){
        return dateList.get(dateList.size()-1);
    }
}
