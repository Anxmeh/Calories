package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.elements.CircularProgressBar;
import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.DishIngredientsView;
import com.example.caloriescounter.models.MyFragment;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestTestActivity extends BaseActivity {

    private static final int    ONE_SECOND_IN_MS = 1000;

    // view elements
//    private CircularProgressBar mCountdownBar1;
//    private CircularProgressBar mCountdownBar2;
//    private CircularProgressBar mCountdownBar3;

    private CircularProgressBar mCountUpBar1;
    private CircularProgressBar mCountUpBar2;
    private CircularProgressBar mCountUpBar3;

//    private CircularProgressBar mCounterNoText1;
//    private CircularProgressBar mCounterNoText2;
//    private CircularProgressBar mCounterNoText3;

    // some countdown timers to provide a little action
    private CountDownTimer mTimerCountDown;
    private CountDownTimer      mTimerCountUp;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // wire up the layout
        // this.setContentView(R.layout.activity_main);
        // setContentView(R.layout.activity_main);
        super.addContentView(R.layout.activity_test_test);

        final Button btnStart = findViewById(R.id.button_start);


        // запуск службы
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // используем явный вызов службы
//                startService(
//                        new Intent(TestTestActivity.this, MyAlarmService.class));
            }
        });

        // wire up the ui elements
//        this.mCountdownBar1 = (CircularProgressBar) this.findViewById(R.id.countdown_bar1);
//        this.mCountdownBar2 = (CircularProgressBar) this.findViewById(R.id.countdown_bar2);
//        this.mCountdownBar3 = (CircularProgressBar) this.findViewById(R.id.countdown_bar3);

        this.mCountUpBar1 = (CircularProgressBar) this.findViewById(R.id.countup_bar1);
        this.mCountUpBar2 = (CircularProgressBar) this.findViewById(R.id.countup_bar2);
        this.mCountUpBar3 = (CircularProgressBar) this.findViewById(R.id.countup_bar3);
        TestTestActivity.this.mCountUpBar2.setProgress(30);

//        this.mCounterNoText1 = (CircularProgressBar) this.findViewById(R.id.counter_no_text1);
//        this.mCounterNoText2 = (CircularProgressBar) this.findViewById(R.id.counter_no_text2);
//        this.mCounterNoText3 = (CircularProgressBar) this.findViewById(R.id.counter_no_text3);

        // enable the back btn on newer phones
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
//        {
//            this.enableUpButton();
//        }
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void enableUpButton()
//    {
//        this.getActionBar().setDisplayHomeAsUpEnabled(true);
//    }

//    @Override
//    @SuppressWarnings("synthetic-access")
//    protected void onResume()
//    {
//        super.onResume();
//
//        // start some timers so that things move
//        this.mTimerCountDown = new CountDownTimer(30 * ONE_SECOND_IN_MS, ONE_SECOND_IN_MS)
//        {
//            @Override
//            public void onTick(final long millisUntilFinished)
//            {
//                final int secondsRemaining = (int) (millisUntilFinished / ONE_SECOND_IN_MS);
////                MainActivity.this.mCountdownBar1.setProgress(secondsRemaining);
////                MainActivity.this.mCountdownBar2.setProgress(secondsRemaining);
////                MainActivity.this.mCountdownBar3.setProgress(secondsRemaining);
////
////                MainActivity.this.mCounterNoText1.setProgress(secondsRemaining);
////                MainActivity.this.mCounterNoText3.setProgress(secondsRemaining);
//            }
//
//            @Override
//            public void onFinish()
//            {
////                MainActivity.this.mCountdownBar1.setProgress(0);
////                MainActivity.this.mCountdownBar2.setProgress(0);
////                MainActivity.this.mCountdownBar3.setProgress(0);
////
////                MainActivity.this.mCounterNoText1.setProgress(0);
////                MainActivity.this.mCounterNoText3.setProgress(0);
////
////                // make it disappear (because we can)
////                MainActivity.this.mCountdownBar3.setVisibility(View.INVISIBLE);
//            }
//        }.start();
//
//        this.mTimerCountUp = new CountDownTimer(30 * ONE_SECOND_IN_MS, ONE_SECOND_IN_MS)
//        {
//            @Override
//            public void onTick(final long millisUntilFinished)
//            {
//                final int secondsElapsed = 30 - ((int) (millisUntilFinished / ONE_SECOND_IN_MS));
//                MainActivity.this.mCountUpBar1.setProgress(secondsElapsed);
//                MainActivity.this.mCountUpBar2.setProgress(secondsElapsed);
//                MainActivity.this.mCountUpBar3.setProgress(secondsElapsed);
//
//               // MainActivity.this.mCounterNoText2.setProgress(secondsElapsed);
//            }
//
//            @Override
//            public void onFinish()
//            {
//                MainActivity.this.mCountUpBar1.setProgress(30);
//                MainActivity.this.mCountUpBar2.setProgress(30);
//                MainActivity.this.mCountUpBar3.setProgress(30);
//
//             //   MainActivity.this.mCounterNoText2.setProgress(30);
//
//                // make it disappear (because we can)
//                MainActivity.this.mCountUpBar3.setVisibility(View.INVISIBLE);
//            }
//        }.start();
//    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // stop any running timers
        // there are needed to be clear and be sure that the timers dont cause exceptions when this activity is not in focus
        if (this.mTimerCountDown != null)
        {
            this.mTimerCountDown.cancel();
        }
        if (this.mTimerCountUp != null)
        {
            this.mTimerCountUp.cancel();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(final MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                // Respond to the action bar's Up/Home button
//                this.finish();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}