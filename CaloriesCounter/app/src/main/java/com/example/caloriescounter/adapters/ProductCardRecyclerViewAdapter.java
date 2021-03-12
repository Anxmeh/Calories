package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriescounter.R;
import com.example.caloriescounter.click_listeners.OnDeleteListener;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.viewHolders.ProductCardViewHolder;

import java.util.List;

public class ProductCardRecyclerViewAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private List<Ingredients> productList;
    private OnDeleteListener deleteListener;
    private Context context;

    public ProductCardRecyclerViewAdapter(List<Ingredients> productListList, OnDeleteListener deleteListener, Context context) {
        this.productList = productListList;
        this.deleteListener = deleteListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final @NonNull ProductCardViewHolder holder, final int position) {
        if (productList != null && position < productList.size()) {
            final Ingredients product = productList.get(position);
            holder.product_name.setText(product.getProductName());
            holder.product_protein.setText(Double.toString(Math.round(product.getProductProtein() * product.getProductWeight() / 100 * 100.0) / 100.0));
            holder.product_fat.setText(Double.toString(Math.round(product.getProductFat() * product.getProductWeight() / 100 * 100.0) / 100.0));
            holder.product_carbs.setText(Double.toString(Math.round(product.getProductCarbohydrate() * product.getProductWeight() / 100 * 100.0) / 100.0));
            holder.product_calories.setText(Double.toString(Math.round(product.getProductCalories() * product.getProductWeight() / 100 * 100.0) / 100.0));
            holder.product_weight.setText(Double.toString(product.getProductWeight()));

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.deleteItem(productList.get(position));
                }
            });

            holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteListener.deleteItem(productList.get(position));
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

