package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvDetailName, tvDetailCategory, tvDetailPrice, tvDetailQuantity, tvDetailTotalValue, tvDetailDesc;
    private Button btnPlusOne, btnMinusOne, btnDeleteProduct, btnBack;
    private DatabaseHelper dbHelper;
    private int productId;
    private Product product;
    private DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new DatabaseHelper(this);
        formatter = new DecimalFormat("#,###");

        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailQuantity = findViewById(R.id.tvDetailQuantity);
        tvDetailTotalValue = findViewById(R.id.tvDetailTotalValue);
        tvDetailDesc = findViewById(R.id.tvDetailDesc);

        btnPlusOne = findViewById(R.id.btnPlusOne);
        btnMinusOne = findViewById(R.id.btnMinusOne);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnBack = findViewById(R.id.btnBack);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadProductInfo();

        btnPlusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    int newQty = product.getQuantity() + 1;
                    dbHelper.updateQuantity(productId, newQty);
                    loadProductInfo();
                    Toast.makeText(ProductDetailActivity.this, "Đã nhập thêm 1 sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMinusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    if (product.getQuantity() <= 0) {
                        Toast.makeText(ProductDetailActivity.this, "Hết hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        int newQty = product.getQuantity() - 1;
                        dbHelper.updateQuantity(productId, newQty);
                        loadProductInfo();
                        Toast.makeText(ProductDetailActivity.this, "Đã bán 1 sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteProduct(productId);
                Toast.makeText(ProductDetailActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadProductInfo() {
        product = dbHelper.getProductById(productId);
        if (product != null) {
            tvDetailName.setText(product.getName());
            tvDetailCategory.setText("Loại: " + product.getCategory());
            tvDetailPrice.setText("Giá bán: " + formatter.format(product.getPrice()) + " đ");
            tvDetailQuantity.setText("Số lượng tồn: " + product.getQuantity() + (product.getQuantity() < 5 ? " (Sắp hết hàng)" : ""));
            long totalVal = (long) product.getPrice() * product.getQuantity();
            tvDetailTotalValue.setText("Giá trị tồn kho: " + formatter.format(totalVal) + " đ");
            tvDetailDesc.setText("Mô tả: " + (product.getDescription() != null && !product.getDescription().isEmpty() ? product.getDescription() : "Không có mô tả"));
        }
    }
}
