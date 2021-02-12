package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.caloriescounter.models.Vitamin;

import java.util.List;

public class CustomAdapterSpinner extends ArrayAdapter {
    private Context context;
    private int textViewResourceId;
    //private Vitamin[] objects;
    private List<Vitamin> objects;
    public static boolean flag = false;
    public CustomAdapterSpinner(Context context, int textViewResourceId,
                                List<Vitamin> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, textViewResourceId, null);
        if (flag != false) {
            TextView tv = (TextView) convertView;
            Vitamin curr;
            curr = objects.get(position);
            tv.setText(curr.getVitaminName());
        }
        return convertView;
    }
}
