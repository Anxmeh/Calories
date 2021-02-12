package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.DailyMenuRecyclerAdapter;
import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.adapters.ProductCardRecyclerViewAdapter;
import com.example.caloriescounter.adapters.VitaminsDailyRecyclerAdapter;
import com.example.caloriescounter.adapters.VitaminsRecyclerAdapter;
import com.example.caloriescounter.click_listeners.OnChangeAmountVitamins;
import com.example.caloriescounter.click_listeners.OnChangeDailyVitaminsListener;
import com.example.caloriescounter.click_listeners.OnDeleteListener;
import com.example.caloriescounter.click_listeners.OnDeleteListenerDailyMenu;
import com.example.caloriescounter.click_listeners.OnDeleteListenerVitamins;
import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.DailyMenuView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.DishIngredientsView;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.ProgressTextView;
import com.example.caloriescounter.models.RemoveDailyView;
import com.example.caloriescounter.models.UserSettingsView;
import com.example.caloriescounter.models.UserVitaminsDailyView;
import com.example.caloriescounter.models.UserVitaminsView;
import com.example.caloriescounter.models.VitaminDailyCheckView;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayActivity extends BaseActivity implements OnDeleteListenerDailyMenu, OnChangeDailyVitaminsListener {

    private static final String TAG = TodayActivity.class.getSimpleName();
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewVit;
    private List<Product> products;
    private List<Ingredients> prodsindish;
    private List<DailyMenuView> dailyMenu;
    private DailyMenuRecyclerAdapter adapter;
    public Product addedDish;
    private UserSettingsView userSettings;
   // Calendar c = Calendar.getInstance();
    //  c.set(2020, 12, 22);

   private List<UserVitaminsDailyView> userVitamins;
   // VitaminsRecyclerAdapter adapterVit;
    VitaminsDailyRecyclerAdapter adapterVitDaily;

    private final Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatDate = new SimpleDateFormat("MMM d, yyyy",  new Locale("uk","UA"));

    private Product addedProduct;
    private DailyMenuView addedProduct2;

   // private ListView listView;
    private Dish dish;
    private ArrayList<Product> addedProducts2 = new ArrayList<Product>();
    private double dishCalories = 0;
    private double dishWeight = 0;
    private double caloriesInProduct = 0;
    //EditText inputSearch;


    ProgressTextView progressCalories;
    ProgressTextView progressFat;
    ProgressTextView progressCarbs;
    ProgressTextView progressProtein;

    private FloatingActionButton fab;

    ProductAdapter customAdapter;

    final Context context = this;

    public TextView txtDishProtein;
    public TextView txtDishFat;
    public TextView txtDishCarbs;
    public TextView txtDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_today);
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Меню на сьогодні");


        sessionManager = SessionManager.getInstance(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewVit = findViewById(R.id.recycler_view_vit);



        // final TextView addedprod = findViewById(R.id.resultDish);
        //listView = findViewById(R.id.listViewProducts);
       // inputSearch = (EditText) findViewById(R.id.inputSearch);
        txtDishProtein = findViewById(R.id.dishProtein);
        txtDishFat = findViewById(R.id.dishFat);
        txtDishCarbs = findViewById(R.id.dishCarbohydrate);
        txtDate = findViewById(R.id.dateAct);
        progressCalories = (ProgressTextView) findViewById(R.id.progressCalories);
        progressFat = (ProgressTextView) findViewById(R.id.progressFat);
        progressCarbs = (ProgressTextView) findViewById(R.id.progressCarbs);
        progressProtein = (ProgressTextView) findViewById(R.id.progressProtein);
        fab = findViewById(R.id.floating_action_button);

        setUserData();
        Date currentTime = Calendar.getInstance().getTime();
        txtDate.setText(currentTime.toString());
        int monthShow = calendar.get(Calendar.MONTH)+1;

        txtDate.setText(formatDate.format(currentTime));
        //txtDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + monthShow + "/" + calendar.get((Calendar.YEAR)));
       // txtDate.setText(daySelect + "/" + (monthSelect + 1) + "/" + yearSelect);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodayActivity.this, ChooseProductActivity.class);
                startActivity(intent);
            }

        });
