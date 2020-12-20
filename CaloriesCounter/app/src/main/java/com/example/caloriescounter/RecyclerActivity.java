package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.adapters.ProductCardRecyclerViewAdapter;
import com.example.caloriescounter.click_listeners.OnDeleteListener;
import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.DishIngredientsView;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.models.MyFragment;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerActivity extends AppCompatActivity implements OnDeleteListener {

    private static final String TAG = RecyclerActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Product> products;
    private List<Ingredients> prodsindish;
    private ProductCardRecyclerViewAdapter adapter;
    public Product addedDish;

    private Product addedProduct;

    private ListView listView;
    private Dish dish;
    private ArrayList<Product> addedProducts2 = new ArrayList<Product>();
    private double dishCalories = 0;
    private double dishWeight = 0;
    private double caloriesInProduct = 0;
    EditText inputSearch;

    TextView addedprod;

    ProductAdapter customAdapter;

    final Context context = this;

    public TextView txtDishProtein;
    public TextView txtDishFat;
    public TextView txtDishCarbs;
    public TextView txtDishWeight;
    public TextView txtDishCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        recyclerView = findViewById(R.id.recycler_view);

        addedprod = findViewById(R.id.resultDish);

        // final TextView addedprod = findViewById(R.id.resultDish);
        listView = findViewById(R.id.listViewProducts);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        txtDishProtein = findViewById(R.id.dishProtein);
        txtDishFat = findViewById(R.id.dishFat);
        txtDishCarbs = findViewById(R.id.dishCarbohydrate);
        txtDishCalories = findViewById(R.id.dishCalories);
        txtDishWeight = findViewById(R.id.dishWeight);

        setRecyclerView();


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
                            final ProductAdapter adapterP = new ProductAdapter(products, RecyclerActivity.this);
                            listView.setAdapter(adapterP);


                            inputSearch.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                    adapterP.getFilter().filter(cs);
                                }

                                @Override
                                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                              int arg3) {
                                }

                                @Override
                                public void afterTextChanged(Editable arg0) {
                                }
                            });

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Product product = (Product) adapterP.getItem(position);
                                    // Product product2 = products.get(position);
                                    addedProducts2.add(product);
                                    //dishCalories = 0;
                                    // caloriesInProduct =product.getCalories();

                                    //addedprod.append(product.getName() + " ");
                                    // addedprod.setText(Integer.toString(addedProducts2.size()));


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
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            //Вводим текст и отображаем в строке ввода на основном экране:
                                                            caloriesInProduct = product.getCalories() * Double.parseDouble(userInput.getText().toString()) / 100;
                                                            dishCalories += caloriesInProduct;
                                                            // addedprod.setText(Double.toString(dishCalories));

                                                            // получим экземпляр FragmentTransaction из нашей Activity
