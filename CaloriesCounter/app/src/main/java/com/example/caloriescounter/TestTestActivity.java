package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.caloriescounter.models.MyFragment;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestTestActivity extends AppCompatActivity {

    EditText textActivity;
    Button buttonSendToFragment;
   // MyFragment myFragment;

    private ListView listView;
    private List<Product> products;
    EditText inputSearch;
    ProductAdapter customAdapter;

    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_test);

       // textActivity = (EditText) findViewById(R.id.activitytext);
        buttonSendToFragment = (Button) findViewById(R.id.sendtofragment);

        final TextView addedprod = findViewById(R.id.etProduct);
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
                            final ProductAdapter adapter = new ProductAdapter(products, TestTestActivity.this);
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

                                    AlertDialog.Builder builder = new AlertDialog.Builder(TestTestActivity.this);
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

                                                            // получим экземпляр FragmentTransaction из нашей Activity
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            FragmentTransaction fragmentTransaction = fragmentManager
                                                                    .beginTransaction();

                                                            // добавляем фрагмент
                                                         //  MyFragment myFragment = new MyFragment();
                                                            MyFragment myFragment = MyFragment.newInstance(product.getName());
                                                            //fragmentTransaction.add(R.id.myfragment, myFragment);
                                                            fragmentTransaction.add(R.id.container, myFragment);
                                                            fragmentTransaction.commit();

                                                            buttonSendToFragment.setOnClickListener(new View.OnClickListener() {

                                                                @Override
                                                                public void onClick(View arg0) {
                                                                    // TODO Auto-generated method stub
                                                                   // String text = textActivity.getText().toString();
                                                                   // TextView textFragment = (TextView) findViewById(R.id.prodInList);
                                                                   // textFragment.setText(product.getName());
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










//        // получим экземпляр FragmentTransaction из нашей Activity
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager
//                .beginTransaction();
//
//        // добавляем фрагмент
//        myFragment = new MyFragment();
//        fragmentTransaction.add(R.id.myfragment, myFragment);
//        fragmentTransaction.commit();
//
//        buttonSendToFragment.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                String text = textActivity.getText().toString();
//                TextView textFragment = (TextView) findViewById(R.id.fragmenttext);
//                textFragment.setText(text);
//            }
//        });

    }
}