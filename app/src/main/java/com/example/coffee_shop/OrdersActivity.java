package com.example.coffee_shop;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private FirestoreHelper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Orders");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderAdapter();
        recyclerView.setAdapter(adapter);

        firestoreHelper = new FirestoreHelper();
        loadOrders();
    }

    private void loadOrders() {
        firestoreHelper.getOrders(new FirestoreHelper.OnOrdersLoadedListener() {
            @Override
            public void onSuccess(java.util.List<Order> orders) {
                adapter.setOrders(orders);
                if (orders.isEmpty()) {
                    Toast.makeText(OrdersActivity.this, "No orders yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(OrdersActivity.this, "Failed to load orders: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
