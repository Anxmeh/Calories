package com.example.caloriescounter.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriescounter.R;

public class ProductCardViewHolder extends RecyclerView.ViewHolder {

    public TextView product_name;
    private View view;
    public TextView product_protein;
    public TextView product_fat;
    public TextView product_carbs;
    public TextView product_weight;
    public TextView product_calories;
    public Button delete;

    public ProductCardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        product_name = itemView.findViewById(R.id.product_name);
        product_protein = itemView.findViewById(R.id.product_protein);
        product_fat = itemView.findViewById(R.id.product_fat);
        product_carbs = itemView.findViewById(R.id.product_carbohydrate);
        product_calories = itemView.findViewById(R.id.product_calories);
        product_weight = itemView.findViewById(R.id.product_weight);
        delete = itemView.findViewById(R.id.btnDelete);

        product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public View getView() {
        return view;
    }
}
