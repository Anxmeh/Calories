package com.example.caloriescounter.click_listeners;

import com.example.caloriescounter.models.UserVitaminsView;

public interface OnChangeAmountVitamins {
    void changeItem(UserVitaminsView vitamin);
    void addItem(UserVitaminsView vitamin);
    void removeItem(UserVitaminsView vitamin);

}
