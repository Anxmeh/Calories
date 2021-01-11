package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewDishActivity extends AppCompatActivity {

    //private GridView gridView;
    private ListView listView;
    private List<Product> products;
    EditText inputSearch;
    ProductAdapter customAdapter;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_dish);

        Toolbar homeToolbar = findViewById(R.id.home_toolbar);
        homeToolbar.setTitle("Меню на сьогодні");
        setSupportActionBar(homeToolbar);

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
        sessionManager = SessionManager.getInstance(this);


        final TextView addedprod = findViewById(R.id.tvListProducts);
        listView = findViewById(R.id.listViewProducts);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .getProducts()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            products = response.body();
                            final ProductAdapter adapter = new ProductAdapter(products, CreateNewDishActivity.this);
                            //customAdapter = new ProductAdapter(products, CreateNewDishActivity.this);

                            listView.setAdapter(adapter);
                            //listView.setAdapter(customAdapter);

                            inputSearch.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                    //Когда пользователь вводит какой-нибудь текст:
                                    adapter.getFilter().filter(cs);
                                   // CreateNewDishActivity.this.adapter.getFilter().filter(cs);

                                }

                                @Override
                                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                              int arg3) {
                                }

                                @Override
                                public void afterTextChanged(Editable arg0) {
                                }
                            });






                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewDishActivity.this);
                                    builder.setMessage("Are you sure you want to delete category")
                                            .setCancelable(false)
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Good you changed your mind!", Toast.LENGTH_LONG);
                                                    toast.show();
                                                }
                                            })
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Product product = products.get(position);
                                                    products.remove(product);
                                                    customAdapter.notifyDataSetChanged();
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "You are deleted category!", Toast.LENGTH_LONG);
                                                    toast.show();
                                                }
                                            });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                    return true;
                                }
                            });

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //Log.d("itemClick: position = " + position + ", id = " + id);
                                   // Log.d("itemClick: position = " + position + ", "tete");

                                    Product product = (Product) adapter.getItem(position);
                                    Product product2 = products.get(position);

                                    addedprod.append(product.getName() + " ");


                                    //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                                    LayoutInflater li = LayoutInflater.from(context);
                                    View promptsView = li.inflate(R.layout.prompt, null);

                                    //Создаем AlertDialog
                                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                                    //Настраиваем prompt.xml для нашего AlertDialog:
                                    mDialogBuilder.setView(promptsView);

                                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                                    final EditText userInput = (EditText) promptsView.findViewById(R.id.inputWeight);

                                    //Настраиваем сообщение в диалоговом окне:
                                    mDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            //Вводим текст и отображаем в строке ввода на основном экране:
                                                            addedprod.append(userInput.getText() + "\n");
                                                        }
                                                    })
                                            .setNegativeButton("Отмена",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    //Создаем AlertDialog:
                                    AlertDialog alertDialog = mDialogBuilder.create();

                                    //и отображаем его:
                                    alertDialog.show();





















                                    // Intent intent = new Intent(ProductsActivity.this, ClickedProductActivity.class).
                                    //putExtra("product", product);
                                    //  startActivity(intent);
                                }
                            });

                        } else {
                            products = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        products = null;
                        t.printStackTrace();
                    }
                });
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