//                                                            FragmentManager fragmentManager = getFragmentManager();
//                                                            FragmentTransaction fragmentTransaction = fragmentManager
//                                                                    .beginTransaction();
//
//                                                            // добавляем фрагмент
//                                                            MyFragment myFragment = MyFragment.newInstance(product.getName(), userInput.getText().toString(),
//                                                                    Double.toString(product.getProtein()), Double.toString(product.getFat()),
//                                                                    Double.toString(product.getCarbohydrate()), Double.toString(product.getCarbohydrate()));
//                                                            //fragmentTransaction.add(R.id.myfragment, myFragment);
//                                                            fragmentTransaction.add(R.id.container, myFragment);
//                                                            fragmentTransaction.commit();
//
//                                                            // CommonUtils.showLoading(this);this

                                                            final DishIngredientsView modelDish = new DishIngredientsView();
                                                            modelDish.setProductName(product.getName());
                                                            modelDish.setProductCalories(product.getCalories());
                                                            modelDish.setProductProtein(product.getProtein());
                                                            modelDish.setProductFat(product.getFat());
                                                            modelDish.setProductCarbohydrate(product.getCarbohydrate());
                                                            modelDish.setProductWeight(Double.parseDouble(userInput.getText().toString()));


                                                            NetworkService.getInstance()
                                                                    .getJSONApi()
                                                                    .addProductToDish(modelDish)
                                                                    .enqueue(new Callback<Product>() {
                                                                        @Override
                                                                        public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                                                                            CommonUtils.hideLoading();
                                                                            if (response.errorBody() == null && response.isSuccessful()) {
                                                                                assert response.body() != null;
                                                                                addedProduct = response.body();

                                                                                String succeed = "Add succeed";

                                                                                ////////////
                                                                                //CommonUtils.showLoading(this);
                                                                                NetworkService.getInstance()
                                                                                        .getJSONApi()
                                                                                        .calculateDish()
                                                                                        .enqueue(new Callback<Dish>() {
                                                                                            @Override
                                                                                            public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response) {
                                                                                                CommonUtils.hideLoading();
                                                                                                if (response.errorBody() == null && response.isSuccessful()) {
                                                                                                    assert response.body() != null;

                                                                                                    dish = response.body();
                                                                                                    addedprod.setText("Weight: " + dish.getDishWeight() + ", Calories: " + dish.getDishCalories());
                                                                                                    txtDishCalories.setText(Double.toString(dish.getDishCalories()));
                                                                                                    txtDishWeight.setText(Double.toString(dish.getDishWeight()));
                                                                                                    txtDishProtein.setText(Double.toString(Math.round(dish.getDishProtein()*100.0)/100.0));
                                                                                                    txtDishFat.setText(Double.toString(Math.round(dish.getDishFat()*100.0)/100.0));
                                                                                                    txtDishCarbs.setText(Double.toString(Math.round(dish.getDishCarbohydrate()*100.0)/100.0));


                                                                                                    //////////////////////

                                                                                                    NetworkService.getInstance()
                                                                                                            .getJSONApi()
                                                                                                            .getProductsinDish()
                                                                                                            .enqueue(new Callback<List<Ingredients>>() {
                                                                                                                @Override
                                                                                                                public void onResponse(@NonNull Call<List<Ingredients>> call, @NonNull Response<List<Ingredients>> response) {
                                                                                                                    CommonUtils.hideLoading();
                                                                                                                    if (response.errorBody() == null && response.isSuccessful()) {
                                                                                                                        assert response.body() != null;
                                                                                                                        if (prodsindish != null)
                                                                                                                            prodsindish.clear();
                                                                                                                        prodsindish.addAll(0, response.body());
                                                                                                                        adapter.notifyDataSetChanged();
                                                                                                                    } else {
                                                                                                                        prodsindish = null;
                                                                                                                    }
                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onFailure(@NonNull Call<List<Ingredients>> call, @NonNull Throwable t) {
                                                                                                                    CommonUtils.hideLoading();
                                                                                                                    prodsindish = null;
                                                                                                                    t.printStackTrace();
                                                                                                                }
                                                                                                            });
                                                                                                    /////////////////////


                                                                                                } else {
                                                                                                    dish = null;
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t) {
                                                                                                CommonUtils.hideLoading();
                                                                                                dish = null;
                                                                                                t.printStackTrace();
                                                                                            }
                                                                                        });
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
                                                                        public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                                                                            CommonUtils.hideLoading();
                                                                            String error = "Error occurred while getting request!";
                                                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                                                    error, Toast.LENGTH_LONG);
                                                                            toast.show();
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

                                    //Создаем AlertDialog:
                                    AlertDialog alertDialog = mDialogBuilder.create();
                                    //и отображаем его:
                                    alertDialog.show();
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

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.VERTICAL, false));
        prodsindish = new ArrayList<>();
        adapter = new ProductCardRecyclerViewAdapter(prodsindish, this, this);

        recyclerView.setAdapter(adapter);

        int largePadding = 16;
        int smallPadding = 4;
        // recyclerView.addItemDecoration(new CategoryGridItemDecoration(largePadding, smallPadding));
    }

    @Override
    public void deleteItem(final Ingredients product) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Видалення")
                .setMessage("Ви дійсно бажаєте видалити \"" + product.getProductName() + "\"?")
                .setNegativeButton("Скасувати", null)
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "Delete product by name " + product.getProductName());
                        prodsindish.remove(product);

                        //deleteConfirm(productEntry);


