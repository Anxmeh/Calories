package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.network.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    protected Context mContext;
    private SessionManager sessionManager;
    TextView txtTitle;
    Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  setContentView(R.layout.activity_base);

        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        setContentView(R.layout.activity_base);

        homeToolbar = findViewById(R.id.home_toolbar);
       // homeToolbar.setTitle("Меню на сьогодні");
        setSupportActionBar(homeToolbar);

        txtTitle = findViewById(R.id.title_home);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.bringToFront();
        View headerView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) headerView.findViewById(R.id.header);
        nav_user.setText("NEWHEADER");
        //txtTitle.setText("Hohoh");
        sessionManager = SessionManager.getInstance(this);

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

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

    }

    public void addContentView(int layoutId) {
        //homeToolbar.setTitle("Меню на сьогодні");
        //setSupportActionBar(homeToolbar);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layoutId, null, false);
        drawerLayout.addView(contentView, 0);
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
            case R.id.dailyWater:
                intent = new Intent(this, WaterActivity.class);
                startActivity(intent);
                break;
            case R.id.userSettings:
                intent = new Intent(this, SettingsActivity.class);
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
               // textView.setText(message);
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