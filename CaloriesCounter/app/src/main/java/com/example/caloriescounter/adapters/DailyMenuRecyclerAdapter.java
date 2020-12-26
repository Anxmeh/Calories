package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriescounter.R;
import com.example.caloriescounter.click_listeners.OnDeleteListenerDailyMenu;
import com.example.caloriescounter.click_listeners.OnDeleteListenerProducts;
import com.example.caloriescounter.models.DailyMenuView;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.RemoveDailyView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.viewHolders.ProductCardViewHolder;

import java.util.List;

public class DailyMenuRecyclerAdapter extends RecyclerView.Adapter<ProductCardViewHolder>{
    private List<DailyMenuView> dailyMenuList;
    private ImageRequester imageRequester;
    private OnDeleteListenerDailyMenu deleteListener;
    private Context context;
    private final String BASE_URL = NetworkService.getBaseUrl();

    public DailyMenuRecyclerAdapter(List<DailyMenuView> productListList, OnDeleteListenerDailyMenu deleteListener, Context context) {
        this.dailyMenuList = productListList;
        imageRequester = ImageRequester.getInstance();
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
        if (dailyMenuList != null && position < dailyMenuList.size()) {
            final DailyMenuView product = dailyMenuList.get(position);
            holder.product_name.setText(product.getProductName());
            // holder.product_protein.setText("Б: " + product.getProductProtein());
            //holder.product_protein.setText(product.getProductProtein());
            holder.product_protein.setText(Double.toString(product.getProductProtein()));
            holder.product_fat.setText(Double.toString(product.getProductFat()));
            holder.product_carbs.setText(Double.toString(product.getProductCarbohydrate()));
            holder.product_calories.setText(Double.toString(product.getProductCalories()));
            holder.product_weight.setText(Double.toString(product.getProductWeight()));

            final RemoveDailyView model = new RemoveDailyView();
            model.setProductId(product.getProductId());
            model.setDateOfMeal(product.getDateOfMeal());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.deleteItem(model, product);
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

//            holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    deleteListener.deleteItem(dailyMenuList.get(position));
//                    return true;
//                }
//            });

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
        return dailyMenuList.size();
    }
}
