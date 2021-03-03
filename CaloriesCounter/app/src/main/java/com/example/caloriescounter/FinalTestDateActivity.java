package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.caloriescounter.models.LineChartXAxisValueFormatter;
import com.example.caloriescounter.models.MyMarkerView;
import com.example.caloriescounter.models.UserDailyWeight;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalTestDateActivity extends BaseActivity {

    private LineChart chart;
    Calendar calendar = Calendar.getInstance();
    private List<UserDailyWeight> dailyWeights;
    int userDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_final_test_date);
        this.getSupportActionBar().setTitle("Статистика ваги");

        chart = findViewById(R.id.reportingChart);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.getDescription().setEnabled(false);

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
                            setData(userDays, 5);

                            Legend l = chart.getLegend();
                            l.setEnabled(false);

                            XAxis xAxis = chart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            //   xAxis.setTypeface(tfLight);
                            xAxis.setTextSize(10f);
                            xAxis.enableGridDashedLine(10f, 10f, 0f);
                         //   xAxis.setTextColor(Color.WHITE);
                            xAxis.setDrawAxisLine(true);
                            xAxis.setDrawGridLines(true);
                        //    xAxis.setTextColor(Color.rgb(255, 192, 56));
                            xAxis.setCenterAxisLabels(true);
                            xAxis.setGranularityEnabled(true);
                            xAxis.setGranularity(1f);
                           xAxis.setValueFormatter( new LineChartXAxisValueFormatter());


                            YAxis leftAxis = chart.getAxisLeft();
                            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                            //  leftAxis.setTypeface(tfLight);
                          //  leftAxis.setTextColor(ColorTemplate.getHoloBlue());
                            leftAxis.setDrawGridLines(true);
                            leftAxis.setGranularityEnabled(true);
                            leftAxis.setAxisMinimum(45f);
                            leftAxis.setAxisMaximum(55f);
                            leftAxis.enableGridDashedLine(10f, 10f, 0f);
                            leftAxis.setTextSize(13f);
                           // leftAxis.setYOffset(-2f);
                          //  leftAxis.setTextColor(Color.rgb(255, 192, 56));

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
   //     getStatistic();

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
            int days = (int) TimeUnit.MILLISECONDS.toDays(now);



        // float x = now;

            float x = days;

            Log.e("now" , Float.toString(x));
            Log.e("now days" , Integer.toString(days));
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







        LineDataSet set1 = new LineDataSet(values, "Вага");

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.DKGRAY);
        set1.setValueTextColor(Color.DKGRAY);
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setDrawFilled(true);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.LTGRAY);
        set1.setDrawCircleHole(false);

        ////



        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.DKGRAY);
        }

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
}

