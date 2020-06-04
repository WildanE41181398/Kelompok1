package com.example.kelompok1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class TopUp extends AppCompatActivity {

    TextView tv_saldo;
    Button btn_mengerti;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Top Up Saldo");
        tv_saldo = findViewById(R.id.tv_saldohome);
        btn_mengerti = findViewById(R.id.btn_saldomengerti);

        btn_mengerti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TopUp.this, BerandaOrenz.class);
                startActivity(intent);
            }
        });

    }
}
