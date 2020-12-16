package com.example.caloriescounter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.toolbox.NetworkImageView;

import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;
    private ImageRequester imageRequester;
    private NetworkImageView editImage;
    private ActionBarDrawerToggle mToggle;
    private final String BASE_URL = NetworkService.getBaseUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        imageRequester = ImageRequester.getInstance();
        editImage = findViewById(R.id.chooseImage);
        imageRequester.setImageFromUrl(editImage, BASE_URL + "/images/testAvatarHen.jpg");//
    }

    public void btnRegistration(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void btnLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickList(View v) {
        Intent intent = new Intent(this, CreateNewDishActivity.class);
        startActivity(intent);
    }

    public void onClickAdd(View v) {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }

//    public void btnAddNewDish(View v) {
//        Intent intent = new Intent(this, CreateNewDishActivity.class);
//        startActivity(intent);
//    }


    public void btnAddNewDish(View v) {
        Intent intent = new Intent(this, RecyclerActivity.class);
        startActivity(intent);
    }

//    public void btnAddNewDish(View v) {
//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);
//    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavItemSelected(MenuItem menuItem) {
        Intent intent;
        Toast toast;
        // Handle item selection
        switch (menuItem.getItemId()) {
            case R.id.main:
                drawerLayout.closeDrawers();
                break;
            case R.id.categories_v1:
//                intent = new Intent(this, CategoriesActivity.class);
//                startActivity(intent);
                break;
            case R.id.categories_v2:
//                intent = new Intent(this, CategoriesRecyclerActivity.class);
//                startActivity(intent);
                break;
            case R.id.search:
                toast = Toast.makeText(getApplicationContext(),
                        "You have clicked SEARCH", Toast.LENGTH_LONG);
                toast.show();
                drawerLayout.closeDrawers();
                break;
            case R.id.login:
//                intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
                break;
            case R.id.register:
//                intent = new Intent(this, RegisterActivity.class);
//                startActivity(intent);
                break;
            case R.id.profile:
//                if (!sessionManager.isLogged) {
//                    intent = new Intent(this, LoginActivity.class);
//                } else {
//                    intent = new Intent(this, ProfileActivity.class);
//                }
//                startActivity(intent);
                break;
            case R.id.logout:
                sessionManager = SessionManager.getInstance(this);
                String message = "See you later!";
//                textView.setText(message);
                sessionManager.logout();
                toast = Toast.makeText(getApplicationContext(),
                        "You have been signed out successfully", Toast.LENGTH_LONG);
                toast.show();
                drawerLayout.closeDrawers();
                break;
            case R.id.upload:
//                intent = new Intent(this, UploaderActivity.class);
//                startActivity(intent);
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