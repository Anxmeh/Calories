package com.example.caloriescounter.click_listeners;

import com.example.caloriescounter.models.DailyMenuView;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.RemoveDailyView;

public interface OnDeleteListenerDailyMenu {
    void deleteItem(RemoveDailyView product, DailyMenuView view);
}
