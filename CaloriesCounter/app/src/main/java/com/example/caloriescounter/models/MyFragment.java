package com.example.caloriescounter.models;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.caloriescounter.R;

public class MyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
//        View myFragmentView = inflater.inflate(R.layout.sub_fields_prod,
//               container, false);
//        String srt = this.getArguments().getString("name");
//        EditText addedprod = (EditText) myFragmentView.findViewById(R.id.prodInList);
//        addedprod.setText(srt);
//        return myFragmentView;

        String srt = this.getArguments().getString("name");
        Context context = getActivity().getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        //layout.setBackgroundColor(Color.BLUE);
        TextView text = new TextView(context);
        text.setText(srt);
        text.setTextSize(30);
        layout.addView(text);
        Button btnRemove = new Button(context);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
               }
        });



        btnRemove.setText("-");
       // btnRemove.setIc
       // btnRemove.setBackgroundColor("#6567dc");
        layout.addView(btnRemove);

        return layout;
    }

    public static MyFragment newInstance(String name) {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void closeFragment() {

        //getActivity().getFragmentManager().beginTransaction().remove(this);
        //getActivity().getFragmentManager().popBackStack();

       // getActivity().getSupportFragmentManager().beginTransaction().remove(this);


        android.app.FragmentManager fm = getActivity().getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        // fm.beginTransaction().remove(fragment).commit();

        fm.beginTransaction().remove(this).commit();
    }

    // onClickListener button
//      public void onClick(View v) {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.frameLayout);
//        fm.beginTransaction().remove(fragment).commit(); } });
}
