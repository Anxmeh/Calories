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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.CustomAdapterSpinner;
import com.example.caloriescounter.adapters.VitaminsRecyclerAdapter;
import com.example.caloriescounter.click_listeners.OnChangeAmountVitamins;
import com.example.caloriescounter.click_listeners.OnDeleteListenerVitamins;
import com.example.caloriescounter.models.AddVitaminView;
import com.example.caloriescounter.models.UserVitaminsView;
import com.example.caloriescounter.models.Vitamin;
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

public class VitaminSettingsActivity extends BaseActivity implements OnDeleteListenerVitamins, OnChangeAmountVitamins {

    private static final String TAG = VitaminSettingsActivity.class.getSimpleName();
    private FloatingActionButton fab;
    private CustomAdapterSpinner spinnerAdapter;
    private Spinner spinnerVitamins;
    private List<Vitamin> vitamins;
    private List<UserVitaminsView> userVitamins;
    private UserVitaminsView userVitamin;
    private VitaminsRecyclerAdapter adapter;
    private RecyclerView recyclerViewVitamins;
    final Context context = this;
    private int amount = 0;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_vitamin_settings);
        this.getSupportActionBar().setTitle("Мої вітаміни");

        fab = findViewById(R.id.floating_action_button);
        recyclerViewVitamins = findViewById(R.id.recycler_view);
        sessionManager = SessionManager.getInstance(this);
        sessionManager = SessionManager.getInstance(this);
        if (!sessionManager.isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        setRecyclerViewVitamins();
        getUserVitamins();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builderDialig = new MaterialAlertDialogBuilder(VitaminSettingsActivity.this);
                spinnerVitamins = new Spinner(VitaminSettingsActivity.this);
                if (spinnerVitamins.getParent() != null) {
                    ((ViewGroup) spinnerVitamins.getParent()).removeView(spinnerVitamins);
                }
                builderDialig.setView(spinnerVitamins);
                builderDialig.setTitle("Оберіть зі списку");
                builderDialig.create().show();
                spinnerAdapter = new CustomAdapterSpinner(VitaminSettingsActivity.this,
                        android.R.layout.simple_spinner_item, vitamins);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerVitamins.setAdapter(spinnerAdapter);
                spinnerVitamins.post(new Runnable() {
                    @Override
                    public void run() {
                        spinnerVitamins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                CustomAdapterSpinner.flag = true;
                                Vitamin current = (Vitamin) spinnerVitamins.getSelectedItem();
                                UserVitaminsView addVitaminModel = new UserVitaminsView();
                                addVitaminModel.setVitaminId(current.getId());
                                addVitaminModel.setAmount(0);
                                addVitaminModel.setVitaminName(current.getVitaminName());
                                addVitamin(addVitaminModel);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vitamin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt_name, null);
                android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(context);
                mDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.inputVitaminName);

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        final AddVitaminView vitaminModel = new AddVitaminView();
                                        vitaminModel.setVitaminName(userInput.getText().toString());

                                        CommonUtils.showLoading(VitaminSettingsActivity.this);
                                        NetworkService.getInstance()
                                                .getJSONApi()
                                                .addVitamin(vitaminModel)
                                                .enqueue(new Callback<List<Vitamin>>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<List<Vitamin>> call, @NonNull Response<List<Vitamin>> response) {
                                                        CommonUtils.hideLoading();
                                                        if (response.errorBody() == null && response.isSuccessful()) {
                                                            assert response.body() != null;
                                                            getVitaminsList();
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
                                                    public void onFailure(@NonNull Call<List<Vitamin>> call, @NonNull Throwable t) {
                                                        CommonUtils.hideLoading();
                                                        String error = "Посмилка з'єднання";
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getVitaminsList() {
        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .getAllVitamins()
                .enqueue(new Callback<List<Vitamin>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Vitamin>> call, @NonNull Response<List<Vitamin>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            vitamins = response.body();
                        } else {
                            vitamins = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Vitamin>> call, @NonNull Throwable t) {
                        vitamins = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    public void getUserVitamins() {
        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .getUserVitamins()
                .enqueue(new Callback<List<UserVitaminsView>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<UserVitaminsView>> call, @NonNull Response<List<UserVitaminsView>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            if (userVitamins != null)
                                userVitamins.clear();
                            if (response.body() != null)
                                userVitamins.addAll(0, response.body());
                            adapter.notifyDataSetChanged();
                            getVitaminsList();
                        } else {
                            userVitamins = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<UserVitaminsView>> call, @NonNull Throwable t) {
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

    public void addVitamin(UserVitaminsView model) {
        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .addUserVitamin(model)
                .enqueue(new Callback<UserVitaminsView>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVitaminsView> call, @NonNull Response<UserVitaminsView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            userVitamin = response.body();
                            userVitamins.add(userVitamin);
                            adapter.notifyDataSetChanged();
                        } else {
                            vitamins = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVitaminsView> call, @NonNull Throwable t) {
                        userVitamin = null;
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
        adapter = new VitaminsRecyclerAdapter(userVitamins, this, this, this);

        recyclerViewVitamins.setAdapter(adapter);
    }

    @Override
    public void deleteItem(final UserVitaminsView vitamin) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Видалення")
                .setMessage("Ви дійсно бажаєте видалити \"" + vitamin.getVitaminName() + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userVitamins.remove(vitamin);
                        adapter.notifyDataSetChanged();
                        NetworkService.getInstance()
                                .getJSONApi()
                                .removeUserVitamin(vitamin.getVitaminId())
                                .enqueue(new Callback<List<UserVitaminsView>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<List<UserVitaminsView>> call, @NonNull Response<List<UserVitaminsView>> response) {
                                        CommonUtils.hideLoading();
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
                                    public void onFailure(@NonNull Call<List<UserVitaminsView>> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Помилка з'єднання";
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                error, Toast.LENGTH_LONG);
                                        toast.show();
                                        t.printStackTrace();
                                    }
                                });
                    }
                })
                .show();
    }

    @Override
    public void changeItem(final UserVitaminsView vitamin) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);
        android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.inputWeight);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    amount = Integer.parseInt(userInput.getText().toString());
                                } catch (NumberFormatException nfe) {
                                    Log.e(TAG, "Could not parse " + nfe);
                                }
                                vitamin.setAmount(amount);

                                CommonUtils.showLoading(context);
                                NetworkService.getInstance()
                                        .getJSONApi()
                                        .changeUserVitamin(vitamin)
                                        .enqueue(new Callback<UserVitaminsView>() {
                                            @Override
                                            public void onResponse(@NonNull Call<UserVitaminsView> call, @NonNull Response<UserVitaminsView> response) {
                                                CommonUtils.hideLoading();
                                                if (response.errorBody() == null && response.isSuccessful()) {
                                                    assert response.body() != null;
                                                    userVitamin = response.body();
                                                    getUserVitamins();
                                                } else {
                                                    vitamins = null;
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<UserVitaminsView> call, @NonNull Throwable t) {
                                                userVitamin = null;
                                                CommonUtils.hideLoading();
                                                String error = "Помилка з'єднання";
                                                Toast toast = Toast.makeText(getApplicationContext(),
                                                        error, Toast.LENGTH_LONG);
                                                toast.show();
                                                t.printStackTrace();
                                            }
                                        });
                            }
                        }).show();
    }

    @Override
    public void addItem(final UserVitaminsView vitamin) {

        int newAmount = vitamin.getAmount() + 1;
        vitamin.setAmount(newAmount);

        CommonUtils.showLoading(context);
        NetworkService.getInstance()
                .getJSONApi()
                .changeUserVitamin(vitamin)
                .enqueue(new Callback<UserVitaminsView>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVitaminsView> call, @NonNull Response<UserVitaminsView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            userVitamin = response.body();
                            adapter.notifyDataSetChanged();
                        } else {
                            vitamins = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVitaminsView> call, @NonNull Throwable t) {
                        userVitamin = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void removeItem(final UserVitaminsView vitamin) {
        int newAmount;
        newAmount = vitamin.getAmount() > 0 ? vitamin.getAmount() - 1 : 0;
        vitamin.setAmount(newAmount);
        CommonUtils.showLoading(context);
        NetworkService.getInstance()
                .getJSONApi()
                .changeUserVitamin(vitamin)
                .enqueue(new Callback<UserVitaminsView>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVitaminsView> call, @NonNull Response<UserVitaminsView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            userVitamin = response.body();
                            adapter.notifyDataSetChanged();
                        } else {
                            vitamins = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVitaminsView> call, @NonNull Throwable t) {
                        userVitamin = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }
}