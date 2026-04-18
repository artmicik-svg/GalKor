package com.example.galkor.korepanov;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.galkor.R;

public class ProfileFragment extends Fragment {
    private SessionManager sessionManager;
    private TextView emailText;
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        emailText = view.findViewById(R.id.emailText);
        logoutButton = view.findViewById(R.id.logoutButton);

        String email = sessionManager.getUserEmail();
        if (email != null && !email.isEmpty()) {
            emailText.setText(email);
        } else {
            emailText.setText("Не авторизован");
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Navigation.findNavController(v).navigate(R.id.loginFragment);
            }
        });
    }
}