package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.caloriescounter.models.RegisterView;
import com.example.caloriescounter.models.UserDailyWeight;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.Tokens;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeightStatActivity extends BaseActivity {

    private List<UserDailyWeight> dailyWeights;
    Calendar calendar = Calendar.getInstance();
    private GraphView graph;
    private GraphView graphWeight;
    private LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
    private Date d0;
    private Date minDate, maxDate;
    private int numdata;
    private String[] hLabels;
    //private DateFormat dateTimeFormatter;
    private DateFormat dateTimeFormatter;
    SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM", new Locale("uk", "UA"));
    private int mNumLabels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_weight_stat);
        this.getSupportActionBar().setTitle("Статистика ваги");


        graph = (GraphView) findViewById(R.id.graph);
        graphWeight = (GraphView) findViewById(R.id.graphWeight);
        // dateTimeFormatter = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        dateTimeFormatter = DateFormat.getDateTimeInstance();
        getStatistic();
       initGraphNumbers(graph);


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
                            mNumLabels = dailyWeights.size();
                            initGraph(graphWeight);

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


    public void initGraphNumbers(final GraphView graph) {
        Button btn = (Button) findViewById(R.id.btnAddSeriesNumbers);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.addSeries(new LineGraphSeries(generateData()));
            }

        });
    }

    public void initGraph(final GraphView graph) {
        Button btn = (Button) findViewById(R.id.btnAddSeries);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//                int count = dailyWeights.size();
//              //  hLabels = new String[dailyWeights.size()];
//                // new DataPoint(dd1, 1),
//                DataPoint[] values = new DataPoint[count];
//                Date[]  dates = new Date[count];
//                int i = 0;
//                for (UserDailyWeight day : dailyWeights) {
//                    Date x = day.getDateOfWeight();
//                    if (i == 0) {
//                        minDate = x;
//                        calendar.setTime(x);
//                    }
//                    // String date = formatDate.format(day.getDateOfWeight());
//                  //  hLabels[i] = formatDate.format(day.getDateOfWeight());
//                    double y = day.getWeight();
//
//                    dates[i] = day.getDateOfWeight();
//                    calendar.setTime(dates[i]);
//                    Date gh = calendar.getTime();
//
//                    DataPoint data = new DataPoint(x, y);
//                    Log.e("dateinforuuu:", x.toString());
//                    Log.e("gh:", x.toString());
//
//                    values[i] = data;
//                  //  Log.e("In init:", hLabels[i] + " : " + Double.toString(y));
//                    i++;
//
//                }
//                maxDate = calendar.getTime();
//                numdata = count;
//
//                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                        new DataPoint(dates[0], 1),
//                        new DataPoint(dates[1], 5),
////                        new DataPoint(d3, 3),
////                        new DataPoint(d4, 2),
////                        new DataPoint(d5, 5),
////                        new DataPoint(d6, 1)
//                });
//
//
//                graphWeight.addSeries(series);
              //  graphWeight.addSeries(new LineGraphSeries(generateDataWeight()));
        graphWeight.addSeries(new LineGraphSeries(generateDataWeight()));


        // майже працюэ
//        graphWeight.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
//            @Override
//            public String formatLabel(double value, boolean isValueX) {
//                if (isValueX) {
//                    Log.e("Value", Double.toString(value));
//                    // transform number to time
//                    return formatDate.format(new Date((long) value));
//                } else {
//                    // return null;
//                    Log.e("Value in else", Double.toString(value));
//                    return super.formatLabel(value, isValueX);
//                }
//            }
//        });

//              

                graphWeight.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            Log.e("Value", Double.toString(value));
                            // transform number to time
                            return formatDate.format(new Date((long) value));
                        } else {
                            // return null;
                            Log.e("Value in else", Double.toString(value));
                            return super.formatLabel(value, isValueX);
                        }
                    }
                });




        //  graphWeight.getViewport().setScalable(true);


//                graphWeight.setCustomLabelFormatter(new CustomLabelFormatter()
//                {
//                    @Override
//                    public String formatLabel(double value, boolean isValueX)
//                    {
//                        if (isValueX)
//                        {
//                            return dateTimeFormatter.format(new Date((long) value));
//                        }
//                        return null; // let graphview generate Y-axis label for us
//                    }
//                });


        Log.e("mNumLabels:", Integer.toString(mNumLabels));
        //  set date label formatter
//                graphWeight.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphWeight.getContext()));
        //   graphWeight.getGridLabelRenderer().setHumanRounding(false);
          //      graphWeight.getGridLabelRenderer().setNumHorizontalLabels(2);
    //  graphWeight.getGridLabelRenderer().setNumHorizontalLabels(mNumLabels-1); // only 4 because of the space
        Log.e("Numdata:", Integer.toString(numdata));

                graphWeight.getViewport().setMinX(minDate.getTime());
                graphWeight.getViewport().setMaxX(maxDate.getTime());
                graphWeight.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers




                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphWeight);
               staticLabelsFormatter.setHorizontalLabels(new String[] {"21/02", "22/02", "23/02", "24/02", "25/02", "26/02"});
            //    staticLabelsFormatter.setHorizontalLabels(new String[] {"21/02", "26/02"});
               // staticLabelsFormatter.setHorizontalLabels(hLabels);
                graphWeight.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                graphWeight.getViewport().setScalable(true);
                graphWeight.getViewport().setScrollable(true);
        //     graphWeight.getViewport().setScalable(true);
////
////// set manual x bounds to have nice steps
//                graphWeight.getViewport().setMinX(minDate.getTime());
//                graphWeight.getViewport().setMaxX(maxDate.getTime());
//                graphWeight.getViewport().setXAxisBoundsManual(true);
//
////// as we use dates as labels, the human rounding to nice readable numbers
////// is not necessary
          // graphWeight.getGridLabelRenderer().setHumanRounding(false);
        }

         });
    }

    private DataPoint[] generateData() {
        Random rand = new Random();
        int count = 30;

        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double f = rand.nextDouble() * 0.15 + 0.3;
            double y = Math.sin(i * f + 2) + rand.nextDouble() * 0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private DataPoint[] generateDataWeight() {

        int count = dailyWeights.size();
        hLabels = new String[dailyWeights.size()];
        // new DataPoint(dd1, 1),
        DataPoint[] values = new DataPoint[count];
        int i = 0;
        for (UserDailyWeight day : dailyWeights) {
            Date x = day.getDateOfWeight();
            if (i == 0) {
                minDate = x;
                calendar.setTime(x);
            }
            // String date = formatDate.format(day.getDateOfWeight());
            hLabels[i] = formatDate.format(day.getDateOfWeight());


            double y = day.getWeight();
            DataPoint data = new DataPoint(x, y);
            Log.e("dateinfor:", x.toString());

            values[i] = data;
            Log.e("In init:", hLabels[i] + " : " + Double.toString(y));
            i++;

        }

        //calendar.add(Calendar.DATE, i - 1);
        calendar.add(Calendar.DATE, 10);
        maxDate = calendar.getTime();
        numdata = count;
        Log.e("mindate:", minDate.toString());
        Log.e("maxdate:", maxDate.toString());
        return values;
    }
}