package com.example.caloriescounter.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.caloriescounter.R;
import com.google.android.material.button.MaterialButton;

public class ProductCardViewHolder extends RecyclerView.ViewHolder {

   // public NetworkImageView category_image;
    public TextView product_name;
    private View view;
    public MaterialButton product_button;

    public ProductCardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        //category_image = itemView.findViewById(R.id.category_image);
        product_name = itemView.findViewById(R.id.product_name);
        product_button = itemView.findViewById(R.id.category_button);

//        category_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Category category = new Category(1, "Hello", "belka.jpg");
//                Intent intent = new Intent(view.getContext(), ClickedCategoryActivity.class).
//                        putExtra("category", category);
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    public View getView() {
        return view;
    }
}
