package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {
    private EditText edtAddName, edtAddPrice, edtAddQuantity, edtAddDesc;
    private Spinner spinnerCategory;
    private Switch switchActive;
    private Button btnSaveProduct, btnBackAdd;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new DatabaseHelper(this);

        edtAddName = findViewById(R.id.edtAddName);
        edtAddPrice = findViewById(R.id.edtAddPrice);
        edtAddQuantity = findViewById(R.id.edtAddQuantity);
        edtAddDesc = findViewById(R.id.edtAddDesc);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        switchActive = findViewById(R.id.switchActive);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnBackAdd = findViewById(R.id.btnBackAdd);

        // Setup Spinner categories
        String[] categories = {"Điện tử", "Thực phẩm", "Văn phòng phẩm", "Gia dụng", "Khác"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtAddName.getText().toString().trim();
                String priceStr = edtAddPrice.getText().toString().trim();
                String qtyStr = edtAddQuantity.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();
                int active = switchActive.isChecked() ? 1 : 0;
                String desc = edtAddDesc.getText().toString().trim();

                if (name.isEmpty()) {
                    edtAddName.setError("Tên sản phẩm không được để trống");
                    return;
                }

                if (priceStr.isEmpty()) {
                    edtAddPrice.setError("Giá bán không được để trống");
                    return;
                }

                int price = 0;
                try {
                    price = Integer.parseInt(priceStr);
                } catch (NumberFormatException e) {
                    edtAddPrice.setError("Giá bán không hợp lệ");
                    return;
                }

                if (price <= 0) {
                    edtAddPrice.setError("Giá bán phải lớn hơn 0");
                    return;
                }

                if (qtyStr.isEmpty()) {
                    edtAddQuantity.setError("Số lượng không được để trống");
                    return;
                }

                int quantity = 0;
                try {
                    quantity = Integer.parseInt(qtyStr);
                } catch (NumberFormatException e) {
                    edtAddQuantity.setError("Số lượng không hợp lệ");
                    return;
                }

                if (quantity < 0) {
                    edtAddQuantity.setError("Số lượng không được âm");
                    return;
                }

                Product product = new Product(name, price, quantity, category, active, desc);
                long newId = dbHelper.insertProduct(product);

                if (newId != -1) {
                    Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