//        progressFat.setMaxValue(30);
//        progressCarbs.setMaxValue(50);
//        progressProtein.setMaxValue(50);
        //setUserData();
        setRecyclerView();
        loadListPr();
        setRecyclerViewVit();




//        NetworkService.getInstance()
//                .getJSONApi()
//                .getProducts()
//                .enqueue(new Callback<List<Product>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
//                        CommonUtils.hideLoading();
//                        if (response.errorBody() == null && response.isSuccessful()) {
//                            assert response.body() != null;
//                            products = response.body();
//                            final ProductAdapter adapterP = new ProductAdapter(products, TodayActivity.this);
//                          //  listView.setAdapter(adapterP);
//
//
//                            inputSearch.addTextChangedListener(new TextWatcher() {
//
//                                @Override
//                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                                    adapterP.getFilter().filter(cs);
//                                }
//
//                                @Override
//                                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//                                                              int arg3) {
//                                }
//
//                                @Override
//                                public void afterTextChanged(Editable arg0) {
//                                }
//                            });
//
//                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    Product product = (Product) adapterP.getItem(position);
//                                    // Product product2 = products.get(position);
//                                    addedProducts2.add(product);
//                                    //dishCalories = 0;
//                                    // caloriesInProduct =product.getCalories();
//
//                                    //addedprod.append(product.getName() + " ");
//                                    // addedprod.setText(Integer.toString(addedProducts2.size()));
//
//
//                                    //Получаем вид с файла prompt.xml, который применим для диалогового окна:
//                                    LayoutInflater li = LayoutInflater.from(context);
//                                    View promptsView = li.inflate(R.layout.prompt, null);
//
//                                    //Создаем AlertDialog
//                                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
//
//                                    //Настраиваем prompt.xml для нашего AlertDialog:
//                                    mDialogBuilder.setView(promptsView);
//
//                                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
//                                    final EditText userInput = (EditText) promptsView.findViewById(R.id.inputWeight);
//
//                                    //Настраиваем сообщение в диалоговом окне:
//                                    mDialogBuilder
//                                            .setCancelable(false)
//                                            .setPositiveButton("OK",
//                                                    new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//                                                            //Вводим текст и отображаем в строке ввода на основном экране:
//                                                            caloriesInProduct = product.getCalories() * Double.parseDouble(userInput.getText().toString()) / 100;
//                                                            dishCalories += caloriesInProduct;
//
////
////                                                            // CommonUtils.showLoading(this);this
////
////                                                            final DishIngredientsView modelDish = new DishIngredientsView();
////                                                            modelDish.setProductName(product.getName());
////                                                            modelDish.setProductCalories(product.getCalories());
////                                                            modelDish.setProductProtein(product.getProtein());
////                                                            modelDish.setProductFat(product.getFat());
////                                                            modelDish.setProductCarbohydrate(product.getCarbohydrate());
////                                                            modelDish.setProductWeight(Double.parseDouble(userInput.getText().toString()));
//
//                                                            final DailyMenuView modelDaily = new DailyMenuView();
//                                                            modelDaily.setProductName(product.getName());
//                                                            modelDaily.setProductCalories(product.getCalories());
//                                                            modelDaily.setProductProtein(product.getProtein());
//                                                            modelDaily.setProductFat(product.getFat());
//                                                            modelDaily.setProductCarbohydrate(product.getCarbohydrate());
//                                                            modelDaily.setProductWeight(Double.parseDouble(userInput.getText().toString()));
//                                                           // Calendar c = Calendar.getInstance();
//                                                          //  c.set(2020, 12, 22);
//                                                            modelDaily.setDateOfMeal(currentTime);
//                                                            modelDaily.setProductId(product.getId());
//
//
//                                                            NetworkService.getInstance()
//                                                                    .getJSONApi()
//                                                                    .addDailyProduct(modelDaily)
//                                                                    .enqueue(new Callback<DailyMenuView>() {
//                                                                        @Override
//                                                                        public void onResponse(@NonNull Call<DailyMenuView> call, @NonNull Response<DailyMenuView> response) {
//                                                                            CommonUtils.hideLoading();
//                                                                            if (response.errorBody() == null && response.isSuccessful()) {
//                                                                                assert response.body() != null;
//                                                                                addedProduct2 = response.body();
//
//                                                                                String succeed = "Add succeed";
//
//                                                                                ////////////
//                                                                                //CommonUtils.showLoading(this);
//                                                                                NetworkService.getInstance()
//                                                                                        .getJSONApi()
//                                                                                        .calculateDish()
//                                                                                        .enqueue(new Callback<Dish>() {
//                                                                                            @Override
//                                                                                            public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response) {
//                                                                                                CommonUtils.hideLoading();
//                                                                                                if (response.errorBody() == null && response.isSuccessful()) {
//                                                                                                    assert response.body() != null;
//
//                                                                                                    dish = response.body();
//
//
//                                                                                                  //  txtDishProtein.setText(Double.toString(Math.round(dish.getDishProtein()*100.0)/100.0));
//                                                                                                    //txtDishFat.setText(Double.toString(Math.round(dish.getDishFat()*100.0)/100.0));
//                                                                                                 //   txtDishCarbs.setText(Double.toString(Math.round(dish.getDishCarbohydrate()*100.0)/100.0));
//
//loadListPr();
//
//                                                                                                    adapter.notifyDataSetChanged();
//                                                                                                    //////////////////////
//
////                                                                                                    NetworkService.getInstance()
////                                                                                                            .getJSONApi()
////                                                                                                            .getProductsinDish()
////                                                                                                            .enqueue(new Callback<List<Ingredients>>() {
////                                                                                                                @Override
////                                                                                                                public void onResponse(@NonNull Call<List<Ingredients>> call, @NonNull Response<List<Ingredients>> response) {
////                                                                                                                    CommonUtils.hideLoading();
////                                                                                                                    if (response.errorBody() == null && response.isSuccessful()) {
////                                                                                                                        assert response.body() != null;
////                                                                                                                        if (prodsindish != null)
////                                                                                                                            prodsindish.clear();
////                                                                                                                        prodsindish.addAll(0, response.body());
////                                                                                                                        adapter.notifyDataSetChanged();
////                                                                                                                    } else {
////                                                                                                                        prodsindish = null;
////                                                                                                                    }
////                                                                                                                }
////
////                                                                                                                @Override
////                                                                                                                public void onFailure(@NonNull Call<List<Ingredients>> call, @NonNull Throwable t) {
////                                                                                                                    CommonUtils.hideLoading();
////                                                                                                                    prodsindish = null;
////                                                                                                                    t.printStackTrace();
////                                                                                                                }
////                                                                                                            });
//                                                                                                    /////////////////////
//
//
//                                                                                                } else {
//                                                                                                    dish = null;
//                                                                                                }
//                                                                                            }
//
//                                                                                            @Override
//                                                                                            public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t) {
//                                                                                                CommonUtils.hideLoading();
//                                                                                                dish = null;
//                                                                                                t.printStackTrace();
//                                                                                            }
//                                                                                        });
//                                                                                ///////////
////                                                                                Toast toast = Toast.makeText(getApplicationContext(),
////                                                                                        succeed, Toast.LENGTH_LONG);
////                                                                                toast.show();
////                                                                                Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
////                                                                                startActivity(intent);
//                                                                            } else {
//                                                                                String errorMessage;
//                                                                                try {
//                                                                                    assert response.errorBody() != null;
//                                                                                    errorMessage = response.errorBody().string();
//                                                                                } catch (IOException e) {
//                                                                                    errorMessage = response.message();
//                                                                                    e.printStackTrace();
//                                                                                }
//                                                                                Toast toast = Toast.makeText(getApplicationContext(),
//                                                                                        errorMessage, Toast.LENGTH_LONG);
//                                                                                toast.show();
//                                                                            }
//                                                                        }
//
//                                                                        @Override
//                                                                        public void onFailure(@NonNull Call<DailyMenuView> call, @NonNull Throwable t) {
//                                                                            CommonUtils.hideLoading();
//                                                                            String error = "Error occurred while getting request!";
//                                                                            Toast toast = Toast.makeText(getApplicationContext(),
//                                                                                    error, Toast.LENGTH_LONG);
//                                                                            toast.show();
//                                                                            t.printStackTrace();
//                                                                        }
//                                                                    });
//
//                                                        }
//                                                    })
//                                            .setNegativeButton("Відміна",
//                                                    new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//                                                            dialog.cancel();
//                                                        }
//                                                    });
//
//                                    //Создаем AlertDialog:
//                                    AlertDialog alertDialog = mDialogBuilder.create();
//                                    //и отображаем его:
//                                    alertDialog.show();
//                                }
//                            });
//
//                        } else {
//                            products = null;
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
//                        CommonUtils.hideLoading();
//                        products = null;
//                        t.printStackTrace();
//                    }
//                });


    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        dailyMenu = new ArrayList<>();
        adapter = new DailyMenuRecyclerAdapter(dailyMenu, this, this);

        recyclerView.setAdapter(adapter);

        int largePadding = 16;
        int smallPadding = 4;
        // recyclerView.addItemDecoration(new CategoryGridItemDecoration(largePadding, smallPadding));
    }

    @Override
    public void deleteItem(final RemoveDailyView product, final DailyMenuView model) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Видалення")
                //.setMessage("Ви дійсно бажаєте видалити \"" + product.getProductName() + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "Delete product by name " + product.getProductId());
                       // prodsindish.remove(product);
                        DailyMenuView v = new DailyMenuView();
                        v.setProductId(product.getProductId());

                        dailyMenu.remove(model);

                        //deleteConfirm(productEntry);
