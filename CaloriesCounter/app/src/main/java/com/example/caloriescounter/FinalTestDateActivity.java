package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.models.AddUserDailyWeightViewModel;
import com.example.caloriescounter.models.ClaimsXAxisValueFormatter;
import com.example.caloriescounter.models.ClaimsYAxisValueFormatter;
import com.example.caloriescounter.models.LineChartXAxisValueFormatter;
import com.example.caloriescounter.models.MyMarkerView;
import com.example.caloriescounter.models.MyXAxisValueFormatter;
import com.example.caloriescounter.models.UserDailyWeight;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalTestDateActivity extends BaseActivity {

    private LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    private LineChart mChart;
   // private LineChart volumeReportChart;
    Calendar calendar = Calendar.getInstance();
    private List<UserDailyWeight> dailyWeights;
    int userDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_final_test_date);
        this.getSupportActionBar().setTitle("Статистика ваги");

        mChart = findViewById(R.id.chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
//
//        volumeReportChart = findViewById(R.id.reportingChart);
//        volumeReportChart.setTouchEnabled(true);
//        volumeReportChart.setPinchZoom(true);

        chart = findViewById(R.id.reportingChart);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);

        MyMarkerView mv1 = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        mv1.setChartView(chart);
        chart.setMarker(mv1);


        CommonUtils.showLoading(this);


        NetworkService.getInstance()
                .getJSONApi()
                .getDailyWeight()
                .enqueue(new Callback<List<UserDailyWeight>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<UserDailyWeight>> call, @NonNull Response<List<UserDailyWeight>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            dailyWeights = response.body();
                            userDays = dailyWeights.size();
                            //   initGraph(graphWeight);


                            setData(userDays, 5);

                            Legend l = chart.getLegend();
                            l.setEnabled(false);

                            XAxis xAxis = chart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                            //   xAxis.setTypeface(tfLight);
                            xAxis.setTextSize(10f);
                            xAxis.setTextColor(Color.WHITE);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setDrawGridLines(true);
                            xAxis.setTextColor(Color.rgb(255, 192, 56));
                            xAxis.setCenterAxisLabels(true);
                            xAxis.setGranularity(1f); // one hour
                            xAxis.setValueFormatter( new LineChartXAxisValueFormatter());
//        xAxis.setValueFormatter(new ValueFormatter() {
//
//            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
//
//            public String getFormattedValue(float value, AxisBase axis) {
//
//                long millis = TimeUnit.HOURS.toMillis((long) value);
//                return mFormat.format(new Date(millis));
//            }
//        });

                            YAxis leftAxis = chart.getAxisLeft();
                            leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                            //  leftAxis.setTypeface(tfLight);
                            leftAxis.setTextColor(ColorTemplate.getHoloBlue());
                            leftAxis.setDrawGridLines(true);
                            leftAxis.setGranularityEnabled(true);
                            leftAxis.setAxisMinimum(40f);
                            leftAxis.setAxisMaximum(60f);
                            leftAxis.setYOffset(-9f);
                            leftAxis.setTextColor(Color.rgb(255, 192, 56));

                            YAxis rightAxis = chart.getAxisRight();
                            rightAxis.setEnabled(false);

                        } else {
                            String errorMessage;
                            try {
                                assert response.errorBody() != null;
                                errorMessage = response.errorBody().string();
                            } catch (IOException e) {
                                errorMessage = response.message();
                                e.printStackTrace();
                            }
                            dailyWeights = null;
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    errorMessage, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<UserDailyWeight>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Помилка у запиті";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });












        MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        renderData1();






//        Legend l = chart.getLegend();
//        l.setEnabled(false);
//
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
//     //   xAxis.setTypeface(tfLight);
//        xAxis.setTextSize(10f);
//        xAxis.setTextColor(Color.WHITE);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setDrawGridLines(true);
//        xAxis.setTextColor(Color.rgb(255, 192, 56));
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setGranularity(1f); // one hour
//        xAxis.setValueFormatter( new LineChartXAxisValueFormatter());
////        xAxis.setValueFormatter(new ValueFormatter() {
////
////            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
////
////            public String getFormattedValue(float value, AxisBase axis) {
////
////                long millis = TimeUnit.HOURS.toMillis((long) value);
////                return mFormat.format(new Date(millis));
////            }
////        });
//
//        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//      //  leftAxis.setTypeface(tfLight);
//        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
//        leftAxis.setDrawGridLines(true);
//        leftAxis.setGranularityEnabled(true);
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setAxisMaximum(170f);
//        leftAxis.setYOffset(-9f);
//        leftAxis.setTextColor(Color.rgb(255, 192, 56));
//
//        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setEnabled(false);











//
//
//        LimitLine ll1 = new LimitLine(30f,"Title");
//        ll1.setLineColor(getResources().getColor(R.color.black));
//        ll1.setLineWidth(4f);
//        ll1.enableDashedLine(10f, 10f, 0f);
//        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        ll1.setTextSize(10f);
//
//        LimitLine ll2 = new LimitLine(35f, "");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//
//        XAxis xAxis = volumeReportChart.getXAxis();
//        YAxis leftAxis = volumeReportChart.getAxisLeft();
//        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
//        xAxis.setPosition(position);
//        volumeReportChart.getDescription().setEnabled(true);
//        Description description = new Description();
//
//        description.setText("Week");
//        description.setTextSize(15f);
//
//        xAxis.setValueFormatter(new ClaimsXAxisValueFormatter(dates));
//        leftAxis.setValueFormatter(new ClaimsYAxisValueFormatter());






//            ArrayList<BarEntry> entries = new ArrayList<>();
//            entries.add(new BarEntry(4f, 0));
//            entries.add(new BarEntry(8f, 1));
//            entries.add(new BarEntry(6f, 2));
//            entries.add(new BarEntry(12f, 3));
//            entries.add(new BarEntry(18f, 4));
//            entries.add(new BarEntry(9f, 5));
//
//            BarDataSet dataset = new BarDataSet(entries, "# of Calls");
//
//            ArrayList<String> labels = new ArrayList<String>();
//            labels.add("January");
//            labels.add("February");
//            labels.add("March");
//            labels.add("April");
//            labels.add("May");
//            labels.add("June");
//
//            BarChart chart = new BarChart(this);
//            setContentView(chart);
//
//            BarData data = new BarData(labels, dataset);
//            chart.setData(data);
//
//            chart.setDescription("# of times Alice called Bob");

    }

    private void setData(int count, float range) {
///////////////////
        // now in hours
        //long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
     //   long now = System.currentTimeMillis();
//      long now = calendar.getTimeInMillis();
//
//        Log.e("now1" , Long.toString(now));
//        ArrayList<Entry> values = new ArrayList<>();
//
//        // count = hours
//        float to = now + count;
//
//        // increment by 1 hour
//      //  for (float x = now; x < to; x++) {
//        for (int i = 0; i < count; i++) {
//
//
//            float x = now;
//            Log.e("now" , Float.toString(x));
//            calendar.add(Calendar.DATE, 1);
//          now = calendar.getTimeInMillis();
//// Obtain a number between [0 - 49].
//          //  int n = rand.nextInt(50);
//          //  float y = getRandom(range, 50);
//            float y = i*10.5f;
//            values.add(new Entry(x, y)); // add one entry per hour
//            calendar.add(Calendar.DATE, 1);
//        }

//////////////
        getStatistic();

//calendar.setTime(dailyWeights.get(0).getDateOfWeight());
      //  long now = calendar.getTimeInMillis();
        long now;

       // Log.e("now1" , Long.toString(now));
        //Log.e("count" , Integer.toString(count));
        ArrayList<Entry> values = new ArrayList<>();


        // count = hours
        //float to = now + count;

        // increment by 1 hour
        //  for (float x = now; x < to; x++) {
        for (int i = 0; i < count; i++) {
            calendar.setTime(dailyWeights.get(i).getDateOfWeight());
            now = calendar.getTimeInMillis();
            float x = now;
            Log.e("now" , Float.toString(x));
           // calendar.add(Calendar.DATE, 1);
            //now = calendar.getTimeInMillis();
// Obtain a number between [0 - 49].
            //  int n = rand.nextInt(50);
            //  float y = getRandom(range, 50);
         //   float y = i*10.5f;
            float y =  (float) dailyWeights.get(i).getWeight();
            values.add(new Entry(x, y)); // add one entry per hour
          //  calendar.add(Calendar.DATE, 1);
        }






        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // create a data object with the data sets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
    }


    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }


//    public void renderData(List<String> dates, List<Double> allAmounts) {
//
//        final ArrayList<String> xAxisLabel = new ArrayList<>();
//        xAxisLabel.add("1");
//        xAxisLabel.add("7");
//        xAxisLabel.add("14");
//        xAxisLabel.add("21");
//        xAxisLabel.add("28");
//        xAxisLabel.add("30");
//
//        XAxis xAxis = volumeReportChart.getXAxis();
//        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
//        xAxis.setPosition(position);
//        xAxis.enableGridDashedLine(2f, 7f, 0f);
//        xAxis.setAxisMaximum(5f);
//        xAxis.setAxisMinimum(0f);
//        xAxis.setLabelCount(6, true);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setGranularity(7f);
//        xAxis.setLabelRotationAngle(315f);
//
//        xAxis.setValueFormatter(new ClaimsXAxisValueFormatter(dates));
//
//        xAxis.setCenterAxisLabels(true);
//
//
//        xAxis.setDrawLimitLinesBehindData(true);
//
//
//
////        LimitLine ll1 = new LimitLine(Float.parseFloat(UISetters.getDateInNumber()), UISetters.getDateInNumber());
////        ll1.setLineColor(getResources().getColor(R.color.BlueViolet));
////        ll1.setLineWidth(4f);
////        ll1.enableDashedLine(10f, 10f, 0f);
////        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
////        ll1.setTextSize(10f);
////
////        LimitLine ll2 = new LimitLine(35f, "");
////        ll2.setLineWidth(4f);
////        ll2.enableDashedLine(10f, 10f, 0f);
////        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
////        ll2.setTextSize(10f);
////        ll2.setLineColor(Color.parseColor("#FFFFFF"));
//
//        xAxis.removeAllLimitLines();
//      //  xAxis.addLimitLine(ll1);
//      //  xAxis.addLimitLine(ll2);
//
//
//        YAxis leftAxis = volumeReportChart.getAxisLeft();
//        leftAxis.removeAllLimitLines();
//        //leftAxis.addLimitLine(ll1);
//        //leftAxis.addLimitLine(ll2);
//
//      //  leftAxis.setAxisMaximum(findMaximumValueInList(allAmounts).floatValue() + 100f);
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
//        leftAxis.setDrawZeroLine(false);
//        leftAxis.setDrawLimitLinesBehindData(false);
//        //XAxis xAxis = mBarChart.getXAxis();
//        leftAxis.setValueFormatter(new ClaimsYAxisValueFormatter());
//
//        volumeReportChart.getDescription().setEnabled(true);
//        Description description = new Description();
//        // description.setText(UISetters.getFullMonthName());//commented for weekly reporting
//        description.setText("Week");
//        description.setTextSize(15f);
//        volumeReportChart.getDescription().setPosition(0f, 0f);
//        volumeReportChart.setDescription(description);
//        volumeReportChart.getAxisRight().setEnabled(false);
//
//        //setData()-- allAmounts is data to display;
//        setDataForWeeksWise(allAmounts);
//
//    }

