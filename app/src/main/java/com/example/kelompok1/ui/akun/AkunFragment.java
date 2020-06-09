package com.example.kelompok1.ui.akun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.R;

import java.util.HashMap;

public class AkunFragment extends Fragment {

    private AkunViewModel akunViewModel;
    private String id;
    private SessionManager sessionManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        akunViewModel = ViewModelProviders.of(this).get(AkunViewModel.class);
        View root = inflater.inflate(R.layout.fragment_akun, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetail();
        id = user.get(SessionManager.ID);
        textView.setText(id + "\n LOGOUT");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });

        return root;
    }
}