//                        final RemoveDailyView model = new RemoveDailyView();
//                        model.setProductId(product.getProductId());
//                        model.setDateOfMeal(product.);


//                        final Ingredients model = new Ingredients();
//                        model.setProductName(product.getProductName());
//                        model.setProductCalories(Double.parseDouble(calories));
//                        model.setProductProtein(Double.parseDouble(protein));
//                        model.setProductFat(Double.parseDouble(fat));
//                        model.setProductCarbohydrate(Double.parseDouble(carb));
//                        model.setProductWeight(Double.parseDouble(weight));

/////////////////////////////////////
                        NetworkService.getInstance()
                                .getJSONApi()
                                .removeDailyProduct(product)
                                .enqueue(new Callback<List<DailyMenuView>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<List<DailyMenuView>> call, @NonNull Response<List<DailyMenuView>> response) {
                                        CommonUtils.hideLoading();
                                        if (response.errorBody() == null && response.isSuccessful()) {
                                            assert response.body() != null;
                                            loadListPr();
//                                            Toast toast = Toast.makeText(getApplicationContext(),
//                                                    "hello", Toast.LENGTH_LONG);
//                            toast.show();
                                            // addedProduct = response.body();
                                          //  dish = response.body();
//                                            txtDishCalories.setText(Double.toString(dish.getDishCalories()));
//                                            txtDishWeight.setText(Double.toString(dish.getDishWeight()));
//                                            // double n= Math.round(dish.getDishProtein()*100.0)/100.0;
//                                            txtDishProtein.setText(Double.toString(Math.round(dish.getDishProtein()*100.0)/100.0));
//                                            txtDishFat.setText(Double.toString(Math.round(dish.getDishFat()*100.0)/100.0));
//                                            txtDishCarbs.setText(Double.toString(Math.round(dish.getDishCarbohydrate()*100.0)/100.0));
//

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
                                    public void onFailure(@NonNull Call<List<DailyMenuView>> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Error occurred while getting request!";
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                error, Toast.LENGTH_LONG);
//                        toast.show();
                                        t.printStackTrace();
                                    }
                                });
                        /////////////////////////////////////////////////
