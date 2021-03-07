package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.ProductCardRecyclerViewAdapter;
import com.example.caloriescounter.click_listeners.OnDeleteListener;
import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerActivity extends BaseActivity implements OnDeleteListener {

    private static final String TAG = RecyclerActivity.class.getSimpleName();
    final Context context = this;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private List<Ingredients> prodsindish;
    private ProductCardRecyclerViewAdapter adapter;
    private Product addedProduct;
    private ListView listView;
    private Dish dish;
       private FloatingActionButton fab;
    private TextView txtDishProtein;
    private TextView txtDishFat;
    private TextView txtDishCarbs;
    private TextView txtDishWeight;
    private TextView txtDishCalories;
    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_recycler);
        this.getSupportActionBar().setTitle("Нова страва");
        recyclerView = findViewById(R.id.recycler_view);

        fab = findViewById(R.id.floating_action_button);
        listView = findViewById(R.id.listViewProducts);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        txtDishProtein = findViewById(R.id.dishProtein);
        txtDishFat = findViewById(R.id.dishFat);
        txtDishCarbs = findViewById(R.id.dishCarbohydrate);
        txtDishCalories = findViewById(R.id.dishCalories);
        txtDishWeight = findViewById(R.id.dishWeight);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecyclerActivity.this, ChooseIngredientActivity.class);
                finish();
                startActivity(intent);
            }

        });
        sessionManager = SessionManager.getInstance(this);
        setRecyclerView();
        loadList();
        calculate();

    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        prodsindish = new ArrayList<>();
        adapter = new ProductCardRecyclerViewAdapter(prodsindish, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void deleteItem(final Ingredients product) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Видалення")
                .setMessage("Ви дійсно бажаєте видалити \"" + product.getProductName() + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "Delete product by name " + product.getProductName());
                        prodsindish.remove(product);

                        NetworkService.getInstance()
                                .getJSONApi()
                                .removeProductInDish(product)
                                .enqueue(new Callback<Dish>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response) {
                                        CommonUtils.hideLoading();
                                        if (response.errorBody() == null && response.isSuccessful()) {
                                            assert response.body() != null;
                                            dish = response.body();
                                            txtDishCalories.setText(Double.toString(dish.getDishCalories()));
                                            txtDishWeight.setText(Double.toString(dish.getDishWeight()));
                                            txtDishProtein.setText(Double.toString(Math.round(dish.getDishProtein() * 100.0) / 100.0));
                                            txtDishFat.setText(Double.toString(Math.round(dish.getDishFat() * 100.0) / 100.0));
                                            txtDishCarbs.setText(Double.toString(Math.round(dish.getDishCarbohydrate() * 100.0) / 100.0));
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
                                    public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Помилка з'єднання!";
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                error, Toast.LENGTH_LONG);
                                        toast.show();
                                        t.printStackTrace();
                                    }
                                });
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    public void onClickAddDish(View view) {
        AddProductView model = new AddProductView();
        model.setProtein(Math.round(dish.getDishProtein() * 100 / dish.getDishWeight() * 100.0) / 100.0);
        model.setFat(Math.round(dish.getDishFat() * 100 / dish.getDishWeight() * 100.0) / 100.0);
        model.setCarbohydrate(Math.round(dish.getDishCarbohydrate() * 100 / dish.getDishWeight() * 100.0) / 100.0);
        model.setCalories(Math.round(dish.getDishCalories() * 100 / dish.getDishWeight() * 100.0) / 100.0);
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dish_name, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);
        final EditText userInputDishName = (EditText) promptsView.findViewById(R.id.inputDishName);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                model.setName(userInputDishName.getText().toString());

                                NetworkService.getInstance()
                                        .getJSONApi()
                                        .addProduct(model)
                                        .enqueue(new Callback<Product>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                                                CommonUtils.hideLoading();
                                                if (response.errorBody() == null && response.isSuccessful()) {
                                                    assert response.body() != null;
                                                    addedProduct = response.body();
                                                    String succeed = "Додано";
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            succeed, Toast.LENGTH_LONG);
                                                    toast.show();
                                                    Intent intent = new Intent(RecyclerActivity.this, ProductsActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    String errorMessage;
                                                    try {
                                                        assert response.errorBody() != null;
                                                        errorMessage = response.errorBody().string();
                                                    } catch (IOException e) {
                                                        errorMessage = response.message();
                                                        e.printStackTrace();
                                                    }
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            errorMessage, Toast.LENGTH_LONG);
                                                    toast.show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
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
                .setNegativeButton("Відміна",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    public void loadList() {
        NetworkService.getInstance()
                .getJSONApi()
                .getProductsinDish()
                .enqueue(new Callback<List<Ingredients>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Ingredients>> call, @NonNull Response<List<Ingredients>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            if (prodsindish != null)
                                prodsindish.clear();
                            prodsindish.addAll(0, response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            prodsindish = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Ingredients>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        prodsindish = null;
                        t.printStackTrace();
                    }
                });
    }

    public void calculate() {
        NetworkService.getInstance()
                .getJSONApi()
                .calculateDish()
                .enqueue(new Callback<Dish>() {
                    @Override
                    public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            dish = response.body();
                            txtDishCalories.setText(Double.toString(dish.getDishCalories()));
                            txtDishWeight.setText(Double.toString(dish.getDishWeight()));
                            txtDishProtein.setText(Double.toString(dish.getDishProtein()));
                            txtDishFat.setText(Double.toString(dish.getDishFat()));
                            txtDishCarbs.setText(Double.toString(dish.getDishCarbohydrate()));
                        } else {
                            dish = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        dish = null;
                        t.printStackTrace();
                    }
                });
    }
}