package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.caloriescounter.R;
import com.example.caloriescounter.click_listeners.OnChangeAmountVitamins;
import com.example.caloriescounter.click_listeners.OnDeleteListenerProducts;
import com.example.caloriescounter.click_listeners.OnDeleteListenerVitamins;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.UserVitaminsView;
import com.example.caloriescounter.models.Vitamin;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.viewHolders.ProductCardViewHolder;
import com.example.caloriescounter.viewHolders.VitaminCardViewHolder;

import java.util.List;

public class VitaminsRecyclerAdapter extends RecyclerView.Adapter<VitaminCardViewHolder>{
    private List<UserVitaminsView> vitaminsList;
    private ImageRequester imageRequester;
    private OnDeleteListenerVitamins deleteListener;
    private OnChangeAmountVitamins changeAmountListener;


    private Context context;
    private final String BASE_URL = NetworkService.getBaseUrl();

    public VitaminsRecyclerAdapter(List<UserVitaminsView> vitaminsList, OnDeleteListenerVitamins deleteListener,
                                   OnChangeAmountVitamins changeAmountListener, Context context) {
        this.vitaminsList = vitaminsList;
        imageRequester = ImageRequester.getInstance();
        this.deleteListener = deleteListener;
        this.changeAmountListener = changeAmountListener;
        this.context = context;
    }
    @NonNull
    @Override
    public VitaminCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vitamin, parent, false);
        return new VitaminCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final @NonNull VitaminCardViewHolder holder, final int position) {
        if (vitaminsList != null && position < vitaminsList.size()) {
            final UserVitaminsView vitamin = vitaminsList.get(position);
            holder.txtNameVitamin.setText(vitamin.getVitaminName());
            holder.txtAmount.setText(Integer.toString(vitamin.getAmount()));

            holder.txtAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountListener.changeItem(vitaminsList.get(position));
                    //return true;
                }
            });

            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountListener.addItem(vitaminsList.get(position)); }
                    //return true;
                });

            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountListener.removeItem(vitaminsList.get(position)); }
                //return true;
            });
            // holder.product_protein.setText("Б: " + product.getProductProtein());
            //holder.product_protein.setText(product.getProductProtein());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.deleteItem(vitaminsList.get(position));
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
                    deleteListener.deleteItem(vitaminsList.get(position));
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
        return vitaminsList.size();
    }
}
