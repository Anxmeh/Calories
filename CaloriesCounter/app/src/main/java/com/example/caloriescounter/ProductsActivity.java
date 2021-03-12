package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.caloriescounter.adapters.ProductListRecyclerAdapter;
import com.example.caloriescounter.click_listeners.OnDeleteListenerProducts;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends BaseActivity implements OnDeleteListenerProducts {

    private static final String TAG = ProductsActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Product> products;
    private ProductListRecyclerAdapter adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_products);
        this.getSupportActionBar().setTitle("Мої продукти");
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatingActionButton);

        setRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .getProducts()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            products.clear();
                            products.addAll(0, response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            products = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        products = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        products = new ArrayList<>();
        adapter = new ProductListRecyclerAdapter(products, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void deleteItem(final Product product) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Видалення")
                .setMessage("Ви дійсно бажаєте видалити \"" + product.getName() + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                            products.remove(product);
                        adapter.notifyDataSetChanged();

                        NetworkService.getInstance()
                                .getJSONApi()
                                .removeProduct(product.getId())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                        CommonUtils.hideLoading();
                                        if (response.errorBody() == null && response.isSuccessful()) {
                                            String succeed = "Видалено";
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    succeed, Toast.LENGTH_LONG);
                                            toast.show();
                                        } else {
                                            String errorMessage;
                                            try {
                                                assert response.errorBody() != null;
                                                errorMessage = response.errorBody().string();
                                            } catch (IOException e) {
                                                errorMessage = response.message();
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Помилка з'єднання!";
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                error, Toast.LENGTH_LONG);
                                        toast.show();
                                        t.printStackTrace();
                                    }
                                });
                    }
                })
                .show();
    }
}