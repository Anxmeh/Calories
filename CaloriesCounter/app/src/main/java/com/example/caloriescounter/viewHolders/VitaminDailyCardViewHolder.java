package com.example.caloriescounter.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriescounter.R;

public class VitaminDailyCardViewHolder extends RecyclerView.ViewHolder {

    private View view;
    public TextView txtNameVitamin;
    public TextView txtAmountVitamin;
    public Button delete;
    public CheckBox checkBoxVitamin;

    public VitaminDailyCardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        txtNameVitamin = itemView.findViewById(R.id.txtNameVitamin);
        txtAmountVitamin = itemView.findViewById(R.id.txtAmountVitamin);
        delete = itemView.findViewById(R.id.btnDelete);
        checkBoxVitamin = (CheckBox)itemView.findViewById(R.id.checkVitamin);
    }

    public View getView() {
        return view;
    }
}
