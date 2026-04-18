package com.example.galkor.galimov;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.galkor.R;
import com.example.galkor.korepanov.AppDatabase;

public class ItemDetailFragment extends Fragment {
    private AppDatabase db;
    private Item currentItem;
    private long itemId;
    private TextView nameText;
    private TextView descText;
    private TextView priceText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(requireContext());

        if (getArguments() != null) {
            itemId = getArguments().getLong("item_id", -1);
        } else {
            itemId = -1;
        }

        nameText = view.findViewById(R.id.nameText);
        descText = view.findViewById(R.id.descText);
        priceText = view.findViewById(R.id.priceText);
        Button editButton = view.findViewById(R.id.editButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        if (itemId != -1) {
            loadItem();
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putLong("item_id", itemId);
                Navigation.findNavController(v).navigate(R.id.addEditItemFragment, args);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentItem != null) {
                            db.itemDao().delete(currentItem);
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireContext(), "Удалено", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(v).navigateUp();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private void loadItem() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentItem = db.itemDao().getItemById(itemId);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentItem != null) {
                            nameText.setText(currentItem.getName());
                            descText.setText(currentItem.getDescription());
                            priceText.setText(String.format("%.2f ₽", currentItem.getPrice()));
                        }
                    }
                });
            }
        }).start();
    }
}