package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.caloriescounter.models.UserDailyWeight;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
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
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestDatesActivity extends AppCompatActivity {

    private List<UserDailyWeight> dailyWeights;
    Calendar calendar = Calendar.getInstance();
    private GraphView graph;
    //private GraphView graphWeight;
    private LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
    private Date d0;
    private Date minDate, maxDate;
    private int numdata;
    private String[] hLabels;
    //private DateFormat dateTimeFormatter;
    private DateFormat dateTimeFormatter;
    SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM", new Locale("uk", "UA"));
    private int mNumLabels;
    private int NumLabels = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dates);

        graph = (GraphView) findViewById(R.id.graph);
      final GraphView graphWeight  = (GraphView) findViewById(R.id.graphWeight);
        // dateTimeFormatter = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        dateTimeFormatter = DateFormat.getDateTimeInstance();
        getStatistic(graphWeight);
        initGraphNumbers(graph);
    }

    public void getStatistic(GraphView graphWeight) {
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

    public void initGraph(GraphView graphWeight) {
//        Button btn = (Button) findViewById(R.id.btnAddSeries);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {




              //  graphWeight.addSeries(new LineGraphSeries(generateDataWeight()));
                generateDataWeight(graphWeight);


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


        graphWeight.getGridLabelRenderer().setNumHorizontalLabels(NumLabels);

                Log.e("mNumLabels:", Integer.toString(mNumLabels));
                //  set date label formatter
             //  graphWeight.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(TestDatesActivity.this));


                //   graphWeight.getGridLabelRenderer().setHumanRounding(false);

               // graphWeight.getGridLabelRenderer().setNumHorizontalLabels(mNumLabels); // only 4 because of the space
                Log.e("Numdata:", Integer.toString(numdata));
        Log.e("maxdate:", Long.toString(maxDate.getTime()));
        // set manual x bounds to have nice steps
        graphWeight.getViewport().setMinX(minDate.getTime());
        graphWeight.getViewport().setMaxX(maxDate.getTime());
        graphWeight.getViewport().setXAxisBoundsManual(true);
        graphWeight.getViewport().setScalable(true);
        graphWeight.getViewport().setScrollable(true);
        graphWeight.getGridLabelRenderer().setHumanRounding(true);
             //   graphWeight.getGridLabelRenderer().setHumanRounding(false);

//                graphWeight.getViewport().setMinX(minDate.getTime());
//                graphWeight.getViewport().setMaxX(maxDate.getTime());
              //  graphWeight.getViewport().setXAxisBoundsManual(true);
           //     graphWeight.getViewport().setScalable(true);
             //   graphWeight.getViewport().setScrollable(true);

                // as we use dates as labels, the human rounding to nice readable numbers
                // is not nessecary



//                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphWeight);
//              //  staticLabelsFormatter.setHorizontalLabels(new String[] {"21/02", "22/02", "23/02", "24/02", "25/02", "26/02"});
//                staticLabelsFormatter.setHorizontalLabels(hLabels);
//                graphWeight.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
             //   graphWeight.getViewport().setScalable(true);
               // graphWeight.getViewport().setScrollable(true);
                //     graphWeight.getViewport().setScalable(true);
////
////// set manual x bounds to have nice steps
//                graphWeight.getViewport().setMinX(minDate.getTime());
//                graphWeight.getViewport().setMaxX(maxDate.getTime());
//                graphWeight.getViewport().setXAxisBoundsManual(true);
//
////// as we use dates as labels, the human rounding to nice readable numbers
////// is not necessary

            //}

     //   });
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

//    private DataPoint[] generateDataWeight() {
//
//        int count = dailyWeights.size();
//       // hLabels = new String[dailyWeights.size()];
//        // new DataPoint(dd1, 1),
//        DataPoint[] values = new DataPoint[count];
//        int i = 0;
//        for (UserDailyWeight day : dailyWeights) {
//            calendar.setTime(day.getDateOfWeight());
//            //Date x = day.getDateOfWeight();
//            Date x = calendar.getTime();
//            if (i == 0) {
//                minDate = x;
//                calendar.setTime(x);
//            }
//            // String date = formatDate.format(day.getDateOfWeight());
//         //   hLabels[i] = formatDate.format(day.getDateOfWeight());
//
//
//            double y = day.getWeight();
//            DataPoint data = new DataPoint(x, y);
//            Log.e("dateindate:", x.toString());
//            Log.e("dateinfor:", Long.toString(calendar.getTimeInMillis()));
//
//            values[i] = data;
//           // Log.e("In init:", hLabels[i] + " : " + Double.toString(y));
//            i++;
//
//        }
//
//       // calendar.add(Calendar.DATE, i - 1);
//        maxDate = calendar.getTime();
//        numdata = count;
//        Log.e("mindate:", minDate.toString());
//        Log.e("maxdate:", maxDate.toString());
//        return values;
//    }

    private DataPoint[] generateDataWeight(GraphView graphWeight) {

        int count = dailyWeights.size();
        // hLabels = new String[dailyWeights.size()];
        // new DataPoint(dd1, 1),
        DataPoint[] values = new DataPoint[count];
        int i = 0;
        for (UserDailyWeight day : dailyWeights) {
            calendar.setTime(day.getDateOfWeight());
            //Date x = day.getDateOfWeight();
            Date x = calendar.getTime();
            if (i == 0) {
                minDate = x;
                calendar.setTime(x);
            }
            // String date = formatDate.format(day.getDateOfWeight());
            //   hLabels[i] = formatDate.format(day.getDateOfWeight());


            double y = day.getWeight();
            DataPoint data = new DataPoint(x, y);
            Log.e("dateindate:", x.toString());
            Log.e("dateinfor:", Long.toString(calendar.getTimeInMillis()));

            Date datet = new Date(calendar.getTimeInMillis());
            DateFormat formattert = new SimpleDateFormat("dd, MM, HH:mm:ss.SSS");
            formattert.setTimeZone(TimeZone.getTimeZone("EST"));
            String dateFormatted = formattert.format(datet);
            Log.e("dateagain", dateFormatted);

            values[i] = data;
            // Log.e("In init:", hLabels[i] + " : " + Double.toString(y));
            i++;

        }

        // calendar.add(Calendar.DATE, i - 1);
        maxDate = calendar.getTime();
        numdata = count;
        Log.e("mindate:", Long.toString(minDate.getTime()));
       // Log.e("maxdate:", Long.toString(maxDate.getTime()));



        LineGraphSeries<DataPoint> seriesn = new LineGraphSeries<DataPoint>(values);

        graphWeight.addSeries(seriesn);
        return values;
    }
}