//    private void setDataForWeeksWise(List<Double> amounts) {
//
//        ArrayList<Entry> values = new ArrayList<>();
//        values.add(new Entry(1, amounts.get(0).floatValue()));
//        values.add(new Entry(2, amounts.get(1).floatValue()));
//        values.add(new Entry(3, amounts.get(2).floatValue()));
//        values.add(new Entry(4, amounts.get(3).floatValue()));
//
//
//        LineDataSet set1;
//        if (volumeReportChart.getData() != null &&
//                volumeReportChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) volumeReportChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            volumeReportChart.getData().notifyDataChanged();
//            volumeReportChart.notifyDataSetChanged();
//        } else {
//            set1 = new LineDataSet(values, "Total volume");
//            set1.setDrawCircles(true);
//            set1.enableDashedLine(10f, 0f, 0f);
//            set1.enableDashedHighlightLine(10f, 0f, 0f);
//            set1.setColor(getResources().getColor(R.color.Indigo));
//            set1.setCircleColor(getResources().getColor(R.color.Indigo));
//            set1.setLineWidth(2f);//line size
//            set1.setCircleRadius(5f);
//            set1.setDrawCircleHole(true);
//            set1.setValueTextSize(10f);
//            set1.setDrawFilled(true);
//            set1.setFormLineWidth(5f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(5.f);
//
//            if (Utils.getSDKInt() >= 18) {
////                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.blue_bg);
////                set1.setFillDrawable(drawable);
//                set1.setFillColor(Color.WHITE);
//
//            } else {
//                set1.setFillColor(Color.WHITE);
//            }
//            set1.setDrawValues(true);
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1);
//            LineData data = new LineData(dataSets);
//
//            volumeReportChart.setData(data);
//        }
//    }









    public void renderData1() {
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMaximum(10f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLimitLinesBehindData(true);

        LimitLine ll1 = new LimitLine(215f, "Maximum Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(70f, "Minimum Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(350f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);
        setData();
    }

    private void setData() {

//        ArrayList<Entry> values = new ArrayList<>();
//        values.add(new Entry(1, 50));
//        values.add(new Entry(2, 100));
//        values.add(new Entry(3, 80));
//        values.add(new Entry(4, 120));
//        values.add(new Entry(5, 110));
//        values.add(new Entry(7, 150));
//        values.add(new Entry(8, 250));
//        values.add(new Entry(9, 190));

        Date date1 = calendar.getTime();
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(1, 50));
        values.add(new Entry(2, 100));
        values.add(new Entry(3, 80));
        values.add(new Entry(4, 120));
        values.add(new Entry(5, 110));
        values.add(new Entry(7, 150));
        values.add(new Entry(8, 250));
        values.add(new Entry(9, 190));

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Sample Data");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
    }

    public void getStatistic() {
        CommonUtils.showLoading(this);


        NetworkService.getInstance()
                .getJSONApi()
                .getDailyWeight()
                .enqueue(new Callback<List<UserDailyWeight>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<UserDailyWeight>> call, @NonNull Response<List<UserDailyWeight>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            dailyWeights = response.body();
                            userDays = dailyWeights.size();
                         //   initGraph(graphWeight);

                        } else {
                            String errorMessage;
                            try {
                                assert response.errorBody() != null;
                                errorMessage = response.errorBody().string();
                            } catch (IOException e) {
                                errorMessage = response.message();
                                e.printStackTrace();
                            }
                            dailyWeights = null;
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    errorMessage, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<UserDailyWeight>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Помилка у запиті";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }


//
//    private int mNumLabels = 3;
//    private int mNumLab;
//    private Date minDate, maxDate;
//    SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM",  new Locale("uk","UA"));
//    private AddUserDailyWeightViewModel weightUser;
//    private List<UserDailyWeight> dailyWeights;
//    private ArrayList<Date> datesRetr = new ArrayList<Date>();
//    private  GraphView graph;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        super.addContentView(R.layout.activity_final_test_date);
//        this.getSupportActionBar().setTitle("Статистика ваги");
//        graph = (GraphView) findViewById(R.id.graph);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        CommonUtils.showLoading(this);
//
//
//        NetworkService.getInstance()
//                .getJSONApi()
//                .getDailyWeight()
//                .enqueue(new Callback<List<UserDailyWeight>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<UserDailyWeight>> call, @NonNull Response<List<UserDailyWeight>> response) {
//                        CommonUtils.hideLoading();
//                        if (response.errorBody() == null && response.isSuccessful()) {
//                            dailyWeights = response.body();
//                            mNumLab = dailyWeights.size();
//
//                            for(UserDailyWeight day : dailyWeights) {
//                                datesRetr.add(day.getDateOfWeight());
//                            }
//
//                            // initGraph(graphWeight);
//
//                            graph.addSeries(new LineGraphSeries(generateDataWeight()));
//
//                            // set date label formatter
//                            //   graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
//
//                      //      graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
//                            //  майже працюэ
//                            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
//                                @Override
//                                public String formatLabel(double value, boolean isValueX) {
//                                    if (isValueX) {
//                                        Log.e("Value", Double.toString(value));
//                                        // transform number to time
//                                        return formatDate.format(new Date((long) value));
//                                    } else {
//                                        // return null;
//                                        Log.e("Value in else", Double.toString(value));
//                                        return super.formatLabel(value, isValueX);
//                                    }
//                                }
//                            });
//
//
////        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
////          staticLabelsFormatter.setHorizontalLabels(new String[] {"21/02",  "26/02"});
//                            // staticLabelsFormatter.setHorizontalLabels(hLabels);
//                            graph.getGridLabelRenderer().setNumHorizontalLabels(5);
//                            Log.e("mindate:", Long.toString(maxDate.getTime()));
//                            // set manual x bounds to have nice steps
//                            graph.getViewport().setMinX(minDate.getTime());
//                            graph.getViewport().setMaxX(maxDate.getTime());
//                            graph.getViewport().setXAxisBoundsManual(true);
////                            graph.getViewport().setScalable(true);
////                            graph.getViewport().setScrollable(true);
//
//                            // as we use dates as labels, the human rounding to nice readable numbers
//                            // is not nessecary
//                          //  graph.getGridLabelRenderer().setHumanRounding(false);
//
//
//
//
//
//                        } else {
//                            String errorMessage;
//                            try {
//                                assert response.errorBody() != null;
//                                errorMessage = response.errorBody().string();
//                            } catch (IOException e) {
//                                errorMessage = response.message();
//                                e.printStackTrace();
//                            }
//                            dailyWeights = null;
//                            Toast toast = Toast.makeText(getApplicationContext(),
//                                    errorMessage, Toast.LENGTH_LONG);
//                            toast.show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<UserDailyWeight>> call, @NonNull Throwable t) {
//                        CommonUtils.hideLoading();
//                        String error = "Помилка у запиті";
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                error, Toast.LENGTH_LONG);
//                        toast.show();
//                        t.printStackTrace();
//                    }
//                });
//
//
//
//
//
//
//
//
//
//
//       // initGraph(graph);
//    }
//
//    public void initGraph(GraphView graph) {
//
//
//        // generate Dates
////        Calendar calendar = Calendar.getInstance();
////        Date d1 = calendar.getTime();
////              Log.e("d1:", d1.toString());
////
////        calendar.add(Calendar.DATE, 1);
////        calendar.set(Calendar.HOUR_OF_DAY, 0);
////        calendar.set(Calendar.MINUTE, 0);
////        calendar.set(Calendar.SECOND, 0);
////        Date d2 = calendar.getTime();
////        calendar.add(Calendar.DATE, 1);
////        Date d3 = calendar.getTime();
////        calendar.add(Calendar.DATE, 1);
////        Date d4 = calendar.getTime();
////       calendar.add(Calendar.DATE, 1);
////      Date d5 = calendar.getTime();
////        calendar.add(Calendar.DATE, 1);
////        Date d6 = calendar.getTime();
////        Log.e("d6:", d6.toString());
//
//        // you can directly pass Date objects to DataPoint-Constructor
//        // this will convert the Date to double via Date#getTime()
////        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
////                new DataPoint(d1, 1),
////                new DataPoint(d2, 5),
////                new DataPoint(d3, 3),
////                new DataPoint(d4, 2),
////               new DataPoint(d5, 5),
////                new DataPoint(d6, 1)
////        });
////        graph.addSeries(series);
//
////        graph.addSeries(new LineGraphSeries(generateDataWeight()));
////
////        // set date label formatter
////        //   graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
////
////
////        ////  майже працюэ
////        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
////            @Override
////            public String formatLabel(double value, boolean isValueX) {
////                if (isValueX) {
////                    Log.e("Value", Double.toString(value));
////                    // transform number to time
////                    return formatDate.format(new Date((long) value));
////                } else {
////                    // return null;
////                    Log.e("Value in else", Double.toString(value));
////                    return super.formatLabel(value, isValueX);
////                }
////            }
////        });
////
////
//////        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//////          staticLabelsFormatter.setHorizontalLabels(new String[] {"21/02",  "26/02"});
////        // staticLabelsFormatter.setHorizontalLabels(hLabels);
////        graph.getGridLabelRenderer().setNumHorizontalLabels(mNumLabels);
////        Log.e("mindate:", Long.toString(maxDate.getTime()));
////        // set manual x bounds to have nice steps
////        graph.getViewport().setMinX(minDate.getTime());
////        graph.getViewport().setMaxX(maxDate.getTime());
////        graph.getViewport().setXAxisBoundsManual(true);
////        graph.getViewport().setScalable(true);
////        graph.getViewport().setScrollable(true);
////
////        // as we use dates as labels, the human rounding to nice readable numbers
////        // is not nessecary
////        graph.getGridLabelRenderer().setHumanRounding(false);
//    }
//
//
//    private DataPoint[] generateDataWeight() {
//
//        int count = 6;
//        // hLabels = new String[dailyWeights.size()];
//        // new DataPoint(dd1, 1),
//        DataPoint[] values = new DataPoint[count];
//        //  int i = 0;
//        Calendar calendar = Calendar.getInstance();
//        Date first = datesRetr.get(0);
//calendar.setTime(first);
//        for(int i=0; i<6; i++) {
//
//
//            Date d1 = calendar.getTime();
//            if (i == 0) {
//                minDate = d1;
//
//            }
//            if (i==5) {
//                maxDate = d1;
//            }
//            Log.e("d1:", d1.toString());
//            Log.e("dateinml:", Long.toString(calendar.getTimeInMillis()));
//            calendar.add(Calendar.DATE, 1);
//
//            int y = i+2;
//            DataPoint data = new DataPoint(d1, y);
//            values[i] = data;
//
//
//        }
//
//
//
//        return values;
//    }
//
//    public void getStatistic() {
//        CommonUtils.showLoading(this);
//
//
//        NetworkService.getInstance()
//                .getJSONApi()
//                .getDailyWeight()
//                .enqueue(new Callback<List<UserDailyWeight>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<UserDailyWeight>> call, @NonNull Response<List<UserDailyWeight>> response) {
//                        CommonUtils.hideLoading();
//                        if (response.errorBody() == null && response.isSuccessful()) {
//                            dailyWeights = response.body();
//                            mNumLab = dailyWeights.size();
//
//                            for(UserDailyWeight day : dailyWeights) {
//                                datesRetr.add(day.getDateOfWeight());
//                            }
//
//                           // initGraph(graphWeight);
//
//                            graph.addSeries(new LineGraphSeries(generateDataWeight()));
//
//                            // set date label formatter
//                            //   graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
//
//                            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
//                            ////  майже працюэ
////                            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
////                                @Override
////                                public String formatLabel(double value, boolean isValueX) {
////                                    if (isValueX) {
////                                        Log.e("Value", Double.toString(value));
////                                        // transform number to time
////                                        return formatDate.format(new Date((long) value));
////                                    } else {
////                                        // return null;
////                                        Log.e("Value in else", Double.toString(value));
////                                        return super.formatLabel(value, isValueX);
////                                    }
////                                }
////                            });
//
//
////        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
////          staticLabelsFormatter.setHorizontalLabels(new String[] {"21/02",  "26/02"});
//                            // staticLabelsFormatter.setHorizontalLabels(hLabels);
//                            graph.getGridLabelRenderer().setNumHorizontalLabels(mNumLabels);
//                            Log.e("mindate:", Long.toString(maxDate.getTime()));
//                            // set manual x bounds to have nice steps
//                            graph.getViewport().setMinX(minDate.getTime());
//                            graph.getViewport().setMaxX(maxDate.getTime());
//                            graph.getViewport().setXAxisBoundsManual(true);
////                            graph.getViewport().setScalable(true);
////                            graph.getViewport().setScrollable(true);
//
//                            // as we use dates as labels, the human rounding to nice readable numbers
//                            // is not nessecary
//                            graph.getGridLabelRenderer().setHumanRounding(false);
//
//
//
//
//
//                        } else {
//                            String errorMessage;
//                            try {
//                                assert response.errorBody() != null;
//                                errorMessage = response.errorBody().string();
//                            } catch (IOException e) {
//                                errorMessage = response.message();
//                                e.printStackTrace();
//                            }
//                            dailyWeights = null;
//                            Toast toast = Toast.makeText(getApplicationContext(),
//                                    errorMessage, Toast.LENGTH_LONG);
//                            toast.show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<UserDailyWeight>> call, @NonNull Throwable t) {
//                        CommonUtils.hideLoading();
//                        String error = "Помилка у запиті";
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                error, Toast.LENGTH_LONG);
//                        toast.show();
//                        t.printStackTrace();
//                    }
//                });
//    }
}

