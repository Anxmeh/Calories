package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.DishIngredientsView;
import com.example.caloriescounter.models.MyFragment;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestTestActivity extends AppCompatActivity {

    EditText textActivity;
   // Button buttonSendToFragment;
   // MyFragment myFragment;
   private Product addedProduct;

    private ListView listView;
    private List<Product> products;
    private Dish dish;
   // private List<Product> addedProducts;
    private ArrayList<Product> addedProducts2 = new ArrayList<Product>();
    private double dishCalories = 0;
    private double dishWeight = 0;
    private double caloriesInProduct = 0;
    EditText inputSearch;
    ProductAdapter customAdapter;

    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_test);

       // textActivity = (EditText) findViewById(R.id.activitytext);
       // buttonSendToFragment = (Button) findViewById(R.id.sendtofragment);

        final TextView addedprod = findViewById(R.id.resultDish);
        listView = findViewById(R.id.listViewProducts);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

/////////////////////////////////////////
//        CommonUtils.showLoading(this);
//        NetworkService.getInstance()
//                .getJSONApi()
//                .calculateDish()
//                .enqueue(new Callback<Dish>() {
//                    @Override
//                    public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response) {
//                        CommonUtils.hideLoading();
//                        if (response.errorBody() == null && response.isSuccessful()) {
//                            assert response.body() != null;
//                            dish = response.body();
//                            addedprod.setText("Weight: " +dish.getDishWeight() + ", Calories: " + dish.getDishCalories());
//
//                        } else {
//                            dish = null;
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t) {
//                        CommonUtils.hideLoading();
//                        dish = null;
//                        t.printStackTrace();
//                    }
//                });

//        ////////////////////////////////////////////////////////////////////






      //  CommonUtils.showLoading(this);
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
                            final ProductAdapter adapter = new ProductAdapter(products, TestTestActivity.this);
                                                       listView.setAdapter(adapter);


                            inputSearch.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                                                    adapter.getFilter().filter(cs);
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
                                    Product product = (Product) adapter.getItem(position);
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
                                                        public void onClick(DialogInterface dialog,int id) {
                                                            //Вводим текст и отображаем в строке ввода на основном экране:
                                                            caloriesInProduct = product.getCalories() * Double.parseDouble(userInput.getText().toString())/100;
                                                            dishCalories += caloriesInProduct;
                                                           // addedprod.setText(Double.toString(dishCalories));

                                                            // получим экземпляр FragmentTransaction из нашей Activity
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            FragmentTransaction fragmentTransaction = fragmentManager
                                                                    .beginTransaction();

                                                            // добавляем фрагмент
                                                                                                                    MyFragment myFragment = MyFragment.newInstance(product.getName(), userInput.getText().toString(),
                                                                    Double.toString(product.getProtein()), Double.toString(product.getFat()),
                                                                    Double.toString(product.getCarbohydrate()), Double.toString(product.getCarbohydrate()));
                                                            //fragmentTransaction.add(R.id.myfragment, myFragment);
                                                            fragmentTransaction.add(R.id.container, myFragment);
                                                            fragmentTransaction.commit();

                                                           // CommonUtils.showLoading(this);

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
                                                                                                    addedprod.setText("Weight: " +dish.getDishWeight() + ", Calories: " + dish.getDishCalories());

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
                                                        public void onClick(DialogInterface dialog,int id) {
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


        //// тут


    }
}