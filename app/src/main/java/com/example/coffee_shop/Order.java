package com.example.coffee_shop;

import java.util.List;

public class Order {
    private String orderId;
    private String itemName;
    private int coffeeQuantity;
    private List<String> toppings;
    private double totalPrice;
    private long timestamp;

    public Order() {}

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getCoffeeQuantity() { return coffeeQuantity; }
    public void setCoffeeQuantity(int coffeeQuantity) { this.coffeeQuantity = coffeeQuantity; }

    public List<String> getToppings() { return toppings; }
    public void setToppings(List<String> toppings) { this.toppings = toppings; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
