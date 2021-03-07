package com.example.caloriescounter.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriescounter.R;
import com.example.caloriescounter.click_listeners.OnDeleteListener;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.viewHolders.ProductCardViewHolder;

import java.util.List;

public class ProductCardRecyclerViewAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private List<Ingredients> productList;
    private ImageRequester imageRequester;
    private OnDeleteListener deleteListener;
    private Context context;
    private final String BASE_URL = NetworkService.getBaseUrl();

    public ProductCardRecyclerViewAdapter(List<Ingredients> productListList, OnDeleteListener deleteListener, Context context) {
        this.productList = productListList;
        imageRequester = ImageRequester.getInstance();
        this.deleteListener = deleteListener;
        this.context=context;
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            layoutView.setClipToOutline(true);
//        }
        return new ProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final @NonNull ProductCardViewHolder holder, final int position) {
        if (productList != null && position < productList.size()) {
            final Ingredients product = productList.get(position);
            holder.product_name.setText(product.getProductName());
           // holder.product_protein.setText("Б: " + product.getProductProtein());
            //holder.product_protein.setText(product.getProductProtein());

           // txtDishProtein.setText(Double.toString(Math.round(dish.getDishProtein()*100.0)/100.0));
            holder.product_protein.setText(Double.toString(Math.round(product.getProductProtein()*product.getProductWeight()/100*100.0)/100.0));
            holder.product_fat.setText(Double.toString(Math.round(product.getProductFat()*product.getProductWeight()/100*100.0)/100.0));
            holder.product_carbs.setText(Double.toString(Math.round(product.getProductCarbohydrate()*product.getProductWeight()/100*100.0)/100.0));
            holder.product_calories.setText(Double.toString(Math.round(product.getProductCalories()*product.getProductWeight()/100*100.0)/100.0));
            holder.product_weight.setText(Double.toString(product.getProductWeight()));

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.deleteItem(productList.get(position));
                    //return true;
                }
            });
//            holder.product_protein.setOnClickListener(new View.OnClickListener() {
//             @Override
//                public void onClick(View v) {
//                 deleteListener.deleteItem(productList.get(position));
//                 //return true;
//               }
//            });

            holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteListener.deleteItem(productList.get(position));
                    return true;
                }
            });

//            holder.product_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Intent intent = new Intent(holder.itemView.getContext(), ClickedProductActivity.class).
////                           putExtra("product", product);
////                    holder.itemView.getContext().startActivity(intent);
//                }
//            });
           // String url = BASE_URL + "/images/" + category.getImage();
           // imageRequester.setImageFromUrl(holder.category_image, url);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}

