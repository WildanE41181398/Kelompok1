package com.example.kelompok1;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.kelompok1.fcm.OrenzFirebaseMessagingService;
import com.example.kelompok1.ui.akun.AkunFragment;
import com.example.kelompok1.ui.history.HistoryFragment;
import com.example.kelompok1.ui.home.HomeFragment;
import com.example.kelompok1.ui.notifications.NotificationsFragment;
import com.example.kelompok1.ui.promosi.PromosiFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

public class BerandaOrenz extends AppCompatActivity {

    private HomeFragment homeFragment = new HomeFragment();
    private PromosiFragment promosiFragment = new PromosiFragment();
    private HistoryFragment historyFragment = new HistoryFragment();
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private AkunFragment akunFragment = new AkunFragment();
    private String nav;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_orenz);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_promosi ,R.id.navigation_messages, R.id.navigation_profil)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        OrenzFirebaseMessagingService orenz = new OrenzFirebaseMessagingService();
        orenz.onTokenRefresh();

        nav = getIntent().getStringExtra("NAVIGATION");

        if (nav != null){
            switch (nav){
                case "HOME" :
                    navView.setSelectedItemId(R.id.navigation_home);
                    break;

                case "HISTORY" :
                    navView.setSelectedItemId(R.id.navigation_history);
                    break;

                case "PROMOSI" :
                    navView.setSelectedItemId(R.id.navigation_promosi);
                    break;

                case "NOTIFIKASI" :
                    navView.setSelectedItemId(R.id.navigation_messages);
                    break;

                case "AKUN" :
                    navView.setSelectedItemId(R.id.navigation_profil);
                    break;
            }
        }


    }


}
