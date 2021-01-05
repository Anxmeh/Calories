package com.example.caloriescounter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.toolbox.NetworkImageView;

import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        ImageRequester imageRequester = ImageRequester.getInstance();
        NetworkImageView editImage = findViewById(R.id.chooseImage);
        imageRequester.setImageFromUrl(editImage, NetworkService.getBaseUrl() + "/images/testAvatarHen.jpg");
        sessionManager = SessionManager.getInstance(this);
        textView = findViewById(R.id.textMainActivity);

        if (sessionManager.isLogged) {
            String message = "Hello, dear friend! Nice to see you again!";
            textView.setText(message);
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.bringToFront();

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onNavItemSelected(item);
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavItemSelected(MenuItem menuItem) {
        Intent intent;
        Toast toast;
        // Handle item selection
        switch (menuItem.getItemId()) {
            case R.id.main:
                drawerLayout.closeDrawers();
                break;
            case R.id.products:
                intent = new Intent(this, ProductsActivity.class);
                startActivity(intent);
                break;
            case R.id.newDish:
                intent = new Intent(this, RecyclerActivity.class);
                startActivity(intent);
                break;
            case R.id.newProduct:
                intent = new Intent(this, AddProductActivity.class);
                startActivity(intent);
                break;
            case R.id.dailyMenu:
                intent = new Intent(this, TodayActivity.class);
                startActivity(intent);
                break;
            case R.id.click:
                intent = new Intent(this, ClickedProductActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.profile:
                if (!sessionManager.isLogged) {
                    intent = new Intent(this, LoginActivity.class);
                } else {
                    intent = new Intent(this, ProfileActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.logout:
                sessionManager = SessionManager.getInstance(this);
                String message = "See you later!";
                textView.setText(message);
                sessionManager.logout();
                toast = Toast.makeText(getApplicationContext(),
                        "You have been signed out successfully", Toast.LENGTH_LONG);
                toast.show();
                drawerLayout.closeDrawers();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
//public class MainActivity extends AppCompatActivity {
//    private DrawerLayout drawerLayout;
//    private SessionManager sessionManager;
//    private ImageRequester imageRequester;
//    private NetworkImageView editImage;
//    private ActionBarDrawerToggle mToggle;
//    private final String BASE_URL = NetworkService.getBaseUrl();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//
//
//
//
//        imageRequester = ImageRequester.getInstance();
//        editImage = findViewById(R.id.chooseImage);
//        imageRequester.setImageFromUrl(editImage, BASE_URL + "/images/testAvatarHen.jpg");//
//    }
//
//    public void btnRegistration(View v) {
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
//    }
//
//    public void btnLogin(View v) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }
//
//    public void onClickList(View v) {
//        Intent intent = new Intent(this, CreateNewDishActivity.class);
//        startActivity(intent);
//    }
//
//    public void onClickAdd(View v) {
//        Intent intent = new Intent(this, AddProductActivity.class);
//        startActivity(intent);
//    }
//
////    public void btnAddNewDish(View v) {
////        Intent intent = new Intent(this, CreateNewDishActivity.class);
////        startActivity(intent);
////    }
//
//
//    public void btnAddNewDish(View v) {
//        Intent intent = new Intent(this, RecyclerActivity.class);
//        startActivity(intent);
//    }
//
////    public void btnAddNewDish(View v) {
////        Intent intent = new Intent(this, TestActivity.class);
////        startActivity(intent);
////    }
//
//    @SuppressLint("NonConstantResourceId")
//    public boolean onNavItemSelected(MenuItem menuItem) {
//        Intent intent;
//        Toast toast;
//        // Handle item selection
//        switch (menuItem.getItemId()) {
//            case R.id.main:
//                drawerLayout.closeDrawers();
//                break;
//            case R.id.categories_v1:
////                intent = new Intent(this, CategoriesActivity.class);
////                startActivity(intent);
//                break;
//            case R.id.categories_v2:
////                intent = new Intent(this, CategoriesRecyclerActivity.class);
////                startActivity(intent);
//                break;
//            case R.id.search:
//                toast = Toast.makeText(getApplicationContext(),
//                        "You have clicked SEARCH", Toast.LENGTH_LONG);
//                toast.show();
//                drawerLayout.closeDrawers();
//                break;
//            case R.id.login:
////                intent = new Intent(this, LoginActivity.class);
////                startActivity(intent);
//                break;
//            case R.id.register:
////                intent = new Intent(this, RegisterActivity.class);
////                startActivity(intent);
//                break;
//            case R.id.profile:
////                if (!sessionManager.isLogged) {
////                    intent = new Intent(this, LoginActivity.class);
////                } else {
////                    intent = new Intent(this, ProfileActivity.class);
////                }
////                startActivity(intent);
//                break;
//            case R.id.logout:
//                sessionManager = SessionManager.getInstance(this);
//                String message = "See you later!";
////                textView.setText(message);
//                sessionManager.logout();
//                toast = Toast.makeText(getApplicationContext(),
//                        "You have been signed out successfully", Toast.LENGTH_LONG);
//                toast.show();
//                drawerLayout.closeDrawers();
//                break;
//            case R.id.upload:
////                intent = new Intent(this, UploaderActivity.class);
////                startActivity(intent);
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
//
//}