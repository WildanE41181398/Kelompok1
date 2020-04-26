package com.example.kelompok1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class LupakatasandiActivity extends AppCompatActivity {

    EditText Email;
    Button Lanjutkan;
    RequestQueue requestQueue;
    String  EmailHolder;
    ProgressDialog progressDialog;
    String HttpUrl = "http://192.168.1.36/config/User-Registration.php";
    Boolean CheckEditText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupakatasandi);

        Email = (EditText) findViewById(R.id.EditTextEmail);
        Lanjutkan = (Button) findViewById(R.id.ButtonLanjutkan);
        requestQueue = Volley.newRequestQueue(LupakatasandiActivity.this);
        progressDialog = new ProgressDialog(LupakatasandiActivity.this);
        Lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    UserRegistration();

                }
                else {

                    Toast.makeText(LupakatasandiActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }


}