//                        final Ingredients model = new Ingredients();
//                        model.setProductName(product.getProductName());
//                        model.setProductCalories(Double.parseDouble(calories));
//                        model.setProductProtein(Double.parseDouble(protein));
//                        model.setProductFat(Double.parseDouble(fat));
//                        model.setProductCarbohydrate(Double.parseDouble(carb));
//                        model.setProductWeight(Double.parseDouble(weight));


                        NetworkService.getInstance()
                                .getJSONApi()
                                .removeProductInDish(product)
                                .enqueue(new Callback<Dish>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response) {
                                        CommonUtils.hideLoading();
                                        if (response.errorBody() == null && response.isSuccessful()) {
                                            assert response.body() != null;
                                           // addedProduct = response.body();
                                            dish = response.body();
                                            txtDishCalories.setText(Double.toString(dish.getDishCalories()));
                                            txtDishWeight.setText(Double.toString(dish.getDishWeight()));
                                          // double n= Math.round(dish.getDishProtein()*100.0)/100.0;
                                            txtDishProtein.setText(Double.toString(Math.round(dish.getDishProtein()*100.0)/100.0));
                                            txtDishFat.setText(Double.toString(Math.round(dish.getDishFat()*100.0)/100.0));
                                            txtDishCarbs.setText(Double.toString(Math.round(dish.getDishCarbohydrate()*100.0)/100.0));


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
                                    public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Error occurred while getting request!";
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                error, Toast.LENGTH_LONG);
//                        toast.show();
                                        t.printStackTrace();
                                    }
                                });
//
                        addedprod.setText("After" + product.getProductName());

                        adapter.notifyDataSetChanged();


                    }
                })
                .show();
    }

    public void onClickAddDish(View view) {

       // long id, String name, double protein, double fat, double carbohydrate, double calories

        AddProductView model = new AddProductView();
         model.setProtein(dish.getDishProtein()*100/dish.getDishWeight());
        model.setFat(dish.getDishFat()*100/dish.getDishWeight());
        model.setCarbohydrate(dish.getDishCarbohydrate()*100/dish.getDishWeight());
        model.setCalories(dish.getDishCalories()*100/dish.getDishWeight());

//        addedDish.setName(dish.getDishName());
//        addedDish.setCalories(dish.getDishCalories()*100/dish.getDishWeight());
//        addedDish.setCarbohydrate(dish.getDishCarbohydrate()*100/dish.getDishWeight());
//        addedDish.setProtein(dish.getDishProtein()*100/dish.getDishWeight());
//        addedDish.setFat(dish.getDishFat()*100/dish.getDishWeight());

        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dish_name, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText userInputDishName = (EditText) promptsView.findViewById(R.id.inputDishName);

        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                //addedDish.setName(userInput.toString());
                                model.setName(userInputDishName.getText().toString());

                                NetworkService.getInstance()
                                        .getJSONApi()
                                        .addProduct(model)
                                        .enqueue(new Callback<Product>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                                                CommonUtils.hideLoading();
                                                if (response.errorBody() == null && response.isSuccessful()) {
                                                    assert response.body() != null;
                                                    addedProduct = response.body();

                                                    String succeed = "Add succeed";
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            succeed, Toast.LENGTH_LONG);
                                                    toast.show();
                                                    Intent intent = new Intent(RecyclerActivity.this, ProductsActivity.class);
                                                    startActivity(intent);
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
                                            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                                                CommonUtils.hideLoading();
                                                String error = "Error occurred while getting request!";
                                                Toast toast = Toast.makeText(getApplicationContext(),
                                                        error, Toast.LENGTH_LONG);
                                                toast.show();
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

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();
        //и отображаем его:
        alertDialog.show();


    }
}