package com.example.maxi.swipetabs;

import android.content.Intent;
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


public class Fragment_b extends Fragment {

    public static final String WEEK = "WEEK";
    ListView listView;
    Button add, chart;
    DataBaseHandler dataBaseHandler;

    List<String> morningList;
    List<String> nightList;
    List<String> dateList;

    public Fragment_b() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment_b, container, false);

        listView = (ListView) view.findViewById(R.id.listWeek);
        add = (Button) view.findViewById(R.id.btnWeekAdd);
        chart = (Button) view.findViewById(R.id.btnWeekChart);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AddWeekDialog.currentDate().equals(lastDateAdd())) {
                    addWeekDialog(view);
                } else {
                    Toast.makeText(getActivity(), "Sorry, you can only add one record per week", Toast.LENGTH_LONG).show();
                }
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateList.size()>2)
                {
                    showChart(view);
                }else {
                    Toast.makeText(getActivity(), "Sorry, but you need at least 3 records", Toast.LENGTH_LONG).show();
                }
            }
        });

        dataBaseHandler = new DataBaseHandler(getActivity().getBaseContext());
        dataBaseHandler.open();

        refresh();

        return view;
    }

    protected void refresh() {
        List<WeightVO> weightVOs = dataBaseHandler.returnWeightVO(WEEK);
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

                modifyWeekDialog(view, morning, night, date);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtDate = (TextView) view.findViewById(R.id.txtDayDate);
                String date = txtDate.getText().toString();

                Toast.makeText(getActivity(), "Delete press     "+date, Toast.LENGTH_LONG).show();
                deleteWeekDialog(view, date);
                return false;
            }
        });
    }

    public void addWeekDialog(View v){
        AddWeekDialog myDialog = new AddWeekDialog(this);
        myDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private void modifyWeekDialog(View v, String morning, String night, String date){
        ModifyWeekDialog modifyWeekDialog = new ModifyWeekDialog(this, morning, night, date);
        modifyWeekDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private void deleteWeekDialog(View v, String date){
        DeleteWeekDialog deleteWeekDialog = new DeleteWeekDialog(this, date);
        deleteWeekDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private String lastDateAdd(){
        return dateList.size()!=0 ? dateList.get(0) : "";
    }

    public void showChart(View v){
        Intent intent = new Intent(getActivity(), ChartActivity.class);
        intent.putExtra("dateList", (java.io.Serializable) this.dateList);
        intent.putExtra("morningList", (java.io.Serializable) this.morningList);
        intent.putExtra("nightList", (java.io.Serializable) this.nightList);
        intent.putExtra("tab", (java.io.Serializable) this.WEEK);

        startActivity(intent);
    }
}
