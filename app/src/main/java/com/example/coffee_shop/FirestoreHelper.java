package com.example.coffee_shop;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FirestoreHelper {
    private final DatabaseReference ordersRef;

    public interface OnOrderPlacedListener {
        void onSuccess(Order order);
        void onFailure(Exception e);
    }

    public interface OnOrdersLoadedListener {
        void onSuccess(List<Order> orders);
        void onFailure(Exception e);
    }

    public FirestoreHelper() {
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
    }

    public void placeOrder(Order order, OnOrderPlacedListener listener) {
        DatabaseReference newRef = ordersRef.push();
        order.setOrderId(newRef.getKey());
        newRef.setValue(order)
                .addOnSuccessListener(aVoid -> listener.onSuccess(order))
                .addOnFailureListener(listener::onFailure);
    }

    public void getOrders(OnOrdersLoadedListener listener) {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Order> orders = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        orders.add(order);
                    }
                }
                Collections.sort(orders, (a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
                listener.onSuccess(orders);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.toException());
            }
        });
    }
}
