package com.example.caloriescounter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


//    public void onClickAdd(View view) {
//        LayoutInflater ltInflater = getLayoutInflater();
//        final LinearLayout subLayoutFields = (LinearLayout) findViewById(R.id.subLayoutFields);
//        final View view1 = ltInflater.inflate(R.layout.sub_fields, subLayoutFields, true);
//        Button buttonRemove = (Button) view1.findViewById(R.id.btnRemove);
//
//        buttonRemove.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                subLayoutFields.removeView((LinearLayout) (v.getParent().getParent()));
//            }
//        });
//    }

    public void onClickAdd(View view) {
        LayoutInflater ltInflater = getLayoutInflater();
        LinearLayout subLayoutFieldsForBtnAdd = (LinearLayout) findViewById(R.id.subLayoutFields);
        View view1 = ltInflater.inflate(R.layout.sub_fields, subLayoutFieldsForBtnAdd, true);
    }

    public void onClickRemove(View v) {
        View v1 = (View) v.getParent();
        LinearLayout subLayoutFieldsForBtnRemove = (LinearLayout) findViewById(R.id.subLayoutFields);
        subLayoutFieldsForBtnRemove.removeView((LinearLayout)v1.getParent());
    }
}