package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.caloriescounter.adapters.ProductListRecyclerAdapter;
import com.example.caloriescounter.adapters.VitaminsRecyclerAdapter;
import com.example.caloriescounter.click_listeners.OnChangeAmountVitamins;
import com.example.caloriescounter.click_listeners.OnDeleteListenerVitamins;
import com.example.caloriescounter.models.AddVitaminView;
import com.example.caloriescounter.models.DailyMenuView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.UserVitaminsView;
import com.example.caloriescounter.models.Vitamin;
import com.example.caloriescounter.network.NetworkService;
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
    private TextView tx;
    private EditText et1;
    private FloatingActionButton fab;
    private ArrayAdapter adp;
    private CustomAdapterSpinner adp1;
    private Spinner sp;
    private Spinner sp2;
    LinearLayout view;
    String[] s = {"India1 ", "Arica1", "India2 ", "Arica2", "India3 ", "Arica3",
            "India ", "Arica", "India ", "Arica"};
    private List<Vitamin> vitamins;
    private List<UserVitaminsView> userVitamins;
    private UserVitaminsView userVitamin;
    VitaminsRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    final Context context = this;
    private int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_vitamin_settings);
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Мої вітаміни");

        fab = findViewById(R.id.floating_action_button);
        recyclerView = findViewById(R.id.recycler_view);
        //   getVitaminsList();
        setRecyclerView();
        getUserVitamins();


