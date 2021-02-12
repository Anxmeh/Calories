package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    private UserView userProfile;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    protected Context mContext;
    private SessionManager sessionManager;
    TextView txtTitle;
    Toolbar homeToolbar;
    private GoogleSignInClient mGoogleSignInClient;

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
        TextView nav_email = (TextView) headerView.findViewById(R.id.subheader);
       // nav_user.setText("NEWHEADER");

        //txtTitle.setText("Hohoh");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestIdToken("951922290898-kgfog8u4i0q0qo8ms93817n7aejv746c.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        if (sessionManager.isLogged) {
            String  login = sessionManager.fetchUserLogin();
            nav_user.setText(login);
            String  name = sessionManager.fetchUserName();
            nav_email.setText(name);

//            CommonUtils.showLoading(this);
//            NetworkService.getInstance()
//                    .getJSONApi()
//                    .profile()
//                    .enqueue(new Callback<UserView>() {
//                        @SuppressLint("SetTextI18n")
//                        @Override
//                        public void onResponse(@NonNull Call<UserView> call, @NonNull Response<UserView> response) {
//                            CommonUtils.hideLoading();
//                            if (response.errorBody() == null && response.isSuccessful()) {
//                                assert response.body() != null;
//                                userProfile = response.body();
//                                nav_user.setText(userProfile.getName());
//                                nav_email.setText(userProfile.getEmail());
//
//                            } else {
//                                userProfile = null;
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(@NonNull Call<UserView> call, @NonNull Throwable t) {
//                            CommonUtils.hideLoading();
//                            userProfile = null;
//                            t.printStackTrace();
//                        }
//                    });
        }
        else {
            nav_user.setText("Привіт, Гість");
            nav_email.setText("");
        }

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
                mGoogleSignInClient.signOut();

                NetworkService.getInstance()
                        .getJSONApi()
                        .signout()
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                CommonUtils.hideLoading();
                                if (response.errorBody() == null && response.isSuccessful()) {
                                 //   assert response.body() != null;
                                  //  addedProduct = response.body();

                                    String succeed = "Успішно";
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
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            errorMessage, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                CommonUtils.hideLoading();
                                String error = "Error occurred while getting request!";
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        error, Toast.LENGTH_LONG);
                                toast.show();
                                t.printStackTrace();
                            }
                        });








               // drawerLayout.closeDrawers();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.choose:
                intent = new Intent(this, VitaminSettingsActivity.class);
                startActivity(intent);
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