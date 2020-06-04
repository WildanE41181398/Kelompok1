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

import com.example.kelompok1.R;

public class AkunFragment extends Fragment {

    private AkunViewModel akunViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        akunViewModel = ViewModelProviders.of(this).get(AkunViewModel.class);
        View root = inflater.inflate(R.layout.fragment_akun, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        akunViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}
