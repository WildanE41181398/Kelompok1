package com.example.kelompok1.ui.notifications;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.kelompok1.R;

public class ViewAdapterTabNotifications extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    final NotificationsFragment context;

    public ViewAdapterTabNotifications(FragmentManager fm, NotificationsFragment context){
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = FragmentTabNotifications.newInstance();
                break;
            case 1:
                fragment = FragmentTabMessages.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return context.getResources().getString(R.string.tab_notification);
            case 1:
                return context.getResources().getString(R.string.tab_messages);
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
