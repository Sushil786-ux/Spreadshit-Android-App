package com.example.spreadsheetandroidapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.ViewHolder> {

    private List<SheetItem> itemList;

    public SheetAdapter(List<SheetItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sheet_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SheetItem item = itemList.get(position);
        holder.tvCol1.setText(item.getColumn1());
        holder.tvCol2.setText(item.getColumn2());
        holder.tvCol3.setText(item.getColumn3());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCol1, tvCol2, tvCol3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCol1 = itemView.findViewById(R.id.tvCol1);
            tvCol2 = itemView.findViewById(R.id.tvCol2);
            tvCol3 = itemView.findViewById(R.id.tvCol3);
        }
    }
}
