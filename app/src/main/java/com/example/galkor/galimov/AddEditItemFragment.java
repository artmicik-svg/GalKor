package com.example.galkor.galimov;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.galkor.R;
import com.example.galkor.korepanov.AppDatabase;

public class AddEditItemFragment extends Fragment {
    private AppDatabase db;
    private EditText nameInput;
    private EditText descInput;
    private EditText priceInput;
    private long itemId = -1;
    private Item currentItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(requireContext());

        nameInput = view.findViewById(R.id.nameInput);
        descInput = view.findViewById(R.id.descInput);
        priceInput = view.findViewById(R.id.priceInput);
        Button saveButton = view.findViewById(R.id.saveButton);

        if (getArguments() != null) {
            itemId = getArguments().getLong("item_id", -1);
        }

        if (itemId != -1) {
            loadItem();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
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
                            nameInput.setText(currentItem.getName());
                            descInput.setText(currentItem.getDescription());
                            priceInput.setText(String.valueOf(currentItem.getPrice()));
                        }
                    }
                });
            }
        }).start();
    }

    private void saveItem() {
        String name = nameInput.getText().toString().trim();
        String desc = descInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();

        if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Некорректная цена", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (itemId == -1) {
                    Item newItem = new Item(name, desc, price);
                    db.itemDao().insert(newItem);
                } else {
                    currentItem.setName(name);
                    currentItem.setDescription(desc);
                    currentItem.setPrice(price);
                    db.itemDao().update(currentItem);
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).navigateUp();
                    }
                });
            }
        }).start();
    }
}