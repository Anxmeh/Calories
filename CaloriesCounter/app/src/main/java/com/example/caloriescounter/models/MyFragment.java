package com.example.caloriescounter.models;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.example.caloriescounter.AddProductActivity;
import com.example.caloriescounter.ProductsActivity;
import com.example.caloriescounter.R;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFragment extends Fragment {

    Product addedProduct;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
//        View myFragmentView = inflater.inflate(R.layout.sub_fields_prod,
//               container, false);
//        String srt = this.getArguments().getString("name");
//        EditText addedprod = (EditText) myFragmentView.findViewById(R.id.prodInList);
//        addedprod.setText(srt);
//        return myFragmentView;

        String strName = this.getArguments().getString("name");
        String strWeight = this.getArguments().getString("weight");
        String strProtein = this.getArguments().getString("protein");
        String strFat = this.getArguments().getString("fat");
        String strCarb = this.getArguments().getString("carb");
        String strCalories = this.getArguments().getString("carb");

        Context context = getActivity().getApplicationContext();
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        size.weight = 1;

        LinearLayout parentLayout = new LinearLayout(context);
        parentLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layout1 = new LinearLayout(context);
        layout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout1.setOrientation(LinearLayout.HORIZONTAL);
        parentLayout.addView(layout1);

        size.weight = 3;
        TextView name = new TextView(context);
        name.setText(strName);
        name.setTextSize(20);
        name.setLayoutParams(size);
        layout1.addView(name);

        size.weight = 1;
        TextView weight = new TextView(context);
        weight.setText("     " + strWeight + "g");
        weight.setTextSize(20);
        weight.setLayoutParams(size);
        layout1.addView(weight);


        LinearLayout layout2 = new LinearLayout(context);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);

        parentLayout.addView(layout2);

        TextView protein = new TextView(context);
        protein.setText("P:" + strProtein);
        protein.setTextSize(15);
        protein.setTextColor(Color.rgb(0, 255, 127));
        protein.setLayoutParams(size);
        layout2.addView(protein);

        TextView fat = new TextView(context);
        fat.setText("F:" + strFat);
        fat.setTextSize(15);
        fat.setLayoutParams(size);
        fat.setTextColor(Color.rgb(255, 215, 0));
        layout2.addView(fat);

        TextView carbs = new TextView(context);
        carbs.setText("C:" + strCarb);
        carbs.setTextSize(15);
        carbs.setLayoutParams(size);
        carbs.setTextColor(Color.rgb(30, 144, 255));
        layout2.addView(carbs);

        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_action_delete);
        img.setBounds(0, 0, 60, 60);


        Button btnRemove = new Button(context);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment(strName, strWeight, strProtein, strFat, strCarb, strCalories);
            }
        });


        btnRemove.setBackground(null);
        btnRemove.setLayoutParams(size);
        btnRemove.setCompoundDrawables(img, null, null, null);

        layout2.addView(btnRemove);



        return parentLayout;
    }

    public static MyFragment newInstance(String name, String  weight, String protein, String fat, String carb, String calories) {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("weight", weight);
        bundle.putString("protein", protein);
        bundle.putString("fat", fat);
        bundle.putString("carb", carb);
        bundle.putString("calories", calories);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void closeFragment(String name, String weight, String protein, String fat, String carb, String calories) {
        android.app.FragmentManager fm = getActivity().getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        fm.beginTransaction().remove(this).commit();


        final DishIngredientsView model = new DishIngredientsView();
        model.setProductName(name);
        model.setProductCalories(Double.parseDouble(calories));
        model.setProductProtein(Double.parseDouble(protein));
        model.setProductFat(Double.parseDouble(fat));
        model.setProductCarbohydrate(Double.parseDouble(carb));
        model.setProductWeight(Double.parseDouble(weight));



        NetworkService.getInstance()
                .getJSONApi()
                .removeProductInDish(model)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            addedProduct = response.body();


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
                    public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Error occurred while getting request!";
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                error, Toast.LENGTH_LONG);
//                        toast.show();
                        t.printStackTrace();
                    }
                });











    }

    // onClickListener button
//      public void onClick(View v) {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.frameLayout);
//        fm.beginTransaction().remove(fragment).commit(); } });
}
