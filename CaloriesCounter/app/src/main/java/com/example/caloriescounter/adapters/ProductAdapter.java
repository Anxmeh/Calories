package com.example.caloriescounter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.caloriescounter.R;
import com.example.caloriescounter.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter implements Filterable {
    private List<Product> products;
    private List<Product> filteredProducts;
    private LayoutInflater layoutInflater;
    private Context context;

    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.filteredProducts = products;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (products == null)
            return 0;
        return filteredProducts.size();
    }

    @Override
    public Object getItem(int position) {
        if (products == null)
            return null;
        if (products.size() > position)
            return filteredProducts.get(position);
        return null;
    }

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

        tvProductName.setText(filteredProducts.get(position).getName());
        tvProductCalories.setText(Double.toString(filteredProducts.get(position).getCalories()));
        tvProductFat.setText(Double.toString(filteredProducts.get(position).getFat()));
        tvProductCarbohydrate.setText(Double.toString(filteredProducts.get(position).getCarbohydrate()));
        tvProductProtein.setText(Double.toString(filteredProducts.get(position).getProtein()));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = products.size();
                    filterResults.values = products;
                } else {
                    List<Product> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();
                    for (Product product : products) {
                        if (product.getName().contains(searchStr)) {
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
}
