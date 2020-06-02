package com.example.kelompok1.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kelompok1.R;

public class FragmentTabMessages extends Fragment {

    public static FragmentTabMessages newInstance(){
        return new FragmentTabMessages();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragmess_messages, container, false);
        final TextView tv = root.findViewById(R.id.tv_fragmess);
        tv.setText("Dika");

        return root;
    }
}
