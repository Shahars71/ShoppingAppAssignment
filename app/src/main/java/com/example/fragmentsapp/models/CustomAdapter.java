package com.example.fragmentsapp.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentsapp.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<Product> dataSet;
    private ArrayList<Product> originDataSet;

    public CustomAdapter(ArrayList<Product> dataSet) {
        this.dataSet = new ArrayList<Product>(dataSet);
        this.originDataSet = dataSet;
    }

    public void filter(String query) {
        dataSet.clear(); // Clear the current dataset
        if (query.isEmpty()) {
            dataSet.addAll(originDataSet); // Use addAll to avoid reference copy
        } else {
            query = query.toLowerCase().trim();
            for (Product item : originDataSet) {
                if (item.getName().toLowerCase().trim().contains(query)) {
                    dataSet.add(item);
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter of dataset changes
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView amountView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.productNameText);
            amountView = itemView.findViewById(R.id.productAmountText);
        }

    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardrow, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {

        holder.textViewName.setText(dataSet.get(position).getName());
        Integer amount = dataSet.get(position).getAmount();
        holder.amountView.setText(amount.toString());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
