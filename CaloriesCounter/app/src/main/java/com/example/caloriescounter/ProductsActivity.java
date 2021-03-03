package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.adapters.ProductListRecyclerAdapter;
import com.example.caloriescounter.click_listeners.OnDeleteListenerProducts;
import com.example.caloriescounter.models.Dish;
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
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.floating_action_button);

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

        int largePadding = 16;
        int smallPadding = 4;
        //  recyclerView.addItemDecoration(new CategoryGridItemDecoration(largePadding, smallPadding));
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
                        Log.e(TAG, "Delete category by Id " + product.getId());
                        products.remove(product);


                        adapter.notifyDataSetChanged();

                        NetworkService.getInstance()
                                .getJSONApi()
                                .removeProduct(product.getId())
                                .enqueue(new Callback<List<Product>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                                        CommonUtils.hideLoading();
                                        if (response.errorBody() == null && response.isSuccessful()) {
                                            assert response.body() != null;
                                            // addedProduct = response.body();
//                                            dish = response.body();
//                                            txtDishCalories.setText(Double.toString(dish.getDishCalories()));
//                                            txtDishWeight.setText(Double.toString(dish.getDishWeight()));
//                                            // double n= Math.round(dish.getDishProtein()*100.0)/100.0;
//                                            txtDishProtein.setText(Double.toString(Math.round(dish.getDishProtein()*100.0)/100.0));
//                                            txtDishFat.setText(Double.toString(Math.round(dish.getDishFat()*100.0)/100.0));
//                                            txtDishCarbs.setText(Double.toString(Math.round(dish.getDishCarbohydrate()*100.0)/100.0));


                                        } else {
                                            String errorMessage;
                                            try {
                                                assert response.errorBody() != null;
                                                errorMessage = response.errorBody().string();
                                            } catch (IOException e) {
                                                errorMessage = response.message();
                                                e.printStackTrace();
                                            }
//                            Toast toast = Toast.makeText(getApplicationContext(),
//                                    errorMessage, Toast.LENGTH_LONG);
//                            toast.show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Помилка з'єднання!";
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                error, Toast.LENGTH_LONG);
                                        toast.show();
                                        t.printStackTrace();
                                    }
                                });
                        //deleteConfirm(productEntry);
                    }
                })
                .show();
    }


////////////////////


//    private GridView gridView;
//    private List<Product> products;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_products);
//        final TextView addedprod = findViewById(R.id.productName);
//        gridView = findViewById(R.id.gridViewProducts);
//
//        CommonUtils.showLoading(this);
//        NetworkService.getInstance()
//                .getJSONApi()
//                .getProducts()
//                .enqueue(new Callback<List<Product>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
//                        CommonUtils.hideLoading();
//                        if (response.errorBody() == null && response.isSuccessful()) {
//                            assert response.body() != null;
//                            products = response.body();
//                            final ProductAdapter adapter = new ProductAdapter(products, ProductsActivity.this);
//
//                            gridView.setAdapter(adapter);
//                            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                                @Override
//                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
//                                    builder.setMessage("Are you sure you want to delete category")
//                                            .setCancelable(false)
//                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    Toast toast = Toast.makeText(getApplicationContext(),
//                                                            "Good you changed your mind!", Toast.LENGTH_LONG);
//                                                    toast.show();
//                                                }
//                                            })
//                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    Product product = products.get(position);
//                                                    products.remove(product);
//                                                    adapter.notifyDataSetChanged();
//                                                    Toast toast = Toast.makeText(getApplicationContext(),
//                                                            "You are deleted category!", Toast.LENGTH_LONG);
//                                                    toast.show();
//                                                }
//                                            });
//                                    AlertDialog alertDialog = builder.create();
//                                    alertDialog.show();
//                                    return true;
//                                }
//                            });
//
//                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    Product product = products.get(position);
//                                    addedprod.append(product.getName());
//
//                                   // Intent intent = new Intent(ProductsActivity.this, ClickedProductActivity.class).
//                                            //putExtra("product", product);
//                                  //  startActivity(intent);
//                                }
//                            });
//
//                        } else {
//                            products = null;
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
//                        CommonUtils.hideLoading();
//                        products = null;
//                        t.printStackTrace();
//                    }
//                });
//    }
}