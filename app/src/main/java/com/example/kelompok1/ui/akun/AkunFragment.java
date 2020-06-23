package com.example.kelompok1.ui.akun;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.R;
import com.example.kelompok1.TransaksiTahap1;

import java.util.Calendar;
import java.util.HashMap;

public class AkunFragment extends Fragment {

    private AkunViewModel akunViewModel;
    private Button btn_logout;
    private TextView tv_date;
    private Calendar myCalendar;
    private String id;
    private SessionManager sessionManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        akunViewModel = ViewModelProviders.of(this).get(AkunViewModel.class);
        View root = inflater.inflate(R.layout.fragment_akun, container, false);
        btn_logout = root.findViewById(R.id.btn_logout) ;
        tv_date = root.findViewById(R.id.tanggal_lahirBio);
        myCalendar = Calendar.getInstance();
        sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetail();
        id = user.get(SessionManager.ID);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, day);

                        String formatTanggal = "dd MMMM yyyy";
                        SimpleDateFormat dbsdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        tv_date.setText(sdf.format(myCalendar.getTime()));
//                        tglJemput = dbsdf.format(myCalendar.getTime());

//                        if (myCalendar.getTimeInMillis() < (System.currentTimeMillis() - 86401000)){
//                            Toast.makeText(getContext(), "Tanggal yang kamu pilih salah!", Toast.LENGTH_LONG).show();
//                            myCalendar.setTimeInMillis(System.currentTimeMillis() - 1000);
//                            tvDate.setText(sdf.format(myCalendar.getTime()));
//                        }
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return root;
    }
}
