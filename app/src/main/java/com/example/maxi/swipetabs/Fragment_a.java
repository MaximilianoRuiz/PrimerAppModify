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


public class Fragment_a extends Fragment {

    public static final String DAY = "DAY";
    ListView listView;
    Button add, chart;
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
        chart = (Button) view.findViewById(R.id.btnDayChart);

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

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateList.size()>3)
                {
                    showChart(view);
                }else {
                    Toast.makeText(getActivity(), "Sorry, but you need at least 4 records", Toast.LENGTH_LONG).show();
                }
            }
        });

        dataBaseHandler = new DataBaseHandler(getActivity().getBaseContext());
        dataBaseHandler.open();

        refresh();

        return view;
    }

    protected void refresh() {
        List<WeightVO> weightVOs = dataBaseHandler.returnWeightVO(DAY);
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

                modifyDayDialog(view, morning, night, date);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtDate = (TextView) view.findViewById(R.id.txtDayDate);
                String date = txtDate.getText().toString();

                Toast.makeText(getActivity(), "Delete press     "+date, Toast.LENGTH_LONG).show();
                deleteDayDialog(view, date);
                return false;
            }
        });
    }

    public void addDayDialog(View v){
        AddDayDialog myDialog = new AddDayDialog(this);
        myDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private void modifyDayDialog(View v, String morning, String night, String date){
        ModifyDayDialog modifyDayDialog = new ModifyDayDialog(this, morning, night, date);
        modifyDayDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private void deleteDayDialog(View v, String date){
        DeleteDayDialog deleteDayDialog = new DeleteDayDialog(this, date);
        deleteDayDialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private String lastDateAdd(){
        return dateList.size()!=0 ? dateList.get(0) : "";
    }

    public void showChart(View v){
        Intent intent = new Intent(getActivity(), ChartActivity.class);
        intent.putExtra("dateList", (java.io.Serializable) this.dateList);
        intent.putExtra("morningList", (java.io.Serializable) this.morningList);
        intent.putExtra("nightList", (java.io.Serializable) this.nightList);
        intent.putExtra("tab", (java.io.Serializable) this.DAY);

        startActivity(intent);
    }
}
