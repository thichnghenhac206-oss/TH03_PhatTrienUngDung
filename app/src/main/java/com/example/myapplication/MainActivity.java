package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvTotalProducts, tvTotalValue, tvLowStockCount;
    private Button btnAddProduct;
    private ListView lvProducts;
    private DatabaseHelper dbHelper;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        formatter = new DecimalFormat("#,###");

        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvLowStockCount = findViewById(R.id.tvLowStockCount);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        lvProducts = findViewById(R.id.lvProducts);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = productList.get(position);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", selectedProduct.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        productList = dbHelper.getAllProducts();
        adapter = new ProductAdapter(this, productList);
        lvProducts.setAdapter(adapter);

        // Update statistics
        int totalProd = productList.size();
        long totalVal = dbHelper.getTotalValue();
        int lowStock = dbHelper.countLowStock();

        tvTotalProducts.setText("Tổng số sản phẩm: " + totalProd);
        tvTotalValue.setText("Tổng giá trị kho: " + formatter.format(totalVal) + " đ");
        tvLowStockCount.setText("Sắp hết hàng: " + lowStock);
    }
}
