package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.RegisterView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.Tokens;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.example.caloriescounter.network.utils.FileUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends BaseActivity {

     private final String BASE_URL = NetworkService.getBaseUrl();
     private Product addedProduct;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_add_product);
        this.getSupportActionBar().setTitle("Новий продукт");
      //  setContentView(R.layout.activity_add_product);
//        Toolbar homeToolbar = findViewById(R.id.home_toolbar);
//        homeToolbar.setTitle("Новий продукт");
//        setSupportActionBar(homeToolbar);
//
//        drawerLayout = findViewById(R.id.drawerLayout);
//        NavigationView navigationView = findViewById(R.id.navigation);
//        navigationView.bringToFront();
//        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
//        drawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                return onNavItemSelected(item);
//            }
//        });
//
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        sessionManager = SessionManager.getInstance(this);
    }

    public void onClickAddProduct(View view) {
        final TextInputEditText nameProduct = findViewById(R.id.input_name);
        final TextInputLayout nameProductLayout = findViewById(R.id.nameLayout);

        final TextInputEditText calories = findViewById(R.id.input_calories);
        final TextInputLayout caloriesLayout = findViewById(R.id.caloriesLayout);

        final TextInputEditText protein = findViewById(R.id.input_protein);
        final TextInputLayout proteinLayout = findViewById(R.id.proteinLayout);

        final TextInputEditText fat = findViewById(R.id.input_fat);
        final TextInputLayout fatLayout = findViewById(R.id.fatLayout);

        final TextInputEditText carbs = findViewById(R.id.input_carbs);
        final TextInputLayout carbsLayout = findViewById(R.id.carbsLayout);

        boolean isCorrect = true;

        if (Objects.requireNonNull(nameProduct.getText()).toString().equals("")) {
            nameProductLayout.setError("Required field!");
            isCorrect = false;
        } else {
            nameProductLayout.setError(null);
        }

        if (Objects.requireNonNull(calories.getText()).toString().equals("")) {
            caloriesLayout.setError("Required field!");
            isCorrect = false;
        } else {
            caloriesLayout.setError(null);
        }

        if (Objects.requireNonNull(protein.getText()).toString().equals("")) {
            proteinLayout.setError("Required field!");
            isCorrect = false;
        } else {
            proteinLayout.setError(null);
        }

        if (Objects.requireNonNull(fat.getText()).toString().equals("")) {
            fatLayout.setError("Required field!");
            isCorrect = false;
        } else {
            fatLayout.setError(null);
        }

        if (Objects.requireNonNull(carbs.getText()).toString().equals("")) {
            carbsLayout.setError("Required field!");
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
//    @SuppressLint("NonConstantResourceId")
//    public boolean onNavItemSelected(MenuItem menuItem) {
//        Intent intent;
//        Toast toast;
//        // Handle item selection
//        switch (menuItem.getItemId()) {
//            case R.id.main:
//                drawerLayout.closeDrawers();
//                break;
//            case R.id.products:
//                intent = new Intent(this, ProductsActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.newDish:
//                intent = new Intent(this, RecyclerActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.newProduct:
//                intent = new Intent(this, AddProductActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.dailyMenu:
//                intent = new Intent(this, TodayActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.userSettings:
//                intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.login:
//                intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.register:
//                intent = new Intent(this, RegisterActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.profile:
//                if (!sessionManager.isLogged) {
//                    intent = new Intent(this, LoginActivity.class);
//                } else {
//                    intent = new Intent(this, ProfileActivity.class);
//                }
//                startActivity(intent);
//                break;
//            case R.id.logout:
//                sessionManager = SessionManager.getInstance(this);
//                String message = "See you later!";
//                sessionManager.logout();
//                toast = Toast.makeText(getApplicationContext(),
//                        "You have been signed out successfully", Toast.LENGTH_LONG);
//                toast.show();
//                drawerLayout.closeDrawers();
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}