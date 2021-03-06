package com.example.maxi.swipetabs;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

public class ChartActivity extends ActionBarActivity {

    private LineChartView chart;
    private PreviewLineChartView previewChart;
    private LineChartData data;
    private LineChartData previewData;
    private  List<String> dateList;
    private  List<Float> morningList;
    private  List<Float> nightList;
    private String tab;
    private View mLine, nLine;
    private TextView txtMorning, txtNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_fragment);

        Bundle bundle = getIntent().getExtras();
        tab = bundle.getString("tab");
        preparDateList((List<String>) bundle.getSerializable("dateList"));
        prepareMorningList((List<String>) bundle.getSerializable("morningList"));
        prepareNightList((List<String>) bundle.getSerializable("nightList"));

        mLine = (View) findViewById(R.id.morning_line);
        nLine = (View) findViewById(R.id.night_line);
        txtMorning = (TextView) findViewById(R.id.morning_status);
        txtNight = (TextView) findViewById(R.id.night_status);
        txtMorning.setText(setMorningText());
        txtNight.setText(setNightText());

        mLine.setBackgroundColor(ChartUtils.COLOR_BLUE);
        nLine.setBackgroundColor(ChartUtils.COLOR_GREEN);



        chart = (LineChartView) findViewById(R.id.chart);
        previewChart = (PreviewLineChartView) findViewById(R.id.chart_preview);

        generateDefaultDAta2();

        resetViewport();

        chart.setLineChartData(data);
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);

        previewChart.setLineChartData(previewData);
        previewChart.setViewportChangeListener(new ViewportListener());

        previewX(false);
    }

    private void resetViewport() {
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 30;
        v.left = 0;
        v.right = 10;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void generateDefaultDAta2(){
        List<Line> lines = new ArrayList<Line>();

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        List<PointValue> values2 = new ArrayList<PointValue>();
        List<PointValue> values3 = new ArrayList<PointValue>();

        for (int i = 0; i < morningList.size(); ++i) {
            values.add(new PointValue(i, morningList.get(i)));
            axisValues.add(new AxisValue(i).setLabel(dateList.get(i)));
        }

        for (int i = 0; i < nightList.size(); ++i) {
            values2.add(new PointValue(i, nightList.get(i)));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_BLUE);
        line.setHasPoints(false);// too many values so don't draw points.

        Line line2 = new Line(values2);
        line2.setColor(ChartUtils.COLOR_GREEN);
        line2.setHasPoints(false);// too many values so don't draw points.

        lines.add(line);
        lines.add(line2);

        data = new LineChartData(lines);

        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        data.setAxisYLeft(new Axis().setHasLines(true));

        previewData = new LineChartData(data);
        previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
    }

    private void previewX(boolean animate) {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        if(animate) {
            previewChart.setCurrentViewportWithAnimation(tempViewport);
        }else{
            previewChart.setCurrentViewport(tempViewport);
        }
        previewChart.setZoomType(ZoomType.HORIZONTAL);
    }

    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            chart.setCurrentViewport(newViewport);
        }

    }

    public void preparDateList(List<String> dateL){
        List<String> list = new ArrayList<>();
        switch (tab){
            case "DAY":
                for(String str : dateL){
                    String substract = str.substring(str.length() - 5);
                    list.add(str.replace(substract, ""));
                }
                break;
            case "WEEK":
                for(String str : dateL){
                    list.add(str.replace("º week - ", "/"));
                }
                break;
            case "MONTH":
                for(String str : dateL){
                    list.add(str);
                }
                break;
            default:
                list.add("");
        }

        List<String> sortList = new ArrayList<>();
        for(int i = list.size(); i > 0; i--){
            sortList.add(list.get(i-1));
        }
        dateList = sortList;
    }

    public void prepareMorningList(List<String> morningL){
        List<Float> list = new ArrayList<>();
        for (String str : morningL){
            if(!"".equals(str)){
                str = str + "f";
                Float flo = Float.parseFloat(str);
                list.add(flo);
            }
        }

        List<Float> sortList = new ArrayList<>();
        for(int i = list.size(); i > 0; i--){
            sortList.add(list.get(i-1));
        }
        morningList = sortList;
    }

    public void prepareNightList(List<String> nightL){
        List<Float> list = new ArrayList<>();
        for (String str : nightL){
            if(!"".equals(str)){
                str = str + "f";
                Float flo = Float.parseFloat(str);
                list.add(flo);
            }
        }

        List<Float> sortList = new ArrayList<>();
        for(int i = list.size(); i > 0; i--){
            sortList.add(list.get(i-1));
        }
        nightList = sortList;
    }

    private String setMorningText(){
        String text;
        DecimalFormat df = new DecimalFormat("0.0");

        Float dif = morningList.get(0) - morningList.get(morningList.size()-1);
        String sign = dif > 0 ? "less" : "more";
        dif = dif > 0 ? dif * 1 : dif * (-1);
        String startDate = dateList.get(0);
        String endDate = dateList.get(morningList.size()-1);
        text = "Morning : "+df.format(dif)+" kg "+sign+" between "+startDate+" and "+endDate;
        return text;
    }

    private String setNightText(){
        String text;
        DecimalFormat df = new DecimalFormat("0.0");

        Float dif = nightList.get(0) - nightList.get(nightList.size()-1);
        String sign = dif > 0 ? "less" : "more";
        dif = dif > 0 ? dif * 1 : dif * (-1);
        String startDate = dateList.get(0);
        String endDate = dateList.get(nightList.size()-1);
        text = "Night : "+df.format(dif)+" kg "+sign+" between "+startDate+" and "+endDate;
        return text;
    }
}

