package com.example.coffee_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etNeed, etQuantity;
    private CheckBox cbChips, cbMandazi, cbSamosa, cbCake, cbBiscuit, cbDoughnut;
    private Button btnPlaceOrder, btnViewOrders;
    private int quantity = 0;

    private static final double COFFEE_PRICE = 3.50;
    private static final double CHIPS_PRICE = 2.00;
    private static final double MANDAZI_PRICE = 1.50;
    private static final double SAMOSA_PRICE = 2.00;
    private static final double CAKE_PRICE = 3.00;
    private static final double BISCUIT_PRICE = 1.00;
    private static final double DOUGHNUT_PRICE = 1.50;

    private FirestoreHelper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNeed = findViewById(R.id.etNeed);
        etQuantity = findViewById(R.id.etQuantity);
        cbChips = findViewById(R.id.cbChips);
        cbMandazi = findViewById(R.id.cbMandazi);
        cbSamosa = findViewById(R.id.cbSamosa);
        cbCake = findViewById(R.id.cbCake);
        cbBiscuit = findViewById(R.id.cbBiscuit);
        cbDoughnut = findViewById(R.id.cbDoughnut);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnViewOrders = findViewById(R.id.btnViewOrders);

        firestoreHelper = new FirestoreHelper();

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
        btnViewOrders.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, OrdersActivity.class));
        });
    }

    public void onMinusClick(View v) {
        if (quantity > 0) {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
        }
    }

    public void onAddClick(View v) {
        quantity++;
        etQuantity.setText(String.valueOf(quantity));
    }

    private void placeOrder() {
        String itemName = etNeed.getText().toString().trim();

        if (itemName.isEmpty()) {
            Toast.makeText(this, "Please describe what you need", Toast.LENGTH_SHORT).show();
            return;
        }

        String qtyStr = etQuantity.getText().toString();
        try {
            quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            quantity = 0;
        }

        if (quantity <= 0) {
            Toast.makeText(this, "Please select at least 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> selectedToppings = new ArrayList<>();
        double toppingsTotal = 0.0;

        if (cbChips.isChecked()) {
            selectedToppings.add("Chips");
            toppingsTotal += CHIPS_PRICE;
        }
        if (cbMandazi.isChecked()) {
            selectedToppings.add("Mandazi");
            toppingsTotal += MANDAZI_PRICE;
        }
        if (cbSamosa.isChecked()) {
            selectedToppings.add("Samosa");
            toppingsTotal += SAMOSA_PRICE;
        }
        if (cbCake.isChecked()) {
            selectedToppings.add("Cake");
            toppingsTotal += CAKE_PRICE;
        }
        if (cbBiscuit.isChecked()) {
            selectedToppings.add("Biscuit");
            toppingsTotal += BISCUIT_PRICE;
        }
        if (cbDoughnut.isChecked()) {
            selectedToppings.add("Doughnut");
            toppingsTotal += DOUGHNUT_PRICE;
        }

        double totalPrice = (quantity * COFFEE_PRICE) + toppingsTotal;

        Order order = new Order();
        order.setItemName(itemName);
        order.setCoffeeQuantity(quantity);
        order.setToppings(selectedToppings);
        order.setTotalPrice(totalPrice);
        order.setTimestamp(System.currentTimeMillis());

        btnPlaceOrder.setEnabled(false);
        btnPlaceOrder.setText("Placing Order...");

        firestoreHelper.placeOrder(order, new FirestoreHelper.OnOrderPlacedListener() {
            @Override
            public void onSuccess(Order order) {
                runOnUiThread(() -> {
                    btnPlaceOrder.setEnabled(true);
                    btnPlaceOrder.setText(R.string.place_order);
                    Toast.makeText(MainActivity.this, R.string.order_placed_success, Toast.LENGTH_SHORT).show();
                    resetForm();
                });
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> {
                    btnPlaceOrder.setEnabled(true);
                    btnPlaceOrder.setText(R.string.place_order);
                    Toast.makeText(MainActivity.this, R.string.order_failed + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void resetForm() {
        etNeed.setText("");
        quantity = 0;
        etQuantity.setText("0");
        cbChips.setChecked(false);
        cbMandazi.setChecked(false);
        cbSamosa.setChecked(false);
        cbCake.setChecked(false);
        cbBiscuit.setChecked(false);
        cbDoughnut.setChecked(false);
    }
}
