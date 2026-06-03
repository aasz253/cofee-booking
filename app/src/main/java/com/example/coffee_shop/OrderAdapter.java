package com.example.coffee_shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderNumber.setText("Order #" + (position + 1));
        holder.tvItemName.setText(order.getItemName() != null ? order.getItemName() : "No description");
        holder.tvCoffeeQty.setText("Coffee: " + order.getCoffeeQuantity());
        holder.tvToppings.setText("Toppings: " + (order.getToppings() != null ? String.join(", ", order.getToppings()) : "None"));
        holder.tvTotalPrice.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalPrice()));

        if (order.getTimestamp() > 0) {
            Date date = new Date(order.getTimestamp());
            holder.tvTimestamp.setText(dateFormat.format(date));
        } else {
            holder.tvTimestamp.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvItemName, tvCoffeeQty, tvToppings, tvTotalPrice, tvTimestamp;
        MaterialCardView cardView;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCoffeeQty = itemView.findViewById(R.id.tvCoffeeQty);
            tvToppings = itemView.findViewById(R.id.tvToppings);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}
