package com.example.galkor.galimov;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galkor.R;
import com.example.galkor.korepanov.AppDatabase;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private AppDatabase db;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.emptyView);

        db = AppDatabase.getInstance(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FavoriteAdapter(new ArrayList<Item>(), new FavoriteAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Item item) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        item.setFavorite(false);
                        db.itemDao().update(item);
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadFavorites();
                            }
                        });
                    }
                }).start();
            }
        });
        recyclerView.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Item> favorites = db.itemDao().getFavoriteItems();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(favorites);
                        if (favorites.isEmpty()) {
                            emptyView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }
}