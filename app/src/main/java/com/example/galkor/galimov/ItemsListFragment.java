package com.example.galkor.galimov;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galkor.R;
import com.example.galkor.korepanov.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ItemsListFragment extends Fragment implements ItemAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private AppDatabase db;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.emptyView);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);

        db = AppDatabase.getInstance(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ItemAdapter(new ArrayList<Item>(), this);
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addEditItemFragment);
            }
        });

        loadItems();
    }

    private void loadItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Item> items = db.itemDao().getAllItems();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(items);
                        if (items.isEmpty()) {
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
    public void onItemClick(Item item) {
        Bundle args = new Bundle();
        args.putLong("item_id", item.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.itemDetailFragment, args);
    }

    @Override
    public void onFavoriteClick(Item item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                item.setFavorite(!item.isFavorite());
                db.itemDao().update(item);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }
}