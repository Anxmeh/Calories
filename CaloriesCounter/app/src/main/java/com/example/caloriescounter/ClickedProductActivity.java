package com.example.caloriescounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.caloriescounter.models.ProgressTextView;

public class ClickedProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_product);

        ProgressTextView progressTextView = (ProgressTextView) findViewById(R.id.progressTextView);
        progressTextView.setValue(49); // устанавливаем нужное значение
    }
}