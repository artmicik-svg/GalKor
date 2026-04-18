package com.example.galkor.galimov;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galkor.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<Item> items;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Item item);
    }

    public FavoriteAdapter(List<Item> items, OnDeleteClickListener listener) {
        this.items = items;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nameText.setText(item.getName());
        holder.descText.setText(item.getDescription());
        holder.priceText.setText(String.format("%.2f ₽", item.getPrice()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        holder.dateText.setText("Добавлено: " + sdf.format(new Date(item.getCreatedAt())));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onDeleteClick(item);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Можно добавить переход к деталям товара
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView descText;
        TextView priceText;
        TextView dateText;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.favoriteNameText);
            descText = itemView.findViewById(R.id.favoriteDescText);
            priceText = itemView.findViewById(R.id.favoritePriceText);
            dateText = itemView.findViewById(R.id.favoriteDateText);
            deleteButton = itemView.findViewById(R.id.favoriteDeleteButton);
        }
    }
}