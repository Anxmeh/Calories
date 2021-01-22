package com.example.caloriescounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChooseProductActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_choose_product);
    }
}