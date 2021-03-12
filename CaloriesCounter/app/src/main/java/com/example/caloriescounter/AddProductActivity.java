package com.example.caloriescounter;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends BaseActivity {
     private Product addedProduct;
     private String errorMessage = "Обов'язкове поле";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_add_product);
        this.getSupportActionBar().setTitle("Новий продукт");
         }

    public void onClickAddProduct(View view) {
        final TextInputEditText nameProduct = findViewById(R.id.inputName);
        final TextInputLayout nameProductLayout = findViewById(R.id.nameLayout);

        final TextInputEditText calories = findViewById(R.id.inputCalories);
        final TextInputLayout caloriesLayout = findViewById(R.id.caloriesLayout);

        final TextInputEditText protein = findViewById(R.id.inputProtein);
        final TextInputLayout proteinLayout = findViewById(R.id.proteinLayout);

        final TextInputEditText fat = findViewById(R.id.inputFat);
        final TextInputLayout fatLayout = findViewById(R.id.fatLayout);

        final TextInputEditText carbs = findViewById(R.id.inputCarbs);
        final TextInputLayout carbsLayout = findViewById(R.id.carbsLayout);

        boolean isCorrect = true;

        if (Objects.requireNonNull(nameProduct.getText()).toString().equals("")) {
            nameProductLayout.setError(errorMessage);
            isCorrect = false;
        } else {
            nameProductLayout.setError(null);
        }

        if (Objects.requireNonNull(calories.getText()).toString().equals("")) {
            caloriesLayout.setError(errorMessage);
            isCorrect = false;
        } else {
            caloriesLayout.setError(null);
        }

        if (Objects.requireNonNull(protein.getText()).toString().equals("")) {
            proteinLayout.setError(errorMessage);
            isCorrect = false;
        } else {
            proteinLayout.setError(null);
        }

        if (Objects.requireNonNull(fat.getText()).toString().equals("")) {
            fatLayout.setError(errorMessage);
            isCorrect = false;
        } else {
            fatLayout.setError(null);
        }

        if (Objects.requireNonNull(carbs.getText()).toString().equals("")) {
            carbsLayout.setError(errorMessage);
            isCorrect = false;
        } else {
            carbsLayout.setError(null);
        }

        if (!isCorrect)
            return;

        CommonUtils.showLoading(this);
        final AddProductView model = new AddProductView();
        model.setName(nameProduct.getText().toString());
        model.setCalories(Double.parseDouble(calories.getText().toString()));
        model.setProtein(Double.parseDouble(protein.getText().toString()));
        model.setFat(Double.parseDouble(fat.getText().toString()));
        model.setCarbohydrate(Double.parseDouble(carbs.getText().toString()));

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
                            String succeed = "Успішно";
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    succeed, Toast.LENGTH_LONG);
                            toast.show();
                            Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
                            finish();
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
}