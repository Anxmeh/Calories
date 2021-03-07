package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.models.DailyMenuView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseProductActivity extends AppCompatActivity {
    private ListView listView;
    private EditText inputSearch;
    private ArrayList<Product> addedProducts = new ArrayList<Product>();
    private List<Product> products;
    final Context context = this;
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);

        listView = findViewById(R.id.listViewProducts);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        Date currentTime = Calendar.getInstance().getTime();

        NetworkService.getInstance()
                .getJSONApi()
                .getProducts()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            products = response.body();
                            final ProductAdapter adapterP = new ProductAdapter(products, ChooseProductActivity.this);
                            listView.setAdapter(adapterP);
                            inputSearch.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                    adapterP.getFilter().filter(cs);
                                }

                                @Override
                                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                              int arg3) {
                                }

                                @Override
                                public void afterTextChanged(Editable arg0) {
                                }
                            });

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Product product = (Product) adapterP.getItem(position);
                                    addedProducts.add(product);
                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.prompt, null);
                                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                                    mDialogBuilder.setView(promptsView);
                                    final EditText userInput = (EditText) promptsView.findViewById(R.id.inputWeight);
                                    mDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            final DailyMenuView modelDaily = new DailyMenuView();
                                                            modelDaily.setProductName(product.getName());
                                                            modelDaily.setProductCalories(product.getCalories());
                                                            modelDaily.setProductProtein(product.getProtein());
                                                            modelDaily.setProductFat(product.getFat());
                                                            modelDaily.setProductCarbohydrate(product.getCarbohydrate());
                                                            modelDaily.setProductWeight(Double.parseDouble(userInput.getText().toString()));
                                                            modelDaily.setDateOfMeal(currentTime);
                                                            modelDaily.setProductId(product.getId());

                                                            NetworkService.getInstance()
                                                                    .getJSONApi()
                                                                    .addDailyProduct(modelDaily)
                                                                    .enqueue(new Callback<DailyMenuView>() {
                                                                        @Override
                                                                        public void onResponse(@NonNull Call<DailyMenuView> call, @NonNull Response<DailyMenuView> response) {
                                                                            CommonUtils.hideLoading();
                                                                            if (response.errorBody() == null && response.isSuccessful()) {
                                                                                assert response.body() != null;

                                                                                NetworkService.getInstance()
                                                                                        .getJSONApi()
                                                                                        .calculateDish()
                                                                                        .enqueue(new Callback<Dish>() {
                                                                                            @Override
                                                                                            public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response) {
                                                                                                CommonUtils.hideLoading();
                                                                                                if (response.errorBody() == null && response.isSuccessful()) {
                                                                                                    assert response.body() != null;
                                                                                                    dish = response.body();
                                                                                                    Intent intent = new Intent(ChooseProductActivity.this, TodayActivity.class);
                                                                                                    finish();
                                                                                                    startActivity(intent);
                                                                                                } else {
                                                                                                    dish = null;
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t) {

                                                                                                dish = null;
                                                                                                CommonUtils.hideLoading();
                                                                                                String error = "Помилка з'єднання!";
                                                                                                Toast toast = Toast.makeText(getApplicationContext(),
                                                                                                        error, Toast.LENGTH_LONG);
                                                                                                toast.show();
                                                                                                t.printStackTrace();
                                                                                            }
                                                                                        });
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
                                                                        public void onFailure(@NonNull Call<DailyMenuView> call, @NonNull Throwable t) {
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
                            });

                        } else {
                            products = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        products = null;
                        t.printStackTrace();
                    }
                });
    }
}