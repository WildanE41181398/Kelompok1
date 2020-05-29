package com.example.kelompok1.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.kelompok1.R;
import com.google.android.material.tabs.TabLayout;

public class NotificationsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        tabLayout = root.findViewById(R.id.tabs_fragmess);
        viewPager = root.findViewById(R.id.vPager_fragmess);
        init();

        return root;
    }

    private void init(){
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewAdapterTabNotifications viewAdapterTabNotifications =
                new ViewAdapterTabNotifications(getChildFragmentManager(), this);
        viewPager.setAdapter(viewAdapterTabNotifications);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(viewAdapterTabNotifications);
    }
}
