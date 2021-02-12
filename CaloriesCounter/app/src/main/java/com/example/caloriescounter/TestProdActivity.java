package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.adapters.CustomAdapter;
import com.example.caloriescounter.adapters.ProductAdapter;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.Vitamin;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestProdActivity extends AppCompatActivity {
    private String[] cats = { "Васька", "Мурзик", "Барсик", "Рыжик" };
    private TextView tx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_prod);
        tx = findViewById(R.id.txtspin);

        Spinner spinner = findViewById(R.id.spinner);
        CustomAdapter adapter = new CustomAdapter(this,
                android.R.layout.simple_spinner_item, cats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        tx.setText("");
                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        // Set adapter flag that something has been chosen
                        CustomAdapter.flag = true;
                        String current = (String) spinner.getSelectedItem();
                        tx.setText(current.toString());
                        // String[] choose = getResources().getStringArray(R.array.cats);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Ваш выбор: " + current.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });
    }

}


//
//    private ListView listView;
//    private List<Product> products;
//    EditText inputSearch;
//    ProductAdapter customAdapter;
//
//    final Context context = this;
//
//    private LinearLayout mLayout;
//    private EditText mEditText;
//    private Button mButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_prod);
//
////        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
////        mEditText = (EditText) findViewById(R.id.editText);
////        mButton = (Button) findViewById(R.id.button);
////        mButton.setOnClickListener(onClick());
////        TextView textView = new TextView(this);
////        textView.setText("New text");
//
//
//        //final TextView addedprod = findViewById(R.id.etProduct);
//        listView = findViewById(R.id.listViewProducts);
//        inputSearch = (EditText) findViewById(R.id.inputSearch);
//
//        CommonUtils.showLoading(this);
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
//                            final ProductAdapter adapter = new ProductAdapter(products, TestProdActivity.this);
//                            //customAdapter = new ProductAdapter(products, CreateNewDishActivity.this);
//
//                            listView.setAdapter(adapter);
//                            //listView.setAdapter(customAdapter);
//
//                            inputSearch.addTextChangedListener(new TextWatcher() {
//
//                                @Override
//                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                                    //Когда пользователь вводит какой-нибудь текст:
//                                    adapter.getFilter().filter(cs);
//                                    // CreateNewDishActivity.this.adapter.getFilter().filter(cs);
//
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
//
//
//
//
//
//                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                                @Override
//                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(TestProdActivity.this);
//                                    builder.setMessage("Are you sure you want to delete category")
//                                            .setCancelable(false)
//                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    Toast toast = Toast.makeText(getApplicationContext(),
//                                                            "Good you changed your mind!", Toast.LENGTH_LONG);
//                                                    toast.show();
//                                                }
//                                            })
//                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    Product product = products.get(position);
//                                                    products.remove(product);
//                                                    customAdapter.notifyDataSetChanged();
//                                                    Toast toast = Toast.makeText(getApplicationContext(),
//                                                            "You are deleted category!", Toast.LENGTH_LONG);
//                                                    toast.show();
//                                                }
//                                            });
//                                    AlertDialog alertDialog = builder.create();
//                                    alertDialog.show();
//                                    return true;
//                                }
//                            });
//
//                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    Product product = (Product) adapter.getItem(position);
//                                    Product product2 = products.get(position);
//                                   // addedprod.append(product.getName() + " ");
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
//                                                        public void onClick(DialogInterface dialog,int id) {
//                                                            //Вводим текст и отображаем в строке ввода на основном экране:
//                                                         //   addedprod.append(userInput.getText() + "\n");
//
//                                                            LayoutInflater ltInflater = getLayoutInflater();
//                                                            LinearLayout subLayoutFieldsForBtnAdd = (LinearLayout) findViewById(R.id.subLayoutFields);
//                                                            View view1 = ltInflater.inflate(R.layout.sub_fields_prod, subLayoutFieldsForBtnAdd, true);
//                                                            EditText et = (EditText)findViewById(R.id.prodInList);
//
//                                                            et.setText(product.getName());
//                                                        }
//                                                    })
//                                            .setNegativeButton("Отмена",
//                                                    new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog,int id) {
//                                                            dialog.cancel();
//                                                        }
//                                                    });
//
//                                    //Создаем AlertDialog:
//                                    AlertDialog alertDialog = mDialogBuilder.create();
//
//                                    //и отображаем его:
//                                    alertDialog.show();
//
//
//                                    // Intent intent = new Intent(ProductsActivity.this, ClickedProductActivity.class).
//                                    //putExtra("product", product);
//                                    //  startActivity(intent);
//
//
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
//    }
//
//
//
//
//
//
//    public void onClickAdd(View view) {
//        LayoutInflater ltInflater = getLayoutInflater();
//        LinearLayout subLayoutFieldsForBtnAdd = (LinearLayout) findViewById(R.id.subLayoutFields);
//        View view1 = ltInflater.inflate(R.layout.sub_fields_prod, subLayoutFieldsForBtnAdd, true);
//        EditText et = (EditText)findViewById(R.id.prodInList);
//        et.setText("This is not the original Text defined in the XML layout !");
//
//       // mEditText = (EditText) findViewById(R.id.editText);
//    }
//
//    public void onClickRemove(View v) {
//        View v1 = (View) v.getParent();
//        LinearLayout subLayoutFieldsForBtnRemove = (LinearLayout) findViewById(R.id.subLayoutFields);
//        subLayoutFieldsForBtnRemove.removeView((LinearLayout)v1.getParent());
//    }
//
////    private View.OnClickListener onClick() {
////        return new View.OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
////                mLayout.addView(createNewTextView(mEditText.getText().toString()));
////            }
////        };
////    }
////
////    private TextView createNewTextView(String text) {
////        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////        final TextView textView = new TextView(this);
////        textView.setLayoutParams(lparams);
////        textView.setText("New text: " + text);
////        return textView;
////    }
//}