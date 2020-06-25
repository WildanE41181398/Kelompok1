package com.example.kelompok1.ui.history;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.kelompok1.R;
import com.example.kelompok1.ui.notifications.FragmentTabMessages;
import com.example.kelompok1.ui.notifications.FragmentTabNotifications;

public class ViewAdapterTabHistory extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    final HistoryFragment context;

    public ViewAdapterTabHistory(FragmentManager fm, HistoryFragment context){
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = FragmentTabTransaksi.newInstance();
                break;
            case 1:
                fragment = FragmentTabHistory.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Transaksi";
            case 1:
                return "History";
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}


