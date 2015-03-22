package com.example.maxi.swipetabs;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

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
    public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};
    private LineChartData previewData;
    private  List<String> dateList;
    private  List<Float> morningList;
    private  List<Float> nightList;
    private String tab;
    private View mLine, nLine;

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

        mLine.setBackgroundColor(ChartUtils.COLOR_BLUE);
        nLine.setBackgroundColor(ChartUtils.COLOR_GREEN);



        chart = (LineChartView) findViewById(R.id.chart);
            previewChart = (PreviewLineChartView) findViewById(R.id.chart_preview);

            // Generate data for previewed chart and copy of that data for preview chart.
            generateDefaultDAta2();

            resetViewport();

            chart.setLineChartData(data);
            // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
            // zoom/scroll is unnecessary.
            chart.setZoomEnabled(false);
            chart.setScrollEnabled(false);

            previewChart.setLineChartData(previewData);
            previewChart.setViewportChangeListener(new ViewportListener());

            previewX(false);

        }

        private void generateDefaultData() {
            int numValues = 50;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {
                values.add(new PointValue(i, (float) Math.random() * 50f+30));
                axisValues.add(new AxisValue(i).setLabel(days[((int) (Math.random() * 5f))]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN);
            line.setHasPoints(false);// too many values so don't draw points.

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            data = new LineChartData(lines);
            //data.setAxisXBottom(new Axis());

            data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            data.setAxisYLeft(new Axis().setHasLines(true));

            // prepare preview data, is better to use separate deep copy for preview chart.
            // Set color to grey to make preview area more visible.
            previewData = new LineChartData(data);
            previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);

        }

        private void resetViewport() {
            // Reset viewport height range to (0,100)
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = 30;
            v.left = 0;
            v.right = 10;
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        //
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

            //values3.add(new PointValue(0,morningList.get(0)));
            //values3.add(new PointValue(morningList.size(),morningList.get(0)));


            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_BLUE);
            line.setHasPoints(false);// too many values so don't draw points.

            Line line2 = new Line(values2);
            line2.setColor(ChartUtils.COLOR_GREEN);
            line2.setHasPoints(false);// too many values so don't draw points.

            //Line line3 = new Line(values3);
            //line3.setColor(Color.TRANSPARENT);
            //line3.setHasLabels(false);


            lines.add(line);
            lines.add(line2);
            //lines.add(line3);

            data = new LineChartData(lines);
            //data.setAxisXBottom(new Axis());

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
}

