package com.example.caloriescounter.click_listeners;

import com.example.caloriescounter.models.DishIngredientsView;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.models.Product;

public interface OnDeleteListener {
    void deleteItem(Ingredients product);
}