//



                        adapter.notifyDataSetChanged();
                        //loadListPr();


                    }
                })
                .show();
    }

    public void loadListPr() {

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

                            double totalWeight = 0;
                            double totalCalories = 0;
                            double totalFat = 0;
                            double totalProtein = 0;
                            double totalCarbs = 0;

                            for (DailyMenuView item : dailyMenu) {
                                totalWeight +=item.getProductWeight();
                                totalCalories +=item.getProductCalories();
                                totalFat += item.getProductFat();
                                totalProtein += item.getProductProtein();
                                totalCarbs += item.getProductCarbohydrate();
                            }


                            progressCalories.setValue((int) totalCalories);
                            progressCalories.setTextColor(Color.parseColor("#000000"));
                            progressFat.setValue((int)totalFat);
                            progressFat.setTextColor(Color.parseColor("#000000"));
                            progressCarbs.setValue((int)totalCarbs);
                            progressCarbs.setTextColor(Color.parseColor("#000000"));
                            progressProtein.setValue((int)totalProtein);
                            progressProtein.setTextColor(Color.parseColor("#000000"));
                            getUserVitamins();
                            adapter.notifyDataSetChanged();
                        } else {
                            dailyMenu = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<DailyMenuView>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        dailyMenu = null;
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


                            //  double heigh = userSettings.getHeight();
                            // int heightM = (int) userSettings.getHeight() / 100;
                            //  int heightCm = (int) userSettings.getHeight() - (100 * heightM);




                        } else {
                            userSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserSettingsView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        userSettings = null;
                        t.printStackTrace();
                    }
                });

    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void onClickPreviousDate (View view) throws ParseException {
//
//        Date currentTime = c.getTime();
//
//        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String ymd = dmyFormat.format(currentTime);
//
//       // String currStr = currentTime.toString();
//        LocalDate now = LocalDate.parse(ymd); //2015-11-24
//        LocalDate yesterday = now.minusDays(1);
//        txtDate.setText(yesterday.toString());
//
//
//        java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(yesterday.toString());
//       // c.set(d);
//        c.setTime(d);
//       // c.getTime();
//        loadListPr();
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void onClickNextDate(View view) throws ParseException {
//        Date currentTime = c.getTime();
//
//        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String ymd = dmyFormat.format(currentTime);
//
//        // String currStr = currentTime.toString();
//        LocalDate now = LocalDate.parse(ymd); //2015-11-24
//        LocalDate tomorrow = now.plusDays(1);
//        txtDate.setText(tomorrow.toString());
//
//
//        java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(tomorrow.toString());
//        // c.set(d);
//        c.setTime(d);
//        // c.getTime();
//        loadListPr();
//    }

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

                        txtDate.setText(daySelect + "/" + (monthSelect + 1) + "/" + yearSelect);
                        loadListPr();
                    }
                }, year, month, day);
        datePicker.show();
        loadListPr();
    }
     public void onClickPreviousDate (View view) {
        calendar.add(Calendar.DATE, -1);
         int month = calendar.get(Calendar.MONTH)+1;

         Date currentTime =  calendar.getTime();;
         txtDate.setText(formatDate.format(currentTime));
        //txtDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + calendar.get((Calendar.YEAR)));
        loadListPr();
    }

    public void onClickNextDate(View view) {
        calendar.add(Calendar.DATE, 1);
        int month = calendar.get(Calendar.MONTH)+1;
        Date currentTime = calendar.getTime();
        //currentTime = calendar.getTime();
        txtDate.setText(formatDate.format(currentTime));
        //txtDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + calendar.get((Calendar.YEAR)));
        loadListPr();
    }

    public void getUserVitamins() {
        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .getDailyVitamins(calendar.getTime())
                .enqueue(new Callback<List<UserVitaminsDailyView>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<UserVitaminsDailyView>> call, @NonNull Response<List<UserVitaminsDailyView>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            if (userVitamins != null)
                                userVitamins.clear();
                            if (response.body() != null)
                                userVitamins.addAll(0, response.body());
                            adapterVitDaily.notifyDataSetChanged();


                        } else {
                            userVitamins = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<UserVitaminsDailyView>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        userVitamins = null;
                        t.printStackTrace();
                    }
                });
    }

    private void setRecyclerViewVit() {
        recyclerViewVit.setHasFixedSize(true);
        recyclerViewVit.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        userVitamins = new ArrayList<>();
        adapterVitDaily = new VitaminsDailyRecyclerAdapter(userVitamins, this, this );

        recyclerViewVit.setAdapter(adapterVitDaily);

        int largePadding = 16;
        int smallPadding = 4;
        //  recyclerView.addItemDecoration(new CategoryGridItemDecoration(largePadding, smallPadding));
    }

    @Override
    public void checkVitamin(final UserVitaminsDailyView vitamin) {
        Log.e(TAG, "Before is  " + vitamin.isTaken());
        VitaminDailyCheckView model = new VitaminDailyCheckView();
        model.setDateOfVitamin(vitamin.getDateOfVitamin());
        model.setVitaminId(vitamin.getVitaminId());
        model.setTaken(!vitamin.isTaken());
        vitamin.setTaken(!vitamin.isTaken());
        Log.e(TAG, "After is  " + model.isTaken());


                        NetworkService.getInstance()
                                .getJSONApi()
                                .checkDailyVitamin(model)
                                .enqueue(new Callback<VitaminDailyCheckView>() {
                                    @Override
                                    public void onResponse(@NonNull Call<VitaminDailyCheckView> call, @NonNull Response<VitaminDailyCheckView> response) {
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
//                            Toast toast = Toast.makeText(getApplicationContext(),
//                                    errorMessage, Toast.LENGTH_LONG);
//                            toast.show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<VitaminDailyCheckView> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Error occurred while getting request!";
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                error, Toast.LENGTH_LONG);
//                        toast.show();
                                        t.printStackTrace();
                                    }
                                });
                        //deleteConfirm(productEntry);



    }
    ///////////////////////////////////////////////
