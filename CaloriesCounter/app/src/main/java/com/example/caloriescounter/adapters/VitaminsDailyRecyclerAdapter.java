package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriescounter.R;
import com.example.caloriescounter.click_listeners.OnChangeDailyVitaminsListener;
import com.example.caloriescounter.models.UserVitaminsDailyView;
import com.example.caloriescounter.viewHolders.VitaminDailyCardViewHolder;

import java.util.List;

public class VitaminsDailyRecyclerAdapter extends RecyclerView.Adapter<VitaminDailyCardViewHolder> {
    private List<UserVitaminsDailyView> vitaminsList;
    private OnChangeDailyVitaminsListener changeListener;
    private Context context;

    public VitaminsDailyRecyclerAdapter(List<UserVitaminsDailyView> vitaminsList, OnChangeDailyVitaminsListener changeListener, Context context) {
        this.vitaminsList = vitaminsList;
        this.changeListener = changeListener;
        this.context = context;
    }

    @NonNull
    @Override
    public VitaminDailyCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vitamin_daily, parent, false);
        return new VitaminDailyCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final @NonNull VitaminDailyCardViewHolder holder, final int position) {
        if (vitaminsList != null && position < vitaminsList.size()) {
            final UserVitaminsDailyView vitamin = vitaminsList.get(position);
            holder.txtNameVitamin.setText(vitamin.getVitaminName());
            holder.txtAmountVitamin.setText(Integer.toString(vitamin.getAmount()) + "шт");
            holder.checkBoxVitamin.setChecked(vitamin.isTaken());
            holder.checkBoxVitamin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeListener.checkVitamin(vitaminsList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return vitaminsList.size();
    }
}
