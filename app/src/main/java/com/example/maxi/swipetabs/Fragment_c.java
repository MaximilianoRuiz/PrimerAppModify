package com.example.maxi.swipetabs;

import android.app.Activity;
import android.net.Uri;
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
import java.util.List;


public class Fragment_c extends Fragment {

    public static final String MONTH = "MONTH";
    ListView listView;
    Button add;
    DataBaseHandler dataBaseHandler;

    List<String> morningList;
    List<String> nightList;
    List<String> dateList;

    public Fragment_c() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment_c, container, false);

        listView = (ListView) view.findViewById(R.id.listMonth);
        add = (Button) view.findViewById(R.id.btnMonthAdd);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AddMonthDialog.currentDate().equals(lastDateAdd())) {
                    addMonthDialog(view);
                } else {
                    Toast.makeText(getActivity(), "Sorry, you can only add one record per month", Toast.LENGTH_LONG).show();
                }
            }
        });

        dataBaseHandler = new DataBaseHandler(getActivity().getBaseContext());
        dataBaseHandler.open();

        refresh();

        return view;
    }

    protected void refresh() {
        List<WeightVO> weightVOs = dataBaseHandler.returnWeightVO(MONTH);
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
                Toast.makeText(getActivity(), "Position: " + position + "    ID: " + id + "     Date: " + date, Toast.LENGTH_LONG).show();

                modifyMonthDialog(view, morning, night, date);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtDate = (TextView) view.findViewById(R.id.txtDayDate);
                String date = txtDate.getText().toString();

                Toast.makeText(getActivity(), "Delete press     "+date, Toast.LENGTH_LONG).show();
                deleteMonthDialog(view, date);
                return false;
            }
        });
    }

    public void addMonthDialog(View v){
        AddMonthDialog myDialog = new AddMonthDialog(this);
        myDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private void modifyMonthDialog(View v, String morning, String night, String date){
        ModifyMonthDialog modifyMonthDialog = new ModifyMonthDialog(this, morning, night, date);
        modifyMonthDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private void deleteMonthDialog(View v, String date){
        DeleteMonthDialog deleteMonthDialog = new DeleteMonthDialog(this, date);
        deleteMonthDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private String lastDateAdd(){
        return dateList.size()!=0 ? dateList.get(0) : "";
    }
}
