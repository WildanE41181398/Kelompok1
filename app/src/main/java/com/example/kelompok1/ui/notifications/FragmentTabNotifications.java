package com.example.kelompok1.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kelompok1.R;

public class FragmentTabNotifications extends Fragment {

    public static FragmentTabNotifications newInstance(){
        return new FragmentTabNotifications();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragmess_notifications, container, false);

        return root;
    }
}
