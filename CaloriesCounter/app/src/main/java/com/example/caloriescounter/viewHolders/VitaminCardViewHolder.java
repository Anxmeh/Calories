package com.example.caloriescounter.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriescounter.R;

public class VitaminCardViewHolder extends RecyclerView.ViewHolder {

    public TextView txtAmount;
    private View view;
    public TextView txtNameVitamin;
    public Button delete;
    public Button btnRemove;
    public Button btnAdd;

    public VitaminCardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        txtAmount = itemView.findViewById(R.id.txtAmountVitamin);
        txtNameVitamin = itemView.findViewById(R.id.txtNameVitamin);
        delete = itemView.findViewById(R.id.btnDelete);
        btnRemove = itemView.findViewById(R.id.btnRemove);
        btnAdd = itemView.findViewById(R.id.btnAdd);
    }

    public View getView() {
        return view;
    }
}