//        CommonUtils.showLoading(this);
//        NetworkService.getInstance()
//                .getJSONApi()
//                .getUserVitamins()
//                .enqueue(new Callback<List<UserVitaminsView>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<UserVitaminsView>> call, @NonNull Response<List<UserVitaminsView>> response) {
//                        CommonUtils.hideLoading();
//                        if (response.errorBody() == null && response.isSuccessful()) {
//                            assert response.body() != null;
//                            if (userVitamins != null)
//                                userVitamins.clear();
//                            if (response.body() != null)
//                            userVitamins.addAll(0, response.body());
//                            adapter.notifyDataSetChanged();
//                            getVitaminsList();
//                        } else {
//                            userVitamins = null;
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<UserVitaminsView>> call, @NonNull Throwable t) {
//                        CommonUtils.hideLoading();
//                        userVitamins = null;
//                        t.printStackTrace();
//                    }
//                });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   AlertDialog.Builder builder = new AlertDialog.Builder(VitaminSettingsActivity.this);
                MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(VitaminSettingsActivity.this);
                // view = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog, null);
// устанавливаем ее, как содержимое тела диалога
                // view.addView(sp);
//                if(builder.getParent() != null) {
//                    ((ViewGroup)tv.getParent()).removeView(tv);} // <- fix
//                builder.removeView();


                sp = new Spinner(VitaminSettingsActivity.this);


                if (sp.getParent() != null) {
                    ((ViewGroup) sp.getParent()).removeView(sp);
                }
                builder1.setView(sp);
                builder1.setTitle("Оберіть зі списку");
                //   final AlertDialog alertd = builder.create();
                // CustomAdapterSpinner.flag = false;

                // builder.setView(sp);
                // alertd.show();
                builder1.create().show();
////////////////
                adp1 = new CustomAdapterSpinner(VitaminSettingsActivity.this,
                        android.R.layout.simple_spinner_item, vitamins);
                // tx = (TextView) findViewById(R.id.txt1);

                //  sp = new Spinner(VitaminSettingsActivity.this);
                // String str = et1.getText().toString();
                //  sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(adp1);
                sp.post(new Runnable() {
                    @Override
                    public void run() {

                        //  AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                CustomAdapterSpinner.flag = true;
                                // Получаем выбранный объект
                                // Vitamin current = (Vitamin) parent.getSelectedItem();
                                Vitamin current = (Vitamin) sp.getSelectedItem();
                                // tx.setText(current.toString());
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


//        final ArrayAdapter<String> adp = new ArrayAdapter<String>(VitaminSettingsActivity.this,
//                android.R.layout.simple_spinner_item, s);
//
//        tx= (TextView)findViewById(R.id.txt1);
//        final Spinner sp = new Spinner(VitaminSettingsActivity.this);
//        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        sp.setAdapter(adp);
//
//        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                // Получаем выбранный объект
//                String item = (String)parent.getItemAtPosition(position);
//                tx.setText(item);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        };
//        sp.setOnItemSelectedListener(itemSelectedListener);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(VitaminSettingsActivity.this);
//        builder.setView(sp);
//        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.vitamin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_add :
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt_name, null);

                //Создаем AlertDialog
                android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final EditText userInput = (EditText) promptsView.findViewById(R.id.inputVitaminName);

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

//
//
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


                                                            ///////////
//                                                                                Toast toast = Toast.makeText(getApplicationContext(),
//                                                                                        succeed, Toast.LENGTH_LONG);
//                                                                                toast.show();
//                                                                                Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
//                                                                                startActivity(intent);
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

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();
                //и отображаем его:
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

//                            adp = new ArrayAdapter(VitaminSettingsActivity.this,
//                                    android.R.layout.simple_spinner_item, vitamins);
//
//                            adp1 = new CustomAdapterSpinner(VitaminSettingsActivity.this,
//                                    android.R.layout.simple_spinner_item, vitamins);
//                            // tx = (TextView) findViewById(R.id.txt1);
//
//                            sp = new Spinner(VitaminSettingsActivity.this);
//                            // String str = et1.getText().toString();
//                            sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            sp.setAdapter(adp1);
//
//                            //  AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
//                            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    CustomAdapterSpinner.flag = true;
//                                    // Получаем выбранный объект
//                                    //String item = (String)parent.getItemAtPosition(position);
//
//                                    // Vitamin current = (Vitamin) parent.getSelectedItem();
//                                    Vitamin current = (Vitamin) sp.getSelectedItem();
//
//
//                                    // tx.setText(current.toString());
//                                    UserVitaminsView addVitaminModel = new UserVitaminsView();
//                                    addVitaminModel.setVitaminId(current.getId());
//                                    addVitaminModel.setAmount(0);
//                                    addVitaminModel.setVitaminName(current.getVitaminName());
//                                    addVitamin(addVitaminModel);
//
//
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//                                }
//                                //};
//
//                                //sp.setOnItemSelectedListener(itemSelectedListener);
//                            });

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

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        userVitamins = new ArrayList<>();
        adapter = new VitaminsRecyclerAdapter(userVitamins, this, this, this);

        recyclerView.setAdapter(adapter);

        int largePadding = 16;
        int smallPadding = 4;
        //  recyclerView.addItemDecoration(new CategoryGridItemDecoration(largePadding, smallPadding));
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
//
                        NetworkService.getInstance()
                                .getJSONApi()
                                .removeUserVitamin(vitamin.getVitaminId())
                                .enqueue(new Callback<List<UserVitaminsView>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<List<UserVitaminsView>> call, @NonNull Response<List<UserVitaminsView>> response) {
                                        CommonUtils.hideLoading();
                                        if (response.errorBody() == null && response.isSuccessful()) {
                                            assert response.body() != null;
                                        //    userVitamins = response.body();
//                                            userVitamins.remove(vitamin);
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
//                            Toast toast = Toast.makeText(getApplicationContext(),
//                                    errorMessage, Toast.LENGTH_LONG);
//                            toast.show();
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
                        //deleteConfirm(productEntry);
                    }

                })
                .show();

    }

    @Override
    public void changeItem(final UserVitaminsView vitamin) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);

        //Создаем AlertDialog
        android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(context);
        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);
        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText userInput = (EditText) promptsView.findViewById(R.id.inputWeight);
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
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

        int newAm = vitamin.getAmount() + 1;
        vitamin.setAmount(newAm);

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
                            //  getUserVitamins();
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
//        if (amount > 0)
//            amount--;
//        vitamin.setAmount(amount);
        int newAm = vitamin.getAmount() - 1;
        vitamin.setAmount(newAm);

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
                            //getUserVitamins();
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