//    @Override
//    public void deleteItem(final UserVitaminsView vitamin) {
//        new MaterialAlertDialogBuilder(this)
//                .setTitle("Видалення")
//                .setMessage("Ви дійсно бажаєте видалити \"" + vitamin.getVitaminName() + "\"?")
//                .setNegativeButton("Скасувати", null)
//                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.e(TAG, "Delete category by id " + vitamin.getVitaminId());
//                        userVitamins.remove(vitamin);
//                        adapter.notifyDataSetChanged();
////
////                        NetworkService.getInstance()
////                                .getJSONApi()
////                                .removeProduct(product.getId())
////                                .enqueue(new Callback<List<Product>>() {
////                                    @Override
////                                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
////                                        CommonUtils.hideLoading();
////                                        if (response.errorBody() == null && response.isSuccessful()) {
////                                            assert response.body() != null;
////                                            // addedProduct = response.body();
//////                                            dish = response.body();
//////                                            txtDishCalories.setText(Double.toString(dish.getDishCalories()));
//////                                            txtDishWeight.setText(Double.toString(dish.getDishWeight()));
//////                                            // double n= Math.round(dish.getDishProtein()*100.0)/100.0;
//////                                            txtDishProtein.setText(Double.toString(Math.round(dish.getDishProtein()*100.0)/100.0));
//////                                            txtDishFat.setText(Double.toString(Math.round(dish.getDishFat()*100.0)/100.0));
//////                                            txtDishCarbs.setText(Double.toString(Math.round(dish.getDishCarbohydrate()*100.0)/100.0));
////
////
////                                        } else {
////                                            String errorMessage;
////                                            try {
////                                                assert response.errorBody() != null;
////                                                errorMessage = response.errorBody().string();
////                                            } catch (IOException e) {
////                                                errorMessage = response.message();
////                                                e.printStackTrace();
////                                            }
//////                            Toast toast = Toast.makeText(getApplicationContext(),
//////                                    errorMessage, Toast.LENGTH_LONG);
//////                            toast.show();
////                                        }
////                                    }
////
////                                    @Override
////                                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
////                                        CommonUtils.hideLoading();
////                                        String error = "Error occurred while getting request!";
//////                        Toast toast = Toast.makeText(getApplicationContext(),
//////                                error, Toast.LENGTH_LONG);
//////                        toast.show();
////                                        t.printStackTrace();
////                                    }
////                                });
////                        //deleteConfirm(productEntry);
////                    }
//                    }
//                })
//                .show();
//
//    }
//
//    @Override
//    public void changeItem(final UserVitaminsView vitamin) {
//        LayoutInflater li = LayoutInflater.from(context);
//        View promptsView = li.inflate(R.layout.prompt, null);
//
//        //Создаем AlertDialog
//        android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(context);
//        //Настраиваем prompt.xml для нашего AlertDialog:
//        mDialogBuilder.setView(promptsView);
//        //Настраиваем отображение поля для ввода текста в открытом диалоге:
//        final EditText userInput = (EditText) promptsView.findViewById(R.id.inputWeight);
//        //Настраиваем сообщение в диалоговом окне:
////        mDialogBuilder
////                .setCancelable(false)
////                .setPositiveButton("OK",
////                        new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int id) {
////                                //Вводим текст и отображаем в строке ввода на основном экране:
////                                try {
////                                    amount = Integer.parseInt(userInput.getText().toString());
////                                } catch (NumberFormatException nfe) {
////                                    Log.e(TAG, "Could not parse " + nfe);
////                                }
////                                vitamin.setAmount(amount);
////
////                                CommonUtils.showLoading(context);
////                                NetworkService.getInstance()
////                                        .getJSONApi()
////                                        .changeUserVitamin(vitamin)
////                                        .enqueue(new Callback<UserVitaminsView>() {
////                                            @Override
////                                            public void onResponse(@NonNull Call<UserVitaminsView> call, @NonNull Response<UserVitaminsView> response) {
////                                                CommonUtils.hideLoading();
////                                                if (response.errorBody() == null && response.isSuccessful()) {
////                                                    assert response.body() != null;
////                                                    userVitamin = response.body();
////                                                    getUserVitamins();
////
////
////                                                } else {
////                                                    vitamins = null;
////                                                }
////                                            }
////
////                                            @Override
////                                            public void onFailure(@NonNull Call<UserVitaminsView> call, @NonNull Throwable t) {
////                                                CommonUtils.hideLoading();
////                                                userVitamin = null;
////                                                t.printStackTrace();
////                                            }
////                                        });
////
////
////                            }
////                        }).show();
//    }
//
//    @Override
//    public void addItem(final UserVitaminsView vitamin) {
//
//        int newAm = vitamin.getAmount() + 1;
//        vitamin.setAmount(newAm);
//
////        CommonUtils.showLoading(context);
////        NetworkService.getInstance()
////                .getJSONApi()
////                .changeUserVitamin(vitamin)
////                .enqueue(new Callback<UserVitaminsView>() {
////                    @Override
////                    public void onResponse(@NonNull Call<UserVitaminsView> call, @NonNull Response<UserVitaminsView> response) {
////                        CommonUtils.hideLoading();
////                        if (response.errorBody() == null && response.isSuccessful()) {
////                            assert response.body() != null;
////                            userVitamin = response.body();
////                            //  getUserVitamins();
////                            adapter.notifyDataSetChanged();
////
////                        } else {
////                            vitamins = null;
////                        }
////                    }
////
////                    @Override
////                    public void onFailure(@NonNull Call<UserVitaminsView> call, @NonNull Throwable t) {
////                        CommonUtils.hideLoading();
////                        userVitamin = null;
////                        t.printStackTrace();
////                    }
////                });
//    }
//
//    @Override
//    public void removeItem(final UserVitaminsView vitamin) {
////        if (amount > 0)
////            amount--;
////        vitamin.setAmount(amount);
//        int newAm = vitamin.getAmount() - 1;
//        vitamin.setAmount(newAm);
//
////        CommonUtils.showLoading(context);
////        NetworkService.getInstance()
////                .getJSONApi()
////                .changeUserVitamin(vitamin)
////                .enqueue(new Callback<UserVitaminsView>() {
////                    @Override
////                    public void onResponse(@NonNull Call<UserVitaminsView> call, @NonNull Response<UserVitaminsView> response) {
////                        CommonUtils.hideLoading();
////                        if (response.errorBody() == null && response.isSuccessful()) {
////                            assert response.body() != null;
////                            userVitamin = response.body();
////                            //getUserVitamins();
////                            adapter.notifyDataSetChanged();
////
////                        } else {
////                            vitamins = null;
////                        }
////                    }
////
////                    @Override
////                    public void onFailure(@NonNull Call<UserVitaminsView> call, @NonNull Throwable t) {
////                        CommonUtils.hideLoading();
////                        userVitamin = null;
////                        t.printStackTrace();
////                    }
////                });
//
//
//    }
}