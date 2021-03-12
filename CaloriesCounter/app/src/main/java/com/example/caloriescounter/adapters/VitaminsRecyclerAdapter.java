package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.caloriescounter.R;
import com.example.caloriescounter.click_listeners.OnChangeAmountVitamins;
import com.example.caloriescounter.click_listeners.OnDeleteListenerVitamins;
import com.example.caloriescounter.models.UserVitaminsView;
import com.example.caloriescounter.viewHolders.VitaminCardViewHolder;

import java.util.List;

public class VitaminsRecyclerAdapter extends RecyclerView.Adapter<VitaminCardViewHolder> {
    private List<UserVitaminsView> vitaminsList;
    private OnDeleteListenerVitamins deleteListener;
    private OnChangeAmountVitamins changeAmountListener;
    private Context context;

    public VitaminsRecyclerAdapter(List<UserVitaminsView> vitaminsList, OnDeleteListenerVitamins deleteListener,
                                   OnChangeAmountVitamins changeAmountListener, Context context) {
        this.vitaminsList = vitaminsList;
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
                }
            });

            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountListener.addItem(vitaminsList.get(position));
                }
            });

            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountListener.removeItem(vitaminsList.get(position));
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.deleteItem(vitaminsList.get(position));
                }
            });

            holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteListener.deleteItem(vitaminsList.get(position));
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return vitaminsList.size();
    }
}
