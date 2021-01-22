package com.example.caloriescounter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.toolbox.NetworkImageView;

import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
//import com.example.caloriescounter.network.utils.MyWorker;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;

public class MainActivity extends BaseActivity {
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;
    private TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        super.addContentView(R.layout.activity_main);
        this.getSupportActionBar().setTitle("Головна");
       // setContentView(R.layout.activity_main);

//        Toolbar homeToolbar = findViewById(R.id.home_toolbar);
//        setSupportActionBar(homeToolbar);
//
//        ImageRequester imageRequester = ImageRequester.getInstance();
//        NetworkImageView editImage = findViewById(R.id.chooseImage);
//        imageRequester.setImageFromUrl(editImage, NetworkService.getBaseUrl() + "/images/testAvatarHen.jpg");
//        sessionManager = SessionManager.getInstance(this);
//        textView = findViewById(R.id.textMainActivity);
//
//
//        if (sessionManager.isLogged) {
////            String message = "Hello, dear friend! Nice to see you again!";
////            textView.setText(message);
//        }
//
//        drawerLayout = findViewById(R.id.drawerLayout);
//        NavigationView navigationView = findViewById(R.id.navigation);
//        navigationView.bringToFront();
//        View headerView = navigationView.getHeaderView(0);
//        TextView nav_user = (TextView)headerView.findViewById(R.id.header);
//        nav_user.setText("NEWHEADER");
//
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


        //OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
       // WorkManager.getInstance().enqueue(myWorkRequest);



        ////////////////
      //PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 16, TimeUnit.MINUTES).build();
//
       // WorkManager.getInstance(getApplicationContext()).enqueue(myWorkRequest);

        ///////////////////




       // PeriodicWorkRequest worker = new PeriodicWorkRequest.Builder(MyWorker.class, MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS).setConstraints(Constraints.NONE).build();
       // WorkManager.getInstance(getApplicationContext()).enqueue(worker);
//        PeriodicWorkRequest worker = new PeriodicWorkRequest.Builder(TestWorker.class, MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS).setConstraints(Constraints.NONE).addTag(TAG_PERIODIC_WORK_REQUEST).build();
//        WorkManager.getInstance().enqueue(worker);



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
//            case R.id.dailyWater:
//                intent = new Intent(this, WaterActivity.class);
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
//                textView.setText(message);
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
