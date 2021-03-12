package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.caloriescounter.models.LineChartXAxisValueFormatter;
import com.example.caloriescounter.models.MyMarkerView;
import com.example.caloriescounter.models.UserDailyWeight;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalTestDateActivity extends BaseActivity {

    private LineChart chart;
    Calendar calendar = Calendar.getInstance();
    private List<UserDailyWeight> dailyWeights;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_final_test_date);
        this.getSupportActionBar().setTitle("Статистика ваги");

        sessionManager = SessionManager.getInstance(this);

        if (!sessionManager.isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        chart = findViewById(R.id.reportingChart);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.getDescription().setEnabled(false);

        MyMarkerView marker = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        marker.setChartView(chart);
        chart.setMarker(marker);

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
                            setData(dailyWeights.size());

                            Legend legend = chart.getLegend();
                            legend.setEnabled(false);

                            XAxis xAxis = chart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setTextSize(10f);
                            xAxis.enableGridDashedLine(10f, 10f, 0f);
                            xAxis.setDrawAxisLine(true);
                            xAxis.setDrawGridLines(true);
                            xAxis.setCenterAxisLabels(true);
                            xAxis.setGranularityEnabled(true);
                            xAxis.setGranularity(1f);
                            xAxis.setValueFormatter(new LineChartXAxisValueFormatter());

                            YAxis leftAxis = chart.getAxisLeft();
                            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                            leftAxis.setDrawGridLines(true);
                            leftAxis.setGranularityEnabled(true);
                            leftAxis.setAxisMinimum(45f);
                            leftAxis.setAxisMaximum(55f);
                            leftAxis.enableGridDashedLine(10f, 10f, 0f);
                            leftAxis.setTextSize(13f);

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

    private void setData(int count) {
        long now;
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            calendar.setTime(dailyWeights.get(i).getDateOfWeight());
            now = calendar.getTimeInMillis();
            int days = (int) TimeUnit.MILLISECONDS.toDays(now);
            float x = days;
            float y = (float) dailyWeights.get(i).getWeight();
            values.add(new Entry(x, y));
        }

        LineDataSet dataSet = new LineDataSet(values, "Вага");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(Color.DKGRAY);
        dataSet.setValueTextColor(Color.DKGRAY);
        dataSet.setLineWidth(1.5f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(65);
        dataSet.setFillColor(ColorTemplate.getHoloBlue());
        dataSet.setHighLightColor(Color.LTGRAY);
        dataSet.setDrawCircleHole(false);

        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
            dataSet.setFillDrawable(drawable);
        } else {
            dataSet.setFillColor(Color.DKGRAY);
        }

        LineData data = new LineData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);
        chart.setData(data);
    }
}

