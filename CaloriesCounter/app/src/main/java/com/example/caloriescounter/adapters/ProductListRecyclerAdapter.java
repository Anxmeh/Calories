package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriescounter.R;
import com.example.caloriescounter.click_listeners.OnDeleteListenerProducts;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.viewHolders.ProductCardViewHolder;

import java.util.List;

public class ProductListRecyclerAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {
    private List<Product> productList;
    private OnDeleteListenerProducts deleteListener;
    private Context context;

    public ProductListRecyclerAdapter(List<Product> productListList, OnDeleteListenerProducts deleteListener, Context context) {
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
            final Product product = productList.get(position);
            holder.product_name.setText(product.getName());
            holder.product_protein.setText(Double.toString(product.getProtein()));
            holder.product_fat.setText(Double.toString(product.getFat()));
            holder.product_carbs.setText(Double.toString(product.getCarbohydrate()));
            holder.product_calories.setText(Double.toString(product.getCalories()));
            holder.product_weight.setText("100");

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
