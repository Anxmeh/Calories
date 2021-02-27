package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.CustomAdapter;
import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.models.AddUserDailyWeightViewModel;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.UserDailyWeight;
import com.example.caloriescounter.models.Vitamin;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestProdActivity extends AppCompatActivity {
    private int mNumLabels = 6;
    private Date minDate, maxDate;
    SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM",  new Locale("uk","UA"));
    private AddUserDailyWeightViewModel weightUser;
//    public Dates(int numLabels) {
//        mNumLabels = numLabels;
//    }
//
//    public Dates() {
//        mNumLabels = 4;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_prod);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        initGraph(graph);

        GraphView graphWeight = (GraphView) findViewById(R.id.graphWeight);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2)
//
//        });
//        graph.addSeries(series);
      //  new DataPoint(4, 6)

    }

    public void initGraph(GraphView graph) {


        // generate Dates
//        Calendar calendar = Calendar.getInstance();
//        Date d1 = calendar.getTime();
//              Log.e("d1:", d1.toString());
//
//        calendar.add(Calendar.DATE, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        Date d2 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d3 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d4 = calendar.getTime();
//       calendar.add(Calendar.DATE, 1);
//      Date d5 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d6 = calendar.getTime();
//        Log.e("d6:", d6.toString());

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(d1, 1),
//                new DataPoint(d2, 5),
//                new DataPoint(d3, 3),
//                new DataPoint(d4, 2),
//               new DataPoint(d5, 5),
//                new DataPoint(d6, 1)
//        });
//        graph.addSeries(series);

        graph.addSeries(new LineGraphSeries(generateDataWeight()));

        // set date label formatter
     //   graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));


      ////  майже працюэ
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
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


//        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//          staticLabelsFormatter.setHorizontalLabels(new String[] {"21/02",  "26/02"});
       // staticLabelsFormatter.setHorizontalLabels(hLabels);
        graph.getGridLabelRenderer().setNumHorizontalLabels(mNumLabels);
        Log.e("mindate:", Long.toString(maxDate.getTime()));
        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(minDate.getTime());
        graph.getViewport().setMaxX(maxDate.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not nessecary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }


    private DataPoint[] generateDataWeight() {

        int count = 6;
        // hLabels = new String[dailyWeights.size()];
        // new DataPoint(dd1, 1),
        DataPoint[] values = new DataPoint[count];
      //  int i = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        for(int i=0; i<6; i++) {


            Date d1 = calendar.getTime();
            if (i == 0) {
                minDate = d1;

            }
            if (i==5) {
                maxDate = d1;
            }
            Log.e("d1:", d1.toString());
            Log.e("dateinml:", Long.toString(calendar.getTimeInMillis()));
            calendar.add(Calendar.DATE, 1);

            int y = i+2;
            DataPoint data = new DataPoint(d1, y);
            values[i] = data;


        }



        return values;
    }






}


//    LineGraphSeries<DataPoint> seriesWeight = new LineGraphSeries<>(new DataPoint[] {
//            new DataPoint(0, 48),
//            new DataPoint(1, 48),
//            new DataPoint(2, 49),
//            new DataPoint(3, 49),
//            new DataPoint(4, 48),
//            new DataPoint(5, 47),
//            new DataPoint(6, 46),
//            new DataPoint(7, 47),
//            new DataPoint(8, 47.8),
//            new DataPoint(9, 48),
//            new DataPoint(10, 48.2),
//            new DataPoint(11, 48),
//            new DataPoint(12, 47.6),
//            new DataPoint(13, 47.2),
//
//    });
//        graphWeight.getViewport().setYAxisBoundsManual(true);
//                graphWeight.addSeries(seriesWeight);
//                graphWeight.getViewport().setMinY(40);
//                graphWeight.getViewport().setMaxY(60);
//                graphWeight.getViewport().setScalable(true);
//
//
//
//                Calendar calendar = Calendar.getInstance();
//                Date d1 = calendar.getTime();
//                calendar.add(Calendar.DATE, 1);
//                Date d2 = calendar.getTime();
//                calendar.add(Calendar.DATE, 1);
//                Date d3 = calendar.getTime();
//
//
//
//// you can directly pass Date objects to DataPoint-Constructor
//// this will convert the Date to double via Date#getTime()
//                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//        new DataPoint(d1, 1),
//        new DataPoint(d2, 5),
//        new DataPoint(d3, 3)
//        });
//
//        graph.addSeries(series);
//
//// set date label formatter
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
//
//// set manual x bounds to have nice steps
//        graph.getViewport().setMinX(d1.getTime());
//        graph.getViewport().setMaxX(d3.getTime());
//        graph.getViewport().setXAxisBoundsManual(true);
//
//// as we use dates as labels, the human rounding to nice readable numbers
//// is not necessary
//        graph.getGridLabelRenderer().setHumanRounding(false);
//
//
//
//
//
//
//
//        }
//
//
//public void initGraph(GraphView graph) {
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
//        new DataPoint(0, 1),
//        new DataPoint(1, 5),
//        new DataPoint(2, 3),
//        new DataPoint(3, 2),
//        new DataPoint(4, 5),
//        new DataPoint(5, 6)
//        });
//        graph.addSeries(series);
//
//        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
//        new DataPoint(0, 30),
//        new DataPoint(1, 30),
//        new DataPoint(2, 60),
//        new DataPoint(3, 20),
//        new DataPoint(4, 60),
//        new DataPoint(5, 50)
//        });
//
//        // set second scale
//        graph.getSecondScale().addSeries(series2);
//        // the y bounds are always manual for second scale
//        graph.getSecondScale().setMinY(0);
//        graph.getSecondScale().setMaxY(100);
//        series2.setColor(Color.RED);


