package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private LayoutInflater inflater;
    private DecimalFormat formatter;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.inflater = LayoutInflater.from(context);
        this.formatter = new DecimalFormat("#,###");
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvProductName);
            holder.tvPrice = convertView.findViewById(R.id.tvProductPrice);
            holder.tvCategory = convertView.findViewById(R.id.tvProductCategory);
            holder.tvQuantity = convertView.findViewById(R.id.tvProductQuantity);
            holder.tvWarning = convertView.findViewById(R.id.tvProductWarning);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("Giá: " + formatter.format(product.getPrice()) + " đ");
        holder.tvCategory.setText("Loại: " + product.getCategory());
        holder.tvQuantity.setText("SL: " + product.getQuantity());

        if (product.getQuantity() < 5) {
            holder.tvQuantity.setTextColor(Color.RED);
            holder.tvWarning.setVisibility(View.VISIBLE);
            holder.tvWarning.setText("Sắp hết hàng");
        } else {
            holder.tvQuantity.setTextColor(Color.DKGRAY);
            holder.tvWarning.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvName, tvPrice, tvCategory, tvQuantity, tvWarning;
    }
}
