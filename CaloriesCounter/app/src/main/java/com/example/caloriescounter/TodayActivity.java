package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.DailyMenuRecyclerAdapter;
import com.example.caloriescounter.adapters.VitaminsDailyRecyclerAdapter;
import com.example.caloriescounter.click_listeners.OnChangeDailyVitaminsListener;
import com.example.caloriescounter.click_listeners.OnDeleteListenerDailyMenu;
import com.example.caloriescounter.models.AddUserDailyWeightViewModel;
import com.example.caloriescounter.models.DailyMenuView;
import com.example.caloriescounter.models.ProgressTextView;
import com.example.caloriescounter.models.RemoveDailyView;
import com.example.caloriescounter.models.UserSettingsView;
import com.example.caloriescounter.models.UserVitaminsDailyView;
import com.example.caloriescounter.models.VitaminDailyCheckView;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayActivity extends BaseActivity implements OnDeleteListenerDailyMenu, OnChangeDailyVitaminsListener {

    private SessionManager sessionManager;
    private RecyclerView recyclerViewMenu;
    private RecyclerView recyclerViewVitamins;
    private List<DailyMenuView> dailyMenu;
    private DailyMenuRecyclerAdapter adapter;
    private UserSettingsView userSettings;
    private TextView myMenu, myVitamins, txtTodayWeight;
    private List<UserVitaminsDailyView> userVitamins;
    VitaminsDailyRecyclerAdapter adapterVitDaily;
    private AddUserDailyWeightViewModel weightUser;

    private final Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatDate = new SimpleDateFormat("d MMM yyyy", new Locale("uk", "UA"));

    ProgressTextView progressCalories;
    ProgressTextView progressFat;
    ProgressTextView progressCarbs;
    ProgressTextView progressProtein;

    private FloatingActionButton fab;
    final Context context = this;

    private TextView txtDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_today);
        this.getSupportActionBar().setTitle("Меню на сьогодні");

        sessionManager = SessionManager.getInstance(this);

        recyclerViewMenu = findViewById(R.id.recycler_view);
        recyclerViewVitamins = findViewById(R.id.recycler_view_vit);
        txtDate = findViewById(R.id.dateAct);
        txtTodayWeight = findViewById(R.id.txtTodayWeight);
        progressCalories = (ProgressTextView) findViewById(R.id.progressCalories);
        progressFat = (ProgressTextView) findViewById(R.id.progressFat);
        progressCarbs = (ProgressTextView) findViewById(R.id.progressCarbs);
        progressProtein = (ProgressTextView) findViewById(R.id.progressProtein);
        fab = findViewById(R.id.floating_action_button);
        myMenu = findViewById(R.id.myMenu);
        myVitamins = findViewById(R.id.myVitamins);


        if (!sessionManager.isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        Date currentTime = Calendar.getInstance().getTime();

        txtDate.setText(formatDate.format(currentTime));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });


        txtTodayWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdiDdailyWeight(v);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodayActivity.this, ChooseProductActivity.class);
                finish();
                startActivity(intent);
            }
        });

        myVitamins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerViewVitamins.setVisibility(recyclerViewVitamins.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                myVitamins.setText(recyclerViewVitamins.getVisibility() == View.VISIBLE ? "Мої вітаміни" : "Показати вітаміни");

            }
        });

        myMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMenu.setVisibility(recyclerViewMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                myMenu.setText(recyclerViewMenu.getVisibility() == View.VISIBLE ? "Моє меню" : "Показати меню");

            }
        });

        setUserData();
        setRecyclerViewMenu();
        loadListProducts();
        setRecyclerViewVitamins();

    }

    private void setRecyclerViewMenu() {
        recyclerViewMenu.setHasFixedSize(true);
        recyclerViewMenu.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        dailyMenu = new ArrayList<>();
        adapter = new DailyMenuRecyclerAdapter(dailyMenu, this, this);
        recyclerViewMenu.setAdapter(adapter);
    }

    @Override
    public void deleteItem(final RemoveDailyView product, final DailyMenuView model) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Видалення")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dailyMenu.remove(model);
                        NetworkService.getInstance()
                                .getJSONApi()
                                .removeDailyProduct(product)
                                .enqueue(new Callback<List<DailyMenuView>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<List<DailyMenuView>> call, @NonNull Response<List<DailyMenuView>> response) {
                                        CommonUtils.hideLoading();
                                        if (response.errorBody() == null && response.isSuccessful()) {
                                            assert response.body() != null;
                                            loadListProducts();
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
                                    public void onFailure(@NonNull Call<List<DailyMenuView>> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Помилка з'єднання";
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

    public void loadListProducts() {

        NetworkService.getInstance()
                .getJSONApi()
                .getProductsDailyMenu(calendar.getTime())
                .enqueue(new Callback<List<DailyMenuView>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DailyMenuView>> call, @NonNull Response<List<DailyMenuView>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            if (dailyMenu != null)
                                dailyMenu.clear();
                            dailyMenu.addAll(0, response.body());

                            double totalCalories = 0;
                            double totalFat = 0;
                            double totalProtein = 0;
                            double totalCarbs = 0;

                            for (DailyMenuView item : dailyMenu) {
                                 totalCalories += item.getProductCalories();
                                totalFat += item.getProductFat();
                                totalProtein += item.getProductProtein();
                                totalCarbs += item.getProductCarbohydrate();
                           }
                            progressCalories.setValue((int) totalCalories);
                            progressCalories.setTextColor(Color.parseColor("#000000"));
                            progressFat.setValue((int) totalFat);
                            progressFat.setTextColor(Color.parseColor("#000000"));
                            progressCarbs.setValue((int) totalCarbs);
                            progressCarbs.setTextColor(Color.parseColor("#000000"));
                            progressProtein.setValue((int) totalProtein);
                            progressProtein.setTextColor(Color.parseColor("#000000"));
                            getUserVitamins();
                            adapter.notifyDataSetChanged();
                        } else {
                            dailyMenu = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<DailyMenuView>> call, @NonNull Throwable t) {

                        dailyMenu = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    public void setUserData() {
        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .settings()
                .enqueue(new Callback<UserSettingsView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<UserSettingsView> call, @NonNull Response<UserSettingsView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            userSettings = response.body();
                            progressCalories.setMaxValue((int) userSettings.getUserCalories());
                            progressCarbs.setMaxValue((int) userSettings.getUserCarbohydrate());
                            progressProtein.setMaxValue((int) userSettings.getUserProtein());
                            progressFat.setMaxValue((int) userSettings.getUserFat());
                        } else {
                            userSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserSettingsView> call, @NonNull Throwable t) {

                        userSettings = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });

    }

    private void showDatePicker(View view) {
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) TodayActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog datePicker = new DatePickerDialog(TodayActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int yearSelect, int monthSelect, int daySelect) {
                        calendar.set(yearSelect, monthSelect, daySelect);
                        Date currentTime = calendar.getTime();
                        txtDate.setText(formatDate.format(currentTime));
                        loadListProducts();
                    }
                }, year, month, day);
        datePicker.show();
        loadListProducts();
    }

    public void onClickPreviousDate(View view) {
        calendar.add(Calendar.DATE, -1);
        Date currentTime = calendar.getTime();
        txtDate.setText(formatDate.format(currentTime));
        loadListProducts();
    }

    public void onClickNextDate(View view) {
        calendar.add(Calendar.DATE, 1);
        Date currentTime = calendar.getTime();
        txtDate.setText(formatDate.format(currentTime));
        loadListProducts();
    }

    public void getUserVitamins() {
        NetworkService.getInstance()
                .getJSONApi()
                .getDailyVitamins(calendar.getTime())
                .enqueue(new Callback<List<UserVitaminsDailyView>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<UserVitaminsDailyView>> call, @NonNull Response<List<UserVitaminsDailyView>> response) {
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            if (userVitamins != null)
                                userVitamins.clear();
                            if (response.body() != null)
                                userVitamins.addAll(0, response.body());
                            adapterVitDaily.notifyDataSetChanged();
                            GetDailyWeight();
                        } else {
                            userVitamins = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<UserVitaminsDailyView>> call, @NonNull Throwable t) {

                        userVitamins = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    private void setRecyclerViewVitamins() {
        recyclerViewVitamins.setHasFixedSize(true);
        recyclerViewVitamins.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        userVitamins = new ArrayList<>();
        adapterVitDaily = new VitaminsDailyRecyclerAdapter(userVitamins, this, this);
        recyclerViewVitamins.setAdapter(adapterVitDaily);
    }

    @Override
    public void checkVitamin(final UserVitaminsDailyView vitamin) {
        VitaminDailyCheckView model = new VitaminDailyCheckView();
        model.setDateOfVitamin(vitamin.getDateOfVitamin());
        model.setVitaminId(vitamin.getVitaminId());
        model.setTaken(!vitamin.isTaken());
        vitamin.setTaken(!vitamin.isTaken());

        NetworkService.getInstance()
                .getJSONApi()
                .checkDailyVitamin(model)
                .enqueue(new Callback<VitaminDailyCheckView>() {
                    @Override
                    public void onResponse(@NonNull Call<VitaminDailyCheckView> call, @NonNull Response<VitaminDailyCheckView> response) {
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            adapter.notifyDataSetChanged();
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
                    public void onFailure(@NonNull Call<VitaminDailyCheckView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    public void GetDailyWeight() {
        NetworkService.getInstance()
                .getJSONApi()
                .getUserWeight(calendar.getTime())
                .enqueue(new Callback<AddUserDailyWeightViewModel>() {
                    @Override
                    public void onResponse(@NonNull Call<AddUserDailyWeightViewModel> call, @NonNull Response<AddUserDailyWeightViewModel> response) {
                        if (response.errorBody() == null && response.isSuccessful()) {
                            weightUser = response.body();
                            txtTodayWeight.setText(Double.toString(weightUser.getWeight()));
                        } else {
                            String errorMessage;
                            try {
                                assert response.errorBody() != null;
                                errorMessage = response.errorBody().string();
                            } catch (IOException e) {
                                errorMessage = response.message();
                                e.printStackTrace();
                            }
                            weightUser = null;
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    errorMessage, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AddUserDailyWeightViewModel> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        weightUser = null;
                        t.printStackTrace();
                    }
                });
    }

    public void EdiDdailyWeight(View view) {
        AddUserDailyWeightViewModel model = new AddUserDailyWeightViewModel();
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt_usewweight, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.inputWeight);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                model.setWeight(Double.parseDouble(userInput.getText().toString()));
                                model.setDateOfWeight(calendar.getTime());
                                NetworkService.getInstance()
                                        .getJSONApi()
                                        .editDailyWeight(model)
                                        .enqueue(new Callback<AddUserDailyWeightViewModel>() {
                                            @Override
                                            public void onResponse(@NonNull Call<AddUserDailyWeightViewModel> call, @NonNull Response<AddUserDailyWeightViewModel> response) {
                                                if (response.errorBody() == null && response.isSuccessful()) {
                                                    weightUser = response.body();
                                                    txtTodayWeight.setText(Double.toString(weightUser.getWeight()));
                                                } else {
                                                    String errorMessage;
                                                    try {
                                                        assert response.errorBody() != null;
                                                        errorMessage = response.errorBody().string();
                                                    } catch (IOException e) {
                                                        errorMessage = response.message();
                                                        e.printStackTrace();
                                                    }
                                                    weightUser = null;
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            errorMessage, Toast.LENGTH_LONG);
                                                    toast.show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<AddUserDailyWeightViewModel> call, @NonNull Throwable t) {
                                                CommonUtils.hideLoading();
                                                String error = "Помилка з'єднання";
                                                Toast toast = Toast.makeText(getApplicationContext(),
                                                        error, Toast.LENGTH_LONG);
                                                toast.show();
                                                weightUser = null;
                                                t.printStackTrace();
                                            }
                                        });
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }
}