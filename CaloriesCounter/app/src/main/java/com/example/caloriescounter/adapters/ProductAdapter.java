package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.toolbox.NetworkImageView;
import com.example.caloriescounter.R;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends BaseAdapter  implements Filterable {
    private List<Product> products2;
    private List<Product> products;
    private List<Product> filteredProducts;
    private LayoutInflater layoutInflater;
    private ImageRequester imageRequester;
    private final String BASE_URL = NetworkService.getBaseUrl();
    private Context context;

    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.filteredProducts = products;
        imageRequester = ImageRequester.getInstance();
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (products == null)
            return 0;
        //return products.size();
        return filteredProducts.size();
    }

    @Override
    public Object getItem(int position) {
        if (products == null)
            return null;
        if (products.size() > position)
            //return products.get(position);
            return filteredProducts.get(position);
        return null;
    }

//    @Override
//    public long getItemId(int position) {
//        if (products.size() < position)
//            return 0;
//
//        return products.get(position).getId();
//    }

    @Override
    public long getItemId(int position) {
        if (products.size() < position)
            return 0;

        return filteredProducts.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_product, parent, false);
        }
        TextView tvProductName = convertView.findViewById(R.id.productName);
        TextView tvProductCalories = convertView.findViewById(R.id.productCalories);
        TextView tvProductFat = convertView.findViewById(R.id.productFat);
        TextView tvProductCarbohydrate = convertView.findViewById(R.id.productCarbohydrate);
        TextView tvProductProtein = convertView.findViewById(R.id.productProtein);
       // NetworkImageView imageCategory = convertView.findViewById(R.id.categoryImage);

        tvProductName.setText(filteredProducts.get(position).getName());
        tvProductCalories.setText("Calories: " + Double.toString(filteredProducts.get(position).getCalories()));
        tvProductFat.setText("Fat: " + Double.toString(filteredProducts.get(position).getFat()));
        tvProductCarbohydrate.setText("Carbs: " + Double.toString(filteredProducts.get(position).getCarbohydrate()));
        tvProductProtein.setText("Protein: " + Double.toString(filteredProducts.get(position).getProtein()));
        //imageRequester.setImageFromUrl(imageCategory, BASE_URL + "/images/" + products.get(position).getImage());

        return convertView;



    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = products.size();
                    filterResults.values = products;

                }else{
                    List<Product> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(Product product:products){
                        if(product.getName().contains(searchStr)){
                            resultsModel.add(product);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }


                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredProducts = (List<Product>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    public void removeProduct(long id) {

      // private List<Product> newList;

        NetworkService.getInstance()
                .getJSONApi()
                .removeProduct(id)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            products2 = response.body();


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
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Error occurred while getting request!";
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                error, Toast.LENGTH_LONG);
//                        toast.show();
                        t.printStackTrace();
                    }
                });

    }



}
