package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.os.Bundle;

import com.example.caloriescounter.adapters.ProductCardRecyclerViewAdapter;
import com.example.caloriescounter.click_listeners.OnDeleteListener;
import com.example.caloriescounter.models.DishIngredientsView;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity  implements OnDeleteListener {

    private static final String TAG = TestActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Ingredients> ingregients;
    private ProductCardRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView = findViewById(R.id.recycler_view);

        setRecyclerView();

        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .getProductsinDish()
                .enqueue(new Callback<List<Ingredients>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Ingredients>> call, @NonNull Response<List<Ingredients>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                           List<Ingredients> res = response.body();
                            assert response.body() != null;
                            ingregients.clear();
                            ingregients.addAll(0,response.body());
                            int siz = ingregients.size();
                            adapter.notifyDataSetChanged();
                        } else {
                            ingregients = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Ingredients>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        ingregients = null;
                        t.printStackTrace();
                    }
                });

    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        ingregients = new ArrayList<>();
        adapter = new ProductCardRecyclerViewAdapter(ingregients, this, this);

        recyclerView.setAdapter(adapter);

        int largePadding = 16;
        int smallPadding = 4;
       // recyclerView.addItemDecoration(new CategoryGridItemDecoration(largePadding, smallPadding));
    }

    @Override
    public void deleteItem(final Ingredients ingredient) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Видалення")
                .setMessage("Ви дійсно бажаєте видалити \"" + ingredient.getProductName() + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "Delete category by Id "+ ingredient.getProductName());
                        ingregients.remove(ingredient);
                        adapter.notifyDataSetChanged();
                        //deleteConfirm(productEntry);
                    }
                })
                .show();
    